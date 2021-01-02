package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import demo.xy.com.mylibrary.KeyBoardUtils;
import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.uitls.LogUtil;
import demo.xy.com.xytdcq.uitls.Utils;

/**
 * 文字(暂时未用到  使用DrawText)
 */
@SuppressLint("AppCompatCustomView")
public class DrawLayoutText extends RelativeLayout implements IBasePath {
    private final String TAG = DrawLayoutText.class.getSimpleName();
    private String text; // 文字
    private float operateImageSize = 40;
    private int minWidthSize = 100;
    private int minHeightSize = 100;

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
    private EditText mEditText;
    private ImageView deteleImage;
    private ImageView scaleImage;
    public DrawLayoutText(Context context) {
        super(context);
        init(context);
    }

    public DrawLayoutText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawLayoutText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawLayoutText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        vid = String.valueOf(System.currentTimeMillis());
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_edittext,null);
        mEditText = view.findViewById(R.id.edittext);
        deteleImage = view.findViewById(R.id.detele_image);
        scaleImage = view.findViewById(R.id.scale_image);
//        mEditText = new EditText(mContext);
//        mEditText.setHint("输入文本");
//        mEditText.setPadding(10,10,10,10);
//        mEditText.setTextColor(getContext().getResources().getColor(R.color.black));
//        mEditText.setBackgroundResource(R.drawable.edittext_bg_gray_corners);
//        mEditText.setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.ic_pid_w_h12));
//        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_image);
//        scaleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoom_image);
//        minWidthSize = (int) getContext().getResources().getDimension(R.dimen.ic_pid_w_h120);
//        minHeightSize = (int) getContext().getResources().getDimension(R.dimen.ic_pid_w_h60);
//        operateImageSize = getContext().getResources().getDimension(R.dimen.ic_pid_w_h18);
//        mEditText.setMinWidth(minWidthSize);
        setViewWidth(minWidthSize);
//        mEditText.setMinHeight(minHeightSize);
        setViewHeight(minHeightSize);
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.setMargins((int)(operateImageSize * 0.6),(int)(operateImageSize * 0.6),(int)(operateImageSize * 0.6),(int)(operateImageSize * 0.6));
        addView(view);
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtil.w("mEditText onFocusChange " + hasFocus);
                if (hasFocus) {
                    setSelectEdit(true);
                    invalidate();
                    deteleImage.setVisibility(GONE);
                    scaleImage.setVisibility(GONE);
                } else {
                    deteleImage.setVisibility(VISIBLE);
                    scaleImage.setVisibility(VISIBLE);
                }
            }
        });
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

        canvas.drawColor(0x30853214);
        if (isSelect()) {
            canvas.drawColor(0x30888888);
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(1);
            canvas.drawRect(1, 1, getWidth()-1, getHeight()-1, paint);
        }

        if (isSelectEdit()) {
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(1);
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

//    @Override
    public void setFocusable(boolean focusable) {
        LogUtil.w("setFocusable " + focusable);
        if (!focusable) {
            deteleImage.setVisibility(GONE);
            scaleImage.setVisibility(GONE);
            mEditText.setFocusable(false);
            mEditText.setFocusableInTouchMode(false);
            KeyBoardUtils.closeKeybord(mEditText, mContext);
            setSelectEdit(true);
            invalidate();
        }
//        super.setFocusable(focusable);
    }

//    @Override
//    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        LogUtil.e(TAG,"onFocusChanged" + focused);
//        if (!focused) {
//            setSelectEdit(true);
//        }
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.e(TAG,"onTouchEvent" + BlackBoardAcivity.sBlackBoardStatus + isSelect);
        // 不是移动  或移动多选的情况自己不拦截触摸
        if (BlackBoardAcivity.sBlackBoardStatus != 2 || isSelect) {
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
                    mEditText.setFocusable(true);
                    mEditText.setFocusableInTouchMode(true);
                    mEditText.requestFocus();
                    KeyBoardUtils.openKeybord(mEditText,mContext);
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
