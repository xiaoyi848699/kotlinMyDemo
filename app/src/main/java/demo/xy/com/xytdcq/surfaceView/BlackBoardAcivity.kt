package demo.xy.com.xytdcq.surfaceView

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import demo.xy.com.mylibrary.picture.ImageLibraryHelper
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.base.BaseActivity
import demo.xy.com.xytdcq.surfaceView.doodle.ActionTypeEnum
import demo.xy.com.xytdcq.surfaceView.doodle.BlankPage
import demo.xy.com.xytdcq.surfaceView.doodle.PageChannel
import demo.xy.com.xytdcq.surfaceView.hightDoodle.*
import demo.xy.com.xytdcq.surfaceView.utils.EraserUtils
import demo.xy.com.xytdcq.uitls.BitmapUtil
import demo.xy.com.xytdcq.uitls.LogUtil
import demo.xy.com.xytdcq.uitls.ScreenCenter
import demo.xy.com.xytdcq.uitls.ToastUtil
import kotlin.math.abs

/**
 * 画板
 * 物件层级：图片-文字-画线-视频-音频-绘制层
 */
class BlackBoardAcivity : BaseActivity(), IDrawCallback, View.OnTouchListener,IChangeCallback {
    companion object {

        @kotlin.jvm.JvmField
        var sBlackBoardStatus = 0 // 标记画板状态 0：画笔， 1：移动，2：选择 3：橡皮，4 添加文字

        @kotlin.jvm.JvmField
        var eraserSize = 10 // 橡皮擦大小

        @kotlin.jvm.JvmField
        var paintSize = 1 // 画笔宽度1,3,6

        @kotlin.jvm.JvmField
        var paintColor = DrawingBoardView.rts_black // 画笔颜色

        @JvmStatic
        fun startInstance(context: Context, homeworkId: String?, questionId: String?, course: String?) {
            val intent = Intent(context, BlackBoardAcivity::class.java)
            intent.putExtra("homeworkId", homeworkId)
            intent.putExtra("questionId", questionId)
            context.startActivity(intent)
        }
    }
    private var addImageWidth = 300f
    @BindView(R.id.end_view)
    lateinit var endView: FrameLayoutView
    @BindView(R.id.doodle_view)
    lateinit var doodleView: DrawingBoardView//画板

    @BindView(R.id.move_scrollview)
    lateinit var move_scrollview: MyScrollView
    @BindView(R.id.sv_tools_pen)
    lateinit var sv_tools_pen: LinearLayout
    @BindView(R.id.sv_control_top)
    lateinit var sv_control_top: RelativeLayout
    @BindView(R.id.wb_move)
    lateinit var wb_move: ImageView
    @BindView(R.id.wb_select)
    lateinit var wb_select: ImageView
    @BindView(R.id.wb_pen)
    lateinit var wb_pen: ImageView
    @BindView(R.id.wb_eraser)
    lateinit var wb_eraser: ImageView
    @BindView(R.id.wb_image)
    lateinit var wb_image: ImageView
    @BindView(R.id.wb_text)
    lateinit var wb_text: ImageView
    @BindView(R.id.wb_audio)
    lateinit var wb_audio: ImageView
    @BindView(R.id.wb_curve)
    lateinit var wb_curve: ImageView
    @BindView(R.id.wb_straight)
    lateinit var wb_straight: ImageView
    @BindView(R.id.wb_circular)
    lateinit var wb_circular: ImageView
    @BindView(R.id.wb_rectangle)
    lateinit var wb_rectangle: ImageView
    @BindView(R.id.wb_redo)
    lateinit var wb_redo: ImageView
    @BindView(R.id.hint_add_editview)
    lateinit var hintAddEditview: TextView

    @BindView(R.id.wb_normal)
    lateinit var wb_normal: ImageView
    @BindView(R.id.wb_big)
    lateinit var wb_big: ImageView
    @BindView(R.id.wb_bigger)
    lateinit var wb_bigger: ImageView
    @BindView(R.id.wb_black)

    lateinit var wb_black: ImageView
    @BindView(R.id.wb_blue)
    lateinit var wb_blue: ImageView
    @BindView(R.id.wb_red)
    lateinit var wb_red: ImageView

    private var currentPageNum = 0
    private var pageList: ArrayList<PageChannel>? = null
    private var pageList2: ArrayList<PageChannel>? = null
    private var bgColor:Int? = null
    private var blankPage:BlankPage? = null

    private var isSelectPen = true // 是否选择画笔
    private var lastPenType = 1 // 默认画笔
    private var isCanMove = false // 是否可以移动

    private var paths: ArrayList<IBasePath>? = null

    private lateinit var textView:TextView

    private lateinit var eraserUtils:EraserUtils;

    private var downX:Float = 0.0f;
    private var downY:Float = 0.0f;
    private var lastX:Float = 0.0f;
    private var lastY:Float = 0.0f;
    private var isSelectedView:Boolean = false // 框选完成后是否有选中的view
    private var isSelectedSinglePic:Boolean = false // 框选了单张图片
    private var isSelectView:Boolean = false // 移动框选的时候是否有选中view
    private lateinit var startPoint:Point // 单次事件的起始点
    private lateinit var movePoint:Point // 单次事件的移动点
    private lateinit var selectAreStartPoint:Point // 选择区域的起始点
    private lateinit var selectAreMovePoint:Point // 选择区域的结束点

    private var deleteMinX = 0f
    private var deleteMinY = 0f
    private var deleteMaxX = 0f
    private var deleteMaxY = 0f
    private lateinit var centerPoint:Point // 中间点

    override fun getLayout(): Int {
        return R.layout.activity_frame_layout;
    }


    override fun setDataAndEvent() {
        addImageWidth = resources.getDimension(R.dimen.ic_pid_w_h200)
        eraserSize = resources.getDimensionPixelSize(R.dimen.ic_pid_w_h10)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        paths = arrayListOf()
        getCenterPosition()
        pageList = arrayListOf()
        blankPage = BlankPage()
        pageList!!.add(blankPage!!)
//        bgColor = resources.getColor(R.color.protective_color)
        bgColor = Color.TRANSPARENT // 背景颜色

        doodleView.setPageChannel(blankPage,currentPageNum)
        doodleView.init(bgColor!!, paintColor!!, this)
        doodleView!!.setPageList(pageList)
        doodleView!!.setPaintType(ActionTypeEnum.Path.value)
        doodleView.setDrawCallback(this);

        endView.setOnTouchListener(this)


//        pageList2 = arrayListOf()
//        var blankPage2 = BlankPage()
//        pageList2!!.add(blankPage2!!)

//        val layoutParamsTextInfo = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50)
//        val textInfo = TextView(this)
//        textInfo.gravity = Gravity.CENTER_VERTICAL
//        textInfo.textSize = 18f
//        textInfo.setBackgroundColor(Color.CYAN)
//        textInfo.setPadding(5, 0, 0, 0)
//        textInfo.text = "什么系统参数"
//        endView.addView(textInfo, layoutParamsTextInfo)

//        val layoutParamsTextInfo2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 50)
//        textView = TextView(this)
//        textView.text = "init TextView"
//        textView.setTextColor(Color.RED)
//        textView.gravity = Gravity.CENTER_VERTICAL
//        textView.textSize = 18f
//        textView.setBackgroundColor(Color.GRAY)
//        textView.y = 80f
//        textView.x = 80f
//        endView.addView(textView, layoutParamsTextInfo2)
    }

    override fun callBackAddView(view: IBasePath, width: Float, height: Float) {
//        paths?.add(view!!);
//        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        layoutParams.width = (view.viewWidth).toInt()
//        layoutParams.height = (view.viewHeight).toInt()
//        endView.addView(view,layoutParams)
        callBackAutoAddView(view)
    }

    fun callBackAutoAddView(view: IBasePath) {
        if (view is BasePath) {
            view.x = view.startPoint.x
            view.y = view.startPoint.y
            if (view is DrawPicture) {
                var index = 0;
                for (v in this.paths!!) {
                    // 找到最后一张图片的位置 然后在他后面添加图片
                    if (v is DrawPicture) {
                        index++
                    } else {
                        break
                    }
                }
                paths?.add(index, view)
                val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.width = (view.viewWidth).toInt()
                layoutParams.height = (view.viewHeight).toInt()
                endView.addView(view,index,layoutParams)

            } else {
                paths?.add(view!!)
                val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.width = (view.viewWidth).toInt()
                layoutParams.height = (view.viewHeight).toInt()
                endView.addView(view,layoutParams)
            }
        } else if (view is DrawText) {
            var index = 0;
            for (v in this.paths!!) {
                // 找到最后一张图片的位置 然后在他后面添加图片
                if (v is DrawPicture) {
                    index++
                } else {
                    break
                }
            }
            paths?.add(index, view)
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.width = (view.viewWidth).toInt()
            layoutParams.height = (view.viewHeight).toInt()
            endView.addView(view,index,layoutParams)

//            view.x = view.startPoint.x
//            view.y = view.startPoint.y
//            paths?.add(view!!)
//            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            layoutParams.width = (view.viewWidth).toInt()
//            layoutParams.height = (view.viewHeight).toInt()
//            endView.addView(view,layoutParams)
        }
    }


    // 左侧工具栏事件
    @OnClick(R.id.wb_move, R.id.wb_select, R.id.wb_pen, R.id.wb_eraser, R.id.wb_image, R.id.wb_text, R.id.wb_audio,
            R.id.wb_curve, R.id.wb_straight, R.id.wb_circular, R.id.wb_rectangle, R.id.wb_redo)
    fun doOnLeftControl(view: View)
    {
        when(view.id) {
            R.id.wb_move ->selectMove() // 画板移动
            R.id.wb_select ->select() // 选择移动
            R.id.wb_pen ->selectPen() // 画笔
            R.id.wb_eraser ->selectTools(1)//橡皮
            R.id.wb_image ->selectTools(2)
            R.id.wb_text ->selectTools(3)//PPT
            R.id.wb_audio ->selectTools(4)

            R.id.wb_curve ->selectPenTools(ActionTypeEnum.Path.value)
            R.id.wb_straight ->selectPenTools(ActionTypeEnum.Line.value)
            R.id.wb_circular ->selectPenTools(ActionTypeEnum.Circle.value)
            R.id.wb_rectangle ->selectPenTools(ActionTypeEnum.Rectangle.value)
            R.id.wb_redo ->selectPenTools(ActionTypeEnum.Cancel.value)
        }
    }

    // 左侧工具栏事件
    @OnClick(R.id.wb_normal, R.id.wb_big, R.id.wb_bigger, R.id.wb_black, R.id.wb_blue, R.id.wb_red)
    fun doOnTopControl(view: View)
    {
        when(view.id) {
            R.id.wb_normal -> {
                paintSize = 1
                resetTopToolsSize()
                wb_normal.setImageResource(R.drawable.wb_normal_s)
            }
            R.id.wb_big ->{
                paintSize = 3
                resetTopToolsSize()
                wb_big.setImageResource(R.drawable.wb_big_s)
            }
            R.id.wb_bigger ->{
                paintSize = 6
                resetTopToolsSize()
                wb_bigger.setImageResource(R.drawable.wb_bigger_s)
            }

            R.id.wb_black ->{
                paintColor = DrawingBoardView.rts_black // 画笔颜色
                resetTopToolsColor()
                doodleView.setPaintColor(paintColor!!)
                wb_black.setImageResource(R.drawable.wb_black_s)
            }
            R.id.wb_blue ->{
                paintColor = DrawingBoardView.rts_blue // 画笔颜色
                resetTopToolsColor()
                doodleView.setPaintColor(paintColor!!)
                wb_blue.setImageResource(R.drawable.wb_blue_s)
            }
            R.id.wb_red ->{
                paintColor = DrawingBoardView.rts_red // 画笔颜色
                resetTopToolsColor()
                doodleView.setPaintColor(paintColor!!)
                wb_red.setImageResource(R.drawable.wb_red_s)
            }
        }
    }

    private fun dealPenView() {
        if (isSelectPen) {
            sv_tools_pen.visibility = View.VISIBLE
            sv_control_top.visibility = View.VISIBLE
        } else {
            sv_tools_pen.visibility = View.GONE
            sv_control_top.visibility = View.GONE
        }
    }

    private fun selectMove() {
        sBlackBoardStatus = 1
        isCanMove = true
        isSelectPen = false
        doodleView.visibility = View.GONE
        dealPenView();
        resetLeftTools1(0);
        wb_move.setImageResource(R.drawable.wb_move_s);
        if (isSelectedView) {
            updateSelectAll(false)
        }
    }
    private fun select() {
        sBlackBoardStatus = 2
        isCanMove = false
        isSelectPen = false
        doodleView.visibility = View.GONE
        dealPenView();
        resetLeftTools1(1);
        wb_select.setImageResource(R.drawable.wb_select_s)
//        updateSelectAll(true)
//        isSelectedView = true
    }

    private fun selectPen() {
        sBlackBoardStatus = 0
        isCanMove = false
        isSelectPen = true
        doodleView.visibility = View.VISIBLE
        dealPenView();
        resetLeftTools1(2);
        wb_pen.setImageResource(R.drawable.wb_pen_s);
        selectPenTools(lastPenType) // 默认曲线
        if (isSelectedView) {
            updateSelectAll(false)
        }
    }

    private fun selectPenTools(type:Int) {
        sBlackBoardStatus = 0
        isCanMove = false
        isSelectPen = true
        doodleView.visibility = View.VISIBLE
        dealPenView()
        if (type != ActionTypeEnum.Cancel.value) {
            // 撤销不切换画笔状态
            resetLeftTools2()
            lastPenType = type;
        }
        when(type) {
            ActionTypeEnum.Path.value-> {
                wb_curve.setImageResource(R.drawable.wb_curve_s)
                doodleView.setPaintType(ActionTypeEnum.Path.value)
            }
            ActionTypeEnum.Line.value-> {
                wb_straight.setImageResource(R.drawable.wb_straight_s)
                doodleView.setPaintType(ActionTypeEnum.Line.value)
            }
            ActionTypeEnum.Circle.value-> {
                wb_circular.setImageResource(R.drawable.wb_circular_s)
                doodleView.setPaintType(ActionTypeEnum.Circle.value)
            }
            ActionTypeEnum.Rectangle.value-> {
                wb_rectangle.setImageResource(R.drawable.wb_rectangle_s)
                doodleView.setPaintType(ActionTypeEnum.Rectangle.value)
            }
            ActionTypeEnum.Cancel.value-> {
                // 撤销
               if (this.paths != null &&  this.paths!!.size > 0) {
                   // 遍历最后一个画笔删除
                   var index = this.paths!!.size - 1
                   while (index > 0){
                       if (this.paths!![index] is DrawPath) {
                           var lastView = this.paths!![index]
                           if (lastView is DrawPath) {
                               this.paths!!.remove(lastView)
                               endView.removeView(lastView)
                           }
                           break
                       }
                       if (this.paths!![index] is DrawPathLine) {
                           var lastView = this.paths!![index]
                           if (lastView is DrawPathLine) {
                               this.paths!!.remove(lastView)
                               endView.removeView(lastView)
                           }
                           break
                       }
                       if (this.paths!![index] is DrawRect) {
                           var lastView = this.paths!![index]
                           if (lastView is DrawRect) {
                               this.paths!!.remove(lastView)
                               endView.removeView(lastView)
                           }
                           break
                       }
                       if (this.paths!![index] is DrawCircle) {
                           var lastView = this.paths!![index]
                           if (lastView is DrawCircle) {
                               this.paths!!.remove(lastView)
                               endView.removeView(lastView)
                           }
                           break
                       }
                       index--
                   }
               }
            }
        }

        if (isSelectedView) {
            updateSelectAll(false)
        }
    }

    private fun selectText() {
        sBlackBoardStatus = 4
        isCanMove = false
        isSelectPen = false
        doodleView.visibility = View.GONE
        dealPenView()
        resetLeftTools1(5);
        wb_text.setImageResource(R.drawable.wb_text_s);
        if (isSelectedView) {
            updateSelectAll(false)
        }
        // 提示用户点击添加输入框
        hintAddEditview.visibility = View.VISIBLE
    }

    private fun selectEraser() {
        sBlackBoardStatus = 3
        doodleView.isWait = true
//        doodleView.setEraseType()
        isCanMove = false
        isSelectPen = false
        doodleView.visibility = View.GONE
        dealPenView()
        resetLeftTools1(3);
        wb_eraser.setImageResource(R.drawable.wb_eraser_s);
        initEraserUtils()
        if (isSelectedView) {
            updateSelectAll(false)
        }
    }

    private fun initEraserUtils() {
        eraserUtils = EraserUtils.getInstance()
        eraserUtils.init(paths,this)
        eraserUtils.startLinstener()
    }

    private fun resetLeftTools1(index:Int) {
        hintAddEditview.visibility = View.GONE
        move_scrollview.setmIsCanMove(isCanMove)
        if (index != 0) {
            wb_move.setImageResource(R.drawable.wb_move_n);
        }
        if (index != 1) {
            wb_select.setImageResource(R.drawable.wb_select_n);
        }
        if (index != 2) {
            wb_pen.setImageResource(R.drawable.wb_pen_n);
        }
        if (index != 3) {
            wb_eraser.setImageResource(R.drawable.wb_eraser_n);
        }
        if (index != 4) {
            wb_image.setImageResource(R.drawable.wb_image_n);
        }
        if (index != 5) {
            wb_text.setImageResource(R.drawable.wb_text_n);
        }
        if (index != 6) {
            wb_audio.setImageResource(R.drawable.wb_audio_n);
        }
    }

    private fun resetLeftTools2() {
        wb_curve.setImageResource(R.drawable.wb_curve_n)
        wb_straight.setImageResource(R.drawable.wb_straight_n)
        wb_circular.setImageResource(R.drawable.wb_circular_n)
        wb_rectangle.setImageResource(R.drawable.wb_rectangle_n)
        wb_redo.setImageResource(R.drawable.wb_redo_n)
    }

    private fun resetTopToolsSize() {
        wb_normal.setImageResource(R.drawable.wb_normal_n)
        wb_big.setImageResource(R.drawable.wb_big_n)
        wb_bigger.setImageResource(R.drawable.wb_bigger_n)
    }

    private fun resetTopToolsColor() {
        wb_black.setImageResource(R.drawable.wb_black_n)
        wb_blue.setImageResource(R.drawable.wb_blue_n)
        wb_red.setImageResource(R.drawable.wb_red_n)
    }

    fun selectTools(flag:Int){
        when(flag){
            0 -> doodleView.paintBack()
            1->{
                selectEraser()
            }
            2 -> {
                ImageLibraryHelper.showTakePicDialog("添加图片",this@BlackBoardAcivity);
            }
            3 -> {
                selectText();
            }
            4 -> doodleView.saveScreen()
            5 -> doodleView.saveScreen()
            6 -> doodleView.saveScreen()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        try {
            if (event != null) {
                when(event.action) {
                    MotionEvent.ACTION_POINTER_DOWN ->{
                        LogUtil.e("xiaoyi 按下的时候有手指" + event.pointerCount)
                    }
                    MotionEvent.ACTION_POINTER_UP ->{
                        LogUtil.e("xiaoyi 抬起的时候有手指" + event.pointerCount)
                    }
                    MotionEvent.ACTION_DOWN ->{
                        if (sBlackBoardStatus == 4) {
                            return true
                        }
                        downX = event.x
                        downY = event.y
                        lastX = event.x
                        lastY = event.y
                        startPoint = Point(downX,downY)
                        if (sBlackBoardStatus == 2) {
                            isSelectView = false
                            if (isSelectedView) {
                                // 判断如果是上次范围内，这继续移动

                                // 不是上次范围内则取消选择
                                // 判断为点击（如果在区域外则取消选择）
                                if (!(downX in deleteMinX..deleteMaxX && downY in deleteMinY..deleteMaxY)) {
                                    isSelectedView = false
                                    updateSelectAll(false)

                                    // 重新创建选择区域
                                    endView.isDrawSelect = true
                                    endView.startPoint = startPoint
                                }
                            } else {
                                // 创建选择区域
                                endView.isDrawSelect = true
                                endView.startPoint = startPoint
                            }
                        } else if (sBlackBoardStatus == 3) {
                            endView.startPoint = startPoint
                            endView.isDrawEaser = true
                            checkEraser(startPoint)
                        }
                    }
                    MotionEvent.ACTION_MOVE ->{
                        if (sBlackBoardStatus == 4) {
                            return true
                        }
                        var moveX = event.x - lastX
                        var moveY = event.y - lastY
                        movePoint = Point(event.x,event.y)
                        if (sBlackBoardStatus == 2) {
                            if (abs(moveX) > 8 || abs(moveY) > 8) {
                                lastX = event.x
                                lastY = event.y
                                if (isSelectedView) {
                                    // 移动view
                                    updateMove(moveX, moveY, false)
                                } else {
                                    // 选择view区域绘制
                                    endView.endPoint = movePoint

                                    // 判断是否有选中的view
                                    checkSelectView()
                                }
                            }
                        } else if (sBlackBoardStatus == 3){
                            if (abs(moveX) > 8 || abs(moveY) > 8) {
                                lastX = event.x
                                lastY = event.y
                                endView.endPoint = movePoint
                                checkEraser(movePoint)
                            }
                        }
                    }
                    MotionEvent.ACTION_UP ->{
                        movePoint = Point(event.x,event.y)
                        if (sBlackBoardStatus == 4) {
                            addEditTextView(movePoint)
                            return true
                        }
                        if (sBlackBoardStatus == 3) {
                            endView.endPoint = movePoint
                            endView.isDrawEaser = false
                            checkEraser(movePoint)
                            return true
                        }
                        if (isSelectedView) {
                            updateMove(event.x - lastX,event.y - lastY, true)
                            lastX = event.x
                            lastY = event.y
                            if (abs(event.x - startPoint.x) < 8 && abs(event.y - startPoint.y) < 8) {
                                // 判断是否是删除点击
                                if ((event.x in deleteMinX..(deleteMinX + 30) && event.y in deleteMinY..(deleteMinY + 30))) {
                                    deleteSelectAll()
                                    isSelectedView = false
                                }
                            }
                        } else {
//                        if (abs(event.x - startPoint.x) < 8 && abs(event.y - startPoint.y) < 8) {
//                            // 判断是否点击了图片
//
//                        }
                            // 判断是否有选中的view
                            checkSelectView()

                            // 抬起时如果选择有view
                            if (checkAllViewIsSelect()) {
                                isSelectedView = true
                                sBlackBoardStatus = 2

                                // 确定最后的框选区域（暂时保留显示）
                                selectAreStartPoint = startPoint
                                selectAreMovePoint = movePoint

                                // 找出公共区域出来添加可选中删除的按钮
                                findSelectArea()
                            } else {
                                // 取消选择框效果
                                updateSelectAll(false)
                                isSelectedView = false
                            }
                            cancleSelectView ()
                        }
                    }
                }
                return true
            }
        } catch (ex: IllegalArgumentException) {
            LogUtil.e("xiaoyi activity onInterceptTouchEvent IllegalArgumentException$ex")
        }
        return super.onTouchEvent(event)
    }

    /**
     * 检查是否被橡皮擦除
     */
    private fun checkEraser(movePoint: Point) {
        if (eraserUtils == null) {
            initEraserUtils()
        }
        eraserUtils.addEraserPoint(movePoint)
    }

    private fun addEditTextView(point: Point) {
        var drawText = DrawText(this)
        changeSelectCallBack(drawText.vid) // 更新选择自己
        drawText.startPoint = Point(point.x - drawText.viewWidth/2, point.y - drawText.viewHeight/2)
        drawText.endPoint = Point(point.x + drawText.viewWidth/2,point.y + drawText.viewHeight/2)
        drawText.setChangeCallback(this)
        isSelectedSinglePic = false
        callBackAutoAddView(drawText)
        // 添加完成后切换成移动
        select()
    }

    // 找出并绘制共同区域，增加删除按钮
    private fun findSelectArea() {
        deleteMinX = 0f
        deleteMinY = 0f
        deleteMaxX = 0f
        deleteMaxY = 0f
        for (b in this!!.paths!!) {
            if (b is DeleteArea) {
                endView.removeView(b)
                this!!.paths!!.remove(b)
                continue
            }
            if (b.isSelect) {
                if (deleteMinX == 0f || b.startPoint.x < deleteMinX) {
                    deleteMinX = b.startPoint.x
                }
                if (deleteMinY == 0f || b.startPoint.y < deleteMinY) {
                    deleteMinY = b.startPoint.y
                }
                if (b.endPoint.x > deleteMaxX) {
                    deleteMaxX = b.endPoint.x
                }
                if (b.endPoint.y > deleteMaxY) {
                    deleteMaxY = b.endPoint.y
                }
            }

        }
        deleteMinX -= 10
        deleteMinY -= 10
        deleteMaxX += 10
        deleteMaxY += 10
        var deleteAreaView = DeleteArea(this)
        deleteAreaView.startPoint = Point(deleteMinX,deleteMinY)
        deleteAreaView.endPoint = Point(deleteMaxX,deleteMaxY)
        deleteAreaView.viewWidth = deleteMaxX - deleteMinX
        deleteAreaView.viewHeight = deleteMaxY - deleteMinY
        deleteAreaView.x = deleteMinX
        deleteAreaView.y = deleteMinY
        deleteAreaView.isSelect = true
        callBackAddView(deleteAreaView, deleteAreaView.viewWidth ,deleteAreaView.viewHeight)

    }

    private fun updateSelectAll(isSelect:Boolean) {
        for (b in this!!.paths!!) {
            b.isSelect = isSelect
            if (b is DrawPicture ) {
                b.isSelectPic = false
            }
            if (!isSelect && b is DrawText) {
                b.isFocusable = false
            }
            if (!isSelect) {
                if (b is DeleteArea) {
                    endView.removeView(b)
                    this!!.paths!!.remove(b)
                }
            }
        }
    }

    /**
     * 取消多选
     */
    private fun cancleSelectAll() {
        isSelectedView = false
        for (b in this!!.paths!!) {
            b.isSelect = false
            if (b is DeleteArea) {
                endView.removeView(b)
                this!!.paths!!.remove(b)
            }
        }
    }

    /**
     * 删除选中图形曲线
     */
    private fun deleteSelectAll() {
        var newPaths: ArrayList<IBasePath> = arrayListOf()
        for (b in this!!.paths!!) {
            if (b is BasePath) {
                if (b.isSelect || b.isRemove) {
                    endView.removeView(b)
                } else {
                    newPaths.add(b)
                }
            } else if (b is DrawText) {
                if (b.isSelect || b.isRemove) {
                    endView.removeView(b)
                } else {
                    newPaths.add(b)
                }
            }
        }
        this!!.paths!!.clear()
        if (newPaths.size > 0) {
            this!!.paths!!.addAll(newPaths)
        }
    }

    private fun cancleSelectView () {
        endView.isDrawSelect = false
        endView.startPoint = null
        endView.endPoint = null
        endView.invalidate()
    }

    private fun updateMove(moveX:Float, moveY:Float,isMoveEnd:Boolean) {
        deleteMinX += moveX;
        deleteMaxX += moveX;
        deleteMinY += moveY;
        deleteMaxY += moveY;
        for (b in this!!.paths!!) {
            if (b.isSelect) {
                b.move(moveX, moveY,isMoveEnd)
//                b.y = b.startPoint.y //卡顿
//                b.x = b.startPoint.x //卡顿
//                b.scrollTo(b.startPoint.x.toInt(), b.startPoint.y.toInt()) // 反方向内部移动
//                b.scrollBy(b.startPoint.x.toInt(), b.startPoint.y.toInt()) // 不适合
                if (b is BasePath) {
                    b.translationX = b.startPoint.x
                    b.translationY = b.startPoint.y
                } else if (b is DrawText) {
                    b.translationX = b.startPoint.x
                    b.translationY = b.startPoint.y
                }
            }
        }
    }

    // 框选返回传入view判断是否被选择
    private fun checkSelectView() {
        for (b in this!!.paths!!) {
           b.checkIsSelect(startPoint.x, movePoint.x, startPoint.y, movePoint.y)
        }
    }

    // 遍历所有的view 是否有选中的
    private fun checkAllViewIsSelect(): Boolean {
        for (b in this!!.paths!!) {
            if (b.isSelect) {
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode){
                ImageLibraryHelper.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    sendBroadcast( Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, ImageLibraryHelper.fileUri))
                    var file = ImageLibraryHelper.fileUri.path
                    if (file == null) {
                        ToastUtil.showToast(this@BlackBoardAcivity,"图片有误")
                        return
                    }
                    updateSelectAll(false)
                    var imageView = DrawPicture(this)
                    changeSelectCallBack(imageView.vid) // 更新选择自己
                    imageView.startPoint = Point(centerPoint.x - addImageWidth / 2,centerPoint.y - addImageWidth / 2)
                    imageView.endPoint = Point(centerPoint.x + addImageWidth / 2,centerPoint.y + addImageWidth / 2)
                    imageView.viewWidth = addImageWidth
                    imageView.viewHeight = addImageWidth
                    imageView.localPath = file
                    imageView.isSelectPic = true
                    imageView.setChangeCallback(this)
                    isSelectedSinglePic = true
                    callBackAutoAddView(imageView)
                    // 变成可选择
                    select()
                }
                ImageLibraryHelper.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    var file = BitmapUtil.getFileFromMediaUri(this, data!!.data)
                    if (file == null) {
                        ToastUtil.showToast(this@BlackBoardAcivity,"图片有误")
                        return
                    }
                    updateSelectAll(false)
                    var imageView = DrawPicture(this)
                    changeSelectCallBack(imageView.vid) // 更新选择自己
                    imageView.startPoint = Point(centerPoint.x - addImageWidth / 2,centerPoint.y - addImageWidth / 2)
                    imageView.endPoint = Point(centerPoint.x + addImageWidth / 2,centerPoint.y + addImageWidth / 2)
                    imageView.viewWidth = addImageWidth
                    imageView.viewHeight = addImageWidth
                    imageView.localPath = file.canonicalPath
                    imageView.isSelectPic = true
                    imageView.setChangeCallback(this)
                    isSelectedSinglePic = true
                    callBackAutoAddView(imageView)
                    // 变成可选择
                    select()
                }
            }
        }
    }

    /**
     * 中心点
     */
    private fun getCenterPosition() {
        centerPoint = Point((ScreenCenter.getDisplayWidth() / 2).toFloat(), (ScreenCenter.getDisplayHeight()/2).toFloat())
    }

    override fun deleteSelfCallBack(viewId: String?) {
        var newPaths: ArrayList<IBasePath> = arrayListOf()
        for (b in this!!.paths!!) {
            if (b is BasePath) {
                if (b.vid ==viewId || b.isRemove) {
                    endView.removeView(b)
                } else {
                    newPaths.add(b)
                }
            } else if (b is DrawText) {
                if (b.vid ==viewId || b.isRemove) {
                    endView.removeView(b)
                } else {
                    newPaths.add(b)
                }

            }
        }
        this!!.paths!!.clear()
        if (newPaths.size > 0) {
            this!!.paths!!.addAll(newPaths)
        }
    }

    override fun changeSelectCallBack(viewId: String?) {
//        LogUtil.e("DrawPicture", "changeSelectCallBack viewId：$viewId")
        for (b in this!!.paths!!) {
            if (b is DrawPicture) {
//                LogUtil.e("DrawPicture", "changeSelectCallBack vid：" + b.vid)
                // 取消其他的图片选择
                if (b.vid != viewId) {
                    b.isSelectPic = false
                }
            }else if (b is DrawText) {
                // 取消其他的输入框选择
                if (b.vid != viewId) {
                    b.isSelectEdit = false
                }
            } else {
                b.isSelect = false
                if (b is DeleteArea) {
                    endView.removeView(b)
                    this!!.paths!!.remove(b)
                }
            }
        }
        isSelectedView = false
    }

    override fun changeCallBack(viewId: String?, startPoint: Point?,endPoint: Point?, width: Float, height: Float) {
        for (b in this!!.paths!!) {
            if (b.vid == viewId) {
                b.startPoint = startPoint
                b.endPoint = endPoint
                b.viewWidth = width
                b.viewHeight = height
            }
        }
    }

    override fun eraserPath(viewIds: MutableList<String>?) {
        runOnUiThread {
            var newPaths: ArrayList<IBasePath> = arrayListOf()
            for (b in this!!.paths!!) {
                if (b is DrawPath && viewIds != null) {
                    if (viewIds.contains(b.vid)) {
                        endView.removeView(b)
                        continue
                    }
                }
                if (b is DrawPathLine && viewIds != null) {
                    if (viewIds.contains(b.vid)) {
                        endView.removeView(b)
                        continue
                    }
                }
                if (b is DrawRect && viewIds != null) {
                    if (viewIds.contains(b.vid)) {
                        endView.removeView(b)
                        continue
                    }
                }
                if (b is DrawCircle && viewIds != null) {
                    if (viewIds.contains(b.vid)) {
                        endView.removeView(b)
                        continue
                    }
                }
                newPaths.add(b)
            }
            this!!.paths!!.clear()
            if (newPaths.size > 0) {
                this!!.paths!!.addAll(newPaths)
            }
        }
    }
}