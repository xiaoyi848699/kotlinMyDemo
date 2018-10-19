package demo.xy.com.xytdcq.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.view.View
import android.widget.TextView
import butterknife.BindView
import demo.xy.com.xytdcq.BaseAtivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.uitls.HexUtil
import demo.xy.com.xytdcq.uitls.LogUtil
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class ScreenServiceActivity : BaseAtivity(),ScreenCaputre.ScreenCaputreListener{
    override fun getLayout(): Int {
        return R.layout.activity_screen_service
    }

    override fun setDataAndEvent() {
        infoIp.text = HexUtil.getIPAddress(this)
        myWebSocketServer = MyWebSocketServer(16886)
        myWebSocketServer!!.start()
    }

    private var screenCaputre: ScreenCaputre? = null

    private val REQUEST_CODE = 1
    private var mMediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var myWebSocketServer : MyWebSocketServer? = null
    private var mWebSocket: WebSocket? = null

    @BindView(R.id.info) lateinit var mInfo: TextView
    @BindView(R.id.info_ip) lateinit var infoIp: TextView


    fun btnClick(v: View) {
        when (v.id) {
            R.id.button1 ->  prepareScreen()
            R.id.button2 -> screenCaputre!!.stop()
        }
    }


    private inner class MyWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {

        override fun onOpen(webSocket: WebSocket, clientHandshake: ClientHandshake) {
            mWebSocket = webSocket
            LogUtil.e( "onOpen")
            setInfo("WebSocket  onOpen")
        }

        override fun onClose(webSocket: WebSocket, i: Int, s: String, b: Boolean) {
            LogUtil.e( "onClose")
            setInfo("WebSocket  onClose")
        }

        override fun onMessage(webSocket: WebSocket, s: String) {
            LogUtil.e( "onMessage")
            setInfo("WebSocket  onMessage: $s")
        }

        override fun onError(webSocket: WebSocket, e: Exception) {
            LogUtil.e( "onError")
            setInfo("WebSocket  onError: $e")
        }
    }

    private val mHandler = Handler()

    private fun setInfo(message: String) {
        mHandler.post { mInfo.setText(message) }
    }

    private fun prepareScreen() {
        mMediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val captureIntent = mMediaProjectionManager!!.createScreenCaptureIntent()
        startActivityForResult(captureIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK || requestCode != REQUEST_CODE) return
        mediaProjection = mMediaProjectionManager!!.getMediaProjection(resultCode, data)
        if (mediaProjection == null) {
            return
        }
        val dm = resources.displayMetrics
        screenCaputre = ScreenCaputre(dm.widthPixels, dm.heightPixels, mediaProjection)
        screenCaputre!!.setScreenCaputreListener(this)
        screenCaputre!!.start()
    }

    override fun onImageData(buf: ByteArray) {
        if (null != mWebSocket) {
            setInfo("发送H264数据成功，长度：" + buf.size)
            LogUtil.e("send:" + HexUtil.byte2HexStr(buf))
            mWebSocket!!.send(buf)
        }
    }
}
