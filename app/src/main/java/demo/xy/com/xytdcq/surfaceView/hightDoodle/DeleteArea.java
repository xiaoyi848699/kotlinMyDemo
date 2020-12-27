package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import demo.xy.com.xytdcq.R;

/**
 * 删除区域绘制
 */
public class DeleteArea extends BasePath {
    private Bitmap deleteBitmap;
    private Point startPoint;
    public DeleteArea(Context context) {
        super(context);
        init();
    }

    private void init() {
        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_image);
    }

    public DeleteArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DeleteArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DeleteArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void relaseData() {
        if (deleteBitmap != null && !deleteBitmap.isRecycled()) {
            deleteBitmap.recycle();
            deleteBitmap = null;
        }
    }

    @Override
    public boolean isNeedInvalidateOnMoveStatusChane() {
        return true;
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
        canvas.drawColor(0x30888888);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(1);
        paint.setShadowLayer(1,1,1,Color.GRAY);
        PathEffect effects = new DashPathEffect(new float[]{12,6},0);
        paint.setPathEffect(effects);
        canvas.drawRect(4, 4, getWidth() - 4, getHeight() - 4, paint);

        if (isMoveEnd) {
            // 绘制删除按钮
//            Rect rect = new Rect(0,0,79,80); // 绘制大小
            RectF rectF = new RectF(0,0,40,40); // 绘制地方
            canvas.drawBitmap(deleteBitmap, null, rectF, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG", "....getRawX" + event.getRawX() + "getRawY" + event.getRawY());
                Log.i("TAG", "....getX" + event.getX() + "getY" + event.getY());
                startPoint = new Point(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                Log.i("TAG", "ACTION_UP...删除？.....");
                if (event.getX() < 40 && event.getY() < 40) {
                    // 删除

                    Log.i("TAG", "...删除.....");
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
