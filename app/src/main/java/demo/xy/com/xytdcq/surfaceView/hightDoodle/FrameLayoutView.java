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

public class FrameLayoutView extends FrameLayout {
    private boolean isDrawSelect = false;
    protected Paint paint;
    private Point startPoint;
    private Point endPoint;

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
        if (isDrawSelect() && startPoint != null && endPoint != null) {
            if (paint == null) {
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setStrokeWidth(1);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeCap(Paint.Cap.ROUND);
            }
//            canvas.drawColor(0x50C7EDCC);
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(1);
            canvas.drawRect(startPoint.getX() < endPoint.getX() ? startPoint.getX() : endPoint.getX(),
                    startPoint.getY() < endPoint.getY() ? startPoint.getY() :endPoint.getY(),
                    endPoint.getX() > startPoint.getX() ? endPoint.getX() : startPoint.getX(),
                    endPoint.getY() > startPoint.getY() ? endPoint.getY(): startPoint.getY(), paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void addSelfView(View view, float startX, float startY){

    }
}
