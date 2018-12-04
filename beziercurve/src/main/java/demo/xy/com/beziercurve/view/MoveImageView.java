package demo.xy.com.beziercurve.view;

import android.content.Context;
import android.graphics.PointF;

/**
 * Created by xy on 2018/12/4.
 */
public class MoveImageView extends android.support.v7.widget.AppCompatImageView {

    public MoveImageView(Context context) {
        super(context);
    }

    public void setMPointF(PointF pointF) {
        setX(pointF.x);
        setY(pointF.y);
    }
}

