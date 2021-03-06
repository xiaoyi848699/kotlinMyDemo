package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import demo.xy.com.mylibrary.picture.ImageLibraryHelper;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.surfaceView.doodle.Action;
import demo.xy.com.xytdcq.surfaceView.doodle.ActionTypeEnum;
import demo.xy.com.xytdcq.surfaceView.doodle.DoodleChannel;
import demo.xy.com.xytdcq.surfaceView.doodle.MyCircle;
import demo.xy.com.xytdcq.surfaceView.doodle.MyEraser;
import demo.xy.com.xytdcq.surfaceView.doodle.MyLine;
import demo.xy.com.xytdcq.surfaceView.doodle.MyPath;
import demo.xy.com.xytdcq.surfaceView.doodle.MyRect;
import demo.xy.com.xytdcq.surfaceView.doodle.PageChannel;
import demo.xy.com.xytdcq.uitls.BitmapUtil;
import demo.xy.com.xytdcq.uitls.FastClick;
import demo.xy.com.xytdcq.uitls.LogUtil;
import demo.xy.com.xytdcq.uitls.PermissionUtils;
import demo.xy.com.xytdcq.uitls.ToastUtil;


/**
 * 画板
 */
public class DrawingBoardView extends SurfaceView implements SurfaceHolder.Callback {
    private String[] permissions =  new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final int rts_blue = 0x2673f7;
    public static final int rts_red = 0xe73dcd;
    public static final int rts_black = 0x000000;
    public static final int rts_pink = 0xFF00FF;

    private boolean isDestory = false;//时候推出标志

    private SurfaceHolder surfaceHolder;

//    private int bgColor = 0xC7EDCC; // 背景颜色
    private int bgColor = Color.TRANSPARENT; // 背景颜色
    private int paintColor = rts_black/*= Color.BLACK*/; // 默认画笔颜色
    private float paintSize;//画笔大小(修改让其传输)
    private int paintType;//画笔类型(修改让其传输)

    private boolean isDrawing = false;

    private float xyZoom = 1.0f;

    private float paintOffsetY = 0.0f; // 绘制时的Y偏移（去掉ActionBar,StatusBar,marginTop等高度）
    private float paintOffsetX = 0.0f; // 绘制事的X偏移（去掉marginLeft的宽度）

    private float lastX = 0.0f;
    private float lastY = 0.0f;


    private long lastTouchTime = 0;
    private Activity context;

    private int layerId;//图片ID解决橡皮擦问题

    private List<PageChannel> pageList; //页面列表
    private byte isFirstIn = 0;
    private Bitmap cacheBitmap;
    private Canvas tmpCanvas;
//    private DrawThread dt;

    private boolean isRunning = false;//绘制线程是否运行中
    private boolean isWait = false;//画笔才刷新界面
    private boolean isFreeze = false;//当前窗口是否focus

    private Action otherPeopleaction;//别人的动作

    private int canvasHeight;
    private int canvasWidth;
    private int bitmapWidth;
    private int bitmapHeight;
    private int dstbmpWidth;
    private int dstbmpHeight;
    private Bitmap dstbmp;
    private Rect dest;
    private Rect src;

    private PageChannel pageChannel;
    private int currentPage = 0;//当前页面是第几页


    private IDrawCallback drawCallback;

    public void setDrawCallback(IDrawCallback drawCallback) {
        this.drawCallback = drawCallback;
    }

    public boolean isWait() {
        return isWait;
    }

    public void setWait(boolean wait) {
        isWait = wait;
    }

    public DrawingBoardView(Context context) {
        super(context);
        init();
    }

    public DrawingBoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DrawingBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        surfaceHolder = this.getHolder();
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder.addCallback(this);
        this.setFocusable(true);
    }

    /**
     * 初始化（必须调用）
     *
     * @param bgColor 设置板书的背景颜色
     */
    public void init(int bgColor, int paintColor, Activity context) {
        this.context = context;
        this.bgColor = bgColor;
        this.paintColor = paintColor;
        this.paintSize = 1;
        isRunning = true;
        lastTouchTime = System.currentTimeMillis();
//        getThreadInstance().start();

    }

    //使用:同步页面
    public void setPageList(List<PageChannel> pageList) {
        this.pageList = pageList;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }
    //
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (surfaceHolder != null) {
            drawView(0);
        }
    }
    //
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.e("xy...surfaceDestroyed");
    }

    /**
     * 设置绘制时的画笔颜色
     *
     * @param color
     */
    public void setPaintColor(int color) {
        this.paintColor = color;
//        this.pageChannel.paintChannel.setColor(color);
        LogUtil.e("setPaintColor" + color);
        LogUtil.e("setPaintColor" + this.paintColor);
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
     * 设置当前画笔为橡皮擦
     * <p>
     * //     * @param size 橡皮擦的大小（画笔的粗细)
     */
    public void setEraseType() {
//        this.pageChannel.paintChannel.setEraseType(this.bgColor, 20);
        this.paintType = ActionTypeEnum.Eraser.getValue();
//        this.paintSize = size;
    }


    /**
     * 撤销一步
     *
     * @return 撤销是否成功
     */
    public void paintBack() {
        if (pageChannel.paintChannel == null) {
            return;
        }
    }

    public void clear() {
        clearAll();
    }

    private ArrayList<Point> points = new ArrayList<>();
    private BaseLinePath drawPath = null;
    private Point startPoint;
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
        try {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_CANCEL) {
                return false;
            }

            float touchX = event.getX();
            float touchY = event.getY();
            touchX -= paintOffsetX;
            touchY -= paintOffsetY;
            LogUtil.e("x=" + touchX + ", y=" + touchY);
            lastTouchTime = System.currentTimeMillis();
            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    LogUtil.e("ACTION_POINTER_DOWN  PointerCount:" + event.getPointerCount());
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    LogUtil.e("ACTION_POINTER_UP PointerCount:" + event.getPointerCount());
                    break;
                case MotionEvent.ACTION_DOWN:
                    isDrawing = true;
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
                        paintSize = BlackBoardAcivity.paintSize;
                    }
                    points.clear();
                    LogUtil.e("TAG","paintType:::" + paintType);
                   if (paintType ==  ActionTypeEnum.Path.getValue()) {
                       drawPath = new DrawPath(context);
                   } else if (paintType ==  ActionTypeEnum.Line.getValue()) {
                       drawPath = new DrawPathLine(context);
                   } else if (paintType ==  ActionTypeEnum.Rectangle.getValue()) {
                       drawPath = new DrawRect(context);
                   } else if (paintType ==  ActionTypeEnum.Circle.getValue()) {
                       drawPath = new DrawCircle(context);
                   } else {
                       drawPath = new DrawPath(context);
                   }

                    drawPath.setColor(BlackBoardAcivity.paintColor);
                    drawPath.setSize(BlackBoardAcivity.paintSize);
                    maxX = minX = touchX;
                    maxY = minY = touchY;
                    startPoint = new Point(touchX,touchY);
                    points.add(startPoint);
                    onPaintActionStart(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (paintType ==  ActionTypeEnum.Path.getValue()) {
                        if (touchX < minX) {
                            minX = touchX;
                        } else if(touchX > maxX){
                            maxX = touchX;
                        }
                        if (touchY < minY) {
                            minY = touchY;
                        } else if(touchY > maxY){
                            maxY = touchY;
                        }
                        points.add(new Point(touchX,touchY));
                    }
                    onPaintActionMove(touchX, touchY);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (touchX < minX) {
                        minX = touchX;
                    } else if(touchX > maxX){
                        maxX = touchX;
                    }
                    if (touchY < minY) {
                        minY = touchY;
                    } else if(touchY > maxY){
                        maxY = touchY;
                    }
                    onPaintActionEnd(touchX, touchY);
                    Point endPoint = new Point(touchX,touchY);
                    if (paintType ==  ActionTypeEnum.Path.getValue()) {
                        drawPath.setStartPoint(new Point(minX, minY));
                        drawPath.setEndPoint(new Point(maxX, maxY));
                        drawPath.setViewWidth(maxX-minX);
                        drawPath.setViewHeight(maxY-minY);
                    } else if (paintType ==  ActionTypeEnum.Line.getValue()) {
                        drawPath.setStartPoint(new Point(minX-10, minY-10));
                        drawPath.setEndPoint(new Point(maxX+10, maxY+10));
                        drawPath.setViewWidth(Math.max(Math.abs(endPoint.getX() - startPoint.getX()) + 20, 30));
                        drawPath.setViewHeight(Math.max(Math.abs(endPoint.getY() - startPoint.getY()) + 20, 30));
                    } else if (paintType ==  ActionTypeEnum.Rectangle.getValue()) {
                        drawPath.setStartPoint(new Point(minX-10, minY-10));
                        drawPath.setEndPoint(new Point(maxX+10, maxY+10));
                        drawPath.setViewWidth(Math.max(Math.abs(endPoint.getX() - startPoint.getX()) + 20, 30));
                        drawPath.setViewHeight(Math.max(Math.abs(endPoint.getY() - startPoint.getY()) + 20, 30));
                    } else if (paintType ==  ActionTypeEnum.Circle.getValue()) {
                        // 算出起始和结束点
                        drawPath.setStartPoint(new Point(minX-10, minY-10));
                        drawPath.setEndPoint(new Point(maxX+10, maxY+10));
                        drawPath.setViewWidth(Math.max(Math.abs(endPoint.getX() - startPoint.getX()) + 20, 30));
                        drawPath.setViewHeight(Math.max(Math.abs(endPoint.getY() - startPoint.getY()) + 20, 30));
                    } else {
                        drawPath.setStartPoint(new Point(minX, minY));
                        drawPath.setEndPoint(new Point(maxX, maxY));
                        drawPath.setViewWidth(maxX-minX);
                        drawPath.setViewHeight(maxY-minY);
                    }
                    points.add(endPoint);
                    if (drawPath != null) {
                        drawPath.addAll(points);
                    }

                    // 设置居中显示
                    drawPath.setY(drawPath.getStartPoint().getY());
                    drawPath.setX(drawPath.getStartPoint().getX());
                    if (drawCallback != null) {
                        drawCallback.callBackAddView(drawPath,(int) drawPath.getViewWidth(),(int) drawPath.getViewHeight());
                    }
                    isDrawing = false;
                    pageChannel.paintChannel.action = null;
                    if (cacheBitmap != null) {
                        cacheBitmap.recycle();
                        cacheBitmap = null;
                    }
                    invalidate();
                    break;
                default:
                    break;
            }
            return true;
        } catch (IllegalArgumentException ex) {
            LogUtil.e("xiaoyi board onInterceptTouchEvent IllegalArgumentException" + ex.toString());
        }
        return super.onTouchEvent(event);
    }

    private void onPaintActionStart(float x, float y) {
        if (null == pageChannel || pageChannel.paintChannel == null) {
            return;
        }
        if (paintType == ActionTypeEnum.Path.getValue() /*&& dt == null || !dt.isAlive()*/) {
            LogUtil.e("thread is stop..");
            isRunning = true;
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
        if (!isNewPoint(x, y)) {
            return;
        }
        onActionMove(x, y);
    }

    private void onPaintActionEnd(float x, float y) {
        if (null == pageChannel || pageChannel.paintChannel == null) {
            return;
        }
        onActionEnd();
    }


    private void onActionStart(float x, float y) {
        DoodleChannel channel = pageChannel.paintChannel;
        if (null == channel) return;
        lastX = x;
        lastY = y;
        float paintSizeT = paintSize;
        if (paintType == ActionTypeEnum.Eraser.getValue()) {
//            paintSizeT = fst.adjustXInFloat(paintSize);
            paintSizeT = paintSize * xyZoom;
            ;
        }
        //2017/6/8 修改原来画path,增加画其它形状
        channel.action = getActionByPaintType(paintType, x, y, this.paintColor, (int) paintSizeT);
//        channel.action = getActionByPaintType(paintType, x, y, rts_red, (int) paintSizeT);
        if (paintType == ActionTypeEnum.Path.getValue()) {
            channel.action.onDraw(tmpCanvas);
        }
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
        if (paintType == ActionTypeEnum.Path.getValue()) {
            channel.action.onDraw(tmpCanvas);
        } else {
//            drawHistoryActions();
        }
    }

    private void onActionEnd() {
        DoodleChannel channel = pageChannel.paintChannel;
        if (channel == null || channel.action == null) {
            return;
        }
//        if (paintType != ActionTypeEnum.Path.getValue()) {
//            channel.action.onDraw(tmpCanvas);
//        }
        channel.action.onEnd(tmpCanvas);
//        if (paintType != ActionTypeEnum.Path.getValue()) {
//            drawHistoryActions();//防止画笔重影、重绘一次
//        }

        channel.action.setAccount(getImAccount());
        channel.actions.add(channel.action);
        channel.action = null;

    }



    //------------------------------绘画------------------------------------
//    private void drawHistoryActions() {
//        drawView(3);
//    }


    public void clearAll() {
        pageChannel.clearAll();//抽取清除方法
        reDraw();
    }

    //重新画一次
    private boolean reDraw() {
//        drawView(1);
        return true;
    }


    private boolean isNewPoint(float x, float y) {
        if (Math.abs(x - lastX) <= 1f && Math.abs(y - lastY) <= 1f) {
            LogUtil.e("xy...isNewPoint false");
            return false;
        }
        lastX = x;
        lastY = y;
        return true;
    }


    //设置页面的绘画路径保存对象
    public void setPageChannel(PageChannel pageChannel, int currentPage) {
        this.pageChannel = pageChannel;
        this.currentPage = currentPage;
    }

    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        dstbmp = null;
    }

    //设置图片对象
    public void setImageView(Bitmap bitmap) {
        this.bitmap = bitmap;
        dstbmp = null;
        reDraw();
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
                isWait = true;
                action = new MyEraser(x, y, Color.WHITE, size);
                break;
            case 0:
                isWait = false;
                action = new MyPath(x, y, color, size);
                break;
            case 1:
                isWait = true;
                action = new MyLine(x, y, color, size);
                break;
            case 2:// Circle(5),
                isWait = true;
                action = new MyCircle(x, y, color, size, 10);
                break;
            case 3:// Rect(4),
                isWait = true;
                action = new MyRect(x, y, color, size);
                break;
            case 4://triangle(3),
                isWait = true;
                action = new MyPath(x, y, color, size);//需要修改成三角形
                break;
            default://Path(1),
                isWait = false;
                action = new MyPath(x, y, color, size);
                break;
        }
        return action;
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            isFreeze = true;
        } else {
            isFreeze = false;
        }
        super.onWindowFocusChanged(hasWindowFocus);
    }

//    public DrawThread getThreadInstance() {
//        if (dt != null) {
//            return dt;
//        } else {
//            dt = new DrawThread();
//            return dt;
//        }
//    }

//    class DrawThread extends Thread {
//        @Override
//        public void run() {
//            while (isRunning) {
//                if (isWait) {//切换到非画笔
//                    try {
//                        Thread.sleep(300);
//                    } catch (InterruptedException e) {
//                        LogUtil.e("DrawThread InterruptedException" + e.getMessage());
//                    }
//                } else {
//                    if (Math.abs(lastTouchTime - System.currentTimeMillis()) < 300) {//画笔有动作
//                        try {
//                            drawView(2);
//                        } catch (Exception e) {
//                            LogUtil.e("DrawThread Exception" + e.getMessage());
//                        } finally {
//                            try {
//                                if (isFreeze) {
//                                    Thread.sleep(50);
//                                } else {
//                                    Thread.sleep(5);
//                                }
//                            } catch (InterruptedException e) {
//                                LogUtil.e("DrawThread InterruptedException" + e.getMessage());
//                            }
//                        }
//                    } else {
//                        try {
//                            Thread.sleep(50);
//                        } catch (InterruptedException e) {
//                            LogUtil.e("DrawThread 10 InterruptedException" + e.getMessage());
//                        }
//                    }
//                }
//            }
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        if (cacheBitmap == null || cacheBitmap.isRecycled()) {
            cacheBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
            cacheBitmap.setHasAlpha(true);
            tmpCanvas = new Canvas(cacheBitmap);
        }
        // 绘制历史记录(自己的)
        if (pageChannel.paintChannel != null && pageChannel.paintChannel.actions != null) {
            // 绘制当前
            if (pageChannel.paintChannel.action != null) {
                pageChannel.paintChannel.action.onDraw(canvas);
            }
        }

        if (null != cacheBitmap && !cacheBitmap.isRecycled()) {
            canvas.drawBitmap(cacheBitmap, 0, 0, null);
        }
    }
    /**
     * 绘制界面
     *
     * @param flag 0：surfaceChanged 1：reDraw 2：DrawThread 3:绘制历史数据
     */
    public void drawView(int flag) {
        LogUtil.e("drawView flag" + flag);
        if (isDestory || null == surfaceHolder) {
            return;
        }
        Canvas mCanvas = null;
        try {
            mCanvas = surfaceHolder.lockCanvas();
        } catch (Exception e) {
            LogUtil.e("surfaceHolder.lockCanvas Exception:" + e.getMessage());
        }
        if (mCanvas == null) {
            return;
        }
        canvasHeight = mCanvas.getHeight();
        canvasWidth = mCanvas.getWidth();

        switch (flag) {
//            case 0:
//                LogUtil.e("surfaceChanged canvasHeight" + canvasHeight + ",canvasWidth:" + canvasWidth);
////                bgBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
//                cacheBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
//                cacheBitmap.setHasAlpha(true);
//                tmpCanvas = new Canvas(cacheBitmap);
//                tmpCanvas.setDrawFilter(new PaintFlagsDrawFilter(0,
//                        Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
//                drawBgAndBitmap(mCanvas);
//                break;
//            case 1:
//                isWait = true;
//                //橡皮擦功能先保存画布
//                layerId = mCanvas.saveLayer(0, 0, canvasWidth, canvasHeight,
//                        null, Canvas.ALL_SAVE_FLAG);
//                cacheBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
//                cacheBitmap.setHasAlpha(true);
//                tmpCanvas.setBitmap(cacheBitmap);
//                // 绘制历史记录(自己的)
//                if (pageChannel.paintChannel != null && pageChannel.paintChannel.actions != null) {
//                    for (Action a : pageChannel.paintChannel.actions) {
////                       LogUtil.e("reDraw history");
//                        a.onDraw(mCanvas);
//                        a.onDraw(tmpCanvas);
//                        //1.老师端接收到--先画自己矩形--再画橡皮擦路径
//                    }
//                    // 绘制当前
//                    if (pageChannel.paintChannel.action != null) {
//                        pageChannel.paintChannel.action.onDraw(mCanvas);
//                        pageChannel.paintChannel.action.onDraw(tmpCanvas);
//                    }
//                }
//                mCanvas.restoreToCount(layerId);
//                drawBgAndBitmap(mCanvas);
//                isWait = false;
//                break;
            case 2:
                if (cacheBitmap == null || cacheBitmap.isRecycled()) {
                    cacheBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
                    cacheBitmap.setHasAlpha(true);
                    tmpCanvas = new Canvas(cacheBitmap);
                }
                // 绘制历史记录(自己的)
                if (pageChannel.paintChannel != null && pageChannel.paintChannel.actions != null) {
                    for (Action a : pageChannel.paintChannel.actions) {
                        a.onDraw(mCanvas);
                        a.onDraw(tmpCanvas);
                        //1.老师端接收到--先画自己矩形--再画橡皮擦路径
                    }
                    // 绘制当前
                    if (pageChannel.paintChannel.action != null) {
                        pageChannel.paintChannel.action.onDraw(mCanvas);
                        pageChannel.paintChannel.action.onDraw(tmpCanvas);
                    }
                }
                drawBgAndBitmap(mCanvas);
                break;
//            case 3:
////                mCanvas.drawColor(bgColor);
//                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                if (bitmap != null) {
//                    drawBg(mCanvas);
//                }
//                //橡皮擦功能先保存画布
//                layerId = mCanvas.saveLayer(0, 0, canvasWidth, canvasHeight,
//                        null, Canvas.ALL_SAVE_FLAG);
//                // 绘制历史记录(自己的)
//                if (pageChannel.paintChannel != null && pageChannel.paintChannel.actions != null) {
//                    for (Action a : pageChannel.paintChannel.actions) {
////                        LogUtil.e("draw history");
//                        a.onDraw(mCanvas);
//                        //1.老师端接收到--先画自己矩形--再画橡皮擦路径
//                    }
//                    // 绘制当前
//                    if (pageChannel.paintChannel.action != null) {
//                        pageChannel.paintChannel.action.onDraw(mCanvas);
//                    }
//                    //绘制当前别人绘制的
//                    if (null != otherPeopleaction) {
//                        otherPeopleaction.onDraw(mCanvas);
//                    }
//                }
//                mCanvas.restoreToCount(layerId);
//                break;
        }

        if (null != surfaceHolder && null != mCanvas) {
            surfaceHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    private void drawBgAndBitmap(Canvas mCanvas) {
        // 绘制背景
//        mCanvas.drawColor(bgColor);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (bitmap != null) {
//            drawBg(mCanvas);
        }
//        if (bgBitmap != null && !bgBitmap.isRecycled()) {
//            mCanvas.drawBitmap(bgBitmap, 0, 0, null);
//        }
        if (null != cacheBitmap && !cacheBitmap.isRecycled()) {
            mCanvas.drawBitmap(cacheBitmap, 0, 0, null);
        }
    }

//    private void drawBg(Canvas mCanvas) {
//        if (dstbmp == null || src == null || dest == null) {
//            bitmapWidth = bitmap.getWidth();
//            bitmapHeight = bitmap.getHeight();
//            LogUtil.e("canvasWidth" + canvasWidth + "，canvasHeight:" + canvasHeight);
//            LogUtil.e("bitmapWidth:" + bitmapWidth + ",bitmapHeight" + bitmapHeight);
//            dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
//                    bitmapHeight, null, true);
//            cacheBitmap.setHasAlpha(true);
//            dstbmpWidth = dstbmp.getWidth();
//            dstbmpHeight = dstbmp.getHeight();
////            LogUtil.e("dstbmpWidth:"+dstbmpWidth+",dstbmpHeight"+dstbmpHeight);
//            src = new Rect(0, 0, dstbmpWidth, dstbmpHeight);
//            // Rect用于居中显示
//            int left = canvasWidth / 2 - dstbmpWidth / 2;
//            if (left < 0) {
//                left = 0;
//            }
//            int top = canvasHeight / 2 - dstbmpHeight / 2;
//            if (top < 0) {
//                top = 0;
//            }
//            int right = canvasWidth / 2 + dstbmpWidth / 2;
//            if (right > canvasWidth) {
//                right = canvasWidth;
//            }
//            int bottom = canvasHeight / 2 + dstbmpHeight / 2;
//            if (bottom > canvasHeight) {
//                bottom = canvasHeight;
//            }
//            dest = new Rect(left, top, right, bottom);
//        }
////        LogUtil.e("dest.width()："+dest.width()+"dest.height()"+dest.height());
//        if (null != dstbmp && !dstbmp.isRecycled()) {
//            mCanvas.drawBitmap(dstbmp, src, dest, null);
//        }
//    }

    public void onDestoryView() {
        isDestory = true;
        isRunning = false;
//        if (dt != null) {
//            dt = null;
//        }
//        if(null != transactionManager){
//            transactionManager.end();
//            transactionManager = null;
//        }
        ToastUtil.clearOnlyToast();
        //dstbmp.recycle()退出后再次进入会出现trying to use a recycled bitmap异常
//        if (null != dstbmp && !dstbmp.isRecycled()) {
//            dstbmp.recycle();
        dstbmp = null;
//        }
//        if (null != cacheBitmap && !cacheBitmap.isRecycled()) {
//            cacheBitmap.recycle();
        cacheBitmap = null;
//        }
//        if (null != bitmap && !bitmap.isRecycled()) {
//            bitmap.recycle();
        bitmap = null;
//        }
//        fst = null;
        pageChannel = null;
        otherPeopleaction = null;
        this.destroyDrawingCache();
        System.gc();
    }

    public void saveScreen() {
        boolean isAllGranted = PermissionUtils.checkPermissionAllGranted(context,permissions);
        if(!isAllGranted){
            ActivityCompat.requestPermissions(context,permissions,100);
            return;
        }
        if (FastClick.isFastClick()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap saveBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
                Canvas tmpCanvas = new Canvas(saveBitmap);
                tmpCanvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                        Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                tmpCanvas.drawColor(bgColor);
                if (bitmap != null) {
                    tmpCanvas.drawBitmap(dstbmp, 0, 0, null);
                }
                if (null != cacheBitmap) {
                    tmpCanvas.drawBitmap(cacheBitmap, 0, 0, null);
                }

                File imageFile = ImageLibraryHelper.getOutputMediaFile(context);
                BitmapUtil.saveBitmapToFile(saveBitmap, imageFile);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
                ToastUtil.runOnUiShortToast((Activity) context,"屏幕已截取到" + imageFile.getAbsolutePath());
                saveBitmap.recycle();
            }
        }.start();

    }
    private String getImAccount(){
        return "xy_tdcq";
    }
}
