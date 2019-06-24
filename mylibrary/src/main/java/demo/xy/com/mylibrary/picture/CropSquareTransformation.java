package demo.xy.com.mylibrary.picture;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * 毕加索图片加载 控制加载图片大小  大于指定值会按比例缩减大小
 * Created by xy on 2018/6/25.
 *   Picasso.with(context)
     .load(url)
     .placeholder(getDefaultImage(IMAGE_TYPE))
     .transform(new CropSquareTransformation())
     .noFade()
     .error(getDefaultImage(IMAGE_TYPE))
     .into(view);
 */
public class CropSquareTransformation implements Transformation {

   public static int targetWidth = 250;//目标最大宽度

    public static int targetHeight = 250;//目标最大高度


    @Override
    public Bitmap transform(Bitmap source) {

        if (source.getWidth() == 0 || source.getHeight() == 0) {
            return source;
        }

        if (source.getWidth() > source.getHeight()) {//横向长图
            if (source.getHeight() < targetHeight && source.getWidth() <= targetWidth) {
                return source;
            } else {
                //如果图片大小大于等于设置的高度，则按照设置的高度比例来缩放
                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int height = (int)(targetWidth/aspectRatio);
                if (height != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, height, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            }
        } else {//竖向长图
            //如果图片小于设置的宽度，则返回原图
            if (source.getWidth() < targetWidth && source.getHeight() <= targetHeight) {
                return source;
            } else {
                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int width = (int)(targetHeight/aspectRatio);
                if (targetHeight != 0 && width != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, width, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            }
        }
    }

    @Override
    public String key() {
        return "desiredWidth" + " desiredHeight";
    }
}
