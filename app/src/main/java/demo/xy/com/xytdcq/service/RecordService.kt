package demo.xy.com.xytdcq.service

import android.app.Service
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.*
import android.widget.Toast
import com.vincent.videocompressor.VideoCompress
import demo.xy.com.xytdcq.uitls.LogUtil
import demo.xy.com.xytdcq.view.dialog.MyProgressDialog
import java.io.File
import java.io.IOException

/**
 * 录制service
 */
class RecordService : Service() {
    private var mediaProjection: MediaProjection? = null
    private var mediaRecorder: MediaRecorder? = null
    private var virtualDisplay: VirtualDisplay? = null

    private var running: Boolean = false
    private var width = 720
    private var height = 1080
    private var dpi: Int = 0

    private var outputFile: String? = null
    private var outputFileCompressor: String? = null
    private var outputFileDir: String? = null
    private var outputFileName: String? = null
    private val dialog: MyProgressDialog? = null
    private var compressListener: VideoCompress.CompressListener? = null

    override fun onBind(intent: Intent): IBinder? {
        return RecordBinder()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val serviceThread = HandlerThread("service_thread",
                Process.THREAD_PRIORITY_BACKGROUND)
        serviceThread.start()
        running = false
        mediaRecorder = MediaRecorder()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun setMediaProject(project: MediaProjection) {
        mediaProjection = project
    }

    fun isRunning(): Boolean {
        return running
    }

    fun setConfig(width: Int, height: Int, dpi: Int) {
        this.width = width
        this.height = height
        this.dpi = dpi
    }

    fun setCompressListener(compressListener: VideoCompress.CompressListener) {
        this.compressListener = compressListener
    }

    fun startRecord(): Boolean {
        if (mediaProjection == null || running) {
            return false
        }
        outputFileDir = getsaveDirectory()
        outputFileName = "" + System.currentTimeMillis()
        outputFile = "$outputFileDir$outputFileName.mp4"
        outputFileCompressor = outputFileDir + outputFileName + "_compressor.mp4"
        initRecorder()
        createVirtualDisplay()
        mediaRecorder!!.start()
        Toast.makeText(this, "录制开始", Toast.LENGTH_LONG).show()
        running = true
        return true
    }

    fun stopRecord(): Boolean {
        LogUtil.e("stopRecord....")
        Toast.makeText(this, "录制停止", Toast.LENGTH_LONG).show()
        if (!running) {
            return false
        }
        running = false
        mediaRecorder!!.stop()
        mediaRecorder!!.reset()
        virtualDisplay!!.release()
        mediaProjection!!.stop()
        //压缩视频文件
        VideoCompress.compressVideoLow(outputFile, outputFileCompressor, compressListener)
        return true
    }


    private fun createVirtualDisplay() {
        virtualDisplay = mediaProjection!!.createVirtualDisplay("MainScreen", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder!!.surface, null, null)
    }

    private fun initRecorder() {
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)//设置用于录制的视频来源。
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)//设置所录制的音视频文件的格式。
        mediaRecorder!!.setOutputFile(outputFile)
        mediaRecorder!!.setVideoSize(width, height)//设置要拍摄的宽度和视频的高度。
        mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)//设置所录制视频的编码格式
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)//设置所录制的声音的编码格式。
        mediaRecorder!!.setVideoEncodingBitRate(5 * 1024 * 1024)//设置所录制视频的编码位率。
        mediaRecorder!!.setVideoFrameRate(40)//设置录制视频的捕获帧速率。
        try {
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun getsaveDirectory(): String? {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val rootDir = Environment.getExternalStorageDirectory().absolutePath + "/" + "ScreenRecord" + "/"

            val file = File(rootDir)
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null
                }
            }

            Toast.makeText(applicationContext, rootDir, Toast.LENGTH_SHORT).show()

            return rootDir
        } else {
            return null
        }
    }

    inner class RecordBinder : Binder() {
        val recordService: RecordService
            get() = this@RecordService
    }
}
