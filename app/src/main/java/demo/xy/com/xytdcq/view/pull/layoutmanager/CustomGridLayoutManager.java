package demo.xy.com.xytdcq.view.pull.layoutmanager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import demo.xy.com.xytdcq.view.pull.BaseListAdapter;
import demo.xy.com.xytdcq.view.pull.FooterSpanSizeLookUp;


public class CustomGridLayoutManager extends GridLayoutManager implements ILayoutManager {
    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return this;
    }

    @Override
    public int findLastVisiblePosition() {
        return findLastVisibleItemPosition();
    }

    @Override
    public void setUpAdapter(BaseListAdapter adapter) {
        //根据当前行数，和adapter的实时位置判断是否加载Footer
        FooterSpanSizeLookUp lookUp = new FooterSpanSizeLookUp(adapter, getSpanCount());
        setSpanSizeLookup(lookUp);
    }
}
