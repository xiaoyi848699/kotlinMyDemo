package demo.xy.com.xytdcq.surfaceView

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.squareup.picasso.Picasso
import demo.xy.com.mylibrary.picture.ImageLibraryHelper
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.surfaceView.doodle.*
import demo.xy.com.xytdcq.surfaceView.utils.PicassoTarget
import demo.xy.com.xytdcq.uitls.*
import demo.xy.com.xytdcq.view.popup.MYPopupWindowFactory
import demo.xy.com.xytdcq.view.pull.PullRecycler

class SurfaceViewDoodleActivity : AppCompatActivity() ,PicassoTarget.PicassoCallBack{


    val  RTS_BLANK = "blank"//白板创建页面指定的字符串

//    //type: 创建页面或者翻页：翻页(1)、新建(2)、选择PPT返回发送的第一张图片(3)。
    val RTS_FLIP_PAGE = 1
    val RTS_CREATE_PAGE = 2

    @BindView(R.id.sv_control)
    lateinit var mSvControl: RelativeLayout//控制
    @BindView(R.id.close_sv_tools)
    lateinit var close_sv_tools: ImageView//控制工具栏关
    @BindView(R.id.open_sv_tools)
    lateinit var open_sv_tools: ImageView//控制工具栏开

    @BindView(R.id.add_lay)
    lateinit var add_lay: RelativeLayout//添加
    @BindView(R.id.color_layout)
    lateinit var color_layout: RelativeLayout//色板
    @BindView(R.id.el_color)
    lateinit var elColor: TextView
    @BindView(R.id.paint_layout)
    lateinit var paint_layout: RelativeLayout//画笔
    @BindView(R.id.paint_tv)
    lateinit var paint_tv: TextView//当前画笔名称
    @BindView(R.id.eraser_layout)
    lateinit var eraser_layout: RelativeLayout//橡皮擦
    @BindView(R.id.delete_layout)
    lateinit var delete_layout: RelativeLayout//删除
    @BindView(R.id.iv_pre_page)
    lateinit var previousPage: ImageView//上一页
    @BindView(R.id.iv_next_page)
    lateinit var nextPage: ImageView//下一页
    @BindView(R.id.ppt_layout)
    lateinit var ppt_layout: RelativeLayout//PPT
    @BindView(R.id.screen_cut_layout)
    lateinit var screen_cut_layout: RelativeLayout//学生显示屏幕截取
    @BindView(R.id.doodle_view)
    lateinit var doodleView: MyDoodleView//画板


    @BindView(R.id.ppt_content_layout)
    lateinit var ppt_content_layout: RelativeLayout//PPT内容
    @BindView(R.id.ppt_pull_recycler)
    lateinit var pptPullRecycler: PullRecycler//PPT选择

    var unbinder : Unbinder? = null
    private var datas: Array<String>? = null

    private var showPPT = false//默认是否显示ppt
    private var canClick = true
    private val animDuration = 300
    private var paintSelectIndex = 0
    private var currentPageNum = 0
    private var totalPageNum = 1
    private var popupDialogWidth = ScreenCenter.getDisplayWidth()/7
    private var pageList: ArrayList<PageChannel>? = null
    private var bgColor:Int? = null
    private var paintColor:Int? = null
    private var blankPage:BlankPage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_view_doodle)
        //注册ButterKnife
        unbinder = ButterKnife.bind(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initData()
        initView()
    }

    private fun initView() {
        doodleView.setPageChannel(blankPage,currentPageNum)
        doodleView!!.init(this!!.bgColor!!, this!!.paintColor!!,this)
        doodleView!!.setPageList(pageList)
        doodleView!!.setPaintType(ActionTypeEnum.Path.value)
    }

    private fun initData() {
        datas = resources.getStringArray(R.array.rts_paint_tools)
        pageList = arrayListOf()
        blankPage = BlankPage()
        pageList!!.add(blankPage!!)
         bgColor = resources.getColor(R.color.protective_color)
         paintColor = MyDoodleView.rts_red

    }


    private fun setPaintColor(@DrawableRes drawableId: Int) {
        elColor!!.setCompoundDrawables(createDrawable(drawableId), null, null, null)
    }

    private fun setPaint(@DrawableRes drawableId: Int) {
        paint_tv!!.setCompoundDrawables(createDrawable(drawableId), null, null, null)
    }

    private fun createDrawable(@DrawableRes drawableId: Int): Drawable {
        val drawable = resources.getDrawable(drawableId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }
    //工具栏事件
    @OnClick(R.id.iv_pre_page, R.id.iv_next_page, R.id.add_lay, R.id.paint_layout, R.id.delete_layout, R.id.color_layout, R.id.eraser_layout, R.id.cancel_layout, R.id.ppt_layout, R.id.screen_cut_layout, R.id.ppt_content_layout)
    fun doOnBottomControl(view: View)
    {
        when(view.id) {
            R.id.iv_pre_page ->onChangePage(-1)//上一页
            R.id.iv_next_page ->onChangePage(1)//下一页
            R.id.eraser_layout ->selectTools(0)//橡皮
            R.id.cancel_layout ->selectTools(1)//撤销
            R.id.ppt_content_layout ->selectTools(2)//PPT
            R.id.ppt_layout ->selectTools(2)//PPT
            R.id.screen_cut_layout ->selectTools(3)
            R.id.add_lay ->createPage(RTS_BLANK)
            R.id.paint_layout ->paintLayout()
            R.id.delete_layout ->doodleView.clear()//清除所有
            R.id.color_layout ->colorLayout()
        }
    }
    fun onChangePage(pageCount:Int) {
        if(currentPageNum+pageCount >= totalPageNum){
            showToastByMsg("当前是最后一页")
            return
        }
        if(currentPageNum+pageCount < 0){
            showToastByMsg("当前是第一页")
            return
        }
        changePage(pageCount)
    }

    private fun showToastByMsg(s: String) {
        ToastUtil.showToast(this,s)
    }

    fun changePage(pageCount:Int) {
        currentPageNum += pageCount
        var page:PageChannel
        if (pageList!!.size == totalPageNum) {
            page = pageList!![currentPageNum]
        } else {
            page =  BlankPage()
        }
        if (page is BlankPage) {
            doodleView.setPageChannel(page, currentPageNum)
            doodleView.setImageView(null)//默认白板页面
        } else if (page is PicPage) {//图片类型
            var picUrl = page.picUrl
            if (!TextUtils.isEmpty(picUrl)&&!RTS_BLANK.equals(picUrl)){
                doodleView.setPageChannel(page, currentPageNum)
                doDownloadPage2(picUrl)
            }else {
                doodleView.setPageChannel(page, currentPageNum)
                doodleView.setImageView(null)//如果不是图片链接、不指定图片
            }
        }
    }
    fun paintLayout() {
        datas = resources.getStringArray(R.array.rts_paint_tools)
        resetPaint(paintSelectIndex)
        MYPopupWindowFactory.showPopUp(this,paint_layout,datas,popupDialogWidth,paintSelectIndex) {
                MYPopupWindowFactory.dissmiss()
                try {
                    paintSelectIndex = Integer.parseInt(it.tag.toString())
                    resetPaint(paintSelectIndex)
                }catch (e:Exception){
                    LogUtil.e("get index Exception:"+e.message)
                }
        }
    }

    /**
     * 选择并重置画笔
     */
    fun resetPaint(paintSelectIndex:Int) {
        paint_tv.text=(datas!![paintSelectIndex])
        when(paintSelectIndex){
            0->{
                doodleView.isWait = false
                doodleView.setPaintType(ActionTypeEnum.Path.value)//曲线
            }
            1->{
                doodleView.isWait = true
                doodleView.setPaintType(ActionTypeEnum.Line.value)//直线
            }
            2->{
                doodleView.isWait = true
                doodleView.setPaintType(ActionTypeEnum.Rect.value)//矩阵
            }
            3->{
                doodleView.isWait = true
                doodleView.setPaintType(ActionTypeEnum.Circle.value)//圆形
            }
        }
    }

    fun colorLayout() {
        MYPopupWindowFactory.showColorPopUp(this,color_layout,popupDialogWidth) {
                MYPopupWindowFactory.dissmiss()
                try {
                    var index = Integer.parseInt(it.tag.toString())
                    selectColor(index)
                }catch ( e:Exception){
                    LogUtil.e("get index Exception:"+e.message)
                }
        }
    }
    fun selectColor(flag:Int){
        when(flag){
            0->{
                doodleView.setPaintColor(MyDoodleView.rts_blue)
                setPaintColor(R.drawable.rts_color_blue)
            }
            1->{
                doodleView.setPaintColor(MyDoodleView.rts_red)
                setPaintColor(R.drawable.rts_color_red)
            }
            2->{
                doodleView.setPaintColor(MyDoodleView.rts_black)
                setPaintColor(R.drawable.rts_color_black)
            }
            3->{
                doodleView.setPaintColor(MyDoodleView.rts_pink)
                setPaintColor(R.drawable.rts_color_pink)
            }
        }
    }
//    fun addLayout(){
//        datas = resources.getStringArray(R.array.rts_add_tools)
//
//        MYPopupWindowFactory.showPopUp(this, add_lay, datas, popupDialogWidth,-1){
//            MYPopupWindowFactory.dissmiss()
//            try {
//                var index = Integer.parseInt(it.tag.toString())
//                LogUtil.e("addLayout index:$index")
//                when(index){
//                    0->createPage(RTS_BLANK)//创建一个空白页面
//                    1-> ImageLibraryHelper.takePicFromGallery(this)
//                    2->ImageLibraryHelper.takePicFromCamera(this)
//                }
//            }catch (e:Exception ){
//                LogUtil.e("get index Exception:"+e.message)
//            }
//        }
//    }
    fun createPage(picUrl:String) {
        var isPic = RTS_BLANK == picUrl
        var page: PageChannel?
        if (isPic) {//白板类型
            page =  BlankPage()
        } else {//图片类型
            page =  PicPage(picUrl)
        }
        pageList!!.add(currentPageNum+1, page)//页面插入到对应位置
        currentPageNum++ //新建页面的时候，当前页面+1，全部页面+1
        totalPageNum++
        if (isPic) {
            //如果加载成功图片需要发送通知、并且类型是新建页面(2)
            doDownloadPage2(picUrl)
        } else {
            doodleView.setPageChannel(page, currentPageNum)
            doodleView.setImageView(null)
        }
        showToastByMsg("添加成功")
//        pptAdapter.notifyDataSetChanged();
    }
    /**
     * 创建页面拿地址去下载页面
     *
     * @param picUrl   图片地址
     * @param sendFli否发送翻页通知
     * @param type     翻页(1)、新建(2)
     */
    private fun doDownloadPage2( picUrl:String) {
        if (!TextUtils.isEmpty(picUrl)&& RTS_BLANK != picUrl) {
            runOnUiThread {
                    var width = if(0 == doodleView.width)  300 else doodleView.width
                    var height = if(0 == doodleView.height)  400 else doodleView.height
                    LogUtil.e("doDownloadPage2 width$width,height$height")
                    var loadUrl = picUrl
                    if(!loadUrl.contains("http")){
//                        loadUrl = HttpHelper.IMG_HOST+picUrl;

                    }
                    LogUtil.e("loadUrl:$loadUrl");
                    var target = PicassoTarget(loadUrl, this)
                    Picasso.with(this).load(loadUrl)
                            .resize(width, height)
                            .centerInside()
                            .into(target)
            }
        }
    }
    override fun onBitmapFailed() {
        ToastUtil.showToast(this,"图片加载失败")
    }

    override fun onBitmapLoaded(bitmap: Bitmap?) {
        doodleView?.setImageView(bitmap)
    }
    override fun onPrepareLoad() {
        doodleView?.setPageChannel(pageList!!.get(currentPageNum), currentPageNum)
    }
    /**
     * 选择其他工具
     * @param flag 0 橡皮擦  1 撤销  2 PPT 3 截屏
     */
    fun selectTools(flag:Int){
        when(flag){
            0->{
                ToastUtil.showToast(this,"选择了橡皮")
                doodleView.isWait = true
                doodleView.setEraseType()
            }
            1 -> doodleView.paintBack()
            3 -> doodleView.saveScreen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode){
                ImageLibraryHelper.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    sendBroadcast( Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, ImageLibraryHelper.fileUri))
                    createPage(ImageLibraryHelper.fileUri.path)
                }
                ImageLibraryHelper.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    var file = BitmapUtil.getFileFromMediaUri(this, data!!.data)
                    createPage(file.absolutePath)
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()//!!.为空会报异常
    }
}
