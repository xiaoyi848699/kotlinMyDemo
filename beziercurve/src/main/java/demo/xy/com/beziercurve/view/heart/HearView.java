package demo.xy.com.beziercurve.view.heart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class HearView extends AdvancePathView {

    public HearView(Context context) {
        super(context);
    }

    public HearView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HearView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onDraw(Canvas canvas) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointFStart = new PointF();
        pointLeft = new PointF();
        pointRight = new PointF();
        pointFEnd = new PointF();

        pointFStart.x = getMeasuredWidth() / 2-bitmap.getWidth()/2;
        pointFStart.y = getMeasuredHeight() - bitmap.getHeight();

        pointFEnd.y = 0;
        pointFEnd.x = random.nextFloat()*getMeasuredWidth();

        pointLeft.x = random.nextFloat()*getMeasuredWidth();
        pointLeft.y = random.nextFloat()*getMeasuredHeight()  / 2;

        pointRight.x = getMeasuredWidth() - pointLeft.x;
        pointRight.y = random.nextFloat()*getMeasuredHeight() / 2+getMeasuredHeight()/2;
        Log.i("TAG","出发了");
        addHeart();
        return true;
    }

    @Override
    protected void initPoint() {

    }


}
