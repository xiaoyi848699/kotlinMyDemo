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
    public TestPath(Context context) {
        super(context);
    }

    public TestPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

        LogUtil.i("xiaoyi","TestPath...onDraw");
        if (isSelect()) {
            canvas.drawColor(0x30C7EDCC);
            paint.setColor(Color.GRAY);
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
        for (Point p:points) {
            p.setX(p.getX() - startPoint.getX());
            p.setY(p.getY() - startPoint.getY());
            this.points.add(p);
        }
        invalidate();
    }
}
