package demo.xy.com.mylibrary;

import android.content.Context;
import android.content.pm.PackageManager;

import demo.xy.com.mylibrary.log.LogUtil;

public class VersionUtils {
	        
	public static int getSDKVersionNumber() {  
	    int sdkVersion;  
	    try {  
	        sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
	    } catch (NumberFormatException e) {
	        sdkVersion = 0;  
	    }  
	    LogUtil.i("TAG", "sdkVersion:"+sdkVersion);
	    return sdkVersion;  
	}  
	/**
	 * 检测当前系统版本是否大于等于checkVersion版本
	 * @param checkVersion 对比检测版本
	 * @return
	 */
	public static boolean checkSDKVersion(int checkVersion){
		return getSDKVersionNumber() >= checkVersion ? true : false; 
	}

	/**
	 * 获取app版本名
	 */
	public static String getAppVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "V1.0.0";
	}
	/**
	 * 返回程序包名
	 *
	 * @param ctx
	 * @return 获取失败返回""，否则返回程序包名
	 */
	public static String getPackageName(Context ctx) {
		if (ctx == null)
			return "";
		return ctx.getPackageName();
	}

	/**
	 * 获取app版本号
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		int version = 100;
		try {
			version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			return version;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		LogUtil.e("cant getAppVersionCode"+version);
		return version;
	}

}
