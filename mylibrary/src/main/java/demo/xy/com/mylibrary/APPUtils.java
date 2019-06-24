package demo.xy.com.mylibrary;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;
import java.util.Locale;

import demo.xy.com.mylibrary.log.LogUtil;

/**
 * APP相关
 * Created by xy on 2018/3/22.
 */
public class APPUtils {
    /**
     * 判断app是否在运行
     * @param context
     * @param activitySimpleName MainActivity.class.getSimpleName()
     * @return
     */
    public static boolean isAppRunning(Context context,String activitySimpleName) {
        String packageName = context.getPackageName();
        String baseActivityClassName = null;
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(
                android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            baseActivityClassName = runningTaskInfos.get(0).baseActivity.getClassName();
        }
        LogUtil.e("base==" + baseActivityClassName);
        if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(baseActivityClassName) &&
                baseActivityClassName.startsWith(packageName)) {
            if (baseActivityClassName.contains(activitySimpleName))
                return true;
        }
        return false;
    }

    /**
     * 判断app是否在后台运行
     * @param context
     * @return
     */
    public static boolean IsForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断服务是否在运行
     * @param context
     * @param servicePath net.xy.example.data.service.UpdateService
     * @return
     */
    public static boolean  checkUpdateServerIsRun(Context context,String servicePath)
    {
        // 获取Activity管理器
        ActivityManager activityManger = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        // 从窗口管理器中获取正在运行的Service
        List<ActivityManager.RunningServiceInfo> serviceList = activityManger
                .getRunningServices(30);
        // 判断app服务是否已经在运行，如果是 则先停止服务 防止后面查询数据时超时获取其他异常
        if (serviceList != null && serviceList.size() > 0)
        {
            for (int i = 0; i < serviceList.size(); i++)
            {
                if (servicePath.equals(serviceList.get(i).service.getClassName()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 后台换起APP
     * @param activity
     */
    public static void moveTaskToFront(Activity activity) {
        //后台换起
        if(IsForeground(activity) == false)
        {
            ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE) ;
            am.moveTaskToFront(activity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
        }
    }


    /**
     * 获取系统语言
     * @param context
     * @return
     */
    public static String getSystemLanguage(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getLanguage();
//        if(null != lang && (lang.toLowerCase().contains("zh"))){//中文
//            return 0;
//        }
//        return 1;
    }
    /**
     * 获取当前进程名
     *
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }
            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    /**
     * 是否是平板
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     *  获取ApiKey
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }


}
