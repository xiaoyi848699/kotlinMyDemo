package demo.xy.com.xytdcq.screen

import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import demo.xy.com.xytdcq.BaseAtivity
import demo.xy.com.xytdcq.R
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.nio.ByteBuffer

class ScreenClientActivity : BaseAtivity() {
    override fun getLayout(): Int {
        return R.layout.activity_screen_client
    }

    override fun setDataAndEvent() {
        val dm = resources.displayMetrics
        val surfaceHolder = mSurfaceView!!.holder
        surfaceHolder.setFixedSize(dm.widthPixels, dm.heightPixels)
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        savePath = getsaveDirectory()
    }

    private val TAG = "H264Client"

    private val MIME_TYPE = "video/avc"

    private val VIDEO_WIDTH = 720
    private val VIDEO_HEIGHT = 1280
    @BindView(R.id.surfaceView1) lateinit var mSurfaceView: SurfaceView
    @BindView(R.id.ip_et) lateinit var ipEt: EditText
    private var mCodec: MediaCodec? = null
    private var myWebSocketClient: MyWebSocketClient? = null
    private var savePath: String? = null

    fun btnClick(v: View) {
        when (v.id) {
            R.id.button1 -> start()
            R.id.button2 -> myWebSocketClient!!.close()
        }
    }

    fun start() {
        startServer1()
        initDecoder()
    }


    private fun startServer1() {
        try {
            val url = URI("ws://" + ipEt.text.toString() + ":16886")
            myWebSocketClient = MyWebSocketClient(url)
            myWebSocketClient!!.connectBlocking()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private inner class MyWebSocketClient(serverURI: URI) : WebSocketClient(serverURI) {

        override fun onOpen(serverHandshake: ServerHandshake) {
            Log.e(TAG, "onOpen    ")
        }

        override fun onMessage(s: String) {
            Log.e(TAG, "onMessage    $s")
        }

        override fun onMessage(bytes: ByteBuffer?) {
            val buf = ByteArray(bytes!!.remaining())
            bytes.get(buf)
            //保存视频数据(待完善  视频文件不完整)
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(savePath + "save_video.mp4", true)
                out.write(buf)
                out.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //显示视频数据
            onFrame(buf, 0, buf.size)

        }

        override fun onClose(i: Int, s: String, b: Boolean) {
            Log.e(TAG, "onClose    ")
        }

        override fun onError(e: Exception) {
            Log.e(TAG, "onError    $e")
        }
    }

    fun initDecoder() {
        try {
            mCodec = MediaCodec.createDecoderByType(MIME_TYPE)

            val format = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT)
            format.setInteger(MediaFormat.KEY_BIT_RATE, VIDEO_WIDTH * VIDEO_HEIGHT)
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30)
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)

            // 横屏
            //            byte[] header_sps = {0, 0, 0, 1, 103, 66, -128, 31, -38, 1, 64, 22, -24, 6, -48, -95, 53};
            //            byte[] header_pps = {0, 0 ,0, 1, 104, -50, 6, -30};

            // 竖屏
            val header_sps = byteArrayOf(0, 0, 0, 1, 103, 66, -128, 31, -38, 2, -48, 40, 104, 6, -48, -95, 53)
            val header_pps = byteArrayOf(0, 0, 0, 1, 104, -50, 6, -30)
            //
            format.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps))
            format.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps))
            mCodec!!.configure(format, mSurfaceView!!.getHolder().surface,
                    null, 0)
            mCodec!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    //    int mCount = 0;
    fun onFrame(buf: ByteArray, offset: Int, length: Int): Boolean {
        // Get input buffer index
        val inputBuffers = mCodec!!.inputBuffers
        val inputBufferIndex = mCodec!!.dequeueInputBuffer(100)
        //        Log.e(TAG, " inputBufferIndex  " + inputBufferIndex);

        if (inputBufferIndex >= 0) {
            val inputBuffer = inputBuffers[inputBufferIndex]
            inputBuffer.clear()
            inputBuffer.put(buf, offset, length)
            mCodec!!.queueInputBuffer(inputBufferIndex, 0, length, System.currentTimeMillis(), 0)
            //            mCount++;
        } else {
            return false
        }
        // Get output buffer index
        val bufferInfo = MediaCodec.BufferInfo()
        var outputBufferIndex = mCodec!!.dequeueOutputBuffer(bufferInfo, 100)


        while (outputBufferIndex >= 0) {
            mCodec!!.releaseOutputBuffer(outputBufferIndex, true)
            outputBufferIndex = mCodec!!.dequeueOutputBuffer(bufferInfo, 0)
        }
        return true
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
}
