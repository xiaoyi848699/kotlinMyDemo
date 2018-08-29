package demo.xy.com.xytdcq.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import demo.xy.com.xytdcq.surfaceView.GLRender3

/**
 * Created by xy on 2018/7/12.
 */
class MyGLSurfaceView2 : GLSurfaceView {
    lateinit var mRenderer: GLRender3
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun setRenderer(renderer: Renderer?) {
        mRenderer = renderer as GLRender3
        super.setRenderer(renderer)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            mRenderer.setPointer(event)
        }
        return true
    }
}