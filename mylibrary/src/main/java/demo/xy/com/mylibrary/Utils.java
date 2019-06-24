package demo.xy.com.mylibrary;

import android.content.Context;

import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.mylibrary.log.Write;

/**
 * Created by xy on 2018/12/6.
 */
public class Utils {
    public static void initLog(Context context,String logSavePath) {
        Write.init(context,logSavePath);
        LogUtil.setTAG("LogUtil");
    }
    public static void initExceptionHandler(Context context,Class<?>  restartActivity) {
        ExceptionHandler crashHandler = ExceptionHandler.getInstance();
        crashHandler.init(context,restartActivity);
    }
}
