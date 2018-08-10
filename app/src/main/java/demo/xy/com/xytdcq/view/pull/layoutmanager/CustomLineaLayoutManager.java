package demo.xy.com.xytdcq.view.pull.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import demo.xy.com.xytdcq.view.pull.BaseListAdapter;


public class CustomLineaLayoutManager extends LinearLayoutManager implements ILayoutManager{

    public CustomLineaLayoutManager(Context context) {
        super(context);
    }

    public CustomLineaLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLineaLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        return super.scrollVerticallyBy(dy, recycler, state);
    }
}
