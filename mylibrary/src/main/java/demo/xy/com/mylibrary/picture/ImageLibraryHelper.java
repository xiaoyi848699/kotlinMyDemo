package demo.xy.com.mylibrary.picture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.DimenRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import demo.xy.com.mylibrary.BuildConfig;
import demo.xy.com.mylibrary.DateUtils;
import demo.xy.com.mylibrary.PermissionUtils;
import demo.xy.com.mylibrary.R;
import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.mylibrary.log.Write;
import demo.xy.com.mylibrary.storage.StorageType;
import demo.xy.com.mylibrary.storage.StorageUtil;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;


/**
 * @Description: 所有图片上传时，图片名称请遵循以下格式上传；
 * 终端名_用户id_年月日时分秒.jpg
 * 例子：
 * iOS_402882e75caa9675015caa96e31d0000_170925151804.jpg
 * Android_402882e75caa9675015caa96e31d0000_170925151804.jpg
 * web_402882e75caa9675015caa96e31d0000_170925151804.jpg
 * 如同时上传多张图，后接数字
 */

public class ImageLibraryHelper {

    public static final int IMAGE_TYPE = 1;//图片类型
    public static final int HEAD_IMAGE_TYPE = 2;//头像类型
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;//拍照模式
    public static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 200;//系统相册模式
    public static final int CUSTOM_IMAGE_SELECTOR_REQUEST_CODE = 300;//自定义相册模式
    public static final ImageView.ScaleType VIDEO_PREVIEW_SCALETYPE = ImageView.ScaleType.CENTER_INSIDE;//视频播放预览图片显示模式
    public static Uri fileUri;
    private static final String emptyUrl = "http://site.com";
    /**
     * 拍照
     *
     * @param activity
     */
    public static void takePicFromCamera(Activity activity) {
        fileUri = getOutputMediaFileUri(activity);
        if (fileUri != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            activity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
    public static void takePicFromCamera(Activity activity, int cameraRequestCode) {
        fileUri = getOutputMediaFileUri(activity);
        if (fileUri != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            activity.startActivityForResult(intent, cameraRequestCode);
        }
    }

    public static int getImgDefault() {
        return R.drawable.img_default;
    }

    public static int getHeadImgDefault() {
        return R.drawable.default_user_head;
    }

    /**
     * 系统自带图库
     *
     * @param activity
     */
    public static void takePicFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    public static void takePicFromGallery(Activity activity, int picRequestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, picRequestCode);
    }

    /**
     * 创建一个用于保存图片或视频的Uri
     */
    private static Uri getOutputMediaFileUri(Context context) {
        if (isExternalStorageWritable()) {
            Uri uri = Uri.fromFile(getOutputMediaFile(context));
            return uri;
        } else {
            Toast.makeText(context, context.getString(R.string.sdk_not_available), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 创建一个用于保存图片的File
     */
    public static File getOutputMediaFile(Context context) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),
                context.getString(context.getApplicationInfo().labelRes));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "failed to create directory");
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + getUploadFileName() + ".jpg");
    }

    /**
     * 检查外部存储是否可用
     */
    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    //获取上传时使用的文件名
    public static String getUploadFileName() {
        String timeStamp = DateUtils.formatData(new Date().getTime(), DateUtils.PIC_FORMAT, TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT));
//        String timeStamp = DateUtils.formatData(new Date().getTime(),  DateUtils.PIC_FORMAT);
        return "Android_"  + timeStamp;
    }



    public static String getFileSuffix(String filePath){
        return filePath.substring(filePath.lastIndexOf("."));//原后缀
    }


    /**
     * 上传图片统一命名
     *
     * @param oldFile 原文件路径
     * @param newName 新的文件名不带有后缀、后缀从原文件获取
     * @return
     */
    public static File fileRenameToUpLoad(File oldFile, String newName) {
        if (!oldFile.exists()) return null;
        String oldFileName = oldFile.getName();
        String prefix = oldFileName.substring(oldFileName.lastIndexOf("."));//原后缀
        File newFile = new File(oldFile.getParent(), newName + prefix);
        boolean state = oldFile.renameTo(newFile);
        if (state) {
            return newFile;
        }
        return oldFile;
    }
    public static File fileRenameToUpLoad2(File oldFile, String newName) {
        if (!oldFile.exists()) return null;
        String oldFileName = oldFile.getName();
        String prefix = oldFileName.substring(oldFileName.lastIndexOf("."));//原后缀
        String newFilePath = newName + prefix;
        boolean state = CopySdcardFile(oldFile.getAbsolutePath(),newFilePath);
        if (state) {
            return new File(newFilePath);
        }
        return oldFile;
    }
    public static boolean CopySdcardFile(String fromFile, String toFile)
    {

        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;

        } catch (Exception ex)
        {
            Write.debug("CopySdcardFile Exception"+ex.getMessage());
            return false;
        }
    }

    /**
     * 自定义比例裁剪图片
     *
     * @param imageUri
     * @param activity
     * @param rationX
     * @param rationY
     */
    public static void cropImageRatio(Uri imageUri, Activity activity, int rationX, int rationY, int requestCode) {
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_IMAGE);
        File tempFile = new File(path + getUploadFileName() + ".jpg");
        Uri uri = Uri.fromFile(tempFile);
        Uri mUri = Uri.parse(getPath(activity,imageUri));
//        options.
        CropImage.activity(mUri)
                .setOutputUri(uri)//指定裁剪后输出图片uri
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("裁剪图片")
                .setAspectRatio(rationX, rationY)
                .setFixAspectRatio(true)
                .setAutoZoomEnabled(true)
                .start(activity);

//        File srcFile = BitmapUtil.getFileFromMediaUri(activity, imageUri);
//        if(srcFile.getName().length()>3) {
//            String suffix = srcFile.getName().substring(srcFile.getName().lastIndexOf("."));//原后缀
//            UCrop.Options options = new UCrop.Options();
//            if (".jpg".equals(suffix)) {
//                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//            } else if (".png".equals(suffix)) {
//                options.setCompressionFormat(Bitmap.CompressFormat.PNG);
//            } else {
//                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//            }
//            File destFile = new File(Const.PATH_CACHE_NET, ImageLibraryHelper.getUploadFileName() + suffix);
//
//            UCrop uCrop = UCrop.of(imageUri, Uri.fromFile(destFile));
//
//            // Aspect ratio options
////            options.setToolbarColor(ContextCompat.getColor(context!!,R.color.rts_text_orange))
//            options.setToolbarTitle(activity.getString(R.string.kjnova_clipper));
//            //一共三个参数，分别对应裁剪功能页面的“缩放”，“旋转”，“裁剪”界面，对应的传入NONE，就表示关闭了其手势操作，比如这里我关闭了缩放和旋转界面的手势，只留了裁剪页面的手势操作
//            options.setHideBottomControls(true);
//
//            options.setShowCropGrid(false);
//            options.setToolbarColor(ContextCompat.getColor(activity, R.color.black));
//            options.setStatusBarColor(ContextCompat.getColor(activity, R.color.black));
//            options.setAspectRatioOptions(0, new AspectRatio("RATIO", rationX, rationY));
//            uCrop.withOptions(options);
//            uCrop.start(activity,requestCode);
//        }

    }


    //先进行权限查询
    public static void showTakePicDialog(final String title, final Activity activity) {
        String[] peemissions = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if(PermissionUtils.checkPermissionAllGranted(activity,peemissions)){
            takePic(title, activity);
        }else{
            PermissionUtils.requestPermissions(activity,"",peemissions,101);
        }

    }
    //自定义RequestCode
    public static void showTakePicDialog(final String title, final Activity activity, final int cameraRequestCode, final int picRequestCode) {
        String[] peemissions = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if(PermissionUtils.checkPermissionAllGranted(activity,peemissions)){
            takePic(title, activity,cameraRequestCode,picRequestCode);
        }else{
            PermissionUtils.requestPermissions(activity,"",peemissions,picRequestCode);
        }

    }

    private static void takePic(String title, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setItems(R.array.select_pic, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 拍照
                                takePicFromCamera(activity);
                                break;
                            case 1: // 图库
                                takePicFromGallery(activity);
                                break;
                        }
                    }
                }).create().show();
    }
    private static void takePic(String title, final Activity activity, final int cameraRequestCode, final int picRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setItems(R.array.select_pic, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 拍照
                                takePicFromCamera(activity,cameraRequestCode);
                                break;
                            case 1: // 图库
                                takePicFromGallery(activity,picRequestCode);
//                                Intent intent = new Intent();
//                                intent.putExtra(ImageSelectorActivity.EXTRA_MAX_SELECT_NUM,
//                                        1);
//                                ImageSelectorActivity.startInstance(activity,
//                                        picRequestCode, intent);
                                break;
                        }
                    }
                }).create().show();
    }



//    public static void showTakePicDialogByCustom(final String title, final Activity activity, final Intent extras) {
//        RxPermissions rxPermissions = new RxPermissions(activity);
//        rxPermissions.request(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA)
//                .subscribe(aBoolean -> {
//                    if (aBoolean) {
//                        takePicByCustom(title, activity, extras);
//                    } else {
//                        DialogUtils.showPermissionDialog(activity, activity.getResources().getString(R.string.permissions_photo));
//                    }
//                });
//
//    }
    //自定义RequestCode
//    public static void showTakePicDialogByCustom(final String title, final Activity activity, final Intent extras, final int cameraRequestCode, final int picRequestCode) {
//        RxPermissions rxPermissions = new RxPermissions(activity);
//        rxPermissions.request(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA)
//                .subscribe(aBoolean -> {
//                    if (aBoolean) {
//                        takePicByCustom(title, activity, extras,cameraRequestCode,picRequestCode);
//                    } else {
//                        DialogUtils.showPermissionDialog(activity, activity.getResources().getString(R.string.permissions_photo));
//                    }
//                });
//    }
//    private static void takePicByCustom(String title, final Activity activity, final Intent extras) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle(title)
//                .setItems(R.array.select_pic, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0: // 拍照
//                                takePicFromCamera(activity);
//                                break;
//                            case 1: // 自定义图库
//                                ImageSelectorActivity.startInstance(activity,
//                                        CUSTOM_IMAGE_SELECTOR_REQUEST_CODE, extras);
//                                break;
//                        }
//                    }
//                }).create().show();
//    }
//    private static void takePicByCustom(String title, final Activity activity, final Intent extras, final int cameraRequestCode, final int picRequestCode) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle(title)
//                .setItems(R.array.select_pic, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0: // 拍照
//                                takePicFromCamera(activity,cameraRequestCode);
//                                break;
//                            case 1: // 自定义图库
//                                ImageSelectorActivity.startInstance(activity,
//                                        picRequestCode, extras);
//                                break;
//                        }
//                    }
//                }).create().show();
//    }





    //加载指定大小图片--file
    public static void loadFileBySizeToImageView(Context context, ImageView view,
                                                 File url/*, Callback callback*/) {
        Picasso.with(context)
                .load(url)
//                .resize(width, height)
//                .centerCrop()
                .placeholder(getDefaultImage(IMAGE_TYPE))
                .transform(new CropSquareTransformation())
                .noFade()
                .error(getDefaultImage(IMAGE_TYPE))
                .into(view);
//                .into(view,callback);
    }

    //带tag标志
    public static void loadUrlToImageViewByTag(Context context, ImageView view, String url, String tag) {
        if (!isImageUriValid(url)) url = emptyUrl;
        Picasso.with(context)
                .load(url)
                .tag(tag)
                .placeholder(getDefaultImage(IMAGE_TYPE))
                .error(getDefaultImage(IMAGE_TYPE))
                .into(view);
    }

    //普通加载图片--url---固定大小
    public static void loadUrlAsResizeToImageViewByTag(Context context, int width, int height,
                                                       ImageView view, String url, String tag) {
        //http://192.168.2.120:8888/aaa_180x30.jpg--注意是在后缀名前面。乘号是小写x
        String smallUrl = generateSmallPicUrl(url, width, height);
        if (!isImageUriValid(smallUrl)) smallUrl = emptyUrl;
        Picasso.with(context)
                .load(smallUrl)
                .resize(width, height)
                .centerCrop()
                .tag(tag)
                .placeholder(getDefaultImage(IMAGE_TYPE))
                .error(getDefaultImage(IMAGE_TYPE))
                .into(view);
    }


    //普通加载图片--file
    public static void loadFileToImageView(Context context, ImageView view, File file, @DimenRes int width,
                                           @DimenRes int height, float radius, int imageType) {
        int defaultImage = getDefaultImage(imageType);
//        GlideApp.with(context).load(file).placeholder(defaultImage).skipMemoryCache(true).into(view);
//        Picasso.with(context)
//                .load(file)
////                .resizeDimen(width, height)
//                .transform(new RoundTransform(radius))
//                .placeholder(defaultImage)
//                .error(defaultImage)
//                .into(view);
    }

    //==========================普通图片加载库==========================
    //==========================圆角加载库==========================
    //圆角图片--url
    public static void loadUrlImageRoundCorner(Context context, String url, @DimenRes int width,
                                               @DimenRes int height, float radius, ImageView imageView, int imageType) {
        if (!isImageUriValid(url)) url = emptyUrl;
        int defaultImage = getDefaultImage(imageType);
        Picasso.with(context).load(url+"_"+width+"x"+height)
//                .resizeDimen(width, height)
                .transform(new RoundTransform(radius))
//                .centerCrop()
                .placeholder(defaultImage)
                .error(defaultImage)
                .into(imageView);
//        LogUtil.e("loadUrlImageRoundCorner---->"+url);
//        GlideApp.with(context).load(url+"_"+width+"x"+height).placeholder(defaultImage).into(imageView);
    }

    public static void loadUrlImageRoundCornerByTag(Context context, String url, @DimenRes int width, @DimenRes int height,
                                                    float radius, ImageView imageView, String tag, int imageType) {
        if (!isImageUriValid(url)) url = emptyUrl;
        int defaultImage = getDefaultImage(imageType);
        Picasso.with(context).load(url)
                .resizeDimen(width, height)
                .transform(new RoundTransform(radius))
                .centerCrop()
                .placeholder(defaultImage)
                .error(defaultImage)
                .tag(tag)
                .into(imageView);
    }

    //圆角图片--file
    public static void loadRSRoundCorner(Context context, File file, @DimenRes int width,
                                         @DimenRes int height, float radius, ImageView imageView, int imageType) {
        int defaultImage = getDefaultImage(imageType);
        Picasso.with(context).load(file)
                .resizeDimen(width, height)
                .transform(new RoundTransform(radius))
                .centerCrop()
                .placeholder(defaultImage)
                .error(defaultImage)
                .into(imageView);
    }

    //圆角图片--RS资源
    public static void loadRSRoundCorner(Context context, int rs, @DimenRes int width,
                                         @DimenRes int height, float radius, int error_ic, ImageView imageView) {
        Picasso.with(context).load(rs)
                .resizeDimen(width, height)
                .transform(new RoundTransform(radius))
                .centerCrop()
                .placeholder(error_ic)
                .error(error_ic)
                .into(imageView);
    }

    /**
     * 获取默认图片和加载错误图片
     *
     * @param imageType
     * @return
     */
    private static int getDefaultImage(int imageType) {
        int defaultImage = getImgDefault();
        if (imageType == IMAGE_TYPE) {
            defaultImage = getImgDefault();
        } else if (imageType == HEAD_IMAGE_TYPE) {
            defaultImage = getHeadImgDefault();
        }
        return defaultImage;
    }

    //生成加载小图片的url
    public static String generateSmallPicUrl(String url, int width, int height) {
        if (isImageUriValid(url)) {
            return url + "_" + width + "x" + height;
        }
        return url;
    }


    //显示图片
    public static void loadUrlToImage(Context context, String url, @DimenRes int width,
                                      @DimenRes int height, ImageView imageView) {
        if (!isImageUriValid(url)) url = emptyUrl;
        if (imageView.getVisibility() != View.VISIBLE) {
            imageView.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(url)
                .resizeDimen(width, height)
                .transform(new RoundTransform(1))
                .centerCrop()
                .into(imageView);
    }

    public static void loadUrlToImage(Context context, String url, ImageView imageView) {
        if (!isImageUriValid(url)) url = emptyUrl;
        if (imageView.getVisibility() != View.VISIBLE) {
            imageView.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(url).into(imageView);
    }



    public static void initPicasso(Context context,String cachePath) {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] x509Certificates,
                        String s) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] x509Certificates,
                        String s) throws java.security.cert.CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            builder.sslSocketFactory(sslContext.getSocketFactory());

            builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));

            File cachePathFile = new File(cachePath);
            if (!cachePathFile.exists()) {
                cachePathFile.mkdirs();
            }
            long maxSize = Runtime.getRuntime().maxMemory() / 8;//设置图片缓存大小为运行时缓存的八分之一
            builder.cache(new Cache(cachePathFile, maxSize));

            final Picasso picasso = new Picasso.Builder(context)
                    .downloader(new OkHttp3Downloader(builder.build()))
                    .build();
            Picasso.setSingletonInstance(picasso);
            if (BuildConfig.DEBUG) {
                //开启缓存调试模式
                Picasso.with(context).setIndicatorsEnabled(false);
                Picasso.with(context).setLoggingEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("picasso", e.toString());
        }
    }


    /**
     * 判断图片地址是否合法，合法地址如下：
     * String uri = "http://site.com/image.png"; // from Web
     * String uri = "file:///mnt/sdcard/image.png"; // from SD card
     * String uri = "content://media/external/audio/albumart/13"; // from content provider
     * String uri = "assets://image.png"; // from assets
     * String uri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     */
    public static boolean isImageUriValid(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return false;
        }
        if ("blank".equals(uri)) {
            return false;
        }
        List<String> uriSchemes = new ArrayList<>();
        for (ImageDownloader.Scheme scheme : ImageDownloader.Scheme.values()) {
            uriSchemes.add(scheme.name().toLowerCase());
        }

        for (String scheme : uriSchemes) {
            if (uri.toLowerCase().startsWith(scheme)) {
                return true;
            }
        }

        return false;
    }
    /**
     * @param context 上下文对象
     * @param uri     当前相册照片的Uri
     * @return 解析后的Uri对应的String
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String pathHead = "file:///";
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return pathHead + getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + uri.getPath();
        }else{
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            cursor.moveToFirst();
            return cursor.getString(1); // 图片文件路径
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
