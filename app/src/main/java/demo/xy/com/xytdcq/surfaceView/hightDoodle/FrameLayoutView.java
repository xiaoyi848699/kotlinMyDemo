package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Canvas;
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
        Rect anchorRect = new Rect();
        Rect rootViewRect = new Rect();

        getGlobalVisibleRect(anchorRect);
        getGlobalVisibleRect(rootViewRect);

        // 创建 imageView
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.a));
        addView(imageView);

        // 调整显示区域大小
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.width = 100;
        params.height = 100;
        imageView.setLayoutParams(params);

        // 设置居中显示
        imageView.setY(200);
        imageView.setX(200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void addSelfView(View view, float startX, float startY){

    }
}
