package demo.xy.com.beziercurve

import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import demo.xy.com.beziercurve.view.GooViewListener

class MsgPointActivity : AppCompatActivity() {
    private var mTvPoint: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg_point)
        initView()
    }

    private fun initView() {
        mTvPoint = findViewById(R.id.point_conversation)
        mTvPoint!!.text = "10"
        mTvPoint!!.tag = 10
        val listener = object : GooViewListener(this, mTvPoint) {
            override fun onDisappear(mDragCenter: PointF) {
                super.onDisappear(mDragCenter)
                Toast.makeText(this@MsgPointActivity, "消失了", Toast.LENGTH_SHORT).show()
            }

            override fun onReset(isOutOfRange: Boolean) {
                super.onReset(isOutOfRange)
                Toast.makeText(this@MsgPointActivity, "重置了", Toast.LENGTH_SHORT).show()
            }
        }
        mTvPoint!!.setOnTouchListener(listener)
    }
}
