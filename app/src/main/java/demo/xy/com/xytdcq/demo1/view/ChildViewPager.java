package demo.xy.com.xytdcq.demo1.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class ChildViewPager extends ViewPager {
	//	// 触摸时按下的点 **/
//	PointF downP = new PointF();
//	// 触摸时当前的点 **/
//	PointF curP = new PointF();
	private float downX;
	private  boolean isEnd = false;

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	// OnSingleTouchListener onSingleTouchListener;
	public ChildViewPager(Context context) {
		super(context);
	}

	public ChildViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * 内部拦截
	 */
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent arg0) {
//		Log.d("TAG", "dispatchTouchEvent"+isEnd());
//		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
//			downX = arg0.getX();
//			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//			getParent().requestDisallowInterceptTouchEvent(true);
//		}else if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
//			if(isEnd()){
//				//判断当前item位置，以及滑动的方向
//				if(arg0.getX()-downX>0){
//					Log.d("TAG", "child move right");
//					//向右
//					getParent().requestDisallowInterceptTouchEvent(true);
//				}else{
//					//向左
//					Log.d("TAG", "child move left");
//					getParent().requestDisallowInterceptTouchEvent(false);
//				}
//			}else{
//				getParent().requestDisallowInterceptTouchEvent(true);
//			}
//		}
//		return super.dispatchTouchEvent(arg0);
//	}
	/**
	 * 如果这样写有些问题
	 */
//	@Override
//	public boolean onTouchEvent(MotionEvent arg0) {
//		// TODO Auto-generated method stub
//		// 每次进行onTouch事件都记录当前的按下的坐标
//		if (getChildCount() <= 1) {
//			return super.onTouchEvent(arg0);
//		}
//
//		Log.d("TAG", getCurrentItem()+""+getChildCount());
//		curP.x = arg0.getX();
//		curP.y = arg0.getY();
//
//		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
//
//			// 记录按下时候的坐标
//			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
//			downP.x = arg0.getX();
//			downP.y = arg0.getY();
//			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
////			getParent().requestDisallowInterceptTouchEvent(true);
//		}
//
//		if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
//			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
////			getParent().requestDisallowInterceptTouchEvent(true);
//			if(isEnd()){
//				//判断当前item位置，以及滑动的方向
//				if(arg0.getX()-downP.x>0){
//					//向右
//					getParent().requestDisallowInterceptTouchEvent(true);
//				}else{
//					//向左
//					return super.onTouchEvent(arg0);
//				}
//			}else{
//				getParent().requestDisallowInterceptTouchEvent(true);
//			}
//		}
//
//		if (arg0.getAction() == MotionEvent.ACTION_UP
//				|| arg0.getAction() == MotionEvent.ACTION_CANCEL) {
//			// 在up时判断是否按下和松手的坐标为一个点
//			// 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
////			getParent().requestDisallowInterceptTouchEvent(false);
//			if (downP.x == curP.x && downP.y == curP.y) {
//
//				return true;
//			}
//			if(isEnd()){
//				//判断当前item位置，以及滑动的方向
//				if(arg0.getX()-downP.x>0){
//					//向右
//					getParent().requestDisallowInterceptTouchEvent(true);
//				}else{
//					//向左
//					return super.onTouchEvent(arg0);
//				}
//			}else{
//				getParent().requestDisallowInterceptTouchEvent(true);
//			}
//
//		}
//		super.onTouchEvent(arg0); // 注意这句不能 return super.onTouchEvent(arg0);
//									// 否则触发parent滑动
//		return true;
//	}

}
