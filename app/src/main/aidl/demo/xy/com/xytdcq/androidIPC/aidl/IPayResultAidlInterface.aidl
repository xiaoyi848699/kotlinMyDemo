// IPayResultAidlInterface.aidl
package demo.xy.com.xytdcq.androidIPC.aidl;

// Declare any non-default types here with import statements

interface IPayResultAidlInterface {
   /*
       支付成功的回调
       */
       void onPaySuccess();
   		 /*
       支付失败的回调
       */
       void onPayFaild(in int errorCode , in String msg);
}
