package demo.xy.com.xytdcq.demo1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.xy.com.mylibrary.NumberUtils;
import demo.xy.com.xytdcq.R;

public class ShowResultActivity extends Activity {
	private double total=0;
	private int people=0;
	private double max=0;
	private double min=0;
	private double[] data;
	//	private String[] dataStr;
	private ListView mListView;
	private TextView sumTv;
	private double sum = 0;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				sumTv.setText("use money :"+ NumberUtils.getDecimals(sum, 2));
				List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
				for (int i = 0; i < data.length; i++) {
					Map<String, String> mps = new HashMap<String, String>();
					mps.put("str", "str--->"+data[i]);
					datas.add(mps);
				}
				SimpleAdapter mAdapter = new SimpleAdapter(ShowResultActivity.this, datas, R.layout.item_views1, new String[]{"str"}, new int[]{R.id.textView1});
				mListView.setAdapter(mAdapter);
			}else{
				Toast.makeText(ShowResultActivity.this, "input error", Toast.LENGTH_LONG).show();
			}

		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_result);
		mListView = (ListView) findViewById(R.id.listview);
		sumTv = (TextView) findViewById(R.id.sumTv);
		Intent mIntent = getIntent();
		if(null != mIntent){
			Bundle mBundle = mIntent.getExtras();
			if(null != mBundle){
				String totalStr = mBundle.getString("total");
				String peopleStr = mBundle.getString("people");
				String maxStr = mBundle.getString("max");
				String minStr = mBundle.getString("min");
				Log.d("TAG", totalStr+" "+peopleStr+" "+maxStr +" "+minStr);
				try {
					total = Double.parseDouble(totalStr);
					people = Integer.parseInt(peopleStr);
					max = Double.parseDouble(maxStr);
					min = Double.parseDouble(minStr);
					data = new double[people];
//					dataStr = new String[people];
					Log.d("TAG", total+" "+people+" "+max +" "+min);
					new Thread(){
						public void run() {
							calculateData();
						};
					}.start();
				} catch (NumberFormatException e) {
					Toast.makeText(this, "input error", Toast.LENGTH_LONG).show();
					System.out.println("NumberFormatException"+e.getMessage());
					finish();
				}
			}
		}
	}
	private void calculateData() {
		if(min <= 0){
			min = 0.01;
		}
		if(total<=0||people < 1||max<0.01||min * people > max){
			mHandler.sendEmptyMessage(0);
			return;
		}
		int avg = (int) (total/people)*100;
		//总共份数
		int toatalT = (int) (total*100);
		//已经分发份数
		int countT = 0;
		//每个至少最少
		for (int i = 0; i < data.length; i++) {
			data[i] = min * 100;
			countT += min * 100;
		}
		//剩下的随机
//		int index = 1;
		while(toatalT > countT){
			for (int i = 0; i < data.length; i++) {
				double random = Math.random();
				//最多份数减去当前已经获得份数（却保不会超过最大值）
				int fenshu = (int) (max*100-data[i]);
				//本次随机份数
				int rFenshu = (int) (random * fenshu)/5;
				if(rFenshu <=0){
					continue;
				}
				if(countT + rFenshu > toatalT){
					rFenshu = toatalT - countT;
					if(rFenshu <=0){
//						System.out.println("没有分配了"+countT+","+rFenshu);
						break;
					}
					//最后的分配
					data[i] += rFenshu;
					countT += rFenshu;
//					System.out.println("最后的分配"+countT+","+rFenshu);
					break;
				}else if(rFenshu < min * 100){
					//随机的比最小的还小就忽略
//					System.out.println("随机的比最小的还小就忽略"+countT);
					continue;
				}
				random = Math.random();
//				System.out.println("rFenshu"+rFenshu+" "+avg+" "+random);
				//大于最大值的平均时，通过率只有十分之二
				if(rFenshu < avg/3){
					data[i] += rFenshu;
					countT += rFenshu;
					continue;
				}
				//大于最大值的平均时，通过率只有十分之二
				if(rFenshu > avg && random < 0.8){
					continue;
				}
				random = Math.random();
				//大于最大值的两倍平均值时，通过率只有十分之一
				if(rFenshu > avg*2 && random < 0.9){
					continue;
				}
				random = Math.random();
				//大于最大值的三倍平均值时，通过率只有二十分支一
				if(rFenshu > avg*3 && random < 0.95){
					continue;
				}
				data[i] += rFenshu;
				countT += rFenshu;
//				System.out.println("rFenshu"+rFenshu+",,countT"+countT);
			}
//			System.out.println("index"+index);
//			index ++;
		}

		sum = 0;
		for (int i = 0; i < data.length; i++) {
			data[i] = data[i]/100;
//			dataStr[i] = DateUtil.getDecimals(data[i] *min, 2);
			sum += data[i];
			System.out.println(i+"--->"+data[i]);
		}
		System.out.println("sun--->"+sum);
		mHandler.sendEmptyMessage(1);
	}



}
