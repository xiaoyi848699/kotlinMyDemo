package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 空心圆
 * <p/>
 * Created by Administrator on 2015/6/24.
 */
public class MyCircle extends Action {
    private float radius;
    private Paint paint;
    public MyCircle(Float x, Float y, Integer color, Integer size, float radius) {
        super(x, y, color, size);
        this.radius = radius;
    }

    public void onDraw(Canvas canvas) {
        if (null == paint){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setStrokeWidth(size);
        }
        //定义一个矩形区域
        RectF oval = new RectF(startX, startY, stopX, stopY);
        //矩形区域内切椭圆
        canvas.drawOval(oval, paint);
//        canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius, paint);
    }

    public void onMove(float mx, float my) {
        stopX = mx;
        stopY = my;
        radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                + (my - startY) * (my - startY))) / 2);
    }
}
