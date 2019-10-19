package demo.xy.com.mylibrary.log;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志文件写入SK卡
 */
public class Write
{
    /**
     * 日志文件保存路径
     */
    public static  String MYLOG_PATH_SDCARD_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/writeLog/";
    public static Context mContext;
    public static void init(Context context, String savePath) {
        MYLOG_PATH_SDCARD_DIR = savePath;
        mContext = context;
    }


    /**
     * 本类输出的日志文件名称
     */
    private static String MYLOGFILEName = "_log.txt";


    /**
     * 日志名前缀
     */
    private static String prefix = "";

    /**
     * 1M
     */
    private static int MAXLENGTH = 1 * 1024 * 1024;


    /**
     * 日志文件名后缀
     */
    private static int after = 1;

    /**
     * 路径
     */
    private static String path = "";

    /**
     * 路径
     */
    private static String par = "";




    public static void debug(String msg)
    {
        writeLogtoFile("debug", msg);
    }
    public static void info(String msg)
    {
        writeLogtoFile("info", msg);
    }
    public static void error(String msg)
    {
        writeLogtoFile("error", msg);
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     * **/
    private static void writeLogtoFile(String tag, String text){
        if(null == mContext){
            LogUtil.e("please call init method");
            return;
        }
        // 新建或打开日志文件
        Date nowTime = new Date();
        SimpleDateFormat logFile = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());// 日志文件格式
        String needWriteFiel = logFile.format(nowTime);
        SimpleDateFormat myLogSdf = new SimpleDateFormat(
                "MM-dd HH:mm:ss:SSS", Locale.getDefault());// 日志的输出格式
        String needWriteMessage = myLogSdf.format(nowTime)  + " [" + tag + "]"+ " [" + text + "]";
        par = MYLOG_PATH_SDCARD_DIR + needWriteFiel + "/";
        File parEnt = new File(par);
        try
        {
            if (!parEnt.exists())
            {
                boolean tempFlag = parEnt.mkdirs();
                if(!tempFlag){
                    Write.debug("make dirs fail!"+text);
                }
            }
            File[] f = parEnt.listFiles();

            int len = 0;
            if (null != f)
            {
                len = f.length;
            }
            if (len == 0)
            {
                after = 1;
                prefix = needWriteFiel + "-" + after + MYLOGFILEName;
            }
            else
            {
                prefix = needWriteFiel + "-" + after + MYLOGFILEName;
                File file = new File(path);
                long fileLen = file.length();
                if (fileLen >= MAXLENGTH)
                {
                    after = after + 1;
                    prefix = needWriteFiel + "-" + after + MYLOGFILEName;
                }
            }

            path = par + prefix;
            WriteToFile.log2file(path, needWriteMessage, true);

        }
        catch (Exception e)
        {
            Write.debug("" + e.getMessage());
        }

    }

    /**
     * 创建文件 并写入内容
     * @param path
     * @return
     */
    public static File getFileFromPath(String path) {
        boolean isExist;
        File file = new File(path);

        isExist = file.exists();

        if (!isExist) {
            try {
                file.createNewFile();
                WriteToFile.saveDataFile(path, CrashSnapshot.getDeviceInfo(mContext));
            } catch (IOException e) {
                Write.debug("getFileFromPath error:"+e.getMessage());
            }
        }

        return file;
    }
    public static void scanFile(String dirPath)
    {
        if(null == mContext){
            LogUtil.e("please call init method");
            return;
        }
        if (hasKitkat())
        {
            MediaScannerConnection.scanFile(mContext, new String[]
                    { dirPath }, new String[]
                    { "*/*" }, new MediaScannerConnection.OnScanCompletedListener()
            {
                public void onScanCompleted(String path, Uri uri)
                {
                    mContext
                            .getApplicationContext()
                            .sendBroadcast(
                                    new Intent(
                                            android.hardware.Camera.ACTION_NEW_PICTURE,
                                            uri));
                    mContext
                            .getApplicationContext()
                            .sendBroadcast(
                                    new Intent(
                                            "com.android.camera.NEW_PICTURE",
                                            uri));
                }
            });
            scanPhotos(dirPath, mContext
                    .getApplicationContext()); // 实际起作用的方法
            MediaScannerConnection.scanFile(mContext
                    .getApplicationContext(), new String[]
                    { dirPath }, null, null);
        }
        else
        {
            MediaScannerConnection.scanFile(mContext
                    .getApplicationContext(), new String[]
                    { dirPath }, null, null);
            mContext
                    .getApplicationContext()
                    .sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                                    .parse("file://" + dirPath)));
        }

    }

    private static void scanPhotos(String filePath, Context context)
    {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    private static boolean hasKitkat()
    {
        return Build.VERSION.SDK_INT >= 19;
    }



}
