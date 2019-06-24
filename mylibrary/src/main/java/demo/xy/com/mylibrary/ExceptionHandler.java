package demo.xy.com.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.lang.Thread.UncaughtExceptionHandler;

import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.mylibrary.log.Write;

/**
 * 全局异常处理: 当程序发生Uncaught异常时,由该类来接管处理
 *
 ExceptionHandler crashHandler = ExceptionHandler.getInstance();
 crashHandler.init(getApplicationContext());
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

	public static final String TAG = "ExceptionHandler";

	private UncaughtExceptionHandler mHandler;
	private static ExceptionHandler INSTANCE = new ExceptionHandler();
	private Context mContext;
	private static Class<?> restartActivity;

	// 单例
	public ExceptionHandler() {
	}

	public static ExceptionHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		LogUtil.e("ExceptionHandler Exception in........");
		try {
			// 异常处理
			StringBuilder sb = getErrorInfo(ex);
			Write.debug(sb.toString());
			new SharedPreferenceUtils(mContext).saveBoolean("has_exception",true);
			restartAPP(mContext,1000);
		} catch (Exception e) {
			LogUtil.e("ExceptionHandler Exception:"+e.getMessage());
			Write.debug("uncaughtException Exception:"+e.getMessage());
		}
	}

	@NonNull
	private StringBuilder getErrorInfo(Throwable ex) {
		StringBuilder sb = new StringBuilder();
		if(ex != null){
			StackTraceElement[] stackTraceElement= ex.getStackTrace();
			if(null != stackTraceElement){
				for (int i = 0; i <stackTraceElement.length && i < 8;i++ ){
					StackTraceElement stackTraceElements = stackTraceElement[i];
					sb.append("File="+stackTraceElements.getFileName()).append("\n")
							.append("ClassName="+stackTraceElements.getClassName()).append("\n")
							.append("Line="+stackTraceElements.getLineNumber()).append("\n")
							.append("Method="+stackTraceElements.getMethodName()).append("\n");
				}
			}
			sb.append("Message = "+ex.getMessage()).append("\n")
					.append("getStackTrace = " + ex.getStackTrace()).append("\n");
			if(BuildConfig.DEBUG){
				ex.printStackTrace();//输出到控制台
			}
		}else{
			sb.append("uncaughtException Throwable is null").append("\n");
		}
		return sb;
	}

	/**
	 * 重启整个APP
	 * @param context
	 * @param Delayed 延迟多少毫秒
	 */
	public static void restartAPP(Context context, long Delayed){
		LogUtil.e("ExceptionHandler restartAPP...");
		Toast.makeText(context,"重启一下", Toast.LENGTH_LONG).show();
		Intent intent=new Intent(context,restartActivity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		/**杀死整个进程**/
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void init(Context context,  Class<?>  restartActivity) {
		mContext = context;
		this.restartActivity = restartActivity;
		mHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
}
