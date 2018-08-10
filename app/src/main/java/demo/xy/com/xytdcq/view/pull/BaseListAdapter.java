package demo.xy.com.xytdcq.view.pull;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.xy.com.xytdcq.R;


/**
 * Adapter的封装类
 */
public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    protected static final int VIEW_TYPE_LOAD_MORE_FOOTER = 101;    //加载更多视图类型
    protected boolean isLoadMoreFooterShown;                        //是否显示加载更多

    protected static final int VIEW_TYPE_NO_MORE_DATA = 103;         //没有更多的数据
    protected boolean isNoMoreDataShown;                              //是否显示没有更多数据的视图

    protected static final int VIEW_TYPE_HEAD = 105;                //头部类型


    public BaseListAdapter() {
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        //如果是瀑布布局的话, 需要进行特殊处理
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            if (holder.itemView.getLayoutParams() instanceof
                    StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params =
                        (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
        holder.onBindViewHolder(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE_FOOTER) {
            return onCreateLoadMoreFooterViewHolder(parent);
        } else if (VIEW_TYPE_NO_MORE_DATA == viewType) {
            return onCreateNoMoreDataViewHolder(parent);
        } else {
            return onCreateNormalViewHolder(parent, viewType);
        }
    }


    @Override
    public int getItemCount() {
        return getDataCount() + (isLoadMoreFooterShown ? 1 : 0) + ((!isLoadMoreFooterShown && isNoMoreDataShown) ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_FOOTER;
        } else if (!isLoadMoreFooterShown && isNoMoreDataShown && position == getItemCount() - 1) {
            return VIEW_TYPE_NO_MORE_DATA;
        } else {
            return getDataViewType(position);
        }
    }

    /**
     * 改变当前的加载状态
     */
    public void onLoadMoreStateChange(boolean isShown) {
        this.isLoadMoreFooterShown = isShown;
        if (isShown) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    /**
     * 是否显示没有更多数据的视图
     * @param isShown   true显示没有更多数据视图
     */
    public void onNoMoreDataStateChange(boolean isShown) {
        this.isNoMoreDataShown = isShown;
        if (isShown) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    /**
     * 判断这个位置是否是Footer
     */
    public boolean isLoadMoreFooter(int position) {
        return isLoadMoreFooterShown && position == getItemCount() - 1;
    }

    /**
     * 当前位置是否是分节位置
     */
    public boolean isSectionHeader(int position) {
        return false;
    }

    /**
     * 返回每个条目的视图类型
     */
    protected int getDataViewType(int position) {
        return 0;
    }


    /**
     * 默认实现Footer的ViewHolder
     */
    protected BaseViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
        return new WrapperViewHolder(view);
    }


    /**
     * 默认实现暂无数据
     *
     * @param parent
     * @return
     */
    protected BaseViewHolder onCreateNoMoreDataViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_pull_to_refresh_no_more_data, parent, false);
        return new WrapperViewHolder(view);
    }


    /**
     * 默认实现Footer的ViewHolder
     */
    protected class WrapperViewHolder extends BaseViewHolder {

        public WrapperViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public boolean onItemLongClick(View view, int position) {
            return false;
        }
    }

    //所有的条目
    protected abstract int getDataCount();

    /**
     * 需要用户自己去实现的ViewHolder
     */
    protected abstract BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);
}
