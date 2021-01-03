package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import demo.xy.com.mylibrary.KeyBoardUtils;
import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.uitls.LogUtil;
import demo.xy.com.xytdcq.uitls.ScreenCenter;
import demo.xy.com.xytdcq.uitls.Utils;

/**
 * 文字
 */
@SuppressLint("AppCompatCustomView")
public class DrawText extends EditText implements IBasePath {
    private final String TAG = DrawText.class.getSimpleName();
    private String text; // 文字
    private float operateImageSize = 40;
    private int minWidthSize = 100;
    private int maxWidthSize = 100;
    private int minHeightSize = 100;
    private int lineHeightSize = 40;

    protected String vid;
    protected String sid = "root";
    protected String uid;
    protected String rect;
    protected String drawType = PathType.LINE.toString();
    protected int index = 0;
    protected boolean isSelect = false; // 是否选择
    protected boolean isSelectEdit = false; // 是否单独选择
    protected boolean isMoveEnd = true; // 是否在移动结束
    protected boolean isRemove = false; // 是否删除
    protected int listIndex; // 存放对象的列表中的位置，用于刷新数据

    protected ArrayList<Point> points = new ArrayList<>();
    protected Point startPoint;
    protected Point endPoint;
    protected float viewWidth;
    protected float viewHeight;
    protected float offsetX = 0;
    protected float offsetY = 0;

    protected int color = Color.RED;
    protected int size = 2;
    protected Paint paint;
    protected Path path;

    protected IChangeCallback changeCallback;
    private Bitmap deleteBitmap;
    private Bitmap scaleBitmap;
    private float downX = 0;
    private float downY = 0;
    private float downRawX = 0;
    private float downRawY = 0;
    private float lastRawX = 0;
    private float lastRawY = 0;
    private int operate = -1;
    private float downWidth = 0;
    private float downHeight = 0;

    private Context mContext;

    public DrawText(Context context) {
        super(context);
        init(context);
    }

    public DrawText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        vid = String.valueOf(System.currentTimeMillis());
        setHint("输入文本");
        setTextColor(getContext().getResources().getColor(R.color.black));
        setBackgroundResource(R.color.transparent);
        setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.ic_pid_w_h12));
        setLongClickable(false);
        setTextIsSelectable(false);
        setGravity(Gravity.TOP);

        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_image);
        scaleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoom_image);
        minWidthSize = (int) getContext().getResources().getDimension(R.dimen.ic_pid_w_h100);
        int widthSize = (int) getContext().getResources().getDimension(R.dimen.ic_pid_w_h300);
        lineHeightSize = (int) getContext().getResources().getDimension(R.dimen.ic_pid_w_h24);
        operateImageSize = getContext().getResources().getDimension(R.dimen.ic_pid_w_h26);
        minHeightSize = (int) (lineHeightSize +  operateImageSize * 2);
        int padding = (int) (operateImageSize);
        setPadding(padding,padding,padding,padding);
        setMinWidth(widthSize);
        setViewWidth(widthSize);
        setMinHeight(minHeightSize);
        setViewHeight(minHeightSize);
    }

    public void setChangeCallback(IChangeCallback changeCallback) {
        this.changeCallback = changeCallback;
    }

    public String getVid() {
        return vid;
    }

    public boolean isSelectEdit() {
        return isSelectEdit;
    }

    public void setSelectEdit(boolean selectEdit) {
        isSelectEdit = selectEdit;
        invalidate();
    }

    public int getListIndex() {
        return listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    public String getDrawType() {
        return drawType;
    }

    public void setDrawType(String drawType) {
        this.drawType = drawType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        if (isSelectEdit) {
            setSelectEdit(false);
            invalidate();
        }
        // 状态改变才刷新界面
        if (isSelect != select) {
            isSelect = select;
            invalidate();
        }
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
        maxWidthSize = (int) (ScreenCenter.getDisplayWidth() - startPoint.getX() - operateImageSize * 2);
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(float viewWidth) {
        this.viewWidth = viewWidth;
        // 宽度改变时 最小高度需要根据文字长度来计算
        String text = getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        float textWidth = getPaint().measureText(text);
        int line = (int) Math.max(1, Math.ceil(textWidth / (getViewWidth() - operateImageSize * 2)));
        minHeightSize = (int) (lineHeightSize * line +  operateImageSize * 2);
    }

    public float getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(float viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void checkIsSelect(float minX,float maxX,float minY,float maxY) {
        if (Utils.getInterval(minX,startPoint.getX(),maxX,endPoint.getX()) &&
                Utils.getInterval(minY,startPoint.getY(),maxY,endPoint.getY())) {
            setSelect(true);
        } else {
            // 没有选中
            setSelect(false);
        }
    }

    public void move(float moveX, float moveY, boolean isMoveEnd) {
        offsetX = moveX;
        offsetY = moveY;
        startPoint.setY(startPoint.getY() + moveY);
        startPoint.setX(startPoint.getX() + moveX);
        endPoint.setX(startPoint.getX() + getViewWidth());
        endPoint.setY(startPoint.getY() + getViewHeight());
        if (isMoveEnd != this.isMoveEnd) {
            this.isMoveEnd = isMoveEnd;
            if (isNeedInvalidateOnMoveStatusChane()) {
                invalidate();
            }
        }
    }

    @Override
    public boolean isNeedInvalidateOnMoveStatusChane() {
        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) {
            LogUtil.w("onDraw canvas = null");
            return;
        }
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStrokeWidth(size);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);

        }
        // 自己绘制edittext的边框
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        int margin = (int)(operateImageSize * 0.8);
        canvas.drawRoundRect(margin, margin, getWidth()-margin, getHeight()-margin, 10,10, paint);

        // 绘制选择时的边框
        if (isSelectEdit() || isSelect()) {
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(1);
            margin = (int)(operateImageSize * 0.5);
            canvas.drawRect(margin, margin, getWidth()-margin, getHeight()-margin, paint);
        }

        if (isSelectEdit()) {
            if (deleteBitmap == null || deleteBitmap.isRecycled()) {
                deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_image);
            }
            RectF deleteRectF = new RectF(operateImageSize * 0.1f,operateImageSize * 0.1f,operateImageSize * 0.8f,operateImageSize * 0.8f); // 绘制地方
            canvas.drawBitmap(deleteBitmap, null, deleteRectF, paint);

            if (scaleBitmap == null || scaleBitmap.isRecycled()) {
                scaleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoom_image);
            }
            RectF scaleRectF = new RectF(getViewWidth() - operateImageSize * 0.9f,getViewHeight() - operateImageSize * 0.9f,getViewWidth() - operateImageSize * 0.2f,getViewHeight() - operateImageSize * 0.2f); // 绘制地方
            canvas.drawBitmap(scaleBitmap, null, scaleRectF, paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public void setFocusable(boolean focusable) {
        if (!focusable) {
            KeyBoardUtils.closeKeybord(this, mContext);
        }
        super.setFocusable(focusable);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        LogUtil.e(TAG,"onFocusChanged" + focused);
        if (!focused) {
            setSelectEdit(true);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text == null) {
            return;
        }
        float textWidth = getPaint().measureText(text.toString());
        // 如果还能增加宽度就先增加宽度
        if ((getViewWidth() - operateImageSize * 2) < textWidth) {
            if (textWidth < maxWidthSize) {
                LogUtil.e(TAG,"onTextChanged need change width" + textWidth);
                float viewWidth = (textWidth +  operateImageSize * 2);
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                layoutParams.width = (int) viewWidth;
                setLayoutParams(layoutParams);
                setViewWidth(viewWidth);
                invalidate();
                setEndPoint(new Point(viewWidth, getStartPoint().getY()));
                return;
            } else {
                int line = (int) Math.ceil(textWidth / maxWidthSize);
                LogUtil.e(TAG,"onTextChanged need textWidth" + textWidth + ",line:" + line);
                // 无法增加宽度的情况下 如果高度不够实现自动增长
                if (getViewHeight() < (lineHeightSize * line +  operateImageSize * 2)) {
                    float viewHeight = (line * lineHeightSize +  operateImageSize * 2);
                    float viewWidth = (maxWidthSize +  operateImageSize * 2);
                    LogUtil.e(TAG,"onTextChanged need change height" + viewHeight);
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = (int) viewWidth;
                    layoutParams.height = (int) viewHeight;
                    setLayoutParams(layoutParams);
                    setViewWidth(viewWidth);
                    setViewHeight(viewHeight);
                    invalidate();
                    setEndPoint(new Point(viewWidth, viewHeight));
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.e(TAG,"onTouchEvent" + BlackBoardAcivity.sBlackBoardStatus + isSelect);
        // 不是移动  或移动多选的情况自己不拦截触摸
        if (BlackBoardAcivity.sBlackBoardStatus != 2 || isSelect) {
            LogUtil.e(TAG,"super.onTouchEvent(event)");
            getParent().requestDisallowInterceptTouchEvent(false);
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG,"ACTION_DOWN");
                if (!isSelectEdit) {
                    return true;
                }
                downX = event.getX();
                downY = event.getY();
                downWidth = viewWidth;
                downHeight = viewHeight;
                // 判断是否是在按钮位置点击
                if (downX < operateImageSize && downY < operateImageSize) {
                    // 点击的删除按钮
                    operate = 0;
                } else if (downX > (getViewWidth() - operateImageSize) && downY > getViewHeight() - operateImageSize) {
                    // 等比拉伸
                    operate = 1;
                    downRawX = event.getRawX();
                    downRawY = event.getRawY();
                } else {
                    // 移动
                    operate = 10;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e(TAG,"ACTION_MOVE");
                if (!isSelectEdit) {
                    return true;
                }
//                LogUtil.e(TAG,event.getRawX() + "," + event.getRawY() +":,getX" + event.getX() + ",getY" + event.getY() );
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                if (Math.abs(lastRawX - rawX) < 8 && Math.abs(lastRawY - rawY) < 8) {
                    return true;
                }
                if (operate == 10) {
//                    setX(rawX-downX);
//                    setY(rawY-downY);
                    this.setTranslationX(rawX-downX);
                    this.setTranslationY(rawY-downY);
                    lastRawX = rawX;
                    lastRawY = rawY;
                } else if (operate == 1) {
                    int viewWidth = (int) (downWidth + (event.getRawX()-downRawX));
                    viewWidth = viewWidth < minWidthSize ? (int) minWidthSize : viewWidth;
                    int viewHeight = (int) (downHeight + (event.getRawY()-downRawY));
                    viewHeight = viewHeight < minHeightSize ? (int) minHeightSize : viewHeight;
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = viewWidth;
                    layoutParams.height = viewHeight;
                    setLayoutParams(layoutParams);

                    LogUtil.e(TAG,viewWidth + "ACTION_MOVE" + minWidthSize);
                    LogUtil.e(TAG,viewHeight +"ACTION_MOVE" + minHeightSize);
                    lastRawX = rawX;
                    lastRawY = rawY;
                    setViewWidth(viewWidth);
                    setViewHeight(viewHeight);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG,"ACTION_UP");
                if (!isSelectEdit) {
                    setFocusable(true);
                    setFocusableInTouchMode(true);
                    requestFocus();
                    KeyBoardUtils.openKeybord(this,mContext);
//                    ((Activity)mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    if (changeCallback != null) {
                        changeCallback.changeSelectCallBack(vid);
                    }
                    return true;
                }
                if (operate == 0) {
                    if (changeCallback != null) {
                        changeCallback.deleteSelfCallBack(vid);
                    }
                    return true;
                }else if (operate == 10) {
                    setX(event.getRawX()-downX);
                    setY(event.getRawY()-downY);
                    // 起始点和结束底单都会变动
                    setStartPoint(new Point(event.getRawX()-downX,event.getRawY()-downY));
                    setEndPoint(new Point(getStartPoint().getX() + viewWidth, getStartPoint().getY() + viewHeight));
                } else if (operate == 1) {
                    int viewWidth = (int) (downWidth + (event.getRawX()-downRawX));
                    viewWidth = viewWidth < minWidthSize ? (int) minWidthSize : viewWidth;
                    int viewHeight = (int) (downHeight + (event.getRawY()-downRawY));
                    viewHeight = viewHeight < minHeightSize ? (int) minHeightSize : viewHeight;
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = viewWidth;
                    layoutParams.height = viewHeight;
                    setLayoutParams(layoutParams);
                    setViewWidth(viewWidth);
                    setViewHeight(viewHeight);
                    invalidate();
                    // 只需要改动结束点
                    setEndPoint(new Point(getStartPoint().getX() + viewWidth, getStartPoint().getY() + viewHeight));
                }
                // 更新在list中的数据
                if (changeCallback != null) {
                    changeCallback.changeCallBack(vid, startPoint, endPoint, viewWidth, viewHeight);
                }
                break;
        }
        if (isSelectEdit()) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
