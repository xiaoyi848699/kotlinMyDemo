package demo.xy.com.xytdcq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import demo.xy.com.xytdcq.view.DividerItemLinearLayout

class MainActivity : AppCompatActivity() {
//    lateinit可以在任何位置初始化并且可以初始化多次。而lazy在第一次被调用时就被初始化，想要被改变只能重新定义
    //var是一个可变变量 val是一个只读变量，这种声明变量的方式相当于java中的final变量
    @BindView(R.id.listview) lateinit var listview:RecyclerView

    var unbinder : Unbinder? = null
    val context: Context = this
    val items = listOf(
            "全屏",
            "glsurfaceview1",
            "glsurfaceview2",
            "glsurfaceview3",
            "glsurfaceview4",
            "glsurfaceview5",
            "glsurfaceview6",
            "JNI1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //注册ButterKnife
        unbinder = ButterKnife.bind(this)


        listview.layoutManager = LinearLayoutManager(this)
        listview.addItemDecoration(DividerItemLinearLayout(context,
                R.drawable.bg_recycler_divider,
                DividerItemLinearLayout.VERTICAL_LIST))
        listview.adapter = MainAdapter(items){ s: String, i: Int ->
               Toast.makeText(context,"你点击了$s-->$i",Toast.LENGTH_LONG).show()
            when(i){
                0 -> startActivity(Intent(context,FullActivity::class.java))
                1 -> startActivity(Intent(context,GLSurfaceViewActivity1::class.java))
                2 -> startActivity(Intent(context,GLSurfaceViewActivity2::class.java))
                7 -> startActivity(Intent(context,Jni1Activity::class.java))

            }
        }



    }
    class MainAdapter(val items : List<String>, val itemClickListener: (String,Int) -> Unit) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ViewHolder(view,itemClickListener)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.text_tv.text = items[position]
            holder.bind(items[position],position)
        }

        class ViewHolder(val view:View, val itemClickListener: (String,Int) -> Unit): RecyclerView.ViewHolder(view){
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

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()//!!.为空会报异常
    }
}


