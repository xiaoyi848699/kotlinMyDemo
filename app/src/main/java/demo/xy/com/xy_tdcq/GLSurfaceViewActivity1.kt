package demo.xy.com.xy_tdcq

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import demo.xy.com.xy_tdcq.gl.GLRender1
import demo.xy.com.xy_tdcq.view.MyGLSurfaceView


class GLSurfaceViewActivity1 : AppCompatActivity() {
//    lateinit  var mView:GLSurfaceView
    @BindView(R.id.glsurfaceview) lateinit var glSurfaceView: MyGLSurfaceView

    var unbinder : Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doodle)
        //注册ButterKnife
        unbinder = ButterKnife.bind(this)
//        mView = GLSurfaceView(this)
//        setContentView(mView)
        if(IsSupported()){
            glSurfaceView.setRenderer(GLRender1())
        }
    }

    override fun onResume() {
        super.onResume()
//        mView?.onResume()
        glSurfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
//        mView?.onPause()
        glSurfaceView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
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
