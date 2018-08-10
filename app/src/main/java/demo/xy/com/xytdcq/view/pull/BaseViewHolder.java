package demo.xy.com.xytdcq.view.pull;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 基础的渲染器, 负责单个条目的渲染工作
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(final View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onItemLongClick(v, getAdapterPosition());
            }
        });
    }

    /**
     * 用于绑定数据到视图
     *
     * @param position 当前条目的位置
     */
    public abstract void onBindViewHolder(int position);

    /**
     * 单个条目的点击事件
     *
     * @param view     当前Item View
     * @param position 当前条目位置
     */
    public abstract void onItemClick(View view, int position);


    /**
     * 单个条目的长按事件
     *
     * @param view     当前Item View
     * @param position 当前条目位置
     */
    public abstract boolean onItemLongClick(View view, int position);
}
