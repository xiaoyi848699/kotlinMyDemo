package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.surfaceView.utils.BezierUtil;
import demo.xy.com.xytdcq.surfaceView.utils.PointUtils;

/**
 * 直线
 */
public class DrawPathLine extends BaseLinePath {
//    private int penWidth; // 线宽：1.0、3.0、6.0
//    private String penColor; // 色值：#000000、#2673f7、#e73dc
//    private int penType; // 曲线-0，直线-1，圆形-2，矩形-3，三角形-4, 星形 -5，箭头-6
//    private List<Point> paths;

    private Point lineStart = new Point(0,0);
    private Point lineEnd = new Point(0,0);

    public DrawPathLine(Context context) {
        super(context);
    }

    public DrawPathLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawPathLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawPathLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            paint.setColor(color);
            paint.setStrokeWidth(size);
        }
        if (isSelect()) {
            paint.setShadowLayer(4,4,4, Color.GRAY);
        }
        canvas.drawLine(lineStart.getX(), lineStart.getY(), lineEnd.getX(), lineEnd.getY() , paint);
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
                lineStart = p;
                newPoints.add(p);
            } else if (index == points.size() -1) {
                lineEnd = p;
                newPoints.add(p);
            }
            index++;
        }
        this.points.addAll(newPoints);
        invalidate();
        // 发送数据

    }

    public boolean checkEraser(Point pointIn) {
        if (this.points == null || this.points.size() == 0) {
            return true;
        }
        double distance = PointUtils.pointToLine(lineStart.getX() + startPoint.getX(), lineStart.getY() + startPoint.getY(),
            lineEnd.getX() + startPoint.getX(), lineEnd.getY() + startPoint.getY(),
            pointIn.getX(), pointIn.getY());
        LogUtil.e("xiaoyi", "distance" + distance);
        if (distance <= BlackBoardAcivity.eraserSize + size) {
            return true;
        }
        return false;
    }
}
