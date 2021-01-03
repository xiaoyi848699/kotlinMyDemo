package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.surfaceView.utils.BezierUtil;
import demo.xy.com.xytdcq.surfaceView.utils.PointUtils;

public class DrawPath extends BasePath {
    public DrawPath(Context context) {
        super(context);
    }

    public DrawPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setViewHeight(float viewHeight) {
        super.setViewHeight(viewHeight + size * 2);
    }

    @Override
    public void setViewWidth(float viewWidth) {
        super.setViewWidth(viewWidth + size * 2);
    }

    @Override
    public void setX(float x) {
        super.setX(x - size);
    }

    @Override
    public void setY(float y) {
        super.setY(y - size);
    }

    @Override
    public void relaseData() {
        if (points != null) {
            points.clear();
        }
    }

    @Override
    public boolean isNeedInvalidateOnMoveStatusChane() {
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStrokeWidth(size);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);

        }
        paint.setColor(color);
        paint.setStrokeWidth(size);
        if (isSelect()) {
            paint.setShadowLayer(4,4,4,Color.GRAY);
        } else {
            paint.setShadowLayer(0,0,0,color);

        }
        if (points == null || points.size() < 1) {
            return;
        }
        path = new Path();
        Point pointStart = points.get(0);
        float startX = pointStart.getX();
        float startY = pointStart.getY();
        path.moveTo(startX, startY);
        path.lineTo(startX, startY);

        for (Point p : points) {
            float stopX = p.getX();
            float stopY = p.getY();
            float dx = Math.abs(stopX - startX);
            float dy = Math.abs(stopY - startY);
            if (dx >= 2 || dy >= 2) {
                path.quadTo(startX, startY, (stopX + startX) / 2, (stopY + startY) / 2);
                startX = stopX;
                startY = stopY;
            }
        }
        path.lineTo(startX, startY);
        canvas.drawPath(path, paint);
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
    public void addAll(ArrayList<Point> points) {
        ArrayList<Point> newPoints = new ArrayList<>(points.size());
        for (Point p:points) {
            p.setX(p.getX() - startPoint.getX() + size); // 偏移 使得靠边的线不会出现一部分不可见
            p.setY(p.getY() - startPoint.getY() + size);
            newPoints.add(p);
        }
        // 补点
        ArrayList<Point> addPoints = BezierUtil.getAddPoint(newPoints);
        this.points.addAll(addPoints);
        invalidate();
        // 发送数据


    }

    public boolean checkEraser(Point pointIn) {
        if (this.points == null || this.points.size() == 0) {
            return true;
        }
        // 先判断如果不在绘制区内返回false
        if ((pointIn.getX()) >= startPoint.getX() - BlackBoardAcivity.eraserSize // 橡皮半径
                && pointIn.getX() <= endPoint.getX() + BlackBoardAcivity.eraserSize
                && pointIn.getY() >= startPoint.getY() - BlackBoardAcivity.eraserSize
                && pointIn.getY() <= endPoint.getY() + BlackBoardAcivity.eraserSize) {
            // 转换成区域内的相对位置点
            Point point = new Point(pointIn.getX() - startPoint.getX() + size, pointIn.getY() - startPoint.getY() + size);
            for (Point p : this.points) {
                if (PointUtils.checkIndistance(p, point, BlackBoardAcivity.eraserSize)) {
                    return true;
                }
            }
        } else {
            LogUtil.e("checkEraser un in area");
            return false;
        }
        return false;
    }
}
