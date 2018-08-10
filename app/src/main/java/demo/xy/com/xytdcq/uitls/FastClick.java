package demo.xy.com.xytdcq.uitls;

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
