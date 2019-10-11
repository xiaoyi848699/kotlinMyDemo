package demo.xy.com.xytdcq.androidIPC.messenger

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.*




class MessengerService:Service(){
    private val MSG_FROM_CLIENT = 0x10001
    private val MSG_TO_CLIENT = 0x10002
    private var sum:Int = 0
    private val mMessenger = Messenger(@SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msgfromClient: Message) {
            val msgToClient = Message.obtain(msgfromClient)//返回给客户端的消息
            when (msgfromClient.what) {
                //msg 客户端传来的消息
                MSG_FROM_CLIENT -> try {
                    //模拟耗时
                    Thread.sleep(1000)

                    //传递数据
                    val toClicentDate = Bundle()
                    toClicentDate.putString("info", "MessengerService reply :Hellow world $sum")
                    msgToClient.data = toClicentDate
                    msgToClient.what = MSG_TO_CLIENT

                    //传回Client
                    msgfromClient.replyTo.send(msgToClient)
                    sum++

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }

            super.handleMessage(msgfromClient)
        }
    })
    override fun onBind(intent: Intent?): IBinder? {
        return mMessenger.binder
    }

}