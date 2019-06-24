package demo.xy.com.mylibrary.picture;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BitmapUtil {

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity activity, Uri uri) throws Exception {
        InputStream input = activity.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = activity.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 通过Uri获取文件
     *
     * @param context
     * @param uri
     * @return
     */
    public static File getFileFromMediaUri(Context context, Uri uri) {
        if (uri.getScheme().toString().compareTo("content") == 0) {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        } else if (uri.getScheme().toString().compareTo("file") == 0) {
            return new File(uri.toString().replace("file://", ""));
        }
        return null;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 保存bitmap到指定文件
     *
     * @param bitmap
     * @param file 全路径
     */
    public static void saveBitmapToFile(Bitmap bitmap, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 将bitmap处理成黑白图片
     * @param bitmap
     * @return
     */
    public static final Bitmap grey(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }
    /**
     * 将图片变为圆角
     *
     * @param bitmap
     *            原Bitmap图片
     * @param pixels
     *            图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static boolean saveToSDCard(Bitmap bitmap) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        File file = new File("/sdcard/Download/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String filePath = "/sdcard/Download/" + fileName;
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
                fileOutputStream = new FileOutputStream(filePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        fileOutputStream);
            } catch (IOException e) {
                return false;
            } finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 保存图片到本地(JPG)
     *
     * @param bm
     *            保存的图片
     * @return 图片路径
     */
    public static String saveToLocal(Bitmap bm) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        File file = new File("/sdcard/saveImages/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String filePath = "/sdcard/saveImages/" + fileName;
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
                fileOutputStream = new FileOutputStream(filePath);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            } catch (IOException e) {
                return null;
            } finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
        return filePath;
    }

    /**
     * 保存图片到本地(PNG)
     *
     * @param bm
     *            保存的图片
     * @return 图片路径
     */
    public static String saveToLocalPNG(Bitmap bm) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        File file = new File("/sdcard/saveImages/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".png";
        String filePath = "/sdcard/saveImages/" + fileName;
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
                fileOutputStream = new FileOutputStream(filePath);
                bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            } catch (IOException e) {
                return null;
            } finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
        return filePath;
    }

    /**
     * 获取缩略图图片
     *
     * @param imagePath
     *            图片的路径
     * @param width
     *            图片的宽度
     * @param height
     *            图片的高度
     * @return 缩略图图片
     */
    public static Bitmap getImageThumbnail(String imagePath, int width,
                                           int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * LOMO特效
     *
     * @param bitmap
     *            原图片
     * @return LOMO特效图片
     */
    public static Bitmap lomoFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int dst[] = new int[width * height];
        bitmap.getPixels(dst, 0, width, 0, 0, width, height);

        int ratio = width > height ? height * 32768 / width : width * 32768
                / height;
        int cx = width >> 1;
        int cy = height >> 1;
        int max = cx * cx + cy * cy;
        int min = (int) (max * (1 - 0.8f));
        int diff = max - min;

        int ri, gi, bi;
        int dx, dy, distSq, v;

        int R, G, B;

        int value;
        int pos, pixColor;
        int newR, newG, newB;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pos = y * width + x;
                pixColor = dst[pos];
                R = Color.red(pixColor);
                G = Color.green(pixColor);
                B = Color.blue(pixColor);

                value = R < 128 ? R : 256 - R;
                newR = (value * value * value) / 64 / 256;
                newR = (R < 128 ? newR : 255 - newR);

                value = G < 128 ? G : 256 - G;
                newG = (value * value) / 128;
                newG = (G < 128 ? newG : 255 - newG);

                newB = B / 2 + 0x25;

                // ==========边缘黑暗==============//
                dx = cx - x;
                dy = cy - y;
                if (width > height)
                    dx = (dx * ratio) >> 15;
                else
                    dy = (dy * ratio) >> 15;

                distSq = dx * dx + dy * dy;
                if (distSq > min) {
                    v = ((max - distSq) << 8) / diff;
                    v *= v;

                    ri = (int) (newR * v) >> 16;
                    gi = (int) (newG * v) >> 16;
                    bi = (int) (newB * v) >> 16;

                    newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
                    newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
                    newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
                }
                // ==========边缘黑暗end==============//

                dst[pos] = Color.rgb(newR, newG, newB);
            }
        }

        Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
        return acrossFlushBitmap;
    }

    /**
     * 旧时光特效
     *
     * @param bmp
     *            原图片
     * @return 旧时光特效图片
     */
    public static Bitmap oldTimeFilter(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR,
                        newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 暖意特效
     *
     * @param bmp
     *            原图片
     * @param centerX
     *            光源横坐标
     * @param centerY
     *            光源纵坐标
     * @return 暖意特效图片
     */
    public static Bitmap warmthFilter(Bitmap bmp, int centerX, int centerY) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);

        final float strength = 150F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(
                        centerX - k, 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance)
                            / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 根据饱和度、色相、亮度调整图片
     *
     * @param bm
     *            原图片
     * @param saturation
     *            饱和度
     * @param hue
     *            色相
     * @param lum
     *            亮度
     * @return 处理后的图片
     */
    public static Bitmap handleImage(Bitmap bm, int saturation, int hue, int lum) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        ColorMatrix mLightnessMatrix = new ColorMatrix();
        ColorMatrix mSaturationMatrix = new ColorMatrix();
        ColorMatrix mHueMatrix = new ColorMatrix();
        ColorMatrix mAllMatrix = new ColorMatrix();
        float mSaturationValue = saturation * 1.0F / 127;
        float mHueValue = hue * 1.0F / 127;
        float mLumValue = (lum - 127) * 1.0F / 127 * 180;
        mHueMatrix.reset();
        mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);

        mSaturationMatrix.reset();
        mSaturationMatrix.setSaturation(mSaturationValue);
        mLightnessMatrix.reset();

        mLightnessMatrix.setRotate(0, mLumValue);
        mLightnessMatrix.setRotate(1, mLumValue);
        mLightnessMatrix.setRotate(2, mLumValue);

        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix);
        mAllMatrix.postConcat(mLightnessMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    /**
     * 添加图片外边框
     *
     * @param context
     *            上下文
     * @param bm
     *            原图片
     * @param frameName
     *            边框名称
     * @return 带有边框的图片
     */
    public static Bitmap combinateFrame(Context context, Bitmap bm,
                                        String frameName) {
        // 原图片的宽高
        int imageWidth = bm.getWidth();
        int imageHeight = bm.getHeight();

        // 边框
        Bitmap leftUp = decodeBitmap(context, frameName, 0);
        Bitmap leftDown = decodeBitmap(context, frameName, 2);
        Bitmap rightDown = decodeBitmap(context, frameName, 4);
        Bitmap rightUp = decodeBitmap(context, frameName, 6);
        Bitmap top = decodeBitmap(context, frameName, 7);
        Bitmap down = decodeBitmap(context, frameName, 3);
        Bitmap left = decodeBitmap(context, frameName, 1);
        Bitmap right = decodeBitmap(context, frameName, 5);

        Bitmap newBitmap = null;
        Canvas canvas = null;

        // 判断大小图片的宽高
        int judgeWidth = 0;
        int judgeHeight = 0;
        if ("frame7".equals(frameName)) {
            judgeWidth = leftUp.getWidth() + rightUp.getWidth()
                    + top.getWidth() * 5;
            judgeHeight = leftUp.getHeight() + leftDown.getHeight()
                    + left.getHeight() * 5;
        } else if ("frame10".equals(frameName)) {
            judgeWidth = leftUp.getWidth() + rightUp.getWidth()
                    + top.getWidth() * 5;
            judgeHeight = leftUp.getHeight() + leftDown.getHeight()
                    + left.getHeight() * 10;
        } else {
            judgeWidth = leftUp.getWidth() + rightUp.getWidth()
                    + top.getWidth();
            judgeHeight = leftUp.getHeight() + leftDown.getHeight()
                    + left.getHeight();
        }
        // 内边框
        if (imageWidth > judgeWidth && imageHeight > judgeHeight) {
            // 重新定义一个bitmap
            newBitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                    Bitmap.Config.ARGB_8888);

            canvas = new Canvas(newBitmap);
            Paint paint = new Paint();
            // 画原图
            canvas.drawBitmap(bm, 0, 0, paint);
            // 上空余宽度
            int topWidth = imageWidth - leftUp.getWidth() - rightUp.getWidth();
            // 上空余填充个数
            int topCount = (int) Math.ceil(topWidth * 1.0f / top.getWidth());
            for (int i = 0; i < topCount; i++) {
                canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth() * i,
                        0, paint);
            }
            // 下空余宽度
            int downWidth = imageWidth - leftDown.getWidth()
                    - rightDown.getWidth();
            // 下空余填充个数
            int downCount = (int) Math.ceil(downWidth * 1.0f / down.getWidth());
            for (int i = 0; i < downCount; i++) {
                canvas.drawBitmap(down, leftDown.getWidth() + down.getWidth()
                        * i, imageHeight - down.getHeight(), paint);
            }
            // 左空余高度
            int leftHeight = imageHeight - leftUp.getHeight()
                    - leftDown.getHeight();
            // 左空余填充个数
            int leftCount = (int) Math.ceil(leftHeight * 1.0f
                    / left.getHeight());
            for (int i = 0; i < leftCount; i++) {
                canvas.drawBitmap(left, 0,
                        leftUp.getHeight() + left.getHeight() * i, paint);
            }
            // 右空余高度
            int rightHeight = imageHeight - rightUp.getHeight()
                    - rightDown.getHeight();
            // 右空余填充个数
            int rightCount = (int) Math.ceil(rightHeight * 1.0f
                    / right.getHeight());
            for (int i = 0; i < rightCount; i++) {
                canvas.drawBitmap(right, imageWidth - right.getWidth(),
                        rightUp.getHeight() + right.getHeight() * i, paint);
            }
            // 画左上角
            canvas.drawBitmap(leftUp, 0, 0, paint);
            // 画左下角
            canvas.drawBitmap(leftDown, 0, imageHeight - leftDown.getHeight(),
                    paint);
            // 画右下角
            canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
                    imageHeight - rightDown.getHeight(), paint);
            // 画右上角
            canvas.drawBitmap(rightUp, imageWidth - rightUp.getWidth(), 0,
                    paint);

        } else {
            if ("frame7".equals(frameName)) {
                imageWidth = leftUp.getWidth() + top.getWidth() * 5
                        + rightUp.getWidth();
                imageHeight = leftUp.getHeight() + left.getHeight() * 5
                        + leftDown.getHeight();
            } else if ("frame10".equals(frameName)) {
                imageWidth = leftUp.getWidth() + top.getWidth() * 5
                        + rightUp.getWidth();
                imageHeight = leftUp.getHeight() + left.getHeight() * 10
                        + leftDown.getHeight();
            } else {
                imageWidth = leftUp.getWidth() + top.getWidth()
                        + rightUp.getWidth();
                imageHeight = leftUp.getHeight() + left.getHeight()
                        + leftDown.getHeight();
            }
            newBitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(newBitmap);
            Paint paint = new Paint();
            int newImageWidth = imageWidth - left.getWidth() - right.getWidth()
                    + 5;
            int newImageHeight = imageHeight - top.getHeight()
                    - down.getHeight() + 5;
            bm = Bitmap.createScaledBitmap(bm, newImageWidth, newImageHeight,
                    true);
            canvas.drawBitmap(bm, left.getWidth(), top.getHeight(), paint);
            if ("frame7".equals(frameName)) {

                for (int i = 0; i < 5; i++) {
                    canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth()
                            * i, 0, paint);
                }

                for (int i = 0; i < 5; i++) {
                    canvas.drawBitmap(left, 0,
                            leftUp.getHeight() + left.getHeight() * i, paint);
                }

                for (int i = 0; i < 5; i++) {
                    canvas.drawBitmap(right, imageWidth - right.getWidth(),
                            rightUp.getHeight() + right.getHeight() * i, paint);
                }

                for (int i = 0; i < 5; i++) {
                    canvas.drawBitmap(down,
                            leftDown.getWidth() + down.getWidth() * i,
                            imageHeight - down.getHeight(), paint);
                }
                canvas.drawBitmap(leftUp, 0, 0, paint);
                canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth()
                        * 5, 0, paint);
                canvas.drawBitmap(leftDown, 0,
                        leftUp.getHeight() + left.getHeight() * 5, paint);
                canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
                        rightUp.getHeight() + right.getHeight() * 5, paint);

            } else if ("frame10".equals(frameName)) {
                for (int i = 0; i < 5; i++) {
                    canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth()
                            * i, 0, paint);
                }

                for (int i = 0; i < 10; i++) {
                    canvas.drawBitmap(left, 0,
                            leftUp.getHeight() + left.getHeight() * i, paint);
                }

                for (int i = 0; i < 10; i++) {
                    canvas.drawBitmap(right, imageWidth - right.getWidth(),
                            rightUp.getHeight() + right.getHeight() * i, paint);
                }

                for (int i = 0; i < 5; i++) {
                    canvas.drawBitmap(down,
                            leftDown.getWidth() + down.getWidth() * i,
                            imageHeight - down.getHeight(), paint);
                }
                canvas.drawBitmap(leftUp, 0, 0, paint);
                canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth()
                        * 5, 0, paint);
                canvas.drawBitmap(leftDown, 0,
                        leftUp.getHeight() + left.getHeight() * 10, paint);
                canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
                        rightUp.getHeight() + right.getHeight() * 10, paint);
            } else {
                canvas.drawBitmap(leftUp, 0, 0, paint);
                canvas.drawBitmap(top, leftUp.getWidth(), 0, paint);
                canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth(),
                        0, paint);
                canvas.drawBitmap(left, 0, leftUp.getHeight(), paint);
                canvas.drawBitmap(leftDown, 0,
                        leftUp.getHeight() + left.getHeight(), paint);
                canvas.drawBitmap(right, imageWidth - right.getWidth(),
                        rightUp.getHeight(), paint);
                canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
                        rightUp.getHeight() + right.getHeight(), paint);
                canvas.drawBitmap(down, leftDown.getWidth(),
                        imageHeight - down.getHeight(), paint);
            }
        }
        // 回收
        leftUp.recycle();
        leftUp = null;
        leftDown.recycle();
        leftDown = null;
        rightDown.recycle();
        rightDown = null;
        rightUp.recycle();
        rightUp = null;
        top.recycle();
        top = null;
        down.recycle();
        down = null;
        left.recycle();
        left = null;
        right.recycle();
        right = null;
        canvas.save();
        canvas.restore();
        return newBitmap;
    }

    /**
     * 获取边框图片
     *
     * @param context
     *            上下文
     * @param frameName
     *            边框名称
     * @param position
     *            边框的类型
     * @return 边框图片
     */
    private static Bitmap decodeBitmap(Context context, String frameName,
                                       int position) {
        try {
            switch (position) {
                case 0:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/leftup.png"));
                case 1:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/left.png"));
                case 2:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/leftdown.png"));
                case 3:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/down.png"));
                case 4:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/rightdown.png"));
                case 5:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/right.png"));
                case 6:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/rightup.png"));
                case 7:
                    return BitmapFactory.decodeStream(context.getAssets().open(
                            "frames/" + frameName + "/up.png"));
                default:
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加内边框
     *
     * @param bm
     *            原图片
     * @param frame
     *            内边框图片
     * @return 带有边框的图片
     */
    public static Bitmap addBigFrame(Bitmap bm, Bitmap frame) {
        Bitmap newBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        canvas.drawBitmap(bm, 0, 0, paint);
        frame = Bitmap.createScaledBitmap(frame, bm.getWidth(), bm.getHeight(),
                true);
        canvas.drawBitmap(frame, 0, 0, paint);
        canvas.save();
        canvas.restore();
        return newBitmap;

    }

    /**
     * 创建一个缩放的图片
     *
     * @param path
     *            图片地址
     * @param w
     *            图片宽度
     * @param h
     *            图片高度
     * @return 缩放后的图片
     */
    public static Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

}
