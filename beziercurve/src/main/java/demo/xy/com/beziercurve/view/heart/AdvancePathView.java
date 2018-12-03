package demo.xy.com.beziercurve.view.heart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import demo.xy.com.beziercurve.R;

public class AdvancePathView extends RelativeLayout {

    protected Random random;
    private Path mPath;
    private Paint mPaint;
    protected PointF pointFStart, pointFEnd, pointLeft, pointRight;
    protected Bitmap bitmap;

    private int[]colors ={Color.BLUE,Color.CYAN,Color.YELLOW,Color.DKGRAY ,Color.LTGRAY,Color.GREEN,Color.RED};

    public AdvancePathView(Context context) {
        super(context);
        initView();
    }

    public AdvancePathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AdvancePathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        setBackgroundColor(Color.WHITE);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);

        mPath = new Path();

        pointFStart = new PointF();
        pointLeft = new PointF();
        pointRight = new PointF();
        pointFEnd = new PointF();

        random = new Random();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
    }


    protected void initPoint() {
        //开始的出发点（最下面）
        pointFStart.x = getMeasuredWidth() / 2;
        pointFStart.y = getMeasuredHeight() - 10;
        //结束点 （最上面）
        pointFEnd.y = 10;
        pointFEnd.x = getMeasuredWidth() / 2;

        //最左边点
        pointLeft.x = 10;
        pointLeft.y = getMeasuredHeight() * 3 / 4;

        //最右边点
        pointRight.x = getMeasuredWidth() - 10;
        pointRight.y = getMeasuredHeight() / 4;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPoint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(pointLeft.x, pointLeft.y, 10, mPaint);
        canvas.drawCircle(pointRight.x, pointRight.y, 10, mPaint);
        canvas.drawCircle(pointFEnd.x, pointFEnd.y, 10, mPaint);
        canvas.drawCircle(pointFStart.x, pointFStart.y, 10, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        mPath.moveTo(pointFStart.x, pointFStart.y);
        mPath.cubicTo(pointLeft.x, pointLeft.y, pointRight.x, pointRight.y, pointFEnd.x, pointFEnd.y);//同样是用来实现多阶贝塞尔曲线的。
//        mPath.rCubicTo(pointLeft.x, pointLeft.y, pointRight.x, pointRight.y, pointFEnd.x, pointFEnd.y);//同样是用来实现多阶贝塞尔曲线的。与cubicTo类似
        canvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    private Bitmap drawHeart(int color) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
        canvas.drawColor(color, PorterDuff.Mode.SRC_ATOP);
        canvas.setBitmap(null);
        return newBitmap;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


        Button button = new Button(getContext());
        button.setText("添加一个心");

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addHeart();
            }
        });

        addView(button);
    }

    public void addHeart() {
        ImageView imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(ALIGN_PARENT_BOTTOM);
        imageView.setImageBitmap(drawHeart(colors[random.nextInt(colors.length)]));
        addView(imageView, params);
        moveHeart(imageView);
    }



    private void moveHeart(final ImageView view){
        PointF pointLeft = this.pointLeft;
        PointF pointRight = this.pointRight;
        PointF pointFStart = this.pointFStart;
        PointF pointFEnd = this.pointFEnd;


        ValueAnimator animator = ValueAnimator.ofObject(new TypeE(pointLeft, pointRight), pointFStart, pointFEnd);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF value = (PointF) animation.getAnimatedValue();
                view.setX(value.x);
                view.setY(value.y);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                AdvancePathView.this.removeView(view);
            }
        });

        ObjectAnimator af = ObjectAnimator.ofFloat(view, "alpha", 1f, 0);
        af.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                AdvancePathView.this.removeView(view);
            }
        });

       AnimatorSet set = new AnimatorSet();
        set.setDuration(3000);
        set.play(animator).with(af);
        set.start();

    }



    /**
     * 绘制一个增值器
     */
    class TypeE implements TypeEvaluator<PointF> {

        private PointF pointFFirst,pointFSecond;

        public TypeE(PointF start,PointF end){
            this.pointFFirst =start;
            this.pointFSecond = end;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {//三阶贝塞尔曲线公式 B(t)=P0(1-t)^3+3P1t(1-t)^2+3P2t^2(1-t)+P3t^3    t->[0,1]
            PointF result = new PointF();
            float left = 1 - fraction;
            result.x = (float) (startValue.x*Math.pow(left,3)+3*pointFFirst.x*Math.pow(left,2)*fraction+3*pointFSecond.x*Math.pow(fraction, 2)*left+endValue.x*Math.pow(fraction,3));
            result.y= (float) (startValue.y*Math.pow(left,3)+3*pointFFirst.y*Math.pow(left,2)*fraction+3*pointFSecond.y*Math.pow(fraction, 2)*left+endValue.y*Math.pow(fraction,3));
            return result;
        }
    }


}
