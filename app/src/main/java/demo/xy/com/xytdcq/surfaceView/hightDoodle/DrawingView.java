package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.xy.com.xytdcq.surfaceView.doodle.Action;
import demo.xy.com.xytdcq.surfaceView.doodle.ActionTypeEnum;
import demo.xy.com.xytdcq.surfaceView.doodle.DoodleChannel;
import demo.xy.com.xytdcq.surfaceView.doodle.MyCircle;
import demo.xy.com.xytdcq.surfaceView.doodle.MyEraser;
import demo.xy.com.xytdcq.surfaceView.doodle.MyLine;
import demo.xy.com.xytdcq.surfaceView.doodle.MyPath;
import demo.xy.com.xytdcq.surfaceView.doodle.MyRect;
import demo.xy.com.xytdcq.surfaceView.doodle.PageChannel;
import demo.xy.com.xytdcq.surfaceView.doodle.TransactionData;
import demo.xy.com.xytdcq.uitls.LogUtil;

public class DrawingView  extends View {

    public static final int rts_blue = 0x0000FF;
    public static final int rts_red = 0xFF0000;
    public static final int rts_black = 0x000000;
    public static final int rts_pink = 0xFF00FF;



    private int bgColor = 0x00C7EDCC; // 背景颜色

    private List<PageChannel> pageList; //页面列表

    private int paintColor = rts_black/*= Color.BLACK*/; // 默认画笔颜色
    private float paintSize;//画笔大小(修改让其传输)
    private DrawCallback drawCallback;
    private int paintType;//画笔类型(修改让其传输)
    public void setPageList(List<PageChannel> pageList) {
        this.pageList = pageList;
    }
    public void setDrawCallback(DrawCallback drawCallback) {
        this.drawCallback = drawCallback;
    }

    private long lastTouchTime = 0;
    private float xyZoom = 1.0f;
    private Activity context;

    private PageChannel pageChannel;
    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        this.setFocusable(true);
    }

    //设置页面的绘画路径保存对象
    public void setPageChannel(PageChannel pageChannel) {
        this.pageChannel = pageChannel;
    }
    /**
     * 设置当前画笔的形状
     *
     * @param type
     */
    public void setPaintType(int type) {
//        this.pageChannel.paintChannel.setType(type);
        this.paintType = type;
    }
    /**
     * 初始化（必须调用）
     *
     * @param bgColor 设置板书的背景颜色
     */
    public void init(int bgColor, int paintColor, Activity context) {
        this.context = context;
//        this.bgColor = bgColor;
        this.paintColor = paintColor;
        this.paintSize = 1;
        lastTouchTime = System.currentTimeMillis();
//        getThreadInstance().start();

    }


    private ArrayList<Point> points = new ArrayList<>();
    private TestPath testPath = null;
    private float minX;
    private float minY;
    private float maxX;
    private float maxY;
    /**
     * 触摸绘图
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }

        float touchX = event.getX();
        float touchY = event.getY();
        LogUtil.e("x=" + touchX + ", y=" + touchY);
//        LogUtil.e("x=" + touchX + ", y=" + touchY+"--"+pageChannel.paintChannel.type);
//        LogUtil.e("x===" + event.getX() + ", y===" + event.getY()+"--"+pageChannel.paintChannel.type);
        lastTouchTime = System.currentTimeMillis();


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (paintType == ActionTypeEnum.Eraser.getValue()) {
                    double getSize = event.getSize();
                    double getPressure = event.getPressure();
                    int paintSizeT;
                    if (getSize > 0 && getSize < 1) {
                        paintSizeT = (int) (getSize * 130);
                    } else if (getPressure > 0 && getPressure < 1) {
                        paintSizeT = (int) (getPressure * 130);
                    } else {
                        paintSizeT = 30;
                    }
                    if (paintSizeT < 20) {
                        paintSizeT = 20;
                    } else if (paintSizeT > 60) {
                        paintSizeT = 60;
                    }
                    paintSize = paintSizeT / xyZoom;
                } else {
                    paintSize = 1;
                }
                points.clear();
                testPath = new TestPath(context);
                Point point = new Point(touchX,touchY);
                testPath.setStartPoint(point);
                points.add(point);
                onPaintActionStart(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                points.add(new Point(touchX,touchY));
                onPaintActionMove(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                onPaintActionEnd(touchX, touchY);
                Point endPoint = new Point(touchX,touchY);
                testPath.setEndPoint(endPoint);
                testPath.setViewWidth(600);
                testPath.setViewHeight(600);
                testPath.setBackgroundColor(Color.TRANSPARENT);
                points.add(endPoint);
                if (testPath != null) {
                    testPath.addAll(points);
                }

                // 设置居中显示
                testPath.setY(testPath.getStartPoint().getY());
                testPath.setX(testPath.getStartPoint().getX());
                if (drawCallback != null) {
//                    TestPath testPathT = testPath;
//                    drawCallback.callBack(testPath);
                    TextView textView = new TextView(context);
                    textView.setBackgroundColor(Color.GRAY);
                    textView.setText("aaa bbb");
                    textView.setTextColor(Color.BLUE);
                    textView.setY(230);
                    textView.setX(230);
//                    drawCallback.callBack(textView);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void onPaintActionStart(float x, float y) {
        if (null == pageChannel || pageChannel.paintChannel == null) {
            return;
        }
        if (paintType == ActionTypeEnum.Path.getValue()/*  && dt == null|| !dt.isAlive()*/) {
            LogUtil.e("thread is stop..");
            lastTouchTime = System.currentTimeMillis();
//            getThreadInstance().start();
        }
//        //画笔大小需要根据屏幕转换，不然擦除对应不上
        onActionStart(x, y);

    }

    private void onPaintActionMove(float x, float y) {
        if (null == pageChannel || pageChannel.paintChannel == null) {
            return;
        }
        onActionMove(x, y);
    }

    private void onPaintActionEnd(float x, float y) {
        if (null == pageChannel || pageChannel.paintChannel == null) {
            return;
        }
        if (pageChannel.paintChannel.action.getStartIndexX() < x) {
            pageChannel.paintChannel.action.setStartIndexX(x);
        }
        if (pageChannel.paintChannel.action.getStartIndexY() < y) {
            pageChannel.paintChannel.action.setStartIndexY(y);
        }
        if (pageChannel.paintChannel.action.getEndIndexX() > x) {
            pageChannel.paintChannel.action.setEndIndexX(x);
        }
        if (pageChannel.paintChannel.action.getEndIndexY() > y) {
            pageChannel.paintChannel.action.setEndIndexY(y);
        }
        onActionEnd();
    }


    private void onActionStart(float x, float y) {
        DoodleChannel channel = pageChannel.paintChannel;
        if (null == channel) return;
        float paintSizeT = paintSize;
        if (paintType == ActionTypeEnum.Eraser.getValue()) {
//            paintSizeT = fst.adjustXInFloat(paintSize);
            paintSizeT = paintSize * xyZoom;
            ;
        }
        //2017/6/8 修改原来画path,增加画其它形状
        channel.action = getActionByPaintType(paintType, x, y, this.paintColor, (int) paintSizeT);
    }

    private void onActionMove(float x, float y) {
        DoodleChannel channel = pageChannel.paintChannel;
        if (channel == null) {
            return;
        }
        // 绘制当前Action
        if (channel.action == null) {
            // 有可能action被清空，此时收到move，重新补个start
            onPaintActionStart(x, y);
        }
        channel.action.onMove(x, y);
        if (channel.action.getStartIndexX() < x) {
            channel.action.setStartIndexX(x);
        }
        if (channel.action.getStartIndexY() < y) {
            channel.action.setStartIndexY(y);
        }
        if (channel.action.getEndIndexX() > x) {
            channel.action.setEndIndexX(x);
        }
        if (channel.action.getEndIndexY() > y) {
            channel.action.setEndIndexY(y);
        }
    }

    private void onActionEnd() {
        DoodleChannel channel = pageChannel.paintChannel;
        if (channel == null || channel.action == null) {
            return;
        }
        channel.actions.add(channel.action);
        channel.action = null;

    }
    /**
     * 自定义 判断画笔类型返回对应类型的数据
     *
     * @param paintType 画笔类型
     * @param x         x方向数据
     * @param y         y方向数据
     * @param color     画笔颜色
     * @param size      画笔大小
     * @return action
     */
    private Action getActionByPaintType(int paintType, Float x, Float y, Integer color, Integer size) {
        Action action;
        switch (paintType) {
            case -1://橡皮擦
                action = new MyEraser(x, y, Color.WHITE, size);
                break;
            case 1: //Path(1),
                action = new MyPath(x, y, color, size);
                break;
            case 2://Line(2),
                action = new MyLine(x, y, color, size);
                break;
            case 3://triangle(3),
                action = new MyPath(x, y, color, size);//需要修改成三角形
                break;
            case 4:// Rect(4),
                action = new MyRect(x, y, color, size);
                break;
            case 5:// Circle(5),
                action = new MyCircle(x, y, color, size, 10);
                break;
            default://Path(1),
                action = new MyPath(x, y, color, size);
                break;
        }
        action.setStartIndexX(x);
        action.setEndIndexX(x);
        action.setStartIndexY(y);
        action.setEndIndexY(y);
        return action;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.e("xiaoyi ..... drawView flag");
//        if (isDestory || null == surfaceHolder) {
//            return;
//        }
//        Canvas mCanvas = null;
//        try {
//            mCanvas = surfaceHolder.lockCanvas();
//        } catch (Exception e) {
//            LogUtil.e("surfaceHolder.lockCanvas Exception:" + e.getMessage());
//        }
        if (canvas == null) {
            return;
        }
//        layerId = mCanvas.saveLayer(0, 0, canvasWidth, canvasHeight,
//                null, Canvas.ALL_SAVE_FLAG);
//                cacheBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
//                tmpCanvas.setBitmap(cacheBitmap);
        // 绘制历史记录(自己的)
        LogUtil.e("Draw pageChannel.paintChannel");
        if (pageChannel.paintChannel != null && pageChannel.paintChannel.actions != null) {
            for (Action a : pageChannel.paintChannel.actions) {
                a.onDraw(canvas);
//                        a.onDraw(tmpCanvas);
                //1.老师端接收到--先画自己矩形--再画橡皮擦路径
            }
            // 绘制当前
            if (pageChannel.paintChannel.action != null) {
                pageChannel.paintChannel.action.onDraw(canvas);
//                        pageChannel.paintChannel.action.onDraw(tmpCanvas);
            }
        }
//        mCanvas.restoreToCount(layerId);

//        if (null != surfaceHolder && null != mCanvas) {
//            surfaceHolder.unlockCanvasAndPost(mCanvas);
//        }
        super.onDraw(canvas);
    }
}
