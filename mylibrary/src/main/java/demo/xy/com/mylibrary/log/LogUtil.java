package demo.xy.com.mylibrary.log;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

import demo.xy.com.mylibrary.BuildConfig;


/**
 * 日志Log输出
 */
public class LogUtil {
    private static String TAG = "LogUtil";

    public static void setTAG(String TAG) {
        if(!TextUtils.isEmpty(TAG)){
            LogUtil.TAG = TAG;
        }
    }

    public static void i(String message){
        if (BuildConfig.DEBUG){
            Log.i(TAG, "==>" + message);
        }
    }
    public static void d(String message){
        if (BuildConfig.DEBUG){
            Log.d(TAG, "==>" + message);
        }
    }
    public static void e(String message){
        if (BuildConfig.DEBUG){
            Log.e(TAG, "==>" + message);
        }
    }
    public static void w(String message){
        if (BuildConfig.DEBUG){
            Log.w(TAG, "==>" + message);
        }
    }
    public static void i(String tag, String message){
        if (BuildConfig.DEBUG){
            if (null == tag || "".equals(tag)){
                i(message);
            }else{
                Log.i(tag, "==>"+message);
            }
        }
    }
    public static void d(String tag, String message){
        if (BuildConfig.DEBUG){
            if (null == tag || "".equals(tag)){
                d(message);
            }else{
                Log.d(tag, "==>" + message);
            }
        }
    }
    public static void e(String tag, String message){
        if (BuildConfig.DEBUG){
            if (null == tag || "".equals(tag)){
                e(message);
            }else{
                Log.e(tag, "==>" + message);
            }
        }
    }
    public static void w(String tag, String message){
        if (BuildConfig.DEBUG){
            if (null == tag || "".equals(tag)){
                w(message);
            }else{
                Log.w(tag, "==>" + message);
            }
        }
    }

    /**
     * map遍历输出
     * @param params
     */
    public static void printParams(Map<String,String> params){
        if (BuildConfig.DEBUG){
            StringBuffer buffer = new StringBuffer();
            for (Map.Entry<String,String> map : params.entrySet()){
                buffer.append(map.getKey() + ":" + map.getValue() + "<>");
            }
            i("http->",buffer.toString());
        }
    }
}
