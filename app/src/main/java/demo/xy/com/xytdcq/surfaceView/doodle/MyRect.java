package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 方框
 */
public class MyRect extends Action {
    private Paint paint;

    public MyRect(Float x, Float y, Integer color, Integer size) {
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
        canvas.drawRect(startX, startY, stopX, stopY, paint);
    }

    public void onMove(float mx, float my) {
        stopX = mx;
        stopY = my;
    }
}
