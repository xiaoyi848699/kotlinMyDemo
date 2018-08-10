package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * 橡皮擦（与画布背景色相同的Path）
 * <p/>
 * Created by Administrator on 2015/6/24.
 */
public class MyEraser extends Action {

    private Path path;
    private Paint paint;
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public MyEraser(Float x, Float y, Integer color, Integer size) {
        super(x, y, color, size);
        path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
        this.size = size;//重新赋值
    }

//    @Override
//    public boolean isSequentialAction() {
//        return false;//移动时也让触发擦除
//    }

    public void onDraw(Canvas canvas) {
        if (null == paint){
            paint = new Paint();
            //paint.setAlpha(0);
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStyle(Paint.Style.STROKE);
            //使Paint的笔触更加圆滑一点
            paint.setStrokeJoin(Paint.Join.ROUND);
            //使Paint的连接处更加圆滑一点
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            paint.setXfermode(xfermode);
        }
        float dx = Math.abs(stopX - startX);
        float dy = Math.abs(stopY - startY);
        if (dx >= 4 || dy >= 4) {
            path.quadTo(startX, startY, (stopX + startX) / 2, (stopY + startY) / 2);
            startX = stopX;
            startY = stopY;
        }
        canvas.drawPath(path, paint);
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
