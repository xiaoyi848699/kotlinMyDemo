package demo.xy.com.beziercurve.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.xy.com.beziercurve.R;

/**
 * Created by xy on 2018/12/4.
 */
public class ItemAdapter extends BaseAdapter implements View.OnClickListener {
    List<String> data = new ArrayList<>();
    Context mContext;

    public ItemAdapter(Context context) {
        mContext = context;
        for (int i = 0; i < 30; i++) {
            data.add("item+" + i);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
            convertView.setTag(new ViewH(convertView));
        }
        ViewH holder = (ViewH) convertView.getTag();
        holder.tv.setText(data.get(position));
        holder.img.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.add(v);
        }
    }

    private AddClickListener mListener;

    public void setListener(AddClickListener listener) {
        mListener = listener;
    }

    public interface AddClickListener {
        void add(View v);
    }

    public static class ViewH {
        private ImageView img;
        private TextView tv;

        public ViewH(View view) {
            img = ((ImageView) view.findViewById(R.id.item_img));
            tv = ((TextView) view.findViewById(R.id.item_text));
        }
    }
}
