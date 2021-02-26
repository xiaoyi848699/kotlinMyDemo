package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 直线
 */
public class MyLine extends Action {
    private Paint paint;

    public MyLine(Float x, Float y, Integer color, Integer size) {
        super(x, y, color, size);
    }

    public void onDraw(Canvas canvas) {
        if (null == canvas) return;
        if (null == paint){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setStrokeWidth(size);
        }
        canvas.drawColor(selectColor);
        canvas.drawLine(startX + size/2, startY+ size/2, stopX+ size/2, stopY+ size/2, paint);
    }

    public void onMove(float mx, float my) {
        stopX = mx;
        stopY = my;
    }
}
