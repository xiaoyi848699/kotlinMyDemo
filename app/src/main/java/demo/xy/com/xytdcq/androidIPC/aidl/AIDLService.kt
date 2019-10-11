package demo.xy.com.xytdcq.androidIPC.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import demo.xy.com.mylibrary.log.LogUtil


class AIDLService :Service(){
    private var saveName: String? = null
    private var  sum:Int = 0
    private val binder = object : IPersonAidlInterface.Stub() {
        @Throws(RemoteException::class)
        override fun setName(s: String) {
            saveName = s
            sum = 0
//            ToastUtil.showToast(this@AIDLService,"client set info:$saveName")
            LogUtil.e("AIDLService setName$s")
        }

        @Throws(RemoteException::class)
        override fun getName(): String {
//            ToastUtil.showToast(this@AIDLService,"get info success")
            LogUtil.e("AIDLService getName")
            sum++
            return "Service reply:$saveName  $sum"
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

}