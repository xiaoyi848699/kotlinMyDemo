package demo.xy.com.xytdcq.demo1.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
/**
 * childViewPager不在第一个时
 *
 */
public class ParentViewPager2 extends ViewPager {
	private float downX = 0.0f;
	private int childViewPagerIndex = 1;
	/**
	 * childViewPager在最后一个
	 */
	private boolean isScrollEnd = false;
	/**
	 * childViewPager在第一个
	 */
	private boolean isScrollFrist = false;
	private int index = 0;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		Log.d("TAG", "setIndex "+index);
		this.index = index;
	}

	public boolean isScrollEnd() {
		return isScrollEnd;
	}

	public void setScrollEnd(boolean isScrollEnd) {
		this.isScrollEnd = isScrollEnd;
	}

	public boolean isScrollFrist() {
		return isScrollFrist;
	}

	public void setScrollFrist(boolean isScrollFrist) {
		this.isScrollFrist = isScrollFrist;
	}

	public ParentViewPager2(Context context) {
		super(context);
	}

	public ParentViewPager2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * 外部拦截
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		Log.d("TAG", "onInterceptTouchEvent"+isScrollEnd+isScrollFrist+getIndex()+getCurrentItem());
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			downX = arg0.getX();
		}else if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			//index可能不是在第childViewPagerIndex个界面
			if(getIndex() != childViewPagerIndex){
				Log.d("TAG", "no in "+childViewPagerIndex);
				return true;
			}
			if(isScrollEnd){
				if(getIndex() == childViewPagerIndex && arg0.getX()-downX>0){
					Log.d("TAG", "1move right"+arg0.getX()+" "+downX);
					//向右(不拦截，下一级处理)
					return false;
				}else{
					Log.d("TAG", "1move left");
					//向左(自己处理)
					return true;
				}
			}else if(isScrollFrist){
				if(getIndex() == childViewPagerIndex && arg0.getX()-downX>0){
					Log.d("TAG", "2move right"+arg0.getX()+" "+downX);
					//向右(自己处理)
					return true;
				}else{
					Log.d("TAG", "2move left");
					//向左(不拦截，下一级处理)
					return false;
				}
			}else{
				Log.d("TAG", "no deal");
				return false;
			}
		}
		return super.onInterceptTouchEvent(arg0);
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {

		return super.onTouchEvent(arg0);
	}
}
