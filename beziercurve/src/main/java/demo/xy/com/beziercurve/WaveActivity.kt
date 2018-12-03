package demo.xy.com.beziercurve

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import demo.xy.com.beziercurve.view.WaveViewByBezier

class WaveActivity : AppCompatActivity() {

    private var waveViewByBezier: WaveViewByBezier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave)
        waveViewByBezier =findViewById(R.id.wave_bezier)
    }

    override fun onPause() {
        super.onPause()
        waveViewByBezier!!.pauseAnimation()
    }

    override fun onResume() {
        super.onResume()
        waveViewByBezier!!.resumeAnimation()
    }


    override fun onDestroy() {
        super.onDestroy()
        waveViewByBezier!!.stopAnimation()
    }
}
