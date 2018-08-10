package demo.xy.com.xytdcq.view.pull.layoutmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import demo.xy.com.xytdcq.view.pull.BaseListAdapter;


public class CustomStaggerManager extends StaggeredGridLayoutManager implements ILayoutManager {

    public CustomStaggerManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomStaggerManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return this;
    }

    @Override
    public int findLastVisiblePosition() {
        return getLastVisibleItem(findLastVisibleItemPositions(null));
    }

    @Override
    public void setUpAdapter(BaseListAdapter adapter) {

    }


    /**
     * 从一个条目的多个列中获取最大的位置
     *
     * @param lastVisibleItemPositions
     * @return
     */
    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (0 == i) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

}
