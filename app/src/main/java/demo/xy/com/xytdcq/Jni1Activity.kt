package demo.xy.com.xytdcq

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import demo.xy.com.xytdcq.uitls.EncryptionUtils

class Jni1Activity : AppCompatActivity() {
    @BindView(R.id.hint_tv) lateinit var hint_tv: TextView
    @BindView(R.id.java_result)  lateinit var java_result: TextView
    @BindView(R.id.java_result1)  lateinit var java_result1: TextView
    @BindView(R.id.java_result2)  lateinit var java_result2: TextView
    @BindView(R.id.jni_result)  lateinit var jni_result: TextView
    @BindView(R.id.jni_result1)  lateinit var jni_result1: TextView
    @BindView(R.id.jni_result2)  lateinit var jni_result2: TextView
    @BindView(R.id.data)  lateinit var data: TextView

    var encryptStr: String? = null

    var unbinder : Unbinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni1)
        //注册ButterKnife
        unbinder = ButterKnife.bind(this)

        hint_tv.text = stringFromJNI()

    }
    fun btnClick(v: View){
        when(v.id){
            R.id.md5_btn -> {
                var str = "123456"
                java_result.text = "kotlin:"+EncryptionUtils.getMD5Str(str).toUpperCase()
                jni_result.text = "jni:"+stringToMD5(str)
            }
            R.id.java_btn1 -> {//kotlin加密
                encryptStr = EncryptionUtils.publicEncrypt(data.text as String,EncryptionUtils.getPublicKey(EncryptionUtils.publicKey)).toString()
                java_result1.text = "kotlin加密后：$encryptStr"
            }
            R.id.java_btn2 -> {//kotlin解密
                if(!TextUtils.isEmpty(encryptStr)){
                    java_result2.text = "kotlin解密后："+EncryptionUtils.privateDecrypt(encryptStr!!,EncryptionUtils.getPrivateKey(EncryptionUtils.privateKey))
                }else{
                    Toast.makeText(this,"请先加密",Toast.LENGTH_LONG).show()
                }
            }
            R.id.rsa_btn1 -> {//加密
                encryptStr = encryptRSA(data.text as String)
                jni_result1.text = "jni加密后：$encryptStr"
            }
            R.id.rsa_btn2 -> {//解密
                if(null != encryptStr){
                    jni_result2.text = "jni解密后："+decryptRSA(encryptStr!!)
                }else{
                    Toast.makeText(this,"请先加密",Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()//!!.为空会报异常
    }


    external fun stringFromJNI(): String

    external fun stringToMD5(msg: String): String

    external fun encodeAES(msg: String): String

    external fun decodeAES(msg: String): String

    external fun encryptDES(msg: String): String

    external fun decryptDES(msg: String): String

    external fun decryptRSA(msg: String): String

    external fun encryptRSA(msg: String): String

    external fun encryptBase64(msg: String): String

    external fun decryptBase64(msg: String): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}