package demo.xy.com.xytdcq.uitls;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import demo.xy.com.xytdcq.BuildConfig;

public class ToastUtil {
    private ToastUtil(){}
    private static final String TAG = "ToastUtil";
    //----------------- toast 子线程更新工具  ------------
    public static void runOnUiShortToast(final Activity context, final String msg){
        if (TextUtils.isEmpty(msg)) return;
        if ("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
        }else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public static void runOnUiLongToast(final Activity context, final String msg){
        if (TextUtils.isEmpty(msg)) return;
        if ("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
        }else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //----------------- toast 子线程更新工具  ------------




    //----------------- toast 工具------------
    public static void shortShowTest(Context context){
        if (BuildConfig.DEBUG){
            Toast.makeText(context,TAG, Toast.LENGTH_SHORT).show();
        }
    }
    public static void longShowTest(Context context){
        if (BuildConfig.DEBUG){
            Toast.makeText(context,TAG, Toast.LENGTH_LONG).show();
        }
    }

    public static void shortShowTest(Context context, CharSequence message){
        if (BuildConfig.DEBUG){
            if (null == message||"".equals(message)){
                shortShowTest(context);
            }else{
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static void longShowTest(Context context, CharSequence message){
        if (BuildConfig.DEBUG){
            if (null == message||"".equals(message)){
                longShowTest(context);
            }else{
                Toast.makeText(context,message, Toast.LENGTH_LONG).show();
            }
        }
    }
    //----------------- toast 工具------------
    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    private static Toast mToast;
    /**
     * 重复调用也不会重复提示
     * @param hint
     */
    public static void showOnly(Context context,String hint) {
        if(null == mToast){
            mToast = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
        }
        mToast.setText(hint);
        mToast.show();
    }
    /**
     *  销毁时释放内存，不然会导致内存泄漏
     */
    public static void clearOnlyToast() {
        if(null != mToast){
            mToast.cancel();
        }
        mToast = null;
    }
}
