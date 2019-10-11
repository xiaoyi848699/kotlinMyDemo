// ICallPayAidlInterface.aidl
package demo.xy.com.xytdcq.androidIPC.aidl;
//使用要导入完整包名！！
import demo.xy.com.xytdcq.androidIPC.aidl.IPayResultAidlInterface;


interface ICallPayAidlInterface {
     /*
        发起支付请求 接口
        */
        void requestPay(String orderInfo, float payMoney,IPayResultAidlInterface callBack);
}
