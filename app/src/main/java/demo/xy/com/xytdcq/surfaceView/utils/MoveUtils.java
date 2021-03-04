package demo.xy.com.xytdcq.surfaceView.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.mylibrary.log.Write;
import demo.xy.com.mylibrary.thread.ThreadPoolManager;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.BasePath;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.DrawText;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.IBasePath;
import demo.xy.com.xytdcq.surfaceView.hightDoodle.Point;

public class MoveUtils {
    private static final String TAG = MoveUtils.class.getSimpleName();
    private static MoveUtils instance;

    private static final Object object = new Object();

    private LinkedList<Point> eraserPoint = new LinkedList();

    private CheckTask mCheckTask;

    private boolean isMove = true;

    private List<IBasePath> paths;

    private Point mLastPoint;

    private float moveX = 0;

    private float moveY = 0;

    private boolean isMoveEnd;

    private Activity activity;

    public static MoveUtils getInstance() {
        if (instance == null) {
            synchronized (object){
                if (instance == null) {
                    instance = new MoveUtils();
                }
            }
        }
        return instance;
    }

    public void init(List<IBasePath> allPaths, Activity activity) {
        this.activity = activity;
        paths = allPaths;
    }

    /**
     * 添加擦除点路径
     */
    public synchronized void setMoveEnd(float moveX, float moveY, boolean isMoveEnd) {
        LogUtil.e(TAG, "setMoveEnd" + moveX + moveY);
        this.moveX = moveX;
        this.moveY = moveY;
        this.isMoveEnd = isMoveEnd;
    }

    public void startLinstener() {
        if (paths == null || paths.size() == 0) {
            LogUtil.e(TAG, "cant startLinstener paths is null");
            return;
        }
        isMove = true;
        if (mCheckTask == null) {
            synchronized (object){
                if (mCheckTask == null) {
                    mCheckTask = new CheckTask();
                    ThreadPoolManager.getInstance().execute(mCheckTask);
                }
            }
        }
    }

    public void endLinstener() {
        isMove = false;
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
            while (isMove) {
                if (mLastPoint == null || (moveX == 0 && moveY == 0) || (moveX == mLastPoint.getX() && moveY == mLastPoint.getY())) {
                    LogUtil.e("CheckTask...");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Write.error("InterruptedException"+e.getMessage());
                    }
                } else {
                    final float localMoveX = moveX;
                    final float localMoveY = moveY;
                    LogUtil.e("CheckTask.prepare move..");
                    for (final IBasePath b : paths) {
                        LogUtil.e("CheckTask..isSelect." + b.isSelect());
                        if (activity != null && b.isSelect()) {
                            LogUtil.e("CheckTask.movemovemove..");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                b.y = b.startPoint.y //卡顿
//                b.x = b.startPoint.x //卡顿
//                b.scrollTo(b.startPoint.x.toInt(), b.startPoint.y.toInt()) // 反方向内部移动
//                b.scrollBy(b.startPoint.x.toInt(), b.startPoint.y.toInt()) // 不适合
                                    if (b instanceof BasePath) {
                                        ((BasePath) b).setTranslationX(b.getStartPoint().getX());
                                        ((BasePath) b).setTranslationY(b.getStartPoint().getY());
                                    } else if (b instanceof DrawText) {
                                        ((DrawText) b).setTranslationX(b.getStartPoint().getX());
                                        ((DrawText) b).setTranslationY(b.getStartPoint().getY());
                                    }
                                }
                            });
                        }
                    }
                    mLastPoint = new Point(localMoveX, localMoveY);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Write.error("CheckTask sleep" + e.getMessage());
                    }
                }
            }
        }
    }
}
