package demo.xy.com.beziercurve

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import demo.xy.com.beziercurve.view.DividerItemLinearLayout

class BCMainActivity : BCBaseAtivity() {
    override fun getLayout(): Int {
       return R.layout.activity_bcmain;
    }

    lateinit var listview: RecyclerView

    override fun setDataAndEvent() {
        listview = findViewById(R.id.bclistview)
        listview.layoutManager = LinearLayoutManager(this)
        listview.addItemDecoration(DividerItemLinearLayout(context,
                R.drawable.bg_recycler_divider,
                DividerItemLinearLayout.VERTICAL_LIST))
        listview.adapter = MainAdapter(items){ s: String, i: Int ->
            when(i){
                0 -> startActivity(Intent(context,ShowBezierCurveActivity::class.java))
                1 -> startActivity(Intent(context,MsgPointActivity::class.java))
                2 -> startActivity(Intent(context,GuideActivity::class.java))
                3 -> startActivity(Intent(context,HeartActivity::class.java))
            }
        }
    }


    val context: Context = this
    val items = listOf("展示贝塞尔曲线", "QQ消息提示小红点", "ViewPage滑动引导动画", "爱心", "加购物车", "水波")


    class MainAdapter(private val items : List<String>, private val itemClickListener: (String, Int) -> Unit) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ViewHolder(view,itemClickListener)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.text_tv.text = items[position]
            holder.bind(items[position],position)
        }

        class ViewHolder(val view: View, private val itemClickListener: (String, Int) -> Unit): RecyclerView.ViewHolder(view){
            var text_tv: TextView = view.findViewById(R.id.text_tv)
            fun bind(str: String,position:Int) {
//                view.text_tv.text = str
                text_tv.text = str
                view.setOnClickListener {
                    itemClickListener(str,position)
                }
            }

        }


    }
}
