package demo.xy.com.xytdcq

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import demo.xy.com.beziercurve.BCMainActivity
import demo.xy.com.xytdcq.screen.RtmpActivity
import demo.xy.com.xytdcq.screen.ScreenRecordingAndCompress
import demo.xy.com.xytdcq.screen.ScreenSharingActivity
import demo.xy.com.xytdcq.surfaceView.GLSurfaceViewActivity
import demo.xy.com.xytdcq.view.DividerItemLinearLayout





class MainActivity : BaseAtivity() {

    override fun getLayout(): Int {
       return  R.layout.activity_main
    }
    override fun setDataAndEvent() {
        listview.layoutManager = LinearLayoutManager(this)
        listview.addItemDecoration(DividerItemLinearLayout(context,
                R.drawable.bg_recycler_divider,
                DividerItemLinearLayout.VERTICAL_LIST))
        listview.adapter = MainAdapter(items){ s: String, i: Int ->
//            Toast.makeText(context,"你点击了$s-->$i",Toast.LENGTH_LONG).show()
            when(i){
                0 -> startActivity(Intent(context,FullActivity::class.java))
                1 -> startActivity(Intent(context, GLSurfaceViewActivity::class.java))
                2 -> startActivity(Intent(context,Jni1Activity::class.java))
                3 -> startActivity(Intent(context,ChangeAPPIconActivity::class.java))
                4 -> startActivity(Intent(context, ScreenRecordingAndCompress::class.java))
                5 -> startActivity(Intent(context, ScreenSharingActivity::class.java))
                6 -> startActivity(Intent(context, RtmpActivity::class.java))
                7 ->startActivity(Intent(context, BCMainActivity::class.java))
                8 ->startActivity(Intent(context, GuideActivity::class.java))
            }
        }
    }

    //    lateinit可以在任何位置初始化并且可以初始化多次。而lazy在第一次被调用时就被初始化，想要被改变只能重新定义
    //var是一个可变变量 val是一个只读变量，这种声明变量的方式相当于java中的final变量
    @BindView(R.id.listview) lateinit var listview:RecyclerView

    val context: Context = this
    val items = listOf(
            "全屏",
            "surfaceview",
            "JNI1+MD5+RSA+BASE64+AES+DES",
            "切换APP ICON",
            "屏幕录制+视频压缩",
            "websocket实时共享屏幕",
            "RTMP推流发送视频",
            "贝塞尔曲线以及应用",
            "高亮新手引导")


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

        class ViewHolder(val view:View, private val itemClickListener: (String, Int) -> Unit): RecyclerView.ViewHolder(view){
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


