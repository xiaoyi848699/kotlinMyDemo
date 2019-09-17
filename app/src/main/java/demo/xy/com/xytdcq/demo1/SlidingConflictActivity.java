package demo.xy.com.xytdcq.demo1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.demo1.adapter.AdImageAdapter;
import demo.xy.com.xytdcq.demo1.data.CacheData;
import demo.xy.com.xytdcq.demo1.view.ChildViewPager;
import demo.xy.com.xytdcq.demo1.view.MyListView;
import demo.xy.com.xytdcq.demo1.view.MyListViewForScrollView;
import demo.xy.com.xytdcq.demo1.view.ParentViewPager;
import demo.xy.com.xytdcq.demo1.viewFlow.CircleFlowIndicator;
import demo.xy.com.xytdcq.demo1.viewFlow.ViewFlow;

/**
 * 解决滑动冲突demo
 */
public class SlidingConflictActivity extends Activity {
	private ParentViewPager parentViewPager;
	private ChildViewPager viewPagerSon;
	private ImageView mView1;
	private ImageView mView2;
	private ImageView mView3;
	private List<View> pages;
	private List<View> pagesSon;
	private TextView mTextView3;
	private LinearLayout mTopLayout;
	private LinearLayout mTopLayout2;
	private LinearLayout mTopLayout22;
	//	private TextView mTextView1Son;
//	private TextView mTextView2Son;
//	private TextView mTextView3Son;
//	private TextView mTextView4Son;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private MyListView mListView1;
	private MyListViewForScrollView mListView2;
	private MyListView mListView3;
	private SimpleAdapter mAdapter;

	private View headView;
	private View headView2;
	private View headView3;
	private ViewFlow mViewFlow ;
	private ViewFlow mViewFlow2 ;
	private ViewFlow mViewFlow3 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_demo1);
		getView();
		initHeadView();
		initData();
	}

	private void initHeadView() {
		headView = LayoutInflater.from(SlidingConflictActivity.this).inflate(
				R.layout.head_view_flow, null);
		mViewFlow = (ViewFlow) headView.findViewById(R.id.home_viewflow);
		headView2 = LayoutInflater.from(SlidingConflictActivity.this).inflate(
				R.layout.head_view_flow, null);
		mViewFlow2 = (ViewFlow) headView2.findViewById(R.id.home_viewflow);
		headView3 = LayoutInflater.from(SlidingConflictActivity.this).inflate(
				R.layout.head_view_flow, null);
		mViewFlow3 = (ViewFlow) headView3.findViewById(R.id.home_viewflow);
	}
	private void initViewFlow(){
		if(null == CacheData.imgs){
			CacheData.imgs = new ArrayList<Integer>();
		}else{
			CacheData.imgs.clear();
		}
		CacheData.imgs.add(R.drawable.bj1);
		CacheData.imgs.add(R.drawable.cc);
		CacheData.imgs.add(R.drawable.bj1);
		CacheData.imgs.add(R.drawable.cc);

		// 轮播图
		mViewFlow.setAdapter(new AdImageAdapter(SlidingConflictActivity.this,
				CacheData.imgs, 1));
		mViewFlow.setmSideBuffer(CacheData.imgs.size()); // 设置图片5张
		// 轮播图的圆点
		CircleFlowIndicator indic = (CircleFlowIndicator) headView
				.findViewById(R.id.home_viewflowindic);
		mViewFlow.setFlowIndicator(indic);
		mViewFlow.setTimeSpan(5000);
		mViewFlow.setSelection(10 * CacheData.imgs.size()); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
		// 轮播图
		mViewFlow2.setAdapter(new AdImageAdapter(SlidingConflictActivity.this,
				CacheData.imgs, 1));
		mViewFlow2.setmSideBuffer(CacheData.imgs.size()); // 设置图片5张
		// 轮播图的圆点
		CircleFlowIndicator indic2 = (CircleFlowIndicator) headView2
				.findViewById(R.id.home_viewflowindic);
		mViewFlow2.setFlowIndicator(indic2);
		mViewFlow2.setTimeSpan(5000);
		mViewFlow2.setSelection(10 * CacheData.imgs.size()); // 设置初始位置
		mViewFlow2.startAutoFlowTimer(); // 启动自动播放
		// 轮播图
		mViewFlow3.setAdapter(new AdImageAdapter(SlidingConflictActivity.this,
				CacheData.imgs, 1));
		mViewFlow3.setmSideBuffer(CacheData.imgs.size()); // 设置图片5张
		// 轮播图的圆点
		CircleFlowIndicator indic3 = (CircleFlowIndicator) headView3
				.findViewById(R.id.home_viewflowindic);
		mViewFlow3.setFlowIndicator(indic3);
		mViewFlow3.setTimeSpan(5000);
		mViewFlow3.setSelection(10 * CacheData.imgs.size()); // 设置初始位置
		mViewFlow3.startAutoFlowTimer(); // 启动自动播放
	}


	private void initData() {
		pages = new ArrayList<View>();
		LayoutInflater mInflater = LayoutInflater.from(SlidingConflictActivity.this);
		View mView1 = mInflater.inflate(R.layout.views_son, null);
		View mView2 = mInflater.inflate(R.layout.views_listview, null);
		View mView3 = mInflater.inflate(R.layout.item_views1, null);
		initView1(mInflater,mView1);
		mListView1 = (MyListView) mView2.findViewById(R.id.listview);
		mTextView3 = (TextView) mView3.findViewById(R.id.textView1);
		creatAdapter();
		mListView1.setAdapter(mAdapter);
		mListView1.setOnItemClickListener(mClickListener);
//		mListView1.addHeaderView(headView);
		mTextView3.setText("333");
		pages.add(mView1);
		pages.add(mView2);
		pages.add(mView3);
		parentViewPager.setAdapter(new MyPagerAdaper());
		parentViewPager.setCurrentItem(0);
	}
	/**
	 * 创建简单适配器和适配器数据
	 */
	private void creatAdapter() {
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		for (int i = 0; i < 30; i++) {
			Map<String, String> mps = new HashMap<String, String>();
			mps.put("str", "str--->"+i);
			data.add(mps);
		}
		mAdapter = new SimpleAdapter(SlidingConflictActivity.this, data, R.layout.item_views1, new String[]{"str"}, new int[]{R.id.textView1});
	}

	private void initView1(LayoutInflater mInflater,View mView12) {
		pagesSon = new ArrayList<View>();
		viewPagerSon = (ChildViewPager) mView12.findViewById(R.id.viewPager_son);
		tv1 = (TextView) mView12.findViewById(R.id.tv1);
		tv1.setBackgroundColor(getResources().getColor(R.color.color_blue));
		tv2 = (TextView) mView12.findViewById(R.id.tv2);
		tv3 = (TextView) mView12.findViewById(R.id.tv3);
		tv4 = (TextView) mView12.findViewById(R.id.tv4);
		View mView1 = mInflater.inflate(R.layout.views_linear, null);
		View mView2 = mInflater.inflate(R.layout.views_scrollview_listview, null);
		View mView3 = mInflater.inflate(R.layout.views_scrol, null);
		View mView4 = mInflater.inflate(R.layout.views_listview, null);
		pagesSon.add(mView1);
		pagesSon.add(mView2);
		pagesSon.add(mView3);
		pagesSon.add(mView4);
//		mTextView1Son = (TextView) mView1.findViewById(R.id.textView1);
		mTopLayout = (LinearLayout) mView1.findViewById(R.id.top_linear);
		Button mButton = (Button) mView1.findViewById(R.id.button1);
		mButton.setText("升级版");
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent mIntent = new Intent(SlidingConflictActivity.this,ScrollListViewHintActivity.class);
				startActivity(mIntent);
			}
		});
		mListView2 = (MyListViewForScrollView) mView2.findViewById(R.id.listviewForScrollView);
		mTopLayout22 = (LinearLayout) mView2.findViewById(R.id.top_linear);
		mTopLayout2 = (LinearLayout) mView3.findViewById(R.id.top_linear);
		mListView3 = (MyListView) mView4.findViewById(R.id.listview);
//		mTextView1Son.setText("son-111");
		if(null == mAdapter){
			creatAdapter();
		}
		mTopLayout.addView(headView);
		mTopLayout2.addView(headView2);
		mTopLayout22.addView(headView3);
//		mListView2.addHeaderView(headView2);
		initViewFlow();
//		mListView3.addHeaderView(headView);
		mListView2.setAdapter(mAdapter);
		mListView2.setOnItemClickListener(mClickListener);
		mListView3.setAdapter(mAdapter);
		mListView3.setOnItemClickListener(mClickListener);
//		mTextView3Son.setText("son-333");
//		mTextView4Son.setText("son-333");
		viewPagerSon.setAdapter(new MyPagerAdaperSon());
		viewPagerSon.setCurrentItem(0);
		viewPagerSon.setOnPageChangeListener(mChangeListener2);
	}

	private void getView() {
		parentViewPager = (ParentViewPager) findViewById(R.id.viewPager);
		mView1 = (ImageView) findViewById(R.id.iv1);
		mView2 = (ImageView) findViewById(R.id.iv2);
		mView3 = (ImageView) findViewById(R.id.iv3);
		Bitmap bt1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.alarm_level_hint);
		mView1.setImageBitmap(bt1);
		Bitmap bt2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.alarm_level_minor);
		mView2.setImageBitmap(grey(bt2));
		Bitmap bt3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.alarm_level_major);
		mView3.setImageBitmap(grey(bt3));
		parentViewPager.setOnPageChangeListener(mChangeListener);
	}

	public void btnClick(View v){
		switch (v.getId()) {
			case R.id.iv1:
				parentViewPager.setCurrentItem(0);
				break;
			case R.id.iv2:
				parentViewPager.setCurrentItem(1);
				break;
			case R.id.iv3:
				parentViewPager.setCurrentItem(2);
				break;
			case R.id.tv1:
//			Toast.makeText(MainActivity.this, "1", 1).show();
				viewPagerSon.setCurrentItem(0);
				break;
			case R.id.tv2:
				viewPagerSon.setCurrentItem(1);
//			Toast.makeText(MainActivity.this, "2", 1).show();
				break;
			case R.id.tv3:
				viewPagerSon.setCurrentItem(2);
//			Toast.makeText(MainActivity.this, "3", 1).show();
				break;
			case R.id.tv4:
				viewPagerSon.setCurrentItem(3);
//			Toast.makeText(MainActivity.this, "4", 1).show();
				break;
			case R.id.btn19:
				Intent mIntent = new Intent(SlidingConflictActivity.this, PhotoAnimActivity.class);
				startActivity(mIntent);
				break;
			case R.id.btn18:
				mIntent = new Intent(SlidingConflictActivity.this,ScrollListViewHintActivity.class);
				startActivity(mIntent);
				break;
			case R.id.btn17:
				mIntent = new Intent(SlidingConflictActivity.this,RedPagerMainActivity.class);
				startActivity(mIntent);
				break;
			case R.id.btn16:
				break;
			case R.id.btn15:
				break;
			case R.id.btn14:
				break;
			case R.id.btn13:
				break;
			case R.id.btn12:
				break;
			case R.id.btn11:
				break;
			case R.id.btn10:
				break;
			default:
				break;
		}
	}
	/**
	 * 将bitmap处理成黑白图片
	 *
	 * @param bitmap
	 * @return
	 */
	public static final Bitmap grey(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(faceIconGreyBitmap);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return faceIconGreyBitmap;
	}

	private OnPageChangeListener mChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			Bitmap bt1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.alarm_level_hint);
			Bitmap bt2 = BitmapFactory.decodeResource(getResources(),
					R.drawable.alarm_level_minor);
			Bitmap bt3 = BitmapFactory.decodeResource(getResources(),
					R.drawable.alarm_level_major);
			switch (arg0) {
				case 0:
//				viewPagerSon.setEnd(false);
//				parentViewPager.setScrollEnd(false);
					mView1.setImageBitmap(bt1);
					mView2.setImageBitmap(grey(bt2));
					mView3.setImageBitmap(grey(bt3));
					break;
				case 1:
					mView1.setImageBitmap(grey(bt1));
					mView2.setImageBitmap(bt2);
					mView3.setImageBitmap(grey(bt3));
					break;
				case 2:
					mView1.setImageBitmap(grey(bt1));
					mView2.setImageBitmap(grey(bt2));
					mView3.setImageBitmap(bt3);
					break;

				default:
					break;
			}
			parentViewPager.setIndex(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};
	private OnPageChangeListener mChangeListener2 = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			viewPagerSon.setEnd(false);
			parentViewPager.setScrollEnd(false);
			tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
			tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
			tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
			tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
			switch (arg0) {
				case 0:
					tv1.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 1:
					tv2.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 2:
					tv3.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 3:
					viewPagerSon.setEnd(true);
					parentViewPager.setScrollEnd(true);
					tv4.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				default:
					break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};
	OnItemClickListener mClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
								long arg3) {
			Toast.makeText(SlidingConflictActivity.this, "position:"+position, Toast.LENGTH_LONG).show();

		}
	};
	class MyPagerAdaper extends PagerAdapter {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(pages.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(pages.get(position), 0);
			return pages.get(position);
		}

		@Override
		public int getCount() {
			return pages.size();
		}
	}
	class MyPagerAdaperSon extends PagerAdapter {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(pagesSon.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(pagesSon.get(position), 0);
			return pagesSon.get(position);
		}

		@Override
		public int getCount() {
			return pagesSon.size();
		}
	}
}
