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

public class TestPath extends BasePath {
//    private ArrayList<Point> points = new ArrayList<>();
//    private Point startPoint;
//    private Point endPoint;
//    private float viewWidth;
//    private float viewHeight;
//
//    protected int color = Color.RED;
//    protected int size = 2;
//    private Paint paint;
//    private Path path;
    public TestPath(Context context) {
        super(context);
    }

    public TestPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
//
//    public Point getStartPoint() {
//        return startPoint;
//    }
//
//    public void setStartPoint(Point startPoint) {
//        this.startPoint = startPoint;
//    }
//
//    public Point getEndPoint() {
//        return endPoint;
//    }
//
//    public void setEndPoint(Point endPoint) {
//        this.endPoint = endPoint;
//    }
//
//    public float getViewWidth() {
//        return viewWidth;
//    }
//
//    public void setViewWidth(float viewWidth) {
//        this.viewWidth = viewWidth;
//    }
//
//    public float getViewHeight() {
//        return viewHeight;
//    }
//
//    public void setViewHeight(float viewHeight) {
//        this.viewHeight = viewHeight;
//    }

    public TestPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);

            if (isSelect()) {
                paint.setShadowLayer(6,6,6,Color.GREEN);
            }
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

        LogUtil.i("xiaoyi","TestPath...onDraw");
        if (isSelect()) {
            canvas.drawColor(0x50C7EDCC);
            paint.setColor(Color.GRAY);
            paint.setShadowLayer(0,0,0,Color.GRAY);
            if ( startPoint != null && endPoint != null) {
                canvas.drawRect(0, 0, endPoint.getX() - startPoint.getX(), endPoint.getY() - startPoint.getY(), paint);
            }
        }
    }
    public void add(Point point) {
        points.add(point);
        invalidate();
    }
    public void addAll(ArrayList<Point> points) {
        for (Point p:points) {
            p.setX(p.getX() - startPoint.getX());
            p.setY(p.getY() - startPoint.getY());
            this.points.add(p);
        }
        invalidate();
    }

}
