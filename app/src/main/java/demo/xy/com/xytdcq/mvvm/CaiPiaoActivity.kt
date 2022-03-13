package demo.xy.com.xytdcq.mvvm

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import demo.xy.com.mylibrary.dialog.DialogUtils
import demo.xy.com.xytdcq.R
import demo.xy.com.xytdcq.databinding.ActivityCaiPiaoMvvmBinding
import java.util.*


class CaiPiaoActivity : AppCompatActivity() ,ICallback{
    lateinit var vm : CaiPiaoViewModle
    lateinit var binding: ActivityCaiPiaoMvvmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@CaiPiaoActivity, R.layout.activity_cai_piao_mvvm)
        vm = ViewModelProvider.AndroidViewModelFactory(this.application).create(CaiPiaoViewModle::class.java)
        vm.setCallbackMethod(this)
        binding.vm = vm
        binding.lifecycleOwner = this
    }

    override fun showLoading() {
        DialogUtils.showSingle(this,"数据生成中", false, 1000 * 60 * 60)
    }

    override fun dissLoading() {
        DialogUtils.dismissSingle()
    }
//    override fun getLayout(): Int {
//        return R.layout.activity_cai_piao_mvvm
//    }

//    @BindView(R.id.resultRed) lateinit var resultRed: TextView
//    @BindView(R.id.resultBlue) lateinit var resultBlue: TextView
//    @BindView(R.id.history) lateinit var history: Button
//    @BindView(R.id.lv) lateinit var lv: ListView
//
//    var cps: MutableList<Caipiao>? = null
//    var myAdapter: HistoryAdapter? = null
//    override fun setDataAndEvent() {
//        cps = ArrayList()
//        history.text = "清空记录(" + cps!!.size + ")"
//        myAdapter = HistoryAdapter(this@CaiPiaoActivity, cps)
//        lv.adapter = myAdapter
//
//    }

//    fun btnClick(v: View) {
//        when (v.getId()) {
//            R.id.create -> {
//                val cp = createCaipiao2()
//                resultRed.text = cp.a.toString() + "\t\t" + cp.b + "\t\t" + cp.c + "\t\t" + cp.d + "\t\t" + cp.e + "\t\t" + cp.f + "\t\t"
//                resultBlue.text = cp.g.toString() + ""
//                cps!!.add(cp)
//                history.text = "清空记录(" + (cps?.size ?: 0) + ")"
//                myAdapter!!.notifyDataSetChanged()
//            }
//            R.id.history -> {
//                cps?.clear()
//                //cps=db.findAll(Caipiao.class);
//                history.text = "清空记录(" + (cps?.size ?: 0) + ")"
//                myAdapter?.notifyDataSetChanged()
//            }
//
//        }
//    }
}
