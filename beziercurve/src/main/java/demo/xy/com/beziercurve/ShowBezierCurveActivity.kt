package demo.xy.com.beziercurve

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import demo.xy.com.beziercurve.view.BezierCurve

class ShowBezierCurveActivity : AppCompatActivity() ,CompoundButton.OnCheckedChangeListener {

    private var mBezierCurve: BezierCurve? = null
    private var mTextView: TextView? = null
    private var mLoop: Switch? = null
    private var mTangent: Switch? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_bezier_curve)
        initView()
    }

    private fun initView() {
        mBezierCurve = findViewById(R.id.bezier)
        mTextView = findViewById(R.id.textview)
        mLoop = findViewById(R.id.loop)
        mTangent = findViewById(R.id.tangent)
        mTextView!!.text = mBezierCurve!!.orderStr + "阶贝塞尔曲线"
        mLoop!!.setOnCheckedChangeListener(this)
        mTangent!!.setOnCheckedChangeListener(this)
        mLoop!!.isChecked = false
        mTangent!!.isChecked = true
    }

    fun start(view: View) {
        mBezierCurve!!.start()
    }

    fun stop(view: View) {
        mBezierCurve!!.stop()
    }

    fun add(view: View) {
        if (mBezierCurve!!.addPoint()) {
            mTextView!!.text = mBezierCurve!!.orderStr + "阶贝塞尔曲线"
        } else {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show()
        }
    }

    fun del(view: View) {
        if (mBezierCurve!!.delPoint()) {
            mTextView!!.text = mBezierCurve!!.orderStr + "阶贝塞尔曲线"
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, isChecked: Boolean) {
        when (compoundButton.id) {
            R.id.loop -> mBezierCurve!!.setLoop(isChecked)
            R.id.tangent -> mBezierCurve!!.setTangent(isChecked)
        }
    }
}
