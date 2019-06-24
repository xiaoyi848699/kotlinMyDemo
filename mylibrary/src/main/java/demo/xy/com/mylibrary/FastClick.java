package demo.xy.com.mylibrary;

import demo.xy.com.mylibrary.log.LogUtil;

public class FastClick {

	private static long lastClickTime = 0;
	public static boolean isFastClick(){
		long time = System.currentTimeMillis();
		if(Math.abs(time - lastClickTime) < 500){
			LogUtil.e("Cilck is to fast"+time+",lastClickTime"+lastClickTime);
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
