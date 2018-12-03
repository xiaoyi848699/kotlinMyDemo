package demo.xy.com.beziercurve.view.heart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class PathView extends LinearLayout {

    private Paint mPaint;
    private Path mPath;
    private float startX, startY, endX, endY, touchX, touchY;

    private boolean isFill;

    public PathView(Context context) {
        super(context);
        initView();
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        startX = getMeasuredWidth() / 5;
        startY = endY = getMeasuredHeight() *3/ 4;
        endX = getMeasuredWidth() * 4 / 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(20);
        canvas.drawText("痛苦最好是别人的，快乐才是自己的；麻烦将是暂时的，朋友总是永恒的", startX, startY / 6, mPaint);

        mPath.reset();
        //初始化Path
        Path path = new Path();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2f);
        mPaint.setTextSize(20f);
        //以（300,300）为圆心，200为半径绘制圆
        //Path.Direction.CW顺时针绘制圆 Path.Direction.CCW逆时针绘制圆
        path.addCircle(200, 200, 150, Path.Direction.CW);//沿顺时针方向绘制
        //沿path绘制文字
        canvas.drawTextOnPath("痛苦最好是别人的，快乐才是自己的；麻烦将是暂时的，朋友总是永恒的。", path, 0, 0, mPaint);
        canvas.drawPath(path, mPaint);
        mPath.reset();
        Path path2 = new Path();
        path2.addCircle(600, 200, 150, Path.Direction.CCW);//沿逆时针方向绘制
        //沿path绘制文字
        canvas.drawTextOnPath("痛苦最好是别人的，快乐才是自己的；麻烦将是暂时的，朋友总是永恒的。", path2, 0, 0, mPaint);
        canvas.drawPath(path2, mPaint);
        mPath.reset();


        //绘制椭圆
        RectF rectF = new RectF(100, 350, 500, 600);
        path.addOval(rectF, Path.Direction.CW);
        //第二种方法绘制椭圆
        path.addOval(600, 350, 1000, 600, Path.Direction.CW);

        //绘制矩形
        RectF rect = new RectF(100, 650, 500, 900);
        //第一种方法绘制矩形
        path.addRect(rect, Path.Direction.CW);
        //第一种方法绘制矩形
        path.addRect(600, 650, 1000, 900, Path.Direction.CCW);

        //绘制圆角矩形
        RectF roundRect = new RectF(100, 950, 300, 1100);
        //第一种方法绘制圆角矩形
        path.addRoundRect(roundRect, 20, 20, Path.Direction.CW);
        //第二种方法绘制圆角矩形
        path.addRoundRect(350, 950, 550, 1100, 10, 50, Path.Direction.CCW);//和上面效果没啥区别
        //第三种方法绘制圆角矩形
        //float[] radii中有8个值，依次为左上角，右上角，右下角，左下角的rx,ry
        RectF roundRectT = new RectF(600, 950, 800, 1100);
        path.addRoundRect(roundRectT, new float[]{50, 50, 50, 50, 50, 50, 0, 0}, Path.Direction.CCW);
        //第四种方法绘制圆角矩形
        path.addRoundRect(850, 950, 1050, 1100,new float[]{0, 0, 0, 0,50, 50, 50, 50}, Path.Direction.CCW);
        canvas.drawPath(path, mPaint);

        //arcTo 用于绘制弧线（实际是截取圆或椭圆的一部分）。
    }



}
