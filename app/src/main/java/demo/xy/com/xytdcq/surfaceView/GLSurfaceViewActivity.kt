package demo.xy.com.xytdcq.surfaceView

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import demo.xy.com.xytdcq.R

class GLSurfaceViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glsurface_view)
    }
    fun btnClick(v: View){
        when(v.id){
            R.id.button1 ->startActivity(Intent(this, GLSurfaceViewActivity1::class.java))
            R.id.button2 ->startActivity(Intent(this, GLSurfaceViewActivity2::class.java))
            R.id.button3 ->startActivity(Intent(this, SurfaceViewDoodleActivity::class.java))
            R.id.button4 ->startActivity(Intent(this, GLSurfaceViewDoodleActivity::class.java))
            R.id.button5 ->startActivity(Intent(this, DrawingBoardViewActivity::class.java))
            R.id.button6 ->startActivity(Intent(this, FramLayoutActivity::class.java))
        }
    }
}
