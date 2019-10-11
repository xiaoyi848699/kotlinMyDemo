package demo.xy.com.xytdcq.base;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;

import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import demo.xy.com.mylibrary.APPUtils;
import demo.xy.com.mylibrary.Utils;
import demo.xy.com.mylibrary.storage.DevMountInfo;
import demo.xy.com.xytdcq.MainActivity;
import demo.xy.com.xytdcq.uitls.LogUtil;


/**
 * Created by dong on 2017/6/30.
 */

public class App extends Application {
    private static App instance;
    private DevMountInfo mountInfo;
    private String extsdcardPath;
    private String extneralPath;
    private String savePath;
//    private Activity currentActivity;
    private List<Activity> activities = new ArrayList<>();

//    public Activity getCurrentActivity() {
//        if(null == currentActivity){
//            LogUtil.e("currentActivity is null ");
////            clearActivity();
//            return null;
//        }
//        return currentActivity;
//    }

//    public void setCurrentActivity(Activity currentActivity) {
//        this.currentActivity = currentActivity;
//    }

    public String getSavePath() {
        return savePath;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (inMainProcess()) {
            //初始化存储路径
            mountInfo = DevMountInfo.getInstance();
            mountInfo.init(this);
            extsdcardPath = mountInfo.getExternalSDCardPath();
            File mFile = Environment.getExternalStorageDirectory();
            if (null != Environment.getExternalStorageDirectory()) {
                extneralPath = mFile.getPath();
            }
            if(!TextUtils.isEmpty(extneralPath)){
                savePath = extneralPath;
            }else if(!TextUtils.isEmpty(extsdcardPath)){
                savePath = extsdcardPath;
            }else {
                savePath = "";
            }
            // 加载系统默认设置，字体不随用户设置变化
            Resources res = super.getResources();
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());

            //初始化异常捕获
            Utils.initExceptionHandler(getApplicationContext(), MainActivity.class);

            //初始化日志SD卡缓存路径
            String saveLogPath = savePath+ "/writeLog/";
            Utils.initLog(getApplicationContext(),saveLogPath);

        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);



    }

    public static synchronized App getInstance() {
        return instance;
    }

    private boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = APPUtils.getProcessName(this);
        demo.xy.com.mylibrary.log.LogUtil.e("packageName:"+packageName+",processName:"+processName);
        return packageName.equals(processName);
    }

    public  void  addActivity(Activity activity){
        if(null != activities){
            activities.add(activity);
        }else{
            activities = new ArrayList<>();
            activities.add(activity);
        }
    }
    //activity移除
    public  void  removeActivity(Activity activity){
        if(null != activities){
            activities.remove(activity);
        }
    }
    public  void  removeActivity(String activity){
        for (int i = activities.size()-1 ;i >= 0 ;i--) {
            Activity mActivity = activities.get(i);
            LogUtil.e("removeActivity："+mActivity.toString()+","+mActivity.toString().contains(activity));
            if(mActivity.toString().contains(activity)){
                mActivity.finish();
                break;
            }
        }
    }
    //设置到上一个activity
//    public  void  showLastActivity(Activity activity){
//        for (int i = activities.size()-1 ;i >= 0 ;i--) {
//            Activity mActivity = activities.get(i);
//            if(!mActivity.toString().equals(activity.toString())){
//                setCurrentActivity(mActivity);
//                break;
//            }
//        }
//    }
    //清除activity
    public  void  clearActivity(){
        if(null != activities){
            for (int i = activities.size()-1 ;i >= 0 ;i--){
                Activity activity = activities.get(i);
                if(null != activity){
                    activity.finish();
                    activities.remove(i);
                }else{
                    activities.remove(i);
                }
            }
        }
    }
    //清除除指定界面外的其他界面
    public  void  clearOtherActivity(Activity activity){
        if(null != activities){
            if(null != activities){
                for (int i = activities.size()-1 ;i >= 0 ;i--){
                    Activity mActivity = activities.get(i);
                    if(null != mActivity && mActivity.toString().equals(activity.toString())){
                        continue;
                    }else{
                        mActivity.finish();
                        activities.remove(i);
                    }
                }
            }
        }
    }
}
