package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.List;

/**
 * 直线
 */
public class DrawPathLine extends BasePath {
    private int penWidth; // 线宽：1.0、3.0、6.0
    private String penColor; // 色值：#000000、#2673f7、#e73dc
    private int penType; // 曲线-0，直线-1，圆形-2，矩形-3，三角形-4, 星形 -5，箭头-6
    private List<Point> paths;

    public DrawPathLine(Context context) {
        super(context);
    }

    public DrawPathLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawPathLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawPathLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void relaseData() {

    }

    @Override
    public boolean isNeedInvalidateOnMoveStatusChane() {
        return false;
    }
}
