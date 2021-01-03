package demo.xy.com.xytdcq.surfaceView.utils;

import android.graphics.PointF;

import java.util.ArrayList;

import demo.xy.com.xytdcq.surfaceView.hightDoodle.Point;
import demo.xy.com.xytdcq.uitls.LogUtil;

public class BezierUtil {
    public static ArrayList<Point> getAddPoint(ArrayList<Point> points) {
        if (points == null || points.size() < 3) {
            return points;
        }

        LogUtil.w("add Point before" + points.size());
//        for (Point p: points) {
//            LogUtil.w("oldPoint:" + p.toString());
//        }
        ArrayList<Point> newPoint = new ArrayList<>();
        newPoint.addAll(points);
        for (int i = 0 ; i < newPoint.size()-2; i++){
            Point point0 = newPoint.get(i);
            Point point1 = newPoint.get(i + 1);
            Point point2= newPoint.get(i + 2);
            double distance = PointUtils.getDistance(point0,point2);
            LogUtil.w("add Point distance" + distance);
            if (distance > 8) {
                // 需要补点次数
                float times = (float) (distance/4.0);
                float step = 1/(times+1);
                LogUtil.w("add Point step" + step);
                ArrayList<Point> addPoints = new ArrayList<>();
                for (int j = 0; j < times; j++) {
                    addPoints.add(CalculateBezierPointForQuadratic(step * (j+1), point0,point1,point2));
                }
                LogUtil.w("add Point" + addPoints.size());
                newPoint.remove(i+1);
                newPoint.addAll(i+1,addPoints);
                i += addPoints.size()-1;
            }
        }
//        for (Point p: newPoint) {
//            LogUtil.w("newPoint:" + p.toString());
//        }
        LogUtil.w("add Point end" + newPoint.size());
        return newPoint;
    }

    /**
     * 二阶贝塞尔曲线B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    public synchronized static Point CalculateBezierPointForQuadratic(float t, Point p0, Point p1, Point p2) {
        float temp = 1 - t;
        float x = temp * temp * p0.getX() + 2 * t * temp * p1.getX() + t * t * p2.getX();
        float y = temp * temp * p0.getY() + 2 * t * temp * p1.getY() + t * t * p2.getY();
        Point point = new Point(x,y);
        LogUtil.w("CalculateBezierPointForQuadratic:t" + point.toString());
        return point;
    }

    /**
     * 三阶贝塞尔曲线B(t) = P0 * (1-t)^3 + 3 * P1 * t * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 终止点
     * @return t对应的点
     */
    public static PointF CalculateBezierPointForCubic(float t, PointF p0, PointF p1, PointF p2, PointF p3) {
        PointF point = new PointF();
        float temp = 1 - t;
        point.x = p0.x * temp * temp * temp + 3 * p1.x * t * temp * temp + 3 * p2.x * t * t * temp + p3.x * t * t * t;
        point.y = p0.y * temp * temp * temp + 3 * p1.y * t * temp * temp + 3 * p2.y * t * t * temp + p3.y * t * t * t;
        return point;
    }
}
