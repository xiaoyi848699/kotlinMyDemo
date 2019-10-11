package demo.xy.com.xytdcq.androidIPC.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.View
import android.widget.TextView
import butterknife.BindView
import demo.xy.com.mylibrary.dialog.DialogUtils
import demo.xy.com.mylibrary.log.LogUtil
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.base.BaseActivity
import demo.xy.com.xytdcq.uitls.ToastUtil


class AIDLActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_aidl
    }
    @BindView(R.id.aidl_textView) lateinit var textView: TextView
    @BindView(R.id.aidl_conn_status) lateinit var connStatus: TextView
    private var isConn:Boolean = false
    private var iPerson:IPersonAidlInterface? = null
    private var iCallPayAidlInterface:ICallPayAidlInterface? = null
    override fun setDataAndEvent() {
        val intent = Intent(this, AIDLService::class.java)
        bindService(intent,mConn , Context.BIND_AUTO_CREATE)
    }
    private val mConn =object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            iPerson = IPersonAidlInterface.Stub.asInterface(service)
            isConn = true
            connStatus.text = "连接状态：connected!$name"
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isConn = false
            connStatus.text = "连接状态：disconnected!$name"
        }
    }

    override fun onDestroy() {
        unbindService(mConn)
        unbindService(mPayConn)
        super.onDestroy()
    }
    fun btnClick(view: View) {
        when(view.id ){
            R.id.aidl_button8 ->{
                iPerson?.name = "this client set info"
            }
           R.id.aidl_button9 ->{
                textView.text = iPerson?.name
            }
           R.id.aidl_button10 ->{
               DialogUtils.show(this,"支付中...")
               val intent = Intent(this, ThirdPayService::class.java)
               intent.action = "com.alibaba.alipay.third_pay"//复制服务端的action 和包名,建議提取至全局
               intent.addCategory(Intent.CATEGORY_DEFAULT)
               intent.setPackage("demo.xy.com.xytdcq")
               bindService(intent,mPayConn , Context.BIND_AUTO_CREATE)
            }
        }
    }
    private val mPayConn =object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtil.d("onServiceConnected")
            iCallPayAidlInterface = ICallPayAidlInterface.Stub.asInterface(service)
            isConn = true
            connStatus.text = "支付中：connected!$name"
            iCallPayAidlInterface?.requestPay("568652",200.0f,PayResult())
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isConn = false
            connStatus.text = "调起支付失败"
        }
    }
    inner class PayResult:IPayResultAidlInterface.Stub(){
        override fun onPaySuccess() {
            LogUtil.e("AIDLActivity onPaySuccess")
            runOnUiThread {
                DialogUtils.dismiss()
                ToastUtil.showToast(this@AIDLActivity,"onPaySuccess")
                unbindService(mPayConn)
            }

        }

        override fun onPayFaild(errorCode: Int, msg: String?) {
            LogUtil.e("AIDLActivity onPayFaild$msg")
            runOnUiThread {
                DialogUtils.dismiss()
                ToastUtil.showToast(this@AIDLActivity,"onPayFaild$msg")
                unbindService(mPayConn)
            }
        }

    }
}
