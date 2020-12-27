package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.uitls.LogUtil;

/**
 * 图片
 */
public class DrawPicture extends BasePath {
    private float operateImageSize = 40;
    private float minImageSize = 200;
    private String imageUrl; // 图片路径
    private String localPath; // 本地路径
    private int showStatus = -1;// 1:本地 2:网络
    private boolean isSelectPic = false;// 单独选中图片可以缩放
    private Bitmap deleteBitmap;
    private Bitmap leftBitmap;
    private Bitmap bottomBitmap;
    private Bitmap scaleBitmap;

    private  Bitmap bitmap;


    private float downX = 0;
    private float downY = 0;
    private float downRawX = 0;
    private float downRawY = 0;
    private float lastRawX = 0;
    private float lastRawY = 0;
    private int operate = -1;
    private float downWidth = 0;
    private float downHeight = 0;

    public String getImageUrl() {
        return imageUrl;
    }

    private void init() {
        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_image);
        leftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoomleft_image);
        bottomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoomdown_image);
        scaleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoom_image);
        minImageSize = getContext().getResources().getDimension(R.dimen.ic_pid_w_h100);
        operateImageSize = getContext().getResources().getDimension(R.dimen.ic_pid_w_h40);
    }

    @Override
    public void setSelect(boolean select) {
        super.setSelect(select);
        setSelectPic(false);
    }

    public boolean isSelectPic() {
        return isSelectPic;
    }

    public void setSelectPic(boolean selectPic) {
        isSelectPic = selectPic;
        LogUtil.e("DrawPicture","setSelectPic" + isSelectPic);
        invalidate();
    }

    public void setImageUrl(String imageUrl) {
        showStatus = 2;
        this.imageUrl = imageUrl;
        // 先展示默认图片
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.img_default);
        // 加载网络图片
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        showStatus = 1;
        this.localPath = localPath;
        bitmap = BitmapFactory.decodeFile(localPath);
//        setBackground(new BitmapDrawable(getContext().getResources(), bitmap));
        invalidate();
    }

    public DrawPicture(Context context) {
        super(context);
    }

    public DrawPicture(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawPicture(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DrawPicture(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void relaseData() {

    }

    @Override
    public boolean isNeedInvalidateOnMoveStatusChane() {
        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStrokeWidth(size);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);

        }

        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = BitmapFactory.decodeFile(localPath);
        }
        RectF rectF = new RectF(operateImageSize/2,operateImageSize/2,getViewWidth() - operateImageSize/2,getViewHeight()- operateImageSize/2); // 绘制地方
        canvas.drawBitmap(bitmap, null, rectF, paint);

        if (isSelectPic() || isSelect()) {
            canvas.drawColor(0x30888888);
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(1);
//        paint.setShadowLayer(1,1,1,Color.GRAY);
//        PathEffect effects = new DashPathEffect(new float[]{12,6},0);
//        paint.setPathEffect(effects);
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }

        if (isSelectPic()) {
            if (deleteBitmap == null || deleteBitmap.isRecycled()) {
                deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_image);
            }
            RectF deleteRectF = new RectF(0,0,operateImageSize,operateImageSize); // 绘制地方
            canvas.drawBitmap(deleteBitmap, null, deleteRectF, paint);

            if (scaleBitmap == null || scaleBitmap.isRecycled()) {
                scaleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoom_image);
            }
            RectF scaleRectF = new RectF(getViewWidth() - operateImageSize,getViewHeight() - operateImageSize,getViewWidth(),getViewHeight()); // 绘制地方
            canvas.drawBitmap(scaleBitmap, null, scaleRectF, paint);

            if (leftBitmap == null || leftBitmap.isRecycled()) {
                leftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoomleft_image);
            }
            RectF leftRectF = new RectF(getViewWidth() - operateImageSize/2,getViewHeight() / 2 - operateImageSize,getViewWidth(),getViewHeight() / 2 + operateImageSize); // 绘制地方
            canvas.drawBitmap(leftBitmap, null, leftRectF, paint);

            if (bottomBitmap == null || bottomBitmap.isRecycled()) {
                bottomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_zoomdown_image);
            }
            RectF bottomRectF = new RectF(getViewWidth() / 2 - operateImageSize,getViewHeight() - operateImageSize/2,getViewWidth() / 2 + operateImageSize,getViewHeight()); // 绘制地方
            canvas.drawBitmap(bottomBitmap, null, bottomRectF, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 不是移动  或移动多选的情况自己不拦截触摸
        if (BlackBoardAcivity.sBlackBoardStatus != 2 || isSelect) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e("DrawPicture","ACTION_DOWN");
                if (!isSelectPic) {
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
                } else if (downX > (getViewWidth() - operateImageSize) && downY > getViewHeight()/2 - operateImageSize && downY < getViewHeight()/2 + operateImageSize) {
                    // 横向拉伸
                    operate = 2;
                    downRawX = event.getRawX();
                    downRawY = event.getRawY();
                } else if (downX > (getViewWidth()/2 - operateImageSize) && downX < (getViewWidth()/2 + operateImageSize) && downY > getViewHeight() - operateImageSize) {
                    // 竖向拉伸
                    operate = 3;
                    downRawX = event.getRawX();
                    downRawY = event.getRawY();
                } else {
                    // 移动
                    operate = 10;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e("DrawPicture","ACTION_MOVE");
                if (!isSelectPic) {
                    return true;
                }
//                LogUtil.e("DrawPicture",event.getRawX() + "," + event.getRawY() +":,getX" + event.getX() + ",getY" + event.getY() );
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                if (Math.abs(lastRawX - rawX) < 8 && Math.abs(lastRawY - rawY) < 8) {
                    return true;
                }
                if (operate == 10) {
                    setX(rawX-downX);
                    setY(rawY-downY);
                    lastRawX = rawX;
                    lastRawY = rawY;
                } else if (operate == 1) {
                    int viewWidth = (int) (downWidth + (event.getRawX()-downRawX));
                    viewWidth = viewWidth < minImageSize ? (int) minImageSize : viewWidth;
                    int viewHeight = (int) (downHeight + (event.getRawY()-downRawY));
                    viewHeight = viewHeight < minImageSize ? (int) minImageSize : viewHeight;
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = viewWidth;
                    layoutParams.height = viewHeight;
                    setLayoutParams(layoutParams);

                    LogUtil.e("DrawPicture",viewWidth + "ACTION_MOVE" + minImageSize);
                    LogUtil.e("DrawPicture",viewHeight +"ACTION_MOVE" + minImageSize);
                    lastRawX = rawX;
                    lastRawY = rawY;
                    setViewWidth(viewWidth);
                    setViewHeight(viewHeight);
                    invalidate();
                } else if (operate == 2) {
                    int viewWidth = (int) (downWidth + (event.getRawX()-downRawX));
                    viewWidth = viewWidth < minImageSize ? (int) minImageSize : viewWidth;
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = viewWidth;
                    setLayoutParams(layoutParams);
                    lastRawX = rawX;
                    lastRawY = rawY;
                    setViewWidth(viewWidth);
                    invalidate();
                } else if (operate == 3) {
                    int viewHeight = (int) (downHeight + (event.getRawY()-downRawY));
                    viewHeight = viewHeight < minImageSize ? (int) minImageSize : viewHeight;
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = viewHeight;
                    setLayoutParams(layoutParams);
                    lastRawX = rawX;
                    lastRawY = rawY;
                    setViewHeight(viewHeight);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.e("DrawPicture","ACTION_UP");
                if (!isSelectPic) {
                    setSelectPic(true);
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
                    viewWidth = viewWidth < minImageSize ? (int) minImageSize : viewWidth;
                    int viewHeight = (int) (downHeight + (event.getRawY()-downRawY));
                    viewHeight = viewHeight < minImageSize ? (int) minImageSize : viewHeight;
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = viewWidth;
                    layoutParams.height = viewHeight;
                    setLayoutParams(layoutParams);
                    setViewWidth(viewWidth);
                    setViewHeight(viewHeight);
                    invalidate();
                    // 只需要改动结束点
                    setEndPoint(new Point(getStartPoint().getX() + viewWidth, getStartPoint().getY() + viewHeight));
                } else if (operate == 2) {
                    int viewWidth = (int) (downWidth + (event.getRawX()-downRawX));
                    viewWidth = viewWidth < minImageSize ? (int) minImageSize : viewWidth;
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = viewWidth;
                    setLayoutParams(layoutParams);
                    setViewWidth(viewWidth);
                    invalidate();
                    setEndPoint(new Point(getStartPoint().getX() + viewWidth, getStartPoint().getY() + viewHeight));
                } else if (operate == 3) {
                    int viewHeight = (int) (downHeight + (event.getRawY()-downRawY));
                    viewHeight = viewHeight < minImageSize ? (int) minImageSize : viewHeight;
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.height = viewHeight;
                    setLayoutParams(layoutParams);
                    setViewHeight(viewHeight);
                    invalidate();
                    setEndPoint(new Point(getStartPoint().getX() + viewWidth, getStartPoint().getY() + viewHeight));
                }
                // 更新在list中的数据
                if (changeCallback != null) {
                    changeCallback.changeCallBack(vid, startPoint, endPoint, viewWidth, viewHeight);
                }
                break;
        }
        if (isSelectPic()) {
           return true;
        }
        return super.onTouchEvent(event);
    }
}
