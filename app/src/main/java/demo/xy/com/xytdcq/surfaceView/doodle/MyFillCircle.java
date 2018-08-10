package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 实心圆
 */
public class MyFillCircle extends Action {
    private float radius;
    private Paint paint;
    public MyFillCircle(Float x, Float y, Integer color, Integer size, float radius) {
        super(x, y, color, size);
        this.radius = radius;
    }

    public void onDraw(Canvas canvas) {
        if (null == paint){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setStrokeWidth(size);
        }
        canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius, paint);
    }

    public void onMove(float mx, float my) {
        stopX = mx;
        stopY = my;
        radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                + (my - startY) * (my - startY))) / 2);
    }
}
