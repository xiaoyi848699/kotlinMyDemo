package demo.xy.com.xytdcq.surfaceView.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.mylibrary.log.Write;
import demo.xy.com.mylibrary.thread.ThreadPoolManager;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.DrawCircle;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.DrawPath;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.DrawPathLine;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.DrawRect;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.IBasePath;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.IChangeCallback;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.Point;

public class EraserUtils {
    private static EraserUtils instance;

    private static final Object object = new Object();

    private LinkedList<Point> eraserPoint = new LinkedList();

    private CheckTask mCheckTask;

    private boolean isCheck = true;

    private List<IBasePath> paths = new ArrayList<>();

    private IChangeCallback mIChangeCallback;

    private Point mLastPoint;

    public static EraserUtils getInstance() {
        if (instance == null) {
            synchronized (object){
                if (instance == null) {
                    instance = new EraserUtils();
                }
            }
        }
        return instance;
    }

    public synchronized Point getEraserPoint() {
        while (eraserPoint.size() <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Write.error("InterruptedException"+e.getMessage());
            }
        }
        return eraserPoint.removeFirst();
    }

    public void init(List<IBasePath> allPaths, IChangeCallback iChangeCallback) {
        if (allPaths == null || allPaths.size() == 0) {
            LogUtil.e("cant init allPaths is null");
            return;
        }
        if (mIChangeCallback == null) {
            mIChangeCallback = iChangeCallback;
        }
        mLastPoint = null;
        paths.clear();
        for (IBasePath path: allPaths){
            if (path instanceof DrawPath || path instanceof DrawPathLine
                || path instanceof DrawRect || path instanceof DrawCircle) {
                if (path != null) {
                    paths.add(path);
                }
            }
        }
    }

    /**
     * 添加擦除点路径
     * @param point
     */
    public synchronized void addEraserPoint(Point point) {
        eraserPoint.addLast(point);
        notifyAll();
    }

    public void startLinstener() {
        if (paths == null || paths.size() == 0) {
            LogUtil.e("cant startLinstener paths is null");
            return;
        }
        if (mCheckTask == null) {
            mCheckTask = new CheckTask();
            ThreadPoolManager.getInstance().execute(mCheckTask);
        }
    }

    public void endLinstener() {
        isCheck = false;
        if (mCheckTask != null) {
            ThreadPoolManager.getInstance().remove(mCheckTask);
            mCheckTask = null;
        }
    }

    class CheckTask implements Runnable{
        public CheckTask() {

        }
        @Override
        public void run() {
            while (isCheck) {
                Point eraserPoint = getEraserPoint();
                List<String> removeVid = new ArrayList<>();
                if (eraserPoint != null) {
                    checkPoint(eraserPoint, removeVid);
                }
                if (mLastPoint != null) {
                    double distance = PointUtils.getDistance(eraserPoint,mLastPoint);
                    if (distance > BlackBoardAcivity.eraserSize) {
                        // 需要补点，不然擦不干净
                        int times = (int) (distance / BlackBoardAcivity.eraserSize);
                        float stepX = (mLastPoint.getX() - eraserPoint.getX())/times;
                        float stepY = (mLastPoint.getY() - eraserPoint.getY())/times;
                        for (int i = 1 ; i < times;i++) {
                            checkPoint(new Point((mLastPoint.getX() + stepX * i),(mLastPoint.getY() + stepY * i)), removeVid);
                        }
                    }
                }
                if (mIChangeCallback != null && removeVid.size() > 0) {
                    mIChangeCallback.eraserPath(removeVid);
                }
                mLastPoint = eraserPoint;
            }
        }

        private void checkPoint(Point eraserPoint, List<String> removeVid) {
            for (IBasePath drawPath :paths){
                boolean isDelete = false;
                if (drawPath instanceof DrawPath) {
                    isDelete = ((DrawPath)drawPath).checkEraser(eraserPoint);
                } else if (drawPath instanceof DrawPathLine) {
                    isDelete = ((DrawPathLine)drawPath).checkEraser(eraserPoint);
                } else if (drawPath instanceof DrawRect) {
                    isDelete = ((DrawRect)drawPath).checkEraser(eraserPoint);
                } else if (drawPath instanceof DrawCircle) {
                    isDelete = ((DrawCircle)drawPath).checkEraser(eraserPoint);
                }
                if (isDelete) {
                    LogUtil.e("xiaoyi", "distance remove" + drawPath.getVid());
                    removeVid.add(drawPath.getVid());
                }
            }

        }
    }
}
