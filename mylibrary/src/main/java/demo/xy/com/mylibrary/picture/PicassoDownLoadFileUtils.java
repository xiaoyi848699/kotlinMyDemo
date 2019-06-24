package demo.xy.com.mylibrary.picture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.mylibrary.storage.StorageType;
import demo.xy.com.mylibrary.storage.StorageUtil;

/**
 * Created by xy on 2018/6/26.
 */
public class PicassoDownLoadFileUtils {
    public static File getDCIMFile(String imageName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 文件可用
            File dirs = new File(StorageUtil.getDirectoryByDirType(StorageType.TYPE_IMAGE),
                    "save");
            if (!dirs.exists())
                dirs.mkdirs();

            File file = new File(StorageUtil.getDirectoryByDirType(StorageType.TYPE_IMAGE),
                    "save/"+imageName);
            if (!file.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    file.createNewFile();
                } catch (Exception e) {
                }
            }
            return file;
        } else {
            return null;
        }

    }
    public static void download(final Activity activity, String url, final DownloadCallBack downloadCallBack) {
        //获得图片的地址
//        DialogUtils.show(activity,"图片保存中",false);
        //Target
        Target target = new Target(){
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String imageName = System.currentTimeMillis() + ".png";

                File dcimFile = getDCIMFile(imageName);

                FileOutputStream ostream = null;
                try {
                    ostream = new FileOutputStream(dcimFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    LogUtil.e("onBitmapLoaded Exception"+e.getMessage());
                    downloadCallBack.onDownloadFailed();
                }
                LogUtil.e("onBitmapLoaded "+dcimFile.getAbsolutePath());
                downloadCallBack.onDownloadSuccess(dcimFile.getAbsolutePath());
//                Toast.makeText(activity,"图片以保存至:"+dcimFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
//                Toast.makeText(activity,"保存失败", Toast.LENGTH_SHORT).show();
                downloadCallBack.onDownloadFailed();
//                DialogUtils.dismiss();
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                downloadCallBack.onPrepareLoad(placeHolderDrawable);
            }
        };

        //Picasso下载
        Picasso.with(activity).load(url).into(target);
    }
    interface DownloadCallBack{
        void onDownloadSuccess(String path);
        void onDownloadFailed();
        void onPrepareLoad(Drawable placeHolderDrawable);
    }
}
