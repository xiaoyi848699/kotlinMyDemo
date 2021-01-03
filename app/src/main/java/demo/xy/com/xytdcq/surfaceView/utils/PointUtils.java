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
}
