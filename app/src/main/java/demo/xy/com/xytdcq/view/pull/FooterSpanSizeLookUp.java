package demo.xy.com.xytdcq.view.pull;

import android.support.v7.widget.GridLayoutManager;

/**
 * 确定RecycleViewFooter所占据的行数
 */
public class FooterSpanSizeLookUp  extends GridLayoutManager.SpanSizeLookup{
    private BaseListAdapter adapter;
    private int spanCount;

    public FooterSpanSizeLookUp(BaseListAdapter adapter, int spanCount) {
        this.adapter = adapter;
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        //如果当前位置是Footer, 或者分节, 将占据一行
        if (adapter.isLoadMoreFooter(position) || adapter.isSectionHeader(position)){
            return spanCount;
        }else {
            return 1;
        }
    }
}
