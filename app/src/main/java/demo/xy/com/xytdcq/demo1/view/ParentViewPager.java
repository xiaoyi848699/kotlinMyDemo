package demo.xy.com.xytdcq.demo1.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ParentViewPager extends ViewPager {
	private float downX = 0.0f;
	private boolean isScrollEnd = false;
	private int index = 0;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isScrollEnd() {
		return isScrollEnd;
	}

	public void setScrollEnd(boolean isScrollEnd) {
		this.isScrollEnd = isScrollEnd;
	}

	public ParentViewPager(Context context) {
		super(context);
	}

	public ParentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * 外部拦截
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		Log.d("TAG", "onInterceptTouchEvent"+isScrollEnd);
		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			downX = arg0.getX();
		}else if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			//index可能不是在第0个界面
			if(isScrollEnd ||getIndex() != 0){
				if(getIndex() == 0 && arg0.getX()-downX>0){
					Log.d("TAG", "move right"+arg0.getX()+" "+downX);
					//向右(不拦截，下一级处理)
					return false;
				}else{
					Log.d("TAG", "move left");
					//向左(自己处理)
					return true;
				}
			}else{
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
