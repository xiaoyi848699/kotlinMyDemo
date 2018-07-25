package demo.xy.com.xytdcq

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import demo.xy.com.xytdcq.uitls.EncryptionUtils

class Jni1Activity : AppCompatActivity() {
    @BindView(R.id.hint_tv) lateinit var hint_tv: TextView
    @BindView(R.id.java_result)  lateinit var java_result: TextView
    @BindView(R.id.jni_result)  lateinit var jni_result: TextView

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
