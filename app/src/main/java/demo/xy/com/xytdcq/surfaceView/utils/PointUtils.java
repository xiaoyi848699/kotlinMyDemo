package demo.xy.com.xytdcq.surfaceView.utils;

import demo.xy.com.xytdcq.surfaceView.hightDoodle.Point;

public class PointUtils {
    /**
     * 检查两个点的直线距离是否在distance 内
     * @param start 起始点
     * @param end 结束点
     * @param distance 距离
     * @return true 两点的直线距离在distance内
     */
    public static boolean checkIndistance(Point start, Point end, int distance) {
        if (getDistance(start, end) <= distance) {
            return true;
        }
        return false;
    }

    /**
     * 检查两个点的直线距离
     * @param start 起始点
     * @param end 结束点
     * @return  两点的直线距离
     */
    public static double getDistance(Point start, Point end) {
        return Math.sqrt(Math.pow((start.getX() - end.getX()),2) + Math.pow((start.getY() - end.getY()),2));
    }

    /**
     * 获取两个点的之间的补点
     * @param start 起始点
     * @param end 结束点
     * @return  两点的直线距离
     */
    public static double getAddPoint(Point start, Point end) {
        return Math.sqrt(Math.pow((start.getX() - end.getX()),2) + Math.pow((start.getY() - end.getY()),2));
    }

    /**
     * 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
     * @return
     */
    public static double pointToLine(float x1, float y1, float x2, float y2, float x0, float y0) {
        double space = 0;
        double a, b, c;
        a = lineSpace(x1, y1, x2, y2);// 线段的长度
        b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
        c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
        if (c <= 0.000001 || b <= 0.000001) {
            space = 0;
            return space;
        }
        if (a <= 0.000001) {
            space = b;
            return space;
        }
        if (c * c >= a * a + b * b) {
            space = b;
            return space;
        }
        if (b * b >= a * a + c * c) {
            space = c;
            return space;
        }
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        return space;
    }

    // 计算两点之间的距离
    private static double lineSpace(float x1, float y1, float x2, float y2) {
        double lineLength = 0;
        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
                * (y1 - y2));
        return lineLength;
    }
}
