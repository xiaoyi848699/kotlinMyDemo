package demo.xy.com.xytdcq.androidIPC.messenger

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.View
import android.widget.TextView
import butterknife.BindView
import demo.xy.com.mylibrary.dialog.DialogUtils
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.base.BaseActivity


class MessengerActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_messenger
    }
    @BindView(R.id.textView) lateinit var textView: TextView
    @BindView(R.id.conn_status) lateinit var connStatus: TextView
    private val MSG_FROM_CLIENT = 0x10001
    private val MSG_TO_CLIENT = 0x10002
    private var isConn:Boolean = false
    private var mService:Messenger? = null
    private val mMessenger = Messenger(@SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msgFromServer: Message) {
            when (msgFromServer.what) {
                //msg 服务器端传来的消息
                MSG_TO_CLIENT -> {
                    val data = msgFromServer.data
                    textView.text = "服务器返回内容:\n" +
                            data.get("info")
                    DialogUtils.dismiss()
                }
            }
            super.handleMessage(msgFromServer)
        }
    })
    override fun setDataAndEvent() {
        val intent = Intent()
        intent.action = "android.intent.action.MessengerService"
        intent.setPackage("demo.xy.com.xytdcq")//从 Android 5.0开始 隐式Intent绑定服务的方式已不能使用,所以这里需要设置Service所在服务端的包名
        bindService(intent, mConn, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(mConn)
        DialogUtils.dismiss()
        super.onDestroy()
    }
    private val mConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = Messenger(service)
            isConn = true
            connStatus.text = "连接状态：connected!"
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            isConn = false
            connStatus.text = "连接状态：disconnected!"
        }
    }
    fun btnClick(view: View) {
        if(view.id == R.id.button8){
            var msgFromClient:Message =  Message.obtain()
            msgFromClient.what = MSG_FROM_CLIENT
            msgFromClient.replyTo = mMessenger
            if (isConn)
            {
                //往服务端发送消息
                try {
                    DialogUtils.show(this,"Waiting")
                    mService?.send(msgFromClient)
                } catch ( e: RemoteException) {
                    DialogUtils.dismiss()
                    e.printStackTrace()
                }
            }
        }
    }


}
