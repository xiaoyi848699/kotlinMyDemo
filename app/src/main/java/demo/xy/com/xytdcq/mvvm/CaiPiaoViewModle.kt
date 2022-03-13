package demo.xy.com.xytdcq.mvvm

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import demo.xy.com.mylibrary.dialog.DialogUtils
import demo.xy.com.mylibrary.dialog.MyProgressDialog
import demo.xy.com.mylibrary.log.LogUtil
import demo.xy.com.xytdcq.adapter.HistoryAdapter
import demo.xy.com.xytdcq.base.App
import demo.xy.com.xytdcq.bean.Caipiao
import demo.xy.com.xytdcq.uitls.Quicksort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.security.SecureRandomSpi
import java.util.*
import kotlin.concurrent.thread

class CaiPiaoViewModle(application: Application) : AndroidViewModel(application) {
    val listDate by lazy { MutableLiveData<MutableList<Caipiao>>() }
    val currentCpRed by lazy { MutableLiveData<String>() }
    val currentCpBlue by lazy { MutableLiveData<String>() }
    val myAdapter by lazy { HistoryAdapter(application, listDate.value, 2) }
    val cpSize by lazy { MutableLiveData<String>() }
    var cp = Caipiao()
    lateinit var callback:ICallback
    init {
        listDate.value = ArrayList()
        updateData()
    }

    fun getAdapter() : HistoryAdapter {
        return myAdapter
    }

    fun clear() {
        listDate.value?.clear()
        myAdapter.notifyDataSetChanged()
    }

    fun setCallbackMethod(callback:ICallback) {
        this.callback = callback;
    }

    fun createCaipiao() {
        this.callback?.showLoading()
        thread(start = true) {
            createCaipiao2()
        }
    }

    private fun createCaipiao1(): Caipiao {
        val array = IntArray(5)
        val cp = Caipiao()
        val r = SecureRandom()
        var a = r.nextInt(31) + 1
        var flag = true
        while (flag) {
            a = r.nextInt(31) + 1
            if (a !in 1..24) {
                continue
            }
            LogUtil.e("TAG", "yixiao a-->$a")
            if ((a in 1..4 && (r.nextInt(100) + 1) > 51)) {
                continue
            }
            if ((a in 5..8 && (r.nextInt(100) + 1) > 24)) {
                continue
            }
            if ((a in 9..11 && (r.nextInt(100) + 1) > 12)) {
                continue
            }
            if ((a in 12..17 && (r.nextInt(100) + 1) > 10)) {
                continue
            }
            flag = false
        }
        var b = r.nextInt(31) + 2
        flag = true
        while (flag) {
            b = r.nextInt(31) + 2
            if (b !in 2..29 || a == b) {
                continue
            }
            LogUtil.e("TAG", "yixiao b-->$b")
            if ((b in 11..15 && (r.nextInt(100) + 1) > 31)) {
                continue
            }
            if ((b in 3..7 && (r.nextInt(100) + 1) > 21)) {
                continue
            }
            if ((b in 16..21 && (r.nextInt(100) + 1) > 19)) {
                continue
            }
            if ((b in 8..10 && (r.nextInt(100) + 1) > 17)) {
                continue
            }
            if ((b == 2 && (r.nextInt(100) + 1) > 2)) {
                continue
            }
            flag = false
        }
        var c = r.nextInt(31) + 3
        flag = true
        while (flag) {
            c = r.nextInt(31) + 3
            if (c !in 3..33 || a == c || b == c) {
                continue
            }
            LogUtil.e("TAG", "yixiao c-->$c")
            if ((c in 8..19 && (r.nextInt(100) + 1) > 32)) {
                continue
            }
            if ((c in 20..23 && (r.nextInt(100) + 1) > 23)) {
                continue
            }
            if ((c in 24..28 && (r.nextInt(100) + 1) > 18)) {
                continue
            }
            if ((c in 3..8 && (r.nextInt(100) + 1) > 6)) {
                continue
            }
            flag = false
        }
        var d = r.nextInt(31) + 4
        flag = true
        while (flag) {
            d = r.nextInt(31) + 4
            if (d !in 7..34 || a == d || b == d || c == d) {
                continue
            }
            LogUtil.e("TAG", "yixiao d-->$d")
            if ((d in 22..27 && (r.nextInt(100) + 1) > 33)) {
                continue
            }
            if ((d in 14..21 && (r.nextInt(100) + 1) > 22)) {
                continue
            }
            if ((d in 28..31 && (r.nextInt(100) + 1) > 27)) {
                continue
            }
            if ((d in 7..14 && (r.nextInt(100) + 1) > 7)) {
                continue
            }
            flag = false
        }
        var e = r.nextInt(31) + 5
        flag = true
        while (flag) {
            e = r.nextInt(31) + 5
            if (e !in 10..35 || a == e || b == e || c == e || d == e) {
                continue
            }
            LogUtil.e("TAG", "yixiao e-->$e")
            if ((e in 32..35 && (r.nextInt(100) + 1) > 51)) {
                continue
            }
            if ((e in 21..27 && (r.nextInt(100) + 1) > 18)) {
                continue
            }
            if (e in 28..31 && (r.nextInt(100) + 1) > 16) {
                continue
            }
            if ((e in 10..20 && (r.nextInt(100) + 1) > 5)) {
                continue
            }
            flag = false
        }
        var f = r.nextInt(15) + 1
        var g = r.nextInt(15) + 2
        flag = true
        while (flag) {
            f = r.nextInt(15) + 1
            g = r.nextInt(15) + 2
            if(f == g) {
                continue
            }
            LogUtil.e("TAG", "yixiao f-->$f g-->$g")
            if ((f == 1 && (r.nextInt(100) + 1) > 14)) {
                continue
            }
            if (f in 2..4 && (r.nextInt(100) + 1) > 28) {
                continue
            }
            if (f in 5..6 && (r.nextInt(100) + 1) > 17) {
                continue
            }
            if ((f in 7..8 && (r.nextInt(100) + 1) > 12)) {
                continue
            }
            if (f in 9..10 && (r.nextInt(100) + 1) > 7) {
                continue
            }
            if (g == 1) {
                continue
            }
            if (g in 2..3 && (r.nextInt(100) + 1) > 3) {
                continue
            }
            if (g in 4..6 && (r.nextInt(100) + 1) > 17) {
                continue
            }
            if (g in 7..8 && (r.nextInt(100) + 1) > 22) {
                continue
            }
            if (g in 9..11 && (r.nextInt(100) + 1) >= 42) {
                continue
            }
            val s = f + g
            if (s !in 3.. 23) {
                continue
            }
            if (s in 3..6 && (r.nextInt(100) + 1) > 7) {
                continue
            }
            if (s in 7..13 && (r.nextInt(100) + 1) > 33) {
                continue
            }
            if (s in 13..17 && (r.nextInt(100) + 1) > 30) {
                continue
            }
            if (s in 18..20 && (r.nextInt(100) + 1) >= 7) {
                continue
            }
            if (s in 21..23 && (r.nextInt(100) + 1) >= 7) {
                continue
            }
            flag = false
        }
        array[0] = a
        array[1] = b
        array[2] = c
        array[3] = d
        array[4] = e
        val sort = Quicksort()
        sort.quickSort(array, 0, array.size - 1)//排序
        cp.a = array[0]
        cp.b = array[1]
        cp.c = array[2]
        cp.d = array[3]
        cp.e = array[4]
        if (f > g) {
            cp.f = g
            cp.g = f
        } else {
            cp.f = f
            cp.g = g
        }
        return cp
    }

    private fun createCaipiao2() {
        var cp = createCaipiao1()
        val r = SecureRandom()
        var flag = true
        while (flag) {
            var s = cp.a + cp.b + cp.c + cp.d+ cp.e
            if (s !in 30..149) {
                cp = createCaipiao1()
                continue
            }
            if ((s in 30..60 && (r.nextInt(100) + 1) > 9)) {
                cp = createCaipiao1()
                continue
            }
            if ((s in 61..77 && (r.nextInt(100) + 1) > 17)) {
                cp = createCaipiao1()
                continue
            }
            if (s in 78..90 && (r.nextInt(100) + 1) > 23) {
                cp = createCaipiao1()
                continue
            }
            if ((s in 91..101 && (r.nextInt(100) + 1) > 20)) {
                cp = createCaipiao1()
                continue
            }
            if ((s in 102..121 && (r.nextInt(100) + 1) > 20)) {
                cp = createCaipiao1()
                continue
            }
            if ((s in 122..149 && (r.nextInt(100) + 1) > 10)) {
                cp = createCaipiao1()
                continue
            }
            flag = false
        }

        flag = true
        while (flag) {
            var s = cp.a + cp.b + cp.c + cp.d+ cp.e + cp.f + cp.g
            var average = s / 7.0
//            var average = Arrays.stream(intArrayOf(cp.a, cp.b, cp.c, cp.d, cp.e, cp.f, cp.g)).average().asDouble
            LogUtil.e("TAG", "yixiao average-->$average")
            if (average < 5.86 || average > 24) {
                cp = createCaipiao1()
                continue
            }
            if ((average <= 10 && (r.nextInt(100) + 1) > 12)) {
                cp = createCaipiao1()
                continue
            } else if ((average <= 12 && (r.nextInt(100) + 1) > 12.5)) {
                cp = createCaipiao1()
                continue
            } else if ((average <= 17 && (r.nextInt(100) + 1) > 58)) {
                cp = createCaipiao1()
                continue
            } else if (average <= 21 && (r.nextInt(100) + 1) > 16) {
                cp = createCaipiao1()
                continue
            } else if ((average <= 24 && (r.nextInt(100) + 1) > 3)) {
                cp = createCaipiao1()
                continue
            }
            flag = false
        }


        var falg1 = 0
        var falg2 = 0
        var falg3 = 0
        var falg4 = 0


//        while (falg1 + falg2 + falg3 + falg4 < 4) {
//            //最大值和最小值分析
//            if (cp.a > 15 || cp.e < 18) {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a > 94) {
//                    LogUtil.d("TAG", "最小值大于15，最大值小于18")
//                    falg4 = 1
//                } else {
//                    LogUtil.d("TAG", "..................最大值或最小值分析重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            } else if (cp.a < 10 || cp.e > 22) {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 80) {
//                    LogUtil.d("TAG", "最小值小于10，最大值大于22")
//                    falg4 = 1
//                } else {
//                    LogUtil.d("TAG", "..................最大值或最小值分析重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            } else {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 15) {
//                    LogUtil.d("TAG", "最大最小值在其他区间")
//                    falg4 = 1
//                } else {
//                    LogUtil.d("TAG", "..................最大值或最小值分析重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            }
//            //跨度分析
//            if (cp.e - cp.a < 12 || cp.e - cp.a > 32) {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a > 95) {////（//跨度在小于12大于32  5%
//                    LogUtil.d("TAG", "跨度在小于12大于32成立")
//                    falg1 = 1
//                } else {
//                    LogUtil.d("TAG", "..................跨度重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            } else if (cp.e - cp.a <= 29 && cp.e - cp.a >= 19) {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 80) {//（19-29）80%
//                    LogUtil.d("TAG", "跨度在19-29成立")
//                    falg1 = 1
//                } else {
//                    LogUtil.d("TAG", "..................跨度重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            } else {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 20) {//20%
//                    LogUtil.d("TAG", "跨度在其他区间成立")
//                    falg1 = 1
//                } else {
//                    LogUtil.d("TAG", "..................跨度重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            }
//            //尾号计算
//            var count = 0
//            count += cp.a % 10
//            count += cp.b % 10
//            count += cp.c % 10
//            count += cp.d % 10
//            count += cp.e % 10
//            count += cp.f % 10
//            LogUtil.d("TAG", "本次尾号相加$count")
//            if (count <= 17 || count > 34) {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 10) {//8%
//                    LogUtil.d("TAG", "位数和在17,34开外")
//                    falg2 = 1
//                } else {
//                    LogUtil.d("TAG", "....................位数和值重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            } else if (count > 21 && count < 32) {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 80) {//8%
//                    LogUtil.d("TAG", "位数和在21到32")
//                    falg2 = 1
//                } else {
//                    LogUtil.d("TAG", "....................位数和值重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            } else {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 20) {//8%
//                    LogUtil.d("TAG", "位数和在其他区间")
//                    falg2 = 1
//                } else {
//                    LogUtil.d("TAG", "....................位数和值重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            }
//            //和值分析
//            var numberCount = 0
//            numberCount += cp.a
//            numberCount += cp.b
//            numberCount += cp.c
//            numberCount += cp.d
//            numberCount += cp.e
//            numberCount += cp.f
//            LogUtil.d("TAG", "本次所有值的和为：$count")
//            if (numberCount <= 70 || numberCount > 140) {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 15) {//8%
//                    LogUtil.d("TAG", "和值在70、140开外")
//                    falg3 = 1
//                } else {
//                    LogUtil.d("TAG", "...................总和重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            } else if (numberCount > 80 && numberCount < 130) {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 80) {//8%
//                    LogUtil.d("TAG", "和值在80到130")
//                    falg3 = 1
//                } else {
//                    LogUtil.d("TAG", "...................总和重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            } else {
//                val r = Random()
//                val a = r.nextInt(100) + 1
//                if (a < 20) {//8%
//                    LogUtil.d("TAG", "和值在其他区间")
//                    falg3 = 1
//                } else {
//                    LogUtil.d("TAG", "...................总和重置")
//                    cp = createCaipiao1()
//                    continue
//                }
//            }
//        }
        this.cp = cp
        GlobalScope.launch(Dispatchers.Main) {
            listDate.value?.add(cp)
            myAdapter.notifyDataSetChanged()
            updateData()
            callback?.dissLoading()
        }
    }

    private fun updateData() {
        currentCpRed.value =
            cp.a.toString() + "\t\t" + cp.b + "\t\t" + cp.c + "\t\t" + cp.d + "\t\t" + cp.e + "\t\t"
        currentCpBlue.value =  cp.f.toString() + "\t\t" + cp.g.toString()
        cpSize.value = "清空记录(${listDate.value?.size?:0})"
    }
}