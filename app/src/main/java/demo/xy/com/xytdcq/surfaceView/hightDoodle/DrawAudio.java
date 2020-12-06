package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * 音频
 */
public class DrawAudio extends BasePath {
    private String audioUrl; // 音频路径
    private String localAudioUrl; // 本地音频路径

    public DrawAudio(Context context) {
        super(context);
    }

    public DrawAudio(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawAudio(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawAudio(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
