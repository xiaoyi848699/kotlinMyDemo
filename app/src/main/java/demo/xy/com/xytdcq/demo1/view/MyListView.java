package demo.xy.com.xytdcq.demo1.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView{
	private float downX;
	private float downY;
	private boolean canScrollH = true;

	public boolean isCanScrollH() {
		return canScrollH;
	}

	public void setCanScrollH(boolean canScrollH) {
		this.canScrollH = canScrollH;
	}

	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * dispatchTouchEvent 和 onTouchEvent写法都可以
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.d("TAG", "listview dispatchTouchEvent");
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downX = ev.getX();
			downY = ev.getY();
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			Log.d("TAG", "listview ACTION_DOWN"+downX+"  "+downY);
			getParent().requestDisallowInterceptTouchEvent(true);
		}else if(ev.getAction() == MotionEvent.ACTION_MOVE){
			float moveX = Math.abs(ev.getX()- downX);
			float moveY = Math.abs(ev.getY()- downY);
			downX = ev.getX();
			downY = ev.getY();
//			Log.d("TAG", "listview ACTION_MOVE"+ev.getX()+"  "+ev.getY());
			if(isCanScrollH() && moveY < moveX && moveX > 5){
				getParent().requestDisallowInterceptTouchEvent(false);
				Log.d("TAG", "listview requestDisallowInterceptTouchEvent false"+moveY+"  "+moveX);
			}else{
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		}else if(ev.getAction() == MotionEvent.ACTION_UP||ev.getAction() == MotionEvent.ACTION_CANCEL){
			downX = ev.getX();
			downY = ev.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		return super.dispatchTouchEvent(ev);
	}
//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		Log.d("TAG", "listview onTouchEvent");
//		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//			downX = ev.getX();
//			downY = ev.getY();
//			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//			getParent().requestDisallowInterceptTouchEvent(true);
//		}else if(ev.getAction() == MotionEvent.ACTION_MOVE){
//			float moveX = Math.abs(ev.getX()- downX);
//			float moveY = Math.abs(ev.getY()- downY);
//			if(moveY < moveX){
//				getParent().requestDisallowInterceptTouchEvent(false);
//				Log.d("TAG", "listview requestDisallowInterceptTouchEvent false");
//			}else{
//				getParent().requestDisallowInterceptTouchEvent(true);
//			}
//		}else if(ev.getAction() == MotionEvent.ACTION_UP||ev.getAction() == MotionEvent.ACTION_CANCEL){
//			getParent().requestDisallowInterceptTouchEvent(true);
//		}
//		return super.onTouchEvent(ev);
//	}

}
