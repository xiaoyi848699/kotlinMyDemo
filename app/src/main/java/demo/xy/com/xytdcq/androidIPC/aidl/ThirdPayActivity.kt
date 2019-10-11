package demo.xy.com.xytdcq.androidIPC.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import demo.xy.com.mylibrary.log.LogUtil
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.androidIPC.aidl.ThirdPayService.PayAction
import demo.xy.com.xytdcq.base.BaseActivity

/**
 * 第三方支付界面
 */
class ThirdPayActivity : BaseActivity() {

    private var payAction:PayAction?=null

    override fun getLayout(): Int {
        return R.layout.activity_third_pay
    }
    @BindView(R.id.pay_main_layout) lateinit var main_layout: RelativeLayout
    @BindView(R.id.pay_hint) lateinit var payHint: TextView
    private var orderInfo:String?=null
    private var payMoney:Float=0.0f
    override fun setDataAndEvent() {

        val display = windowManager.defaultDisplay // 为获取屏幕宽、高
        val window = window
        val windowLayoutParams = window.attributes // 获取对话框当前的参数值
        windowLayoutParams.width = display.width // 宽度设置为屏幕
        windowLayoutParams.height = display.height // 宽度设置为屏幕
        //状态栏文字根据状态栏颜色变化成深色或者淡色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        orderInfo =  intent.getStringExtra("orderInfo")
        payMoney =  intent.getFloatExtra("payMoney",0.0f)
        payHint.text = "id:$orderInfo money:$payMoney"
        LogUtil.e("come ThirdPayActivity $payHint.text")
        //绑定本进程的支付服务进行回调
        val intent = Intent(this, ThirdPayService::class.java)
        intent.setPackage("demo.xy.com.xytdcq")
        bindService(intent,mResultConn , Context.BIND_AUTO_CREATE)

    }
    override fun onDestroy() {
        overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom)
        unbindService(mResultConn)
        super.onDestroy()
    }
    fun btnClick(view:View){
        if(view.id == R.id.pay_comfirm){
            LogUtil.e("btnClick pay_comfirm")
            payAction?.pay(payMoney)
            finish()
        }else if(view.id == R.id.pay_main_layout){
            LogUtil.e("btnClick onUserCancel")
            payAction?.onUserCancel()
            finish()
        }
    }
    private val mResultConn =object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogUtil.e("third pay activity onServiceConnected")
            payAction = service as PayAction
//            payAction = PayAction(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            LogUtil.e("third pay activity onServiceDisconnected")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        LogUtil.e("onBackPressed onUserCancel")
        payAction?.onUserCancel()

    }

}
