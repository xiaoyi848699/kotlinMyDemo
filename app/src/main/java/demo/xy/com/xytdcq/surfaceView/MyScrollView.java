package demo.xy.com.xytdcq.surfaceView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import demo.xy.com.xytdcq.uitls.LogUtil;

public class MyScrollView extends ScrollView {
    private boolean mIsCanMove = false;
    private boolean mIsMove = false;

    public boolean ismIsCanMove() {
        return mIsCanMove;
    }

    public void setmIsCanMove(boolean mIsCanMove) {
        this.mIsCanMove = mIsCanMove;
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        try {
//            if (ev != null && (ev.getPointerCount() >= 2 || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_2_DOWN)) {
//                return true;
//            }
//        } catch (IllegalArgumentException ex) {
//            LogUtil.e("xiaoyi dispatchTouchEvent IllegalArgumentException" + ex.toString());
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        try {
//            if (ev != null && (ev.getPointerCount() >= 2 || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_2_DOWN)) {
//                return true;
//            }
//        } catch (IllegalArgumentException ex) {
//            LogUtil.e("xiaoyi onTouchEvent IllegalArgumentException" + ex.toString());
//        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        LogUtil.e("xiaoyi onInterceptTouchEvent" + ev.getAction());
//        try {
//            if (ev != null && (ev.getPointerCount() >= 2 || ev.getAction() == MotionEvent.ACTION_POINTER_DOWN || ev.getAction() == MotionEvent.ACTION_POINTER_2_DOWN)) {
//                mIsMove = true;
//                return true;
//            } else {
//                mIsMove = false;
//            }
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//            LogUtil.e("xiaoyi onInterceptTouchEvent IllegalArgumentException" + ex.toString());
//        }
        if (mIsCanMove || mIsMove) {
            return true; // 自己处理
        }
        return false;
    }
}
