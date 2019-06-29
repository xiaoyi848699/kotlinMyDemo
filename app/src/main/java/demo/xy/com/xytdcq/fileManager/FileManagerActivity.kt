package demo.xy.com.xytdcq.fileManager


import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import butterknife.BindView
import demo.xy.com.mylibrary.dialog.DialogUtils
import demo.xy.com.mylibrary.log.Write
import demo.xy.com.mylibrary.storage.DevMountInfo
import demo.xy.com.xytdcq.BaseAtivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.bean.FileInfo
import demo.xy.com.xytdcq.fileManager.adapter.FileListAdapter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * 文件管理器
 */
class FileManagerActivity : BaseAtivity() {


    companion object {
        /**
         * 获取文件管理器启动Intent: 使用 FileManagerActivity.getIntent(Context mContext, String url,
         * boolean showFolder, int resultCodet)直接获取到文件管理启动的intent;
         *
         * @param mContext
         * 上下文对象
         * @param url
         * 打开sd卡文件夹的完整路径,比如：DevMountInfo.getInstance().getSDCardPath()+"/ExcelFile"
         * @param showFolder
         * 是否显示文件夹
         * @param resultCode
         * 如果设置resultCode 就会在点击文件时返回文件路径在filePath中
         * :mIntent.putExtra("filePath", file.getPath()); resultCode =
         * -1时表示点击文件时打开文件
         * @return 启动文件管理器的Intent
         */
        fun getIntent(mContext: Context, url: String,
                      showFolder: Boolean, resultCode: Int): Intent {
            val mIntent = Intent(mContext, FileManagerActivity::class.java)
            mIntent.putExtra("url", url)
            mIntent.putExtra("showFolder", showFolder)
            mIntent.putExtra("resultCode", resultCode)
            return mIntent
        }
    }
    override fun getLayout(): Int {
        return R.layout.activity_file_manager
    }

    @BindView(R.id.file_listview) lateinit var fileListView: ListView

    /**
     * 记录当前的父文件夹
     */
    private var currentParent: File? = null

    /**
     * 记录当前路径下的所有文件夹的文件数组
     */
    private var currentFiles: Array<File>? = null
    /**
     * 文件路径
     */
    private var folderPath: String? = null
    /**
     * 存放文件信息列表
     */
    private var listFiles: ArrayList<FileInfo>? = null
    /**
     * 自定义适配器
     */
    private var myAdapter: FileListAdapter? = null
    /**
     * sd卡路径
     */
    private var sdCardPath: String? = null

    /**
     * 是否显示文件夹(默认显示)
     */
    private var showFolder: Boolean = true

    /**
     * 判断点击文件时是打开文件还是返回文件路径 -1表示去打开文件
     */
    private var resultCode = -1
    override fun setDataAndEvent() {
        initView()
        initData()
    }

    private fun initView() {
        // listview的item点击事件
        fileListView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val file = File(listFiles!!.get(position).filePath)

            if (file.isFile) {
                if (resultCode === -1) {// 点击打开文件
//                            try {
//                                openFile(file)
//                            } catch (e: Exception) {
//                                // 提示打开失败
//                                Write.error("FileManagerActivity: open "
//                                        + file.getName() + " file failed !"
//                                        + e.message)
//                            }

                } else {// 返回选中文件路径
                    val mIntent = Intent()
                    mIntent.putExtra("filePath", file.path)
                    setResult(resultCode, mIntent)
                    finish()

                }
            } else {
                // 获取用户点击的文件夹 下的所有文件
                val tem = file.listFiles()
                if (tem == null) {

                } else {
                    // 获取用户单击的列表项对应的文件夹，设为当前的父文件夹
                    currentParent = file
                    // 保存当前的父文件夹内的全部文件和文件夹
                    currentFiles = tem
                    // 再次更新ListView
                    inflateListView()
                }

            }
        }
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        listFiles = ArrayList()
        myAdapter = FileListAdapter(this@FileManagerActivity, listFiles!!)
        fileListView.adapter = myAdapter
        // 获取传入的需要打开的文件夹路径
        sdCardPath = getSDPath()
        val mBundle = intent.extras
        if (null != mBundle) {
            folderPath = mBundle.getString("url")
            showFolder = mBundle.getBoolean("showFolder")
            resultCode = mBundle.getInt("resultCode")
        } else {
            Write.error("FileManagerActivity:parameter error")
            finish()
        }
        // 获取指定文件夹
        if (!TextUtils.isEmpty(folderPath)) {
            currentParent = File(folderPath)
            inflateListView()
        } else {
            Write.error("FileManagerActivity: 传入url出错! url:$folderPath")
            finish()
        }

    }


    /**
     * 适配listview的数据
     *
     * @param files
     */
    private fun inflateListView() {
        DialogUtils.show(this,"文件加载中",false)
        // 使用异步任务线程来处理，不阻碍主线程的运行
        object : AsyncTask<String, String, List<FileInfo>>() {
            override fun doInBackground(vararg params: String): List<FileInfo>? {
                val listFiles = ArrayList<FileInfo>()
                // 如果文件夹存在就获取此文件夹下的文件
                if (currentParent?.exists()!!) {
                    currentFiles = currentParent!!.listFiles()
                    if (null != currentFiles) {
                        for (i in 0 until currentFiles!!.size) {
                            val mFileInfo = FileInfo()
                            val mFile = currentFiles!![i]
                            // 文件路径
                            mFileInfo.filePath = mFile.path
                            // 获取文件名称
                            mFileInfo.name = mFile.name
                            // 过滤掉文件夹，只显示文件
                            if (!mFile.isDirectory) {
                                mFileInfo.isFile = true

                                // 获取最后修改日期
                                val modTime = mFile.lastModified()
                                val dateFormat = SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm", Locale.getDefault())
                                mFileInfo.fileModify=dateFormat
                                        .format(Date(modTime))
                                mFileInfo.icon = R.drawable.file_icon
                            } else {
                                if (showFolder) {
                                    // 显示文件夹
                                    mFileInfo.icon = R.drawable.folder
                                    mFileInfo.isFile=false
                                } else {
                                    // 不显示文件夹
                                    continue
                                }
                            }
                            listFiles.add(mFileInfo)
                        }
                    } else {
                        return null
                    }
                } else {
                    return null
                }
                return listFiles
            }

            override fun onPostExecute(result: List<FileInfo>?) {

                if (null == result) {
                    Write.error("FileManagerActivity: Folder not exist! url:$folderPath")
                    finish()
                }
                listFiles?.clear()
                if (result != null) {
                    listFiles?.addAll(result)
                }
                DialogUtils.dismiss()
                // 更新显示列表数据
                if (null == myAdapter) {
                    myAdapter = listFiles?.let {
                        FileListAdapter(this@FileManagerActivity,
                                it)
                    }
                    fileListView.adapter = myAdapter
                } else {
                    myAdapter!!.notifyDataSetChanged()
                }
            }
        }.execute()
    }
    /**
     * 获取手机内存路径
     * @return
     */
    private fun getSDPath(): String {
        return DevMountInfo.getInstance().sdCardPath
    }

    /**
     * 监听系统按键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK ->
                    if (showFolder) {
                        try {
                            if (null != currentParent) {
                                if (!sdCardPath.equals(currentParent!!.getCanonicalPath())) {
                                    // 获取上一级目录
                                    currentParent = currentParent!!.getParentFile()
                                    // 列出当前目录下的所有文件
                                    /**
                                     * minchen cwx299872 182line codecc
                                     * add null != currentParent
                                     * 已经被重新赋值了,所以可以继续加判断
                                     */
                                    // add
                                    if (null != currentParent) {
                                        currentFiles = this.currentParent!!.listFiles()
                                    }
                                    // add end
                                    // 再次更新ListView
                                    inflateListView()
                                } else {
                                    return super.onKeyDown(keyCode, event)
                                }
                            } else {
                                return super.onKeyDown(keyCode, event)
                            }
                        } catch (e: IOException) {
                            Write.error("FileManagerActivity:返回上级目录" + e.message)
                        }

                    } else {
                        return super.onKeyDown(keyCode, event)
                    }
        }
        return true
    }

}
