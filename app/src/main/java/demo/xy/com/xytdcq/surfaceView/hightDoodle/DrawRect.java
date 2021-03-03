package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;

import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.surfaceView.utils.PointUtils;
import demo.xy.com.xytdcq.uitls.LogUtil;

/**
 * 矩形
 */
public class DrawRect extends BaseLinePath {

    private float radius;
    private Point centerPoint = new Point(0,0);
    private Point touchStart = new Point(0,0);
    private Point touchEnd = new Point(0,0);

    public DrawRect(Context context) {
        super(context);
    }

    public DrawRect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawRect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawRect(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
        }
        if (isSelect()) {
            paint.setShadowLayer(4,4,4, Color.GRAY);
        } else {
            paint.setShadowLayer(0,0,0, color);
            paint.setStrokeWidth(size);
            paint.setColor(color);
        }
        canvas.drawRect(touchStart.getX(), touchStart.getY(), touchEnd.getX(), touchEnd.getY(), paint);
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

        double distanceTop = PointUtils.pointToLine(touchStart.getX() + startPoint.getX(), touchStart.getY() + startPoint.getY(),
                touchEnd.getX() + startPoint.getX(), touchStart.getY() + startPoint.getY(),
            pointIn.getX(), pointIn.getY());
        LogUtil.e("xiaoyi", "distance distanceTop:" + distanceTop);
        if (distanceTop <= BlackBoardAcivity.eraserSize + size) {
            return true;
        }
        double distanceLeft = PointUtils.pointToLine(touchStart.getX() + startPoint.getX(), touchStart.getY() + startPoint.getY(),
                touchStart.getX() + startPoint.getX(), touchEnd.getY() + startPoint.getY(),
            pointIn.getX(), pointIn.getY());
        LogUtil.e("xiaoyi", "distance distanceLeft" + distanceLeft);
        if (distanceLeft <= BlackBoardAcivity.eraserSize + size) {
            return true;
        }
        double distanceRight = PointUtils.pointToLine(touchEnd.getX() + startPoint.getX(), touchStart.getY() + startPoint.getY(),
                touchEnd.getX() + startPoint.getX(), touchEnd.getY() + startPoint.getY(),
            pointIn.getX(), pointIn.getY());
        LogUtil.e("xiaoyi", "distance distanceRight" + distanceRight);
        if (distanceRight <= BlackBoardAcivity.eraserSize + size) {
            return true;
        }
        double distanceBottom = PointUtils.pointToLine(touchStart.getX() + startPoint.getX(), touchEnd.getY() + startPoint.getY(),
                touchEnd.getX() + startPoint.getX(), touchEnd.getY() + startPoint.getY(),
            pointIn.getX(), pointIn.getY());
        LogUtil.e("xiaoyi", "distance distanceBottom" + distanceBottom);
        if (distanceBottom <= BlackBoardAcivity.eraserSize + size) {
            return true;
        }
        return false;
    }
}
