package demo.xy.com.xytdcq.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * 用于recycleview的条目装饰
 */
class DividerItemLinearLayout : RecyclerView.ItemDecoration {

    //itemDrawable
    private var mDivider: Drawable? = null
    //当前方向
    private var mOrientation: Int = 0

    constructor(context: Context, orientation: Int) {
        val array = context.obtainStyledAttributes(ATTRS)
        mDivider = array.getDrawable(0)
        array.recycle()
        setOrientation(orientation)
    }

    constructor(context: Context, @DrawableRes drawableRes: Int, orientation: Int) {
        mDivider = context.resources.getDrawable(drawableRes)
        setOrientation(orientation)
    }

    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("错误的方向参数!")
        }
        mOrientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (VERTICAL_LIST == mOrientation) {
            drawVertical(c, parent)
        } else if (HORIZONTAL_LIST == mOrientation) {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        //最后一个条目不需要设置分割线
        val childCount = parent.childCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = params.rightMargin + child.right
            val right = left + mDivider!!.intrinsicWidth
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
    }

    //获取分割线尺寸
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        if (VERTICAL_LIST == mOrientation) {
            outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
        } else if (HORIZONTAL_LIST == mOrientation) {
            outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
        }
    }

    companion object {

        //垂直方向
        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        //水平方向
        val VERTICAL_LIST = LinearLayoutManager.VERTICAL

        /**
         * 属性列表
         */
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}