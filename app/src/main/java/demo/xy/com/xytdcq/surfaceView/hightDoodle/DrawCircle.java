package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;

import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.surfaceView.utils.PointUtils;

/**
 * 椭圆、圆
 */
public class DrawCircle extends BaseLinePath {

    private float radius;
    private Point centerPoint = new Point(0,0);
    private Point touchStart = new Point(0,0);
    private Point touchEnd = new Point(0,0);

    public DrawCircle(Context context) {
        super(context);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void relaseData() {

    }

    @Override
    public boolean isNeedInvalidateOnMoveStatusChane() {
        return false;
    }

    public void onDraw(Canvas canvas) {
        if (null == canvas) return;
        if (null == paint){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(size);
        }
        if (isSelect()) {
            paint.setShadowLayer(4,4,4, Color.GRAY);
        } else {
            paint.setShadowLayer(0,0,0, color);
            paint.setStrokeWidth(size);
            paint.setColor(color);
        }
        //定义一个矩形区域
        RectF oval = new RectF(touchStart.getX(), touchStart.getY(), touchEnd.getX(), touchEnd.getY());
        //矩形区域内切椭圆
        canvas.drawOval(oval, paint);
        if (isSelect()) {
            canvas.drawColor(0x30C7EDCC);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(1);
            paint.setShadowLayer(1,1,1,Color.GRAY);
            if ( startPoint != null && endPoint != null) {
                canvas.drawRect(1, 1, getWidth()-2, getHeight()-2, paint);
            }
        }
    }

    public void add(Point point) {
        points.add(point);
        invalidate();
    }

    @Override
    public void addAll(ArrayList<Point> points) {
        ArrayList<Point> newPoints = new ArrayList<>(points.size());
        int index = 0;
        for (Point p:points) {
            p.setX(p.getX() - startPoint.getX() + size/2); // 偏移 使得靠边的线不会出现一部分不可见
            p.setY(p.getY() - startPoint.getY() + size/2);
            if (index == 0) {
                touchStart = p;
                newPoints.add(p);
            } else if (index == points.size() -1) {
                touchEnd = p;
                newPoints.add(p);
            }
            index++;
        }
        radius = (float) ((Math.sqrt((touchEnd.getX() - touchStart.getX()) * (touchEnd.getX() - touchStart.getX())
            + (touchEnd.getY() - touchStart.getY()) * (touchEnd.getY() - touchStart.getY()))) / 2);
        this.points.addAll(newPoints);
        invalidate();
        // 发送数据

    }

    public boolean checkEraser(Point pointIn) {
        if (this.points == null || this.points.size() == 0) {
            return true;
        }
//        //通过矩形得到椭圆中心点
//        Point center = Width/2, Height/2
////得到椭圆
//        a = Width/2
//        b = Height/2
////触摸点 P (x,y)
//        F = (x - center.x)*(x - center.x)/(a*a) + (y - center.y)*(y - center.y)/(b*b)
////判断F > = 0.8 && F < = 1.2 即触摸到，删除椭圆
        float a = getWidth()/2;
        float b = getHeight()/2;
        Point center = new Point(a, b);
        float touchX = pointIn.getX() - startPoint.getX();
        float touchY = pointIn.getY() - startPoint.getY();
        double distance = (touchX - center.getX()) * (touchX - center.getX()) / (a * a) + (touchY - center.getY()) * (touchY - center.getY()) / (b * b);

//        double distance = PointUtils.pointToLine(lineStart.getX() + startPoint.getX(), lineStart.getY() + startPoint.getY(),
//            lineEnd.getX() + startPoint.getX(), lineEnd.getY() + startPoint.getY(),
//            pointIn.getX(), pointIn.getY());
        LogUtil.e("xiaoyi", "distance" + distance);
        if (distance >= 0.8 && distance <= 1.2) {
            return true;
        }
        return false;
    }
}
