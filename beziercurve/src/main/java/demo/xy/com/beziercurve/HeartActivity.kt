package demo.xy.com.beziercurve

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import demo.xy.com.beziercurve.view.heart.AdvancePathView
import demo.xy.com.beziercurve.view.heart.BasicPathView
import demo.xy.com.beziercurve.view.heart.HearView
import demo.xy.com.beziercurve.view.heart.PathView

class HeartActivity : AppCompatActivity() {

    private var basic: BasicPathView? = null
    private var advance: AdvancePathView? = null
    private var hearView: HearView? = null
    private var path: PathView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart)
        initView()
    }

    private fun initView() {
        basic = findViewById(R.id.basic)
        advance = findViewById(R.id.advance)
        hearView = findViewById(R.id.high)
        path = findViewById(R.id.path)
    }
    fun btnClick(v: View){
        when(v.id){
            R.id.path_btn ->{
                path!!.visibility = View.VISIBLE
                basic!!.visibility = View.GONE
                hearView!!.visibility = View.GONE
                advance!!.visibility = View.GONE
            }
            R.id.basic_btn ->{
                basic!!.visibility = View.VISIBLE
                hearView!!.visibility = View.GONE
                advance!!.visibility = View.GONE
                path!!.visibility = View.GONE
            }
            R.id.advance_btn ->{
                advance!!.visibility = View.VISIBLE
                basic!!.visibility = View.GONE
                hearView!!.visibility = View.GONE
                path!!.visibility = View.GONE
            }
            R.id.high_btn ->{
                hearView!!.visibility = View.VISIBLE
                basic!!.visibility = View.GONE
                advance!!.visibility = View.GONE
                path!!.visibility = View.GONE
            }

        }

    }
}
