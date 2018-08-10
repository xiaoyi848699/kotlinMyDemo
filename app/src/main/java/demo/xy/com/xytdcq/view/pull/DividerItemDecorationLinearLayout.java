package demo.xy.com.xytdcq.view.pull;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 用于recycleview的条目装饰
 */
public class DividerItemDecorationLinearLayout extends RecyclerView.ItemDecoration {

    //垂直方向
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    //水平方向
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    /**
     * 属性列表
     */
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    //itemDrawable
    private Drawable mDivider;
    //当前方向
    private int mOrientation;

    public DividerItemDecorationLinearLayout(Context context, int orientation) {
        final TypedArray array = context.obtainStyledAttributes(ATTRS);
        mDivider = array.getDrawable(0);
        array.recycle();
        setOrientation(orientation);
    }

    public DividerItemDecorationLinearLayout(Context context, @DrawableRes int drawableRes, int orientation) {
        mDivider = context.getResources().getDrawable(drawableRes);
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("错误的方向参数!");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (VERTICAL_LIST == mOrientation) {
            drawVertical(c, parent);
        } else if (HORIZONTAL_LIST == mOrientation) {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        //最后一个条目不需要设置分割线
        final int childCount = parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = params.rightMargin + child.getRight();
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (VERTICAL_LIST == mOrientation) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else if (HORIZONTAL_LIST == mOrientation) {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}

