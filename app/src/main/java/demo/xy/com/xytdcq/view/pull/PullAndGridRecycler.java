package demo.xy.com.xytdcq.view.pull;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.uitls.LogUtil;


/**
 * 核心控制器, 负责下拉刷新和加载更多
 */
public class PullAndGridRecycler extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecycleView;
    public static final int ACTION_PULL_TO_REFRESH = 1; //下拉刷新
    public static final int ACTION_LOAD_MORE_REFRESH = 2; //加载更多
    public static final int ACTION_IDLE = 0;              //终止状态
    private int mCurrentState = ACTION_IDLE;                   //初始化为终止状态
    private boolean isLoadMoreEnabled = false;              //默认不启用加载更多
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int VISIBLE_THRESHOLD = 5;        //预加载
    private BaseListAdapter adapter;
    private OnRecycleRefreshListener listener;
    private boolean isPullToRefreshEnable = true;           //默认启用下拉刷新
    private boolean isPullToRefreshAnimationEnable = true;

    private String scrollTag = null; //滑动标志、判断是否需要在滑动中加载图片。

    public PullAndGridRecycler(Context context) {
        super(context);
        setUpView();
    }

    public PullAndGridRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpView();
    }

    public PullAndGridRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpView();
    }

    private void setUpView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_pull_to_refresh, this, true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_text_color);
        mRecycleView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!TextUtils.isEmpty(scrollTag)) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Picasso.with(getContext()).resumeTag(scrollTag);
                        LogUtil.e("停止滑动" + scrollTag);
                    } else {
                        Picasso.with(getContext()).pauseTag(scrollTag);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) {
//                    if (mCurrentState == ACTION_IDLE && isLoadMoreEnabled && checkIfNeedLoadMore()) {
//                        mCurrentState = ACTION_LOAD_MORE_REFRESH;
//                        //显示刷新状态
//                        adapter.onLoadMoreStateChange(true);
//                        //加载更多会禁用下拉刷新
//                        mSwipeRefreshLayout.setEnabled(false);
//                        //通知监听器进行刷新
//                        listener.onRefresh(ACTION_LOAD_MORE_REFRESH);
//                    }
//                }
            }
        });

    }

    public RecyclerView getRecycleView(){
        return mRecycleView;
    }

    public void setScrollTag(String scrollTag) {
        this.scrollTag = scrollTag;
    }

    public void setLayoutManager(RecyclerView.LayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
        mRecycleView.setLayoutManager(mLayoutManager);
    }

    /**
     * 添加条目的装饰
     *
     * @param decoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (null != decoration) {
            mRecycleView.addItemDecoration(decoration);
        }
    }

    /**
     * 是否启用加载更多
     *
     * @param enable
     */
    public void enableLoadMore(boolean enable) {
        isLoadMoreEnabled = enable;
    }


    /**
     * 是否启用下拉刷新
     */
    public void enablePullToRefresh(boolean enable) {
        isPullToRefreshEnable = enable;
        mSwipeRefreshLayout.setEnabled(enable);
    }


    /**
     * 是否显示加载动画
     */
    public void setRefreshAnimation(boolean isLoading) {
        this.isPullToRefreshAnimationEnable = isLoading;
    }

    /**
     * 刷新当前列表
     */
    public void setRefreshing() {
        if (isPullToRefreshAnimationEnable) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
            });
        } else {
            onRefresh();
        }
    }


    /**
     * 设置数据适配器
     */
    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
        mRecycleView.setAdapter(adapter);
//        mLayoutManager.setUpAdapter(adapter);
    }

    /**
     * 设置刷新监听器
     */
    public void setRefreshListener(OnRecycleRefreshListener listener) {
        this.listener = listener;
    }


    /**
     * 加载更多的条件是否达成
     */
//    private boolean checkIfNeedLoadMore() {
//        int lastVisibleItemPosition = mRecycleView.getLayoutManager().findLastVisiblePosition();
//        int totalCount = mRecycleView.getLayoutManager().getItemCount();
//        return totalCount - lastVisibleItemPosition < VISIBLE_THRESHOLD;
//    }


    /**
     * 下拉刷新的监听器
     */
    @Override
    public void onRefresh() {
        mCurrentState = ACTION_PULL_TO_REFRESH;
        if(null != listener){
            listener.onRefresh(ACTION_PULL_TO_REFRESH);
        }else{
            LogUtil.e("onRefresh listener is null");
        }
    }


    /**
     * 刷新时的回调接口
     */
    public interface OnRecycleRefreshListener {
        void onRefresh(int action);
    }


    /**
     * 用户刷新完成时需要调用这个函数通知控制器刷新完成
     */
    public void onRefreshCompleted() {
        switch (mCurrentState) {
            case ACTION_PULL_TO_REFRESH: {
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            }
            case ACTION_LOAD_MORE_REFRESH: {
                adapter.onLoadMoreStateChange(false);
                if (isPullToRefreshEnable) {
                    mSwipeRefreshLayout.setEnabled(true);
                }
                break;
            }
        }
        mCurrentState = ACTION_IDLE;
    }


    public void smoothToBottom() {
        mRecycleView.scrollToPosition(mRecycleView.getAdapter().getItemCount() - 1);
    }

    public void smoothToTop() {
        mRecycleView.scrollToPosition(0);
    }

}
