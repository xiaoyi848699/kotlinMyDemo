package demo.xy.com.xytdcq.surfaceView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
    private boolean mIsCanMove = false;

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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsCanMove) {
            return true; // 自己处理
        }
        return false;
    }
}
