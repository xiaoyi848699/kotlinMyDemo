package demo.xy.com.xytdcq.surfaceView

import android.app.ActivityManager
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.view.MyGLSurfaceView2



class GLSurfaceViewDoodleActivity : AppCompatActivity() {

//    @BindView(R.id.glsurfaceview) lateinit var glSurfaceView: MyGLSurfaceView2
    @BindView(R.id.ll_container) lateinit var llContainer: FrameLayout
    @BindView(R.id.tv_frame_rate) lateinit var tvFrameRate: TextView
    @BindView(R.id.btn_clear) lateinit var btnClear: Button

    var unbinder : Unbinder? = null
    lateinit var glSurfaceView: MyGLSurfaceView2
    lateinit var glRender3: GLRender3

    var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glsuface_view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //注册ButterKnife
        unbinder = ButterKnife.bind(this)
        if(IsSupported()){
            glSurfaceView = MyGLSurfaceView2(this)
            glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            //设置背景透明:
            glSurfaceView.holder.setFormat(PixelFormat.TRANSLUCENT)
            glSurfaceView.setZOrderOnTop(true)
            glRender3 = GLRender3()
            glSurfaceView.setRenderer(glRender3)
            llContainer.addView(glSurfaceView)
            btnClear.setOnClickListener {
                glRender3.clearAll()
            }
            loopGetRate()
        }
    }

    /**利用handler+递归轮询帧率 */
    private fun loopGetRate() {
        handler.postDelayed({
            if( !this.isFinishing){
                tvFrameRate.text = "FPS:" + glRender3.frameCount
                glRender3.frameCount = 0
                loopGetRate()
            }
        }, 1000)
    }
    override fun onResume() {
        super.onResume()
        glSurfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        glSurfaceView.destroyDrawingCache()
        unbinder?.unbind()//!!.为空会报异常
    }

    /**
     * 当前设备是否支持OpenGL ES 2.0
     */


    private fun IsSupported(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000 || Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"))
    }
}
