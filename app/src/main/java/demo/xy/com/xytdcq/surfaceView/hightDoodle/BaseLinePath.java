package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import demo.xy.com.xytdcq.uitls.ScreenCenter;
import demo.xy.com.xytdcq.uitls.Utils;

/**
 * 形状基类，所有涂鸦板上的绘制的形状继承该基类
 */
public abstract class BaseLinePath extends BasePath{

    public BaseLinePath(Context context) {
        super(context);
    }

    public BaseLinePath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLinePath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseLinePath(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public abstract void addAll(ArrayList<Point> points);

}