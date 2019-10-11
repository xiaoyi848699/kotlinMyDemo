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
 * 文件加密和解密
 */
class EncryptionFileActivity : BaseActivity() {


    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("file-encrypt-lib")
    }

    override fun getLayout(): Int {
       return  R.layout.activity_encryption_file
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
            R.id.encrypt ->{
                if(TextUtils.isEmpty(filePath)){
                    ToastUtil.showToast(this,"请先选中加密文件")
                }else{
                    decryptPath = filePath+"_ecrypt"
                    try{
                        NDKInterface.cryptorEncrypt(this!!.filePath!!, this!!.decryptPath!!)
                        ToastUtil.showToast(this,"加密成功")
                    }catch ( e:Throwable){
                        ToastUtil.showToast(this,"加密失败")
                        LogUtil.e("加密失败${e.message}")

                    }

                }
            }
            R.id.dencrypt ->{
                if(TextUtils.isEmpty(decryptPath)){
                    ToastUtil.showToast(this,"请先加密文件")
                }else{
                    try{
                        var strs =  filePath?.split(".")
                        if(strs!!.size == 2){
                            var newPath = strs?.get(0)+"_decrypt."+strs?.get(1)
                            NDKInterface.cryptorDecrypt(this!!.decryptPath!!,newPath)
                            ToastUtil.showToast(this,"解密成功")
                        }else{
                            ToastUtil.showToast(this,"文件错误")
                        }
                    }catch ( e:Throwable){
                        ToastUtil.showToast(this,"解密失败")
                        LogUtil.e("解密失败${e.message}")

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
