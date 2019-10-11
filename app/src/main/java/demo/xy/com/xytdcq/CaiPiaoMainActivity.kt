package demo.xy.com.xytdcq

import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import butterknife.BindView
import demo.xy.com.mylibrary.base.BaseAtivity
import demo.xy.com.mylibrary.log.LogUtil
import demo.xy.com.xytdcq.adapter.HistoryAdapter
import demo.xy.com.xytdcq.bean.Caipiao
import demo.xy.com.xytdcq.uitls.Quicksort
import java.util.*


class CaiPiaoMainActivity : BaseAtivity() {
    override fun getLayout(): Int {
        return R.layout.activity_cai_piao_main
    }

    @BindView(R.id.resultRed) lateinit var resultRed: TextView
    @BindView(R.id.resultBlue) lateinit var resultBlue: TextView
    @BindView(R.id.history) lateinit var history: Button
    @BindView(R.id.lv) lateinit var lv: ListView

    var cps: MutableList<Caipiao>? = null
    var myAdapter: HistoryAdapter? = null
    override fun setDataAndEvent() {
        cps = ArrayList()
        history.text = "清空记录(" + cps!!.size + ")"
        myAdapter = HistoryAdapter(this@CaiPiaoMainActivity, cps)
        lv.adapter = myAdapter
    }
    fun createCaipiao1(): Caipiao {
        val array = IntArray(6)
        val cp = Caipiao()
        val r = Random()
        val a = r.nextInt(33) + 1
        var b = r.nextInt(33) + 1
        while (a == b) {
            b = r.nextInt(33) + 1

        }
        var c = r.nextInt(33) + 1
        while (a == c || b == c) {
            c = r.nextInt(33) + 1
        }
        var d = r.nextInt(33) + 1
        while (a == d || b == d || c == d) {
            d = r.nextInt(33) + 1
        }
        var e = r.nextInt(33) + 1
        while (a == e || b == e || c == e || d == e) {
            e = r.nextInt(33) + 1
        }
        var f = r.nextInt(33) + 1
        while (a == f || b == f || c == f || d == f || e == f) {
            f = r.nextInt(33) + 1
        }
        var g = r.nextInt(16) + 1
        var flag = true
        while (flag) {
            if (g < 4) {
                val ra = r.nextInt(100) + 1
                if (ra < 15) {//15%
                    LogUtil.d("TAG", "篮球小于4")
                    flag = false
                } else {
                    LogUtil.d("TAG", "............篮球重置")
                    g = r.nextInt(16) + 1
                }
            } else if (g > 5 && g < 15) {
                val ra = r.nextInt(100) + 1
                if (ra < 80) {//8%
                    LogUtil.d("TAG", "篮球在5到15之间")
                    flag = false
                } else {
                    LogUtil.d("TAG", "............篮球重置")
                    g = r.nextInt(16) + 1
                }
            } else {
                val ra = r.nextInt(100) + 1
                if (ra < 20) {//8%
                    LogUtil.d("TAG", "篮球在其他区间")
                    flag = false
                } else {
                    LogUtil.d("TAG", "............篮球重置")
                    g = r.nextInt(16) + 1
                }
            }
        }
        array[0] = a
        array[1] = b
        array[2] = c
        array[3] = d
        array[4] = e
        array[5] = f
        val sort = Quicksort()
        sort.quickSort(array, 0, array.size - 1)//排序
        //		for (int i = 0; i < array.length; i++) {
        //			LogUtil.d("TAG", "i..."+array[i]);
        //		}
        cp.a = array[0]
        cp.b = array[1]
        cp.c = array[2]
        cp.d = array[3]
        cp.e = array[4]
        cp.f = array[5]
        cp.g = g
        return cp
    }

    fun createCaipiao2(): Caipiao {
        var cp = createCaipiao1()
        var falg1 = 0
        var falg2 = 0
        var falg3 = 0
        var falg4 = 0


        while (falg1 + falg2 + falg3 + falg4 < 4) {
            //最大值和最小值分析
            if (cp.a > 15 || cp.f < 18) {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a > 94) {
                    LogUtil.d("TAG", "最小值大于15，最大值小于18")
                    falg4 = 1
                } else {
                    LogUtil.d("TAG", "..................最大值或最小值分析重置")
                    cp = createCaipiao1()
                    continue
                }
            } else if (cp.a < 10 || cp.f > 22) {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 80) {
                    LogUtil.d("TAG", "最小值小于10，最大值大于22")
                    falg4 = 1
                } else {
                    LogUtil.d("TAG", "..................最大值或最小值分析重置")
                    cp = createCaipiao1()
                    continue
                }
            } else {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 15) {
                    LogUtil.d("TAG", "最大最小值在其他区间")
                    falg4 = 1
                } else {
                    LogUtil.d("TAG", "..................最大值或最小值分析重置")
                    cp = createCaipiao1()
                    continue
                }
            }
            //跨度分析
            if (cp.f - cp.a < 12 || cp.f - cp.a > 32) {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a > 95) {////（//跨度在小于12大于32  5%
                    LogUtil.d("TAG", "跨度在小于12大于32成立")
                    falg1 = 1
                } else {
                    LogUtil.d("TAG", "..................跨度重置")
                    cp = createCaipiao1()
                    continue
                }
            } else if (cp.f - cp.a <= 29 && cp.f - cp.a >= 19) {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 80) {//（19-29）80%
                    LogUtil.d("TAG", "跨度在19-29成立")
                    falg1 = 1
                } else {
                    LogUtil.d("TAG", "..................跨度重置")
                    cp = createCaipiao1()
                    continue
                }
            } else {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 20) {//20%
                    LogUtil.d("TAG", "跨度在其他区间成立")
                    falg1 = 1
                } else {
                    LogUtil.d("TAG", "..................跨度重置")
                    cp = createCaipiao1()
                    continue
                }
            }
            //尾号计算
            var count = 0
            count += cp.a % 10
            count += cp.b % 10
            count += cp.c % 10
            count += cp.d % 10
            count += cp.e % 10
            count += cp.f % 10
            LogUtil.d("TAG", "本次尾号相加$count")
            if (count <= 17 || count > 34) {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 10) {//8%
                    LogUtil.d("TAG", "位数和在17,34开外")
                    falg2 = 1
                } else {
                    LogUtil.d("TAG", "....................位数和值重置")
                    cp = createCaipiao1()
                    continue
                }
            } else if (count > 21 && count < 32) {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 80) {//8%
                    LogUtil.d("TAG", "位数和在21到32")
                    falg2 = 1
                } else {
                    LogUtil.d("TAG", "....................位数和值重置")
                    cp = createCaipiao1()
                    continue
                }
            } else {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 20) {//8%
                    LogUtil.d("TAG", "位数和在其他区间")
                    falg2 = 1
                } else {
                    LogUtil.d("TAG", "....................位数和值重置")
                    cp = createCaipiao1()
                    continue
                }
            }
            //和值分析
            var numberCount = 0
            numberCount += cp.a
            numberCount += cp.b
            numberCount += cp.c
            numberCount += cp.d
            numberCount += cp.e
            numberCount += cp.f
            LogUtil.d("TAG", "本次所有值的和为：$count")
            if (numberCount <= 70 || numberCount > 140) {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 15) {//8%
                    LogUtil.d("TAG", "和值在70、140开外")
                    falg3 = 1
                } else {
                    LogUtil.d("TAG", "...................总和重置")
                    cp = createCaipiao1()
                    continue
                }
            } else if (numberCount > 80 && numberCount < 130) {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 80) {//8%
                    LogUtil.d("TAG", "和值在80到130")
                    falg3 = 1
                } else {
                    LogUtil.d("TAG", "...................总和重置")
                    cp = createCaipiao1()
                    continue
                }
            } else {
                val r = Random()
                val a = r.nextInt(100) + 1
                if (a < 20) {//8%
                    LogUtil.d("TAG", "和值在其他区间")
                    falg3 = 1
                } else {
                    LogUtil.d("TAG", "...................总和重置")
                    cp = createCaipiao1()
                    continue
                }
            }


        }

        return cp
    }
    fun btnClick(v: View) {
        when (v.getId()) {
            R.id.create -> {
                val cp = createCaipiao2()
                resultRed.text = cp.a.toString() + "\t\t" + cp.b + "\t\t" + cp.c + "\t\t" + cp.d + "\t\t" + cp.e + "\t\t" + cp.f + "\t\t"
                resultBlue.text = cp.g.toString() + ""
                cps!!.add(cp)
                history.text = "清空记录(" + (cps?.size ?: 0) + ")"
                myAdapter!!.notifyDataSetChanged()
            }
            R.id.history -> {
                cps?.clear()
                //cps=db.findAll(Caipiao.class);
                history.text = "清空记录(" + (cps?.size ?: 0) + ")"
                myAdapter?.notifyDataSetChanged()
            }

        }
    }
}
