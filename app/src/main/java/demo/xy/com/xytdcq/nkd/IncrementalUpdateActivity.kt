package demo.xy.com.xytdcq.nkd

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.text.TextUtils
import demo.xy.com.mylibrary.log.LogUtil
import demo.xy.com.xytdcq.BaseAtivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.base.App
import demo.xy.com.xytdcq.nkd.interfaceI.NDKInterface
import demo.xy.com.xytdcq.uitls.ToastUtil
import java.io.File

/**
 * 1.服务器差分  关键代码行在assets中
旧版本apk、新版本apk

BsDiff开源项目（依赖于Bzip2）

1.根据下载的bsdiff4.3-win32-src代码，生成dll动态库，用于得到差分包
1)用了不安全的函数 C++命令行添加-D _CRT_SECURE_NO_WARNINGS
2)用了过时的函数   C++命令行添加-D _CRT_NONSTDC_NO_DEPRECATE
3)C++常规关闭SDL检查

2.仔细阅读源代码，修改bsdiff.cpp原文件
3.根据C/C++代码，编写java层代码，然后生成头文件
4.编写JNI函数，供Java层调用（注意统一编码）
生产dll动态库，放入java工程中差分apk包，生成apk.patch包供android端下载合并

2.android端合并
 */
class IncrementalUpdateActivity : BaseAtivity() {


    init {
        System.loadLibrary("apk-bspatch")
    }


    override fun getLayout(): Int {
        return R.layout.activity_incremental_update
    }

    override fun setDataAndEvent() {
        ApkUpdateTask().execute()
    }
    var newFile = ""
    internal inner class ApkUpdateTask : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            try {
                //1.下载差分包
                LogUtil.e("oldfile: 开始下载")
                //本地处理（正常流程是服务器上下载）
                val opatchFile = App.getInstance().savePath+ File.separator+"apk.patch"

                //获取当前应用的apk文件/data/app/app
                // （注意：已经安装的这个包一定要用分包时候的那个旧包  不该工程代码运行上去的都不行，不然就算大小一致会解析出问题）
                val oldFile = getSourceApkPath(this@IncrementalUpdateActivity, packageName)
                //2.合并得到最新版本的APK文件
                newFile = App.getInstance().savePath+ File.separator+"apk_new.apk"

                LogUtil.e("oldfile:$oldFile")
                LogUtil.e("newfile:$newFile")
                LogUtil.e("patchfile:$opatchFile")
                var f= File(opatchFile)
                var fold= File(oldFile)

                LogUtil.e("patch exists:"+f.exists())
                LogUtil.e("fold exists:"+fold.exists())
                if (oldFile != null && f.exists()) {
                    ToastUtil.showToast(this@IncrementalUpdateActivity, "开始合包")
                    NDKInterface.bsdfff(oldFile,newFile,opatchFile)
                    var fnewFile= File(newFile)
                    LogUtil.e("fnewFile exists:"+fnewFile.exists())
                }else{
                    ToastUtil.showToast(this@IncrementalUpdateActivity, "无法合入")
                    return false
                }

            } catch (e: Exception) {
                LogUtil.e("patchfile:$e.message")
                e.printStackTrace()
                return false
            }

            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            //3.安装
            if (result!!) {
               ToastUtil.showToast(this@IncrementalUpdateActivity, "apk更新")
                installApk(this@IncrementalUpdateActivity, newFile)
            }else{
                ToastUtil.showToast(this@IncrementalUpdateActivity, "合入失败")
            }
        }

    }

    /**
     * 获取已安装Apk文件的源Apk文件
     * 如：/data/app/my.apk
     *
     * @param context
     * @param packageName
     * @return
     */
    fun getSourceApkPath(context: Context, packageName: String): String? {
        if (TextUtils.isEmpty(packageName))
            return ""

        try {
            val appInfo = context.getPackageManager()
                    .getApplicationInfo(packageName, 0)
            return appInfo.sourceDir
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 安装Apk
     *
     * @param context
     * @param apkPath
     */
    fun installApk(context: Context, apkPath: String) {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse("file://$apkPath"),
                "application/vnd.android.package-archive")

        context.startActivity(intent)
    }
}
