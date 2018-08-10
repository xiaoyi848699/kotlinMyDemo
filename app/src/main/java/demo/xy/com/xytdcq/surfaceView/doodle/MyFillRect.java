package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 实心方块
 */
public class MyFillRect extends Action {
    private Paint paint;

    public MyFillRect(Float x, Float y, Integer color, Integer size) {
        super(x, y, color, size);
    }

    public void onDraw(Canvas canvas) {
        if (null == paint){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
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
