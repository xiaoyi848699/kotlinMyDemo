package demo.xy.com.xytdcq.nkd

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import demo.xy.com.mylibrary.storage.DevMountInfo
import demo.xy.com.xytdcq.*
import demo.xy.com.xytdcq.base.App
import demo.xy.com.xytdcq.fileManager.FileManagerActivity
import demo.xy.com.xytdcq.view.DividerItemLinearLayout

class MainNKDActivity : BaseAtivity() {

    override fun getLayout(): Int {
        return R.layout.activity_main_nkd
    }

    @BindView(R.id.listview) lateinit var listview: RecyclerView

    override fun setDataAndEvent() {
        listview.layoutManager = LinearLayoutManager(this)
        listview.addItemDecoration(DividerItemLinearLayout(context,
                R.drawable.bg_recycler_divider,
                DividerItemLinearLayout.VERTICAL_LIST))
        listview.adapter = MainAdapter(items) { s: String, i: Int ->
            //            Toast.makeText(context,"你点击了$s-->$i",Toast.LENGTH_LONG).show()
            when (i) {
                0 -> startActivity(Intent(context, Jni1Activity::class.java))
                1 -> startActivity(Intent(context, EncryptionFileActivity::class.java))
                2 -> startActivity(Intent(context, FileSplittingMergeActivity::class.java))
                3 -> startActivity(Intent(context, IncrementalUpdateActivity::class.java))
                4 -> startActivity(Intent(context, VoiceChangerActivity::class.java))
            }
        }
    }

    val context: Context = this
    private val items = listOf(
            "MD5+RSA+BASE64+AES+DES",
            "文件加密与解密",
            "文件拆分与合并",
            "apk增量更新",
            "FMOD仿QQ变声")


    class MainAdapter(private val items : List<String>, private val itemClickListener: (String, Int) -> Unit) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ViewHolder(view,itemClickListener)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position],position)
        }

        class ViewHolder(val view: View, private val itemClickListener: (String, Int) -> Unit): RecyclerView.ViewHolder(view){
            private var textTv: TextView = view.findViewById(R.id.text_tv)
            fun bind(str: String,position:Int) {
//                view.textTv.text = str
                textTv.text = str
                view.setOnClickListener {
                    itemClickListener(str,position)
                }
            }

        }


    }
}
