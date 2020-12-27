//package demo.xy.com.xytdcq.uitls;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import demo.xy.com.xytdcq.R;
//
//demo.xy.com.xytdcq.uitls.ImageLibraryHelper
//public class ImageLibraryHelper {
//
//    public static String[] permissions =  new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    public static final int IMAGE_TYPE = 1;//图片类型
//    public static final int HEAD_IMAGE_TYPE = 2;//头像类型
//    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;//拍照模式
//    public static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 200;//系统相册模式
//    public static final int CUSTOM_IMAGE_SELECTOR_REQUEST_CODE = 300;//自定义相册模式
//    public static final ImageView.ScaleType VIDEO_PREVIEW_SCALETYPE = ImageView.ScaleType.CENTER_INSIDE;//视频播放预览图片显示模式
//    public static Uri fileUri;
//    private static final String  emptyUrl = "http://site.com";
//    /**
//     * 拍照
//     *
//     * @param activity
//     */
//    public static void takePicFromCamera(Activity activity) {
//        boolean isAllGranted = PermissionUtils.checkPermissionAllGranted(activity,permissions);
//        if(!isAllGranted){
//            ActivityCompat.requestPermissions(activity,permissions,100);
//            return;
//        }
//        fileUri = getOutputMediaFileUri(activity);
//        if (fileUri != null) {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            activity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }
//    }
//    public static void takePicFromCamera(Activity activity,int cameraRequestCode) {
//        fileUri = getOutputMediaFileUri(activity);
//        if (fileUri != null) {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            activity.startActivityForResult(intent, cameraRequestCode);
//        }
//    }
//
//    public static int getImgDefault() {
//        return R.drawable.img_default;
//    }
//
//    public static int getHeadImgDefault() {
//        return R.drawable.default_user_head;
//    }
//
//    /**
//     * 系统自带图库
//     *
//     * @param activity
//     */
//    public static void takePicFromGallery(Activity activity) {
//        boolean isAllGranted = PermissionUtils.checkPermissionAllGranted(activity,permissions);
//        if(!isAllGranted){
//            ActivityCompat.requestPermissions(activity,permissions,100);
//            return;
//        }
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        activity.startActivityForResult(intent, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
//    }
//    public static void takePicFromGallery(Activity activity,int picRequestCode) {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        activity.startActivityForResult(intent, picRequestCode);
//    }
//
//    /**
//     * 创建一个用于保存图片或视频的Uri
//     */
//    private static Uri getOutputMediaFileUri(Context context) {
//        if (isExternalStorageWritable()) {
//            Uri uri = Uri.fromFile(getOutputMediaFile(context));
//            return uri;
//        } else {
//            Toast.makeText(context, "SD卡不可用, 请插入SD卡重试!", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
//
//    /**
//     * 创建一个用于保存图片的File
//     */
//    public static File getOutputMediaFile(Context context) {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES),
//                context.getString(context.getApplicationInfo().labelRes));
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d("TAG", "failed to create directory");
//                return null;
//            }
//        }
//        return new File(mediaStorageDir.getPath() + File.separator + getUploadFileName() + ".jpg");
//    }
//
//    /**
//     * 检查外部存储是否可用
//     */
//    private static boolean isExternalStorageWritable() {
//        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
//    }
//
//    //获取上传时使用的文件名
//    public static String getUploadFileName() {
//        return "Android_xy";
//    }
//
//    public static String getUploadChatImgName() {
//        return "Android_xy_" + new Date().getTime();
//    }
//
//    public static String getFileSuffix(String filePath){
//        return filePath.substring(filePath.lastIndexOf("."));//原后缀
//    }
//
//
//    /**
//     * 上传图片统一命名
//     *
//     * @param oldFile 原文件路径
//     * @param newName 新的文件名不带有后缀、后缀从原文件获取
//     * @return
//     */
//    public static File fileRenameToUpLoad(File oldFile, String newName) {
//        if (!oldFile.exists()) return null;
//        String oldFileName = oldFile.getName();
//        String prefix = oldFileName.substring(oldFileName.lastIndexOf("."));//原后缀
//        File newFile = new File(oldFile.getParent(), newName + prefix);
//        boolean state = oldFile.renameTo(newFile);
//        if (state) {
//            return newFile;
//        }
//        return oldFile;
//    }
//    public static File fileRenameToUpLoad2(File oldFile, String newName) {
//        if (!oldFile.exists()) return null;
//        String oldFileName = oldFile.getName();
//        String prefix = oldFileName.substring(oldFileName.lastIndexOf("."));//原后缀
//        String newFilePath = newName + prefix;
//        boolean state = CopySdcardFile(oldFile.getAbsolutePath(),newFilePath);
//        if (state) {
//            return new File(newFilePath);
//        }
//        return oldFile;
//    }
//    public static boolean CopySdcardFile(String fromFile, String toFile)
//    {
//
//        try
//        {
//            InputStream fosfrom = new FileInputStream(fromFile);
//            OutputStream fosto = new FileOutputStream(toFile);
//            byte bt[] = new byte[1024];
//            int c;
//            while ((c = fosfrom.read(bt)) > 0)
//            {
//                fosto.write(bt, 0, c);
//            }
//            fosfrom.close();
//            fosto.close();
//            return true;
//
//        } catch (Exception ex)
//        {
//            LogUtil.e("tiantian","exception==>"+ ex.getMessage());
//            return false;
//        }
//    }
//
//
//    /**
//     * 获取默认图片和加载错误图片
//     *
//     * @param imageType
//     * @return
//     */
//    private static int getDefaultImage(int imageType) {
//        int defaultImage = getImgDefault();
//        if (imageType == IMAGE_TYPE) {
//            defaultImage = getImgDefault();
//        } else if (imageType == HEAD_IMAGE_TYPE) {
//            defaultImage = getHeadImgDefault();
//        }
//        return defaultImage;
//    }
//
//
//    //==========================圆角加载库==========================
//    //==========================圆形加载库==========================
//    //圆形图片--url
////    public static void loadUrlToCircleImage(Context context, String url, @DimenRes int width,
////                                            @DimenRes int height, ImageView imageView) {
////        if (!isImageUriValid(url)) return;
////        Picasso.with(context).load(url)
////                .resizeDimen(width, height)
////                .transform(new CircleTransform())
////                .centerCrop()
////                .placeholder(R.drawable.ic_logo)
////                .error(R.drawable.ic_logo)
////                .into(imageView);
////    }
//
//
//    //圆形图片--file
////    public static void loadFileToCircleImage(Context context, File file, @DimenRes int width,
////                                             @DimenRes int height, ImageView imageView) {
////        Picasso.with(context).load(file)
////                .resizeDimen(width, height)
////                .transform(new CircleTransform())
////                .centerCrop()
////                .placeholder(R.drawable.img_default)
////                .error(R.drawable.img_default)
////                .into(imageView);
////    }
//
//    //==========================圆形加载库==========================
//    //不缓存加载模式
////    public static void loadImageRoundCornerNoCache(Context context, ImageView view, @DimenRes int width,
////                                                   @DimenRes int height, String url, int radius) {
////        if (!isImageUriValid(url)) return;
////        Picasso.with(context)
////                .load(url)
////                .resizeDimen(width, height)
////                .transform(new RoundTransform(radius))
////                .centerCrop()
////                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
////                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
////                .error(R.drawable.ic_logo)
////                .into(view);
////    }
//    //-----------------图片加载库-----------------------
//
//
//
//    /**
//     * 判断图片地址是否合法，合法地址如下：
//     * String uri = "http://site.com/image.png"; // from Web
//     * String uri = "file:///mnt/sdcard/image.png"; // from SD card
//     * String uri = "content://media/external/audio/albumart/13"; // from content provider
//     * String uri = "assets://image.png"; // from assets
//     * String uri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
//     */
//    public static boolean isImageUriValid(String uri) {
//        if (TextUtils.isEmpty(uri)) {
//            return false;
//        }
//        if ("blank".equals(uri)) {
//            return false;
//        }
//        List<String> uriSchemes = new ArrayList<>();
//        for (ImageDownloader.Scheme scheme : ImageDownloader.Scheme.values()) {
//            uriSchemes.add(scheme.name().toLowerCase());
//        }
//
//        for (String scheme : uriSchemes) {
//            if (uri.toLowerCase().startsWith(scheme)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//    /**
//     * @param context 上下文对象
//     * @param uri     当前相册照片的Uri
//     * @return 解析后的Uri对应的String
//     */
//    @SuppressLint("NewApi")
//    public static String getPath(final Context context, final Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//        String pathHead = "file:///";
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//                if ("primary".equalsIgnoreCase(type)) {
//                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//
//                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return pathHead + getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{split[1]};
//
//                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return pathHead + getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return pathHead + uri.getPath();
//        }else{
//            ContentResolver cr = context.getContentResolver();
//            Cursor cursor = cr.query(uri, null, null, null, null);
//            cursor.moveToFirst();
//            return cursor.getString(1); // 图片文件路径
//        }
//        return null;
//    }
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context       The context.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {column};
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    private static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    private static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    private static boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }
//}
