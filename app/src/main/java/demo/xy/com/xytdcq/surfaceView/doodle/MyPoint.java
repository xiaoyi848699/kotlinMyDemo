package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 点
 */
public class MyPoint extends Action {
    private Paint paint;

    public MyPoint(Float x, Float y, Integer color, Integer size) {
        super(x, y, color, size);
    }

    @Override
    public void onStart(Canvas canvas) {
        super.onStart(canvas);
        onDraw(canvas);
    }

    public void onDraw(Canvas canvas) {
        if (null == canvas) return;
        if (null == paint){
            paint = new Paint();
            paint.setColor(color);
            paint.setStrokeWidth(size);
        }
        canvas.drawPoint(startX, startY, paint);
    }

    @Override
    public void onMove(float mx, float my) {

    }
}
