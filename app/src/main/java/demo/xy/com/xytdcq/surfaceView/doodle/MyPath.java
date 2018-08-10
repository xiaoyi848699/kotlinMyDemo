package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 路径
 * <p/>
 * Created by Administrator on 2015/6/24.
 */
public class MyPath extends Action {
    private Path path;
    private Paint paint;

    public MyPath(Float x, Float y, Integer color, Integer size) {
        super(x, y, color, size);
        path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
    }

//    @Override
//    public boolean isSequentialAction() {
//        return true;
//    }

    public void onDraw(Canvas canvas) {
        if (canvas == null) return;
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }

        float dx = Math.abs(stopX - startX);
        float dy = Math.abs(stopY - startY);
        if (dx >= 4 || dy >= 4) {
            path.quadTo(startX, startY, (stopX + startX) / 2, (stopY + startY) / 2);
            startX = stopX;
            startY = stopY;
        }
//        path.lineTo(mx, my);
        canvas.drawPath(path, paint);
//        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void onEnd(Canvas canvas) {
        path.lineTo(startX, startY);
        canvas.drawPath(path, paint);
    }

    public void onMove(float mx, float my) {
//        path.lineTo(mx, my);
        stopX = mx;
        stopY = my;
    }

}
