package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.List;

/**
 * 图片
 */
public class DrawPicture extends BasePath {
    private String imageUrl; // 图片路径
    private String localPath; // 本地路径

    public DrawPicture(Context context) {
        super(context);
    }

    public DrawPicture(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawPicture(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawPicture(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
