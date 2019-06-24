package demo.xy.com.mylibrary.log;

import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Write the Log to the file
 */
public class WriteFile {

    private static ExecutorService executor = null;

    protected static void setExecutor(ExecutorService executor) {
        WriteFile.executor = executor;
    }

    /**
     *
     * @param path 文件路径或者文件名称
     * @param str 文件内容
     * @param savePath  保存位置  true 保存在SDcard   false 保存在内存里面
     */
    protected static void log2file(final String path, final String str, final boolean savePath) {
        if (TextUtils.isEmpty(path)) {
            LogUtil.e("save path is empty");
            return ;
        }
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }

        if (executor != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if(savePath){
                        PrintWriter out = null;
                        File file = Write.getFileFromPath(path);
                        if(file!=null){
                            try {
//                                 out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                                out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
                                out.println(str);
                                out.flush();
                            } catch (IOException e) {
                                Write.debug("2 log2file error:"+e.getMessage()+str);
                            } finally {
                                try{
                                    if(out != null)out.close();
                                } catch(Exception e){
                                    Write.debug("2 log2file outputstream close error:"+e.getMessage());
                                }
                            }
                        }
                    }else {
                        saveDataFile( path,  str);
                    }
                }
            });
        }
    }
    protected static void saveDataFile(String path, String str) {
        PrintWriter out = null;
        File file = new File(path);
        if(file!=null ){
            try {
                out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
                out.println(str);
                out.flush();
            } catch (FileNotFoundException e) {
                Write.debug("saveDataFile error:"+e.getMessage());
            } catch (IOException e) {
                Write.debug("saveDataFile error:"+e.getMessage());
            } finally {
                try{
                    if(null != out){
                        out.close();
                        out=null;
                    }
                } catch(Exception e){
                    Write.debug("saveDataFile outputstream close error:"+e.getMessage());
                }
                Write.scanFile(file.getPath());
            }
        }

    }

}
