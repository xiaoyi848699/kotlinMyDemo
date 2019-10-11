package demo.xy.com.xytdcq.screen

import android.media.*
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import demo.xy.com.xytdcq.base.BaseActivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.uitls.HexUtil
import demo.xy.com.xytdcq.uitls.LogUtil
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.ByteBuffer

class ScreenClientActivity : BaseActivity() {
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


    val DEFAULT_FREQUENCY = 44100
    val DEFAULT_MAX_BPS = 64
    val DEFAULT_MIN_BPS = 32
    val DEFAULT_ADTS = 1
    val DEFAULT_MIME = "audio/mp4a-latm"
    val DEFAULT_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT
    val DEFAULT_AAC_PROFILE = MediaCodecInfo.CodecProfileLevel.AACObjectLC
    val DEFAULT_CHANNEL_COUNT = 2
    val DEFAULT_AEC = true

    private var mAudioMediaCodec: MediaCodec? = null
    private var mAudioTrack: AudioTrack? = null
    //用来记录解码失败的帧数
    private var count = 0
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
//            var out: FileOutputStream? = null
//            try {
//                out = FileOutputStream(savePath + "save_video.mp4", true)
//                out.write(buf)
//                out.close()
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }

            //显示视频数据，播放声音
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


            try {
                //需要解码数据的类型
                mAudioMediaCodec = MediaCodec.createDecoderByType(DEFAULT_MIME)
                //初始化解码器
                //MediaFormat用于描述音视频数据的相关参数
                val mediaFormat = MediaFormat()
                //数据类型
                mediaFormat.setString(MediaFormat.KEY_MIME, DEFAULT_MIME)
                mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, DEFAULT_AUDIO_ENCODING)
                //声道个数
                mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, DEFAULT_CHANNEL_COUNT)
                //采样率
                mediaFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, DEFAULT_FREQUENCY)
                //比特率
                mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, DEFAULT_MAX_BPS * 1024)
                //用来标记AAC是否有adts头，1->有
                mediaFormat.setInteger(MediaFormat.KEY_IS_ADTS, DEFAULT_ADTS)
                //用来标记aac的类型
                mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, DEFAULT_AAC_PROFILE)
                //ByteBuffer key（暂时不了解该参数的含义，但必须设置）
                val data = byteArrayOf(0x11.toByte(), 0x90.toByte())
                val csd_0 = ByteBuffer.wrap(data)
                mediaFormat.setByteBuffer("csd-0", csd_0)
                //解码器配置
                mAudioMediaCodec!!.configure(mediaFormat, null, null, 0)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "AudioMediaCodec initial error...")
            }

            val minBuffSize = AudioTrack.getMinBufferSize(DEFAULT_FREQUENCY, DEFAULT_CHANNEL_COUNT, DEFAULT_AUDIO_ENCODING)
            if (minBuffSize == AudioTrack.ERROR_BAD_VALUE) {
                Log.e(TAG, "Invalid parameter !")
            }
            mAudioTrack = AudioTrack(AudioManager.STREAM_MUSIC, DEFAULT_FREQUENCY, AudioFormat.CHANNEL_IN_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT, 2048, AudioTrack.MODE_STREAM)//
            if (mAudioTrack!!.getState() == AudioTrack.STATE_UNINITIALIZED) {
                Log.e(TAG, "AudioTrack initialize fail !")
            }
            mAudioMediaCodec!!.start()
            mAudioTrack!!.play()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun isAudio(frame: ByteArray): Boolean {
        return if (frame.size < 5) {
            false
        } else frame[4] == 0xFF.toByte() && frame[5] == 0xF9.toByte()
    }
    //    int mCount = 0;
    fun onFrame(buf: ByteArray, offset: Int, length: Int): Boolean {
        //判断音视频
        LogUtil.e("reciver:" + HexUtil.byte2HexStr(buf))
        if (isAudio(buf)){
            val temp = ByteArray(buf.size - 4)
            System.arraycopy(buf, 4, temp, 0, buf.size - 4)
            val length = temp.size
            LogUtil.e("音频 paly:" + HexUtil.byte2HexStr(temp))
            //输入ByteBuffer
            val codecInputBuffers = mAudioMediaCodec!!.inputBuffers
            //输出ByteBuffer
            val codecOutputBuffers = mAudioMediaCodec!!.outputBuffers
            //等待时间，0->不等待，-1->一直等待
            val kTimeOutUs: Long = 0
            try {
                //返回一个包含有效数据的input buffer的index,-1->不存在
                val inputBufIndex = mAudioMediaCodec!!.dequeueInputBuffer(kTimeOutUs)
                LogUtil.e("inputBufIndex$inputBufIndex")
                if (inputBufIndex >= 0) {
                    //获取当前的ByteBuffer
                    val dstBuf = codecInputBuffers[inputBufIndex]
                    //清空ByteBuffer
                    dstBuf.clear()
                    //填充数据
                    dstBuf.put(temp, offset, length)
                    //将指定index的input buffer提交给解码器
                    mAudioMediaCodec!!.queueInputBuffer(inputBufIndex, 0, length, 0, 0)
                }
                //编解码器缓冲区
                val info = MediaCodec.BufferInfo()
                //返回一个output buffer的index，-1->不存在
                var outputBufferIndex = mAudioMediaCodec!!.dequeueOutputBuffer(info, kTimeOutUs)
                LogUtil.e("outputBufferIndex$outputBufferIndex")
                if (outputBufferIndex < 0) {
                    //记录解码失败的次数
                    count++
                }

                var outputBuffer: ByteBuffer
                while (outputBufferIndex >= 0) {
                    when (outputBufferIndex) {
                        MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> Log.e(TAG, "INFO_OUTPUT_BUFFERS_CHANGED")
                        MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> Log.e(TAG, "INFO_OUTPUT_FORMAT_CHANGE")
                        MediaCodec.INFO_TRY_AGAIN_LATER -> Log.e(TAG, "INFO_TRY_AGAIN_LATER")
                    }
                    //---------------------------------------------------------------
                    //获取解码后的ByteBuffer
                    outputBuffer = codecOutputBuffers[outputBufferIndex]
                    //用来保存解码后的数据
                    val outData = ByteArray(info.size)
                    outputBuffer.get(outData)
                    //清空缓存
                    outputBuffer.clear()
                    //播放解码后的数据
                    mAudioTrack!!.write(outData, 0, info.size)
                    Log.e("DecodeThread", "buff length = " + info.size)
                    //释放已经解码的buffer
                    mAudioMediaCodec!!.releaseOutputBuffer(outputBufferIndex, false)
                    //解码未解完的数据
                    outputBufferIndex = mAudioMediaCodec!!.dequeueOutputBuffer(info, kTimeOutUs)
                    //--------------------------------------------------------
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception.." + e.toString())
                e.printStackTrace()
            }
        } else {
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
