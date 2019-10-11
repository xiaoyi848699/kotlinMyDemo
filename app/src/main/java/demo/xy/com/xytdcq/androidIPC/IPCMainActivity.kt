package demo.xy.com.xytdcq.androidIPC

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import demo.xy.com.xytdcq.FullActivity
import demo.xy.com.xytdcq.MainActivity
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.base.BaseActivity
import demo.xy.com.xytdcq.view.DividerItemLinearLayout

class IPCMainActivity : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_ipcmain
    }

    override fun setDataAndEvent() {
        listview!!.layoutManager = LinearLayoutManager(this)
        listview!!.addItemDecoration(DividerItemLinearLayout(context,
                R.drawable.bg_recycler_divider,
                DividerItemLinearLayout.VERTICAL_LIST))
        listview!!.adapter = MainActivity.MainAdapter(itemsName) { s: String, i: Int ->
            startActivity(Intent(context, itemsAC[i]))
        }
    }

    @BindView(R.id.ipc_listview) lateinit var listview: RecyclerView

    val context: Context = this
    private var itemsName = listOf(
            "Messenger",
            "AIDL")
    private var itemsAC = listOf(
            FullActivity::class.java)

    override fun onDestroy() {
        super.onDestroy()
        itemsName = emptyList()
        itemsAC = emptyList()
    }
}
