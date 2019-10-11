package demo.xy.com.xytdcq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.guideview.GuideView
import com.guideview.GuideViewHelper
import com.guideview.LightType
import com.guideview.style.*
import demo.xy.com.mylibrary.base.BaseAtivity

/**
 * 高亮导航
 */
class HighlightGuideActivity : BaseAtivity(), View.OnClickListener {

    private var helper: GuideViewHelper? = null
    override fun onClick(v: View?) {
        helper!!.nextLight()
    }

    override fun getLayout(): Int {
       return R.layout.activity_highlight_guide
    }
    @BindView(R.id.button5) lateinit var button5: Button
    @BindView(R.id.button6) lateinit var button6: Button
    @BindView(R.id.button7) lateinit var button7: Button
    @BindView(R.id.textView1) lateinit var textView1: TextView
    @BindView(R.id.textView2) lateinit var textView2: TextView
    @BindView(R.id.textView3) lateinit var textView3: TextView

    override fun setDataAndEvent() {
//        showClickDecoToNext()
        showOnCreate()
    }

    /**
     * 自己控制显示下一个高亮
     */
    private fun showClickDecoToNext() {

        val deco_view1 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)
        val deco_view2 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)
        val deco_view3 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)
        val deco_view4 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)

        deco_view1.setOnClickListener(this)
        deco_view2.setOnClickListener(this)
        deco_view3.setOnClickListener(this)
        deco_view4.setOnClickListener(this)

        (deco_view1.findViewById(R.id.tv_deco) as TextView).text = "右上布局\n点击下一个"
        (deco_view2.findViewById(R.id.tv_deco) as TextView).text = "正右方布局\n点击下一个"
        (deco_view3.findViewById(R.id.tv_deco) as TextView).text = "左下方布局\n点击下一个"
        (deco_view4.findViewById(R.id.tv_deco) as TextView).text = "正上方布局\n点击下一个"

        //注意这里要是addView第一个参数传的是View
        // 一定注意LayoutInflater.from(this).inflate中第二个一定要传入个ViewGroup
        //为了保存XML中的LayoutParams
        helper = GuideViewHelper(this)
                .addView(button5, RightTopStyle(deco_view1))
                .addView(button6, CenterRightStyle(deco_view2))
                .addView(button7, LeftBottomStyle(deco_view3, 10))
                .addView(textView1, CenterTopStyle(deco_view4, 10))
//                .addView(textView2, CenterTopStyle(deco_view4, 10))
//                .addView(textView3, CenterTopStyle(deco_view4, 10))
                .type(LightType.Circle)
                .onDismiss {Toast.makeText(applicationContext, "finish", Toast.LENGTH_LONG).show()}
        helper!!.show()

    }

    /**
     * 在OnCreate方法中调用,此时View宽高没测量,若是一次全显示的话用postShowAll(),依次的话postShow();
     * 自动消失的话需加上autoDismiss();
     */
    private fun showOnCreate() {

        val deco_view1 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)
        val deco_view2 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)
        val deco_view3 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)
        val deco_view4 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)
        val deco_view5 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)
        val deco_view6 = LayoutInflater.from(this).inflate(R.layout.layout_decor, window.decorView as ViewGroup, false)


        (deco_view1.findViewById(R.id.hint_info) as TextView).text = "你可以再这里干嘛..."
        (deco_view2.findViewById(R.id.hint_info) as TextView).text = "在这里高亮了"
        (deco_view3.findViewById(R.id.hint_info) as TextView).text = "哎！这里也是的"
        (deco_view4.findViewById(R.id.hint_info) as TextView).text = "然后久是这里了"
        (deco_view5.findViewById(R.id.hint_info) as TextView).text = "嗯 ，效果还可以"
        (deco_view6.findViewById(R.id.hint_info) as TextView).text = "最后是这里..."
        (deco_view6.findViewById(R.id.tv_deco) as TextView).text = "完成"

        //注意这里要是addView第一个参数传的是View
        // 一定注意LayoutInflater.from(this).inflate中第二个一定要传入个ViewGroup
        //为了保存XML中的LayoutParams
        helper = GuideViewHelper(this)
                .addView(button5, LeftTopStyle(deco_view1))
                .addView(button6, CenterRightStyle(deco_view2))
                .addView(button7, LeftBottomStyle(deco_view3, 10))
                .addView(textView1, RightTopStyle(deco_view4, 10))
                .addView(textView2, CenterTopStyle(deco_view5, 10))
                .addView(textView3, LeftTopStyle(deco_view6, 10))
                .type(LightType.Rectangle)
                .autoNext()
                .alpha(150)
                .onDismiss(GuideView.OnDismissListener { })
        helper!!.postShow()

    }
}
