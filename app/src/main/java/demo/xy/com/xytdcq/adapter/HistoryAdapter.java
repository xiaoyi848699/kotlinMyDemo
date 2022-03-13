package demo.xy.com.xytdcq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.bean.Caipiao;

public class HistoryAdapter extends BaseAdapter{
	List<Caipiao> caipiao;
	Context ctx;
	LayoutInflater inflater;
	private int modle;
	
	public HistoryAdapter(Context ctx,List<Caipiao> caipiao, int modle) {
		this.ctx=ctx;
		this.caipiao=caipiao;
		this.inflater=LayoutInflater.from(ctx);
		this.modle=modle;
	}
	@Override
	public int getCount() {
		return caipiao.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return caipiao.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if(convertView==null){
			convertView = inflater.inflate(
					R.layout.caipiao_item, null);
		}
		TextView tvred=(TextView) convertView.findViewById(R.id.resultRed2);
		TextView tvblue=(TextView) convertView.findViewById(R.id.resultBlue2);
		Caipiao cp=caipiao.get(position);
		if (modle == 1) {
			tvred.setText(cp.getA()+"\t\t"+cp.getB()+"\t\t"+cp.getC()+"\t\t"+cp.getD()+"\t\t"+cp.getE()+"\t\t"+cp.getF()+"\t\t");
			tvblue.setText(cp.getG()+"");
		} else {
			tvred.setText(cp.getA()+"\t\t"+cp.getB()+"\t\t"+cp.getC()+"\t\t"+cp.getD()+"\t\t"+cp.getE()+"\t\t");
			tvblue.setText(cp.getF()+"\t\t" + cp.getG()+"");
		}
		return convertView;
	}
	

}
