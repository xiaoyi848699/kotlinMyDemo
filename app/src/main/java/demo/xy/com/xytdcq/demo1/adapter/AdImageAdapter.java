package demo.xy.com.xytdcq.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import demo.xy.com.xytdcq.R;

/**
 * 广告适配器
 * @author xiaoyi
 * 2014年11月26日
 */
public class AdImageAdapter extends BaseAdapter{
		
		private Context mContext;
		private LayoutInflater mInflater;
		private List<Integer> imgs;
		private int from;
		public AdImageAdapter(Context context,List<Integer> imgs,int from) {
			mContext = context;
			this.imgs = imgs;
			this.from = from;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;  
		}
		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			viewHold holder = null;
			if (convertView == null) {
				holder=new viewHold();
				view = mInflater.inflate(R.layout.image_item, null);
				holder.image=(ImageView) view.findViewById(R.id.imgView);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(viewHold) view.getTag();
			}
//			Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
//			ImageLoader.getInstance().displayImage(imgs.get(position%imgs.size()), holder.image, MyApplication.options);
//			Bitmap bt = BitmapFactory.decodeFile(imgs.get(position%imgs.size()));
			holder.image.setBackgroundResource(imgs.get(position%imgs.size()));
			return view;
		}

		class viewHold{
			ImageView image;
		}
}
