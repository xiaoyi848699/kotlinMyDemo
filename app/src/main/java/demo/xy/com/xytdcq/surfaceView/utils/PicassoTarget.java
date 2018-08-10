package demo.xy.com.xytdcq.surfaceView.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import demo.xy.com.xytdcq.uitls.LogUtil;

/**
 * Created by xy on 2018/8/10.
 */
public class PicassoTarget implements Target {
    private String picUrl;
    public interface PicassoCallBack {
        void onBitmapLoaded(Bitmap bitmap);
        void onBitmapFailed();
        void onPrepareLoad();
    }
    private PicassoCallBack callBack;

    public PicassoTarget(String picUrl, PicassoCallBack callBack) {
        this.picUrl = picUrl;
        this.callBack = callBack;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        LogUtil.e("onBitmapLoaded::"+bitmap.getByteCount());
        callBack.onBitmapLoaded(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        LogUtil.e("onBitmapFailed"+picUrl);
        callBack.onBitmapFailed();
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
}
