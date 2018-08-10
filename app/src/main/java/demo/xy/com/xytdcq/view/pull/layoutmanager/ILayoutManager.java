package demo.xy.com.xytdcq.view.pull.layoutmanager;

import android.support.v7.widget.RecyclerView;

import demo.xy.com.xytdcq.view.pull.BaseListAdapter;


/**
 * 布局管理器的统一接口
 */
public interface ILayoutManager {
    /**
     * 获取当前的布局管理器
     * @return
     */
    RecyclerView.LayoutManager getLayoutManager();

    /**
     * 获取最后一个可见条目的位置
     * @return
     */
    int findLastVisiblePosition();


    /**
     * 设置Adapter,便于布局管理器使用Adapter
     */
    void setUpAdapter(BaseListAdapter adapter);
}
