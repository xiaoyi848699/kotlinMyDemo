package demo.xy.com.beziercurve

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import demo.xy.com.beziercurve.adapter.ItemAdapter
import demo.xy.com.beziercurve.view.MoveImageView



class AddShopCartActivity : AppCompatActivity(), ItemAdapter.AddClickListener, Animator.AnimatorListener {
    override fun add(addV: View?) {
        var childCoordinate:IntArray = intArrayOf(1,2)
        var parentCoordinate:IntArray = intArrayOf(1,2)
        var shopCoordinate:IntArray = intArrayOf(1,2)
        //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy。
        addV!!.getLocationInWindow(childCoordinate)
        container!!.getLocationInWindow(parentCoordinate)
        shopImg!!.getLocationInWindow(shopCoordinate)

        //2.自定义ImageView 继承ImageView
        var img =  MoveImageView(this)
        img.setImageResource(R.drawable.heart)
        //3.设置img在父布局中的坐标位置
        img.x = (childCoordinate[0] - parentCoordinate[0]).toFloat()
        img.y = (childCoordinate[1] - parentCoordinate[1]).toFloat()
        //4.父布局添加该Img
        container!!.addView(img)

        //5.利用 二次贝塞尔曲线 需首先计算出 MoveImageView的2个数据点和一个控制点
        var startP =  PointF()
        var endP =  PointF()
        var controlP =  PointF()
        //开始的数据点坐标就是 addV的坐标
        startP.x = (childCoordinate[0] - parentCoordinate[0]).toFloat()
        startP.y = (childCoordinate[1] - parentCoordinate[1]).toFloat()
        //结束的数据点坐标就是 shopImg的坐标
        endP.x = (shopCoordinate[0] - parentCoordinate[0]).toFloat()
        endP.y = (shopCoordinate[1] - parentCoordinate[1]).toFloat()
        //控制点坐标 x等于 购物车x；y等于 addV的y
        controlP.x = endP.x
        controlP.y = startP.y
        //启动属性动画
        var p = PointFTypeEvaluator(controlP)
        var animator = ObjectAnimator.ofObject(img, "mPointF",p, startP, endP)
        animator.duration = 1000
        animator.addListener(this)
        animator.start()
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        //动画结束后 父布局移除 img
        var target = (animation as ObjectAnimator).target
        container!!.removeView(target as View?)
        //shopImg 开始一个放大动画
        var scaleAnim = AnimationUtils.loadAnimation(this, R.anim.shop_scale)
        shopImg!!.startAnimation(scaleAnim)
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }

    private var shopImg: ImageView? = null//购物车 IMG
    private var container: RelativeLayout? = null//ListView 购物车View的父布局
    private var itemLv: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop_cart)
        shopImg = findViewById(R.id.main_img)
        container = findViewById(R.id.main_container)
        itemLv =  findViewById(R.id.main_lv)

        var adapter =  ItemAdapter(this)
        //当前Activity实现 adapter内部 点击的回调
        adapter.setListener(this)
        itemLv!!.adapter = adapter
    }
    /**
     * 自定义估值器
     */
    class PointFTypeEvaluator(var control: PointF) : TypeEvaluator<PointF> {

        /**
         * 每个估值器对应一个属性动画，每个属性动画仅对应唯一一个控制点
         */
//        var control: PointF? = null
        /**
         * 估值器返回值
         */
        var mPointF =  PointF()
        override fun evaluate(fraction: Float, startValue: PointF?, endValue: PointF?): PointF {
            return getBezierPoint(startValue!!, endValue!!, control, fraction)
        }

        /**
         * 二次贝塞尔曲线公式
         *
         * @param start   开始的数据点
         * @param end     结束的数据点
         * @param control 控制点
         * @param t       float 0-1
         * @return 不同t对应的PointF
         */
        private fun getBezierPoint( start:PointF,  end:PointF,  control:PointF,  t:Float):PointF {
            mPointF.x = (1 - t) * (1 - t) * start.x + 2 * t * (1 - t) * control.x + t * t * end.x
            mPointF.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y
            return mPointF
        }

    }
}
