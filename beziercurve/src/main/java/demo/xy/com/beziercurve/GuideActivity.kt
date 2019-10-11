package demo.xy.com.beziercurve

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import demo.xy.com.beziercurve.view.guidview.SpringIndicator
import java.util.*

class GuideActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var springIndicator: SpringIndicator? = null

    private var fragList: ArrayList<Fragment>? = null
    private val titles = arrayOf("1", "2", "3", "4")
    private var list: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        initData()
        initView()
    }

    private fun initData() {
        fragList = ArrayList()
        fragList!!.add(GuideFragment.newInstance(R.drawable.pic0))
        fragList!!.add(GuideFragment.newInstance(R.drawable.pic1))
        fragList!!.add(GuideFragment.newInstance(R.drawable.pic2))
        fragList!!.add(GuideFragment.newInstance(R.drawable.pic3))
        list = ArrayList()
        for (str in titles) {
            list!!.add(str)
        }
    }

    private fun initView() {
        viewPager = findViewById(R.id.view_pager)
        springIndicator = findViewById(R.id.indicator)
        val adapter = MyAdapter(supportFragmentManager, fragList, list)
        viewPager!!.adapter = adapter
        viewPager!!.offscreenPageLimit = 4
        springIndicator!!.setViewPager(viewPager)

    }
}
