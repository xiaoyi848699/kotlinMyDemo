package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 文字
 */
public class DrawText extends BasePath {
    private String text; // 文字

    public DrawText(Context context) {
        super(context);
    }

    public DrawText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
