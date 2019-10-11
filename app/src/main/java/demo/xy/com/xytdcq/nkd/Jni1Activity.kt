package demo.xy.com.xytdcq.nkd

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import demo.xy.com.xytdcq.base.BaseActivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.uitls.EncryptionUtils

class Jni1Activity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_jni1
    }
    override fun setDataAndEvent() {
        hint_tv.text = stringFromJNI()
    }



    @BindView(R.id.hint_tv) lateinit var hint_tv: TextView
    @BindView(R.id.java_result)  lateinit var java_result: TextView
    @BindView(R.id.java_result1)  lateinit var java_result1: TextView
    @BindView(R.id.java_result2)  lateinit var java_result2: TextView
    @BindView(R.id.jni_result)  lateinit var jni_result: TextView
    @BindView(R.id.jni_result1)  lateinit var jni_result1: TextView
    @BindView(R.id.jni_result2)  lateinit var jni_result2: TextView
    @BindView(R.id.bas64_result1)  lateinit var bas64_result1: TextView
    @BindView(R.id.bas64_result2)  lateinit var bas64_result2: TextView
    @BindView(R.id.aes_result1)  lateinit var aes_result1: TextView
    @BindView(R.id.aes_result2)  lateinit var aes_result2: TextView
    @BindView(R.id.des_result1)  lateinit var des_result1: TextView
    @BindView(R.id.des_result2)  lateinit var des_result2: TextView
    @BindView(R.id.data)  lateinit var data: TextView

    var encryptStr: String? = null
    var base64EncryptStr: String? = null
    var aesEncryptStr: String? = null
    var desEncryptStr: String? = null

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
            R.id.base64_btn1 -> {//base64加密
                base64EncryptStr = encryptBase64(data.text as String)
                bas64_result1.text = "jni加密后：$base64EncryptStr"
            }
            R.id.base64_btn2 -> {//base64解密
                if(null != base64EncryptStr){
                    bas64_result2.text = "jni解密后："+decryptBase64(base64EncryptStr!!)
                }else{
                    Toast.makeText(this,"请先base64加密",Toast.LENGTH_LONG).show()
                }
            }
            R.id.aes_btn1 -> {//aes加密
                aesEncryptStr = encodeAES(data.text as String)
                aes_result1.text = "jni加密后：$aesEncryptStr"
            }
            R.id.aes_btn2 -> {//aes解密
                if(null != aesEncryptStr){
                    aes_result2.text = "jni解密后："+decodeAES(aesEncryptStr!!)
                }else{
                    Toast.makeText(this,"请先AES加密",Toast.LENGTH_LONG).show()
                }
            }
            R.id.des_btn1 -> {//des加密
                desEncryptStr = encryptDES(data.text as String)
                des_result1.text = "jni加密后：$desEncryptStr"
            }
            R.id.des_btn2 -> {//des解密
                if(null != desEncryptStr){
                    des_result2.text = "jni解密后："+decryptDES(desEncryptStr!!)
                }else{
                    Toast.makeText(this,"请先DES加密",Toast.LENGTH_LONG).show()
                }
            }

        }
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
