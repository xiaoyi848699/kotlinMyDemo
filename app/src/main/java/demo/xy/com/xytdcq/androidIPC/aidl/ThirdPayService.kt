package demo.xy.com.xytdcq.androidIPC.aidl

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.RemoteException
import android.text.TextUtils
import demo.xy.com.mylibrary.log.LogUtil


class ThirdPayService :Service(){
    private var thirdPartPay: ThirdPartPayImpl? = null
    /**
     * 定义支付宝的支付 服务逻辑动作
     */
    inner class PayAction : Binder() {

        /**
         * 实际的支付是比较复杂的，比如说加密，向服务器发起请求，等待服务器的结果，多次握手等
         *
         *
         * 支付方法
         */
        fun pay(payMoney: Float) {
            LogUtil.e("ThirdPayService pay money is --->$payMoney")
            LogUtil.e("ThirdPayService pay thirdPartPay --->$thirdPartPay")

            if (thirdPartPay != null) {
                //回调告诉远程第三方 支付成功
                thirdPartPay?.onPaySuccess()
            }
        }

        /**
         * 用户点击界面上的取消/退出
         */
        fun onUserCancel() {
            LogUtil.e("ThirdPayService onUserCancel")
            LogUtil.e("ThirdPayService onUserCancel --->$thirdPartPay")
            if (thirdPartPay != null) {
                //回调告诉远程 支付失败
                thirdPartPay?.onPayFaild(-1, "user cancel pay...")
            }
        }
    }
    private class ThirdPartPayImpl(val ctx: Context): ICallPayAidlInterface.Stub() {
        var payCallBack : IPayResultAidlInterface?=null

        override fun requestPay(orderInfo: String?, payMoney: Float, callBack: IPayResultAidlInterface?) {
            this.payCallBack = callBack

            LogUtil.e("requestPay --->orderInfo:$orderInfo payMoney:$payMoney")

            //第三方应用发起请求，拉起 打开一个支付页面
            val intent = Intent()
            intent.setClass(ctx, ThirdPayActivity::class.java!!)
            intent.putExtra("orderInfo", orderInfo)
            intent.putExtra("payMoney", payMoney)
            //新的 task 中打开
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx?.startActivity(intent)
        }

        /**
         * 定义相同的方法，进行回调 ，给外部调用
         */
        fun onPaySuccess() {
            LogUtil.e("ThirdPartPayImpl onPaySuccess$payCallBack")
            try {
                payCallBack?.onPaySuccess()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }

        fun onPayFaild(errorCode: Int, errorMsg: String) {
            LogUtil.e("ThirdPartPayImpl onPayFaild$payCallBack")
            try {
                payCallBack?.onPayFaild(errorCode, errorMsg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }

    }
    override fun onBind(intent: Intent?): IBinder? {
        val action = intent?.action
        LogUtil.e("onBind action --->$action")
        if (!TextUtils.isEmpty(action)) {
            if ("com.alibaba.alipay.third_pay" == action) {
                //通过 action 说明这是第三方要求我们支付宝进行支付
                thirdPartPay = ThirdPartPayImpl(this@ThirdPayService)
                //提取指全局供外部交互时调用
                return thirdPartPay
            }
        }
        //PayActivity 回绑服务 返回的对象 进行交互，无指定action 默认返回此对象
        return PayAction()
    }


}