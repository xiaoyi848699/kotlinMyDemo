package demo.xy.com.xytdcq.nkd

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import butterknife.BindView
import demo.xy.com.xytdcq.base.BaseActivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.base.App
import demo.xy.com.xytdcq.fileManager.FileManagerActivity
import demo.xy.com.xytdcq.nkd.interfaceI.NDKInterface
import demo.xy.com.xytdcq.uitls.LogUtil
import demo.xy.com.xytdcq.uitls.ToastUtil



/**
 * "文件拆分与合并"
 */
class FileSplittingMergeActivity : BaseActivity() {

    init {
        System.loadLibrary("file-splite-merge")
    }

    override fun getLayout(): Int {
       return R.layout.activity_file_splitting_merge
    }

    @BindView(R.id.path_tv) lateinit var pathTv: TextView
    private var filePath:String? = null
    private var decryptPath:String? = null


    override fun setDataAndEvent() {
    }


    fun btnClick(v: View) {
        when (v.id) {
            R.id.getfile -> {
                startActivityForResult(FileManagerActivity.getIntent(this, App.getInstance().savePath,true,200),100)
            }
            R.id.file_split ->{
                if(TextUtils.isEmpty(filePath)){
                    ToastUtil.showToast(this,"请先选中分割文件")
                }else{
                    var strs =  filePath?.split(".")
                    if(strs!!.size == 2){
                        decryptPath = strs?.get(0)+"_%d."+strs?.get(1)
                        try{
                            NDKInterface.spliteFlie(this!!.filePath!!, this!!.decryptPath!!,5)
                            ToastUtil.showToast(this,"文件分割成功")
                        }catch ( e:Throwable){
                            ToastUtil.showToast(this,"分割失败")
                            LogUtil.e("分割失败${e.message}")
                        }
                    }else{
                        ToastUtil.showToast(this,"文件错误")
                    }


                }
            }
            R.id.file_merge ->{
                if(TextUtils.isEmpty(decryptPath)){
                    ToastUtil.showToast(this,"请先分割文件")
                }else{
                    try{
                        var strs =  filePath?.split(".")
                        if(strs!!.size == 2){
                            var newPath = strs?.get(0)+"_merge."+strs?.get(1)
                            NDKInterface.mergeFlie(this!!.decryptPath!!,newPath,5)
                            ToastUtil.showToast(this,"文件合并成功")
                        }else{
                            ToastUtil.showToast(this,"文件错误")
                        }
                    }catch ( e:Throwable){
                        ToastUtil.showToast(this,"合并失败")
                        LogUtil.e("合并失败${e.message}")

                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == 200 ){
            if(null != data && data.hasExtra("filePath")){
                filePath = data.getStringExtra("filePath")
                pathTv.text = filePath
                LogUtil.e("filePath_---->$filePath")
            }
        }
    }
}
