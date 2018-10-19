package demo.xy.com.xytdcq.screen

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.vincent.videocompressor.VideoCompress
import demo.xy.com.xytdcq.BaseAtivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.service.RecordService
import demo.xy.com.xytdcq.uitls.LogUtil
import demo.xy.com.xytdcq.uitls.PermissionUtils
import demo.xy.com.xytdcq.uitls.VersionUtils
import demo.xy.com.xytdcq.view.dialog.MyProgressDialog

class ScreenRecordingAndCompress : BaseAtivity() {
    override fun getLayout(): Int {
       return R.layout.activity_screen_recording_and_compress
    }

    override fun setDataAndEvent() {
        val isAllGranted = PermissionUtils.checkPermissionAllGranted(this, permissions)
        if (!isAllGranted) {
            //弹提示框
            if (VersionUtils.checkSDKVersion(23)) {
                ActivityCompat.requestPermissions(this, permissions, STORAGE_REQUEST_CODE)
            }
        }
        mProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val intent = Intent(this, RecordService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private val RECORD_REQUEST_CODE = 101
    private val STORAGE_REQUEST_CODE = 102

    private var recordService: RecordService? = null
    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
    lateinit var mProjectionManager: MediaProjectionManager
    lateinit var mMediaProjection: MediaProjection

    private var dialog: MyProgressDialog? = null


    @BindView(R.id.text_tv)  lateinit var textView: TextView

    fun btnClick(v: View){
        when(v.id){
            R.id.button1 -> {
                if (recordService!!.isRunning()) {
                    Toast.makeText(this, "正在录制", Toast.LENGTH_LONG).show()
                } else {
                    val captureIntent = mProjectionManager.createScreenCaptureIntent()
                    startActivityForResult(captureIntent, RECORD_REQUEST_CODE)
                    textView.text = "正在录制"
                }
            }
            R.id.button2 -> {
                if (this.recordService!!.isRunning()) {
                    this.recordService!!.stopRecord()
                    textView.text = "录制结束"
                } else {
                    Toast.makeText(this, "请先开始录制", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        LogUtil.e("requestCode:$requestCode,resultCode$resultCode")
        if (requestCode == RECORD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data)
            recordService!!.setMediaProject(mMediaProjection)
            recordService!!.startRecord()
        }
    }
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val binder = service as RecordService.RecordBinder
            recordService = binder.recordService
            recordService!!.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi)
            recordService!!.setCompressListener(compressListener)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    internal var compressListener: VideoCompress.CompressListener = object : VideoCompress.CompressListener {
        override fun onStart() {
            LogUtil.e("开始压缩")
            dialog = MyProgressDialog(this@ScreenRecordingAndCompress, "压缩中...")
            dialog!!.show()
        }

        override fun onSuccess() {//压缩时长大概是录制时长+5s-10s
            LogUtil.e("压缩完成")
            textView.text = "压缩完成"
            if (null != dialog && dialog!!.isShowing()) {
                dialog!!.dismiss()
            }
        }

        override fun onFail() {
            LogUtil.e("压缩失败")
            textView.text = "压缩失败"
            if (null != dialog && dialog!!.isShowing()) {
                dialog!!.dismiss()
            }
        }

        override fun onProgress(percent: Float) {
            LogUtil.e("压缩中：$percent%")
            if (null != dialog && dialog!!.isShowing()) {
                dialog!!.setText("压缩中：$percent%")
            }
            textView.text = "压缩中：$percent%"
        }
    }
}
