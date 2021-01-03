package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.surfaceView.BlackBoardAcivity;
import demo.xy.com.xytdcq.uitls.LogUtil;

public class FrameLayoutView extends FrameLayout {
    private boolean isDrawSelect = false; // 绘制选择区域
    private boolean isDrawEaser = false; // 绘制橡皮
    protected Paint paint;
    private Point startPoint;
    private Point endPoint;

    public boolean isDrawEaser() {
        return isDrawEaser;
    }

    public void setDrawEaser(boolean drawEaser) {
        isDrawEaser = drawEaser;
    }

    public boolean isDrawSelect() {
        return isDrawSelect;
    }

    public void setDrawSelect(boolean drawSelect) {
        isDrawSelect = drawSelect;
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
        invalidate();
    }

    public FrameLayoutView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public FrameLayoutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FrameLayoutView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public FrameLayoutView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
//        Rect anchorRect = new Rect();
//        Rect rootViewRect = new Rect();
//
//        getGlobalVisibleRect(anchorRect);
//        getGlobalVisibleRect(rootViewRect);
//
//        // 创建 imageView
//        ImageView imageView = new ImageView(context);
//        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.a));
//        addView(imageView);
//
//        // 调整显示区域大小
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
//        params.width = 100;
//        params.height = 100;
//        imageView.setLayoutParams(params);
//
//        // 设置居中显示
//        imageView.setY(200);
//        imageView.setX(200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((isDrawSelect() || isDrawEaser()) && startPoint != null && endPoint != null) {
            if (paint == null) {
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setStrokeWidth(1);
                paint.setStyle(Paint.Style.STROKE);////画笔属性是空心圆
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeCap(Paint.Cap.ROUND);
            }
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(1);
            if (BlackBoardAcivity.sBlackBoardStatus == 2) {
                canvas.drawRect(startPoint.getX() < endPoint.getX() ? startPoint.getX() : endPoint.getX(),
                        startPoint.getY() < endPoint.getY() ? startPoint.getY() :endPoint.getY(),
                        endPoint.getX() > startPoint.getX() ? endPoint.getX() : startPoint.getX(),
                        endPoint.getY() > startPoint.getY() ? endPoint.getY(): startPoint.getY(), paint);
            } else if (BlackBoardAcivity.sBlackBoardStatus == 3){
                /**
                 * 四个参数：
                 * 参数一：圆心的x坐标
                 * 参数二：圆心的y坐标
                 * 参数三：圆的半径
                 * 参数四：定义好的画笔
                */
                canvas.drawCircle(endPoint.getX(), endPoint.getY(), BlackBoardAcivity.eraserSize, paint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void addSelfView(View view, float startX, float startY){

    }
}
