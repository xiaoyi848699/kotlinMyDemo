package demo.xy.com.xytdcq.demo1;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import demo.xy.com.xytdcq.demo1.view.ParentViewPager2;
import demo.xy.com.xytdcq.demo1.viewFlow.CircleFlowIndicator;
import demo.xy.com.xytdcq.demo1.viewFlow.ViewFlow;

public class ScrollListViewHintActivity extends Activity {
	private ParentViewPager2 parentViewPager;
	private ChildViewPager viewPagerSon;
	private List<View> pages;
	private List<View> pagesSon;
	private TextView mTextView3;
	private LinearLayout mTopLayout;
	private LinearLayout mTopLayout2;
	private LinearLayout mTopLayout22;
	private LinearLayout BottomLy;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView ptv1;
	private TextView ptv2;
	private TextView ptv3;
	private TextView ptv4;
	private TextView topTitle;
	private MyListView mListView0;
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
	private int lastScroll = -1;
	//	private boolean isVisiable= true;
	private int duration = 1000;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				mListView0.setSelection(msg.arg1);
//				mListView0.setSelectionAfterHeaderView();
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll_list_view_hint);
		getView();
		initHeadView();
		initData();
	}

	private void initHeadView() {
		headView = LayoutInflater.from(ScrollListViewHintActivity.this).inflate(
				R.layout.head_view_flow, null);
		mViewFlow = (ViewFlow) headView.findViewById(R.id.home_viewflow);
		headView2 = LayoutInflater.from(ScrollListViewHintActivity.this).inflate(
				R.layout.head_view_flow, null);
		mViewFlow2 = (ViewFlow) headView2.findViewById(R.id.home_viewflow);
		headView3 = LayoutInflater.from(ScrollListViewHintActivity.this).inflate(
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
		mViewFlow.setAdapter(new AdImageAdapter(ScrollListViewHintActivity.this,
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
		mViewFlow2.setAdapter(new AdImageAdapter(ScrollListViewHintActivity.this,
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
		mViewFlow3.setAdapter(new AdImageAdapter(ScrollListViewHintActivity.this,
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
		LayoutInflater mInflater = LayoutInflater.from(ScrollListViewHintActivity.this);
		View mView0 = mInflater.inflate(R.layout.views_listview_title, null);
		View mView1 = mInflater.inflate(R.layout.views_son, null);
		View mView2 = mInflater.inflate(R.layout.views_listview, null);
		View mView3 = mInflater.inflate(R.layout.item_views1, null);
		initView1(mInflater,mView1);
		mListView0 = (MyListView) mView0.findViewById(R.id.listview);
		View mViewTitle = mInflater.inflate(R.layout.views_title, null);
		topTitle = (TextView) mView0.findViewById(R.id.top_title);
		mListView1 = (MyListView) mView2.findViewById(R.id.listview);
		mTextView3 = (TextView) mView3.findViewById(R.id.textView1);
		creatAdapter();
		mListView0.addHeaderView(mViewTitle);
		mListView0.setAdapter(mAdapter);
		mListView0.setOnItemClickListener(mClickListener);
//		mListView0.setOnScrollListener(mOnScrollListener);
		mListView0.setOnTouchListener(mOnTouchListener);
		mListView1.setAdapter(mAdapter);
		mListView1.setOnItemClickListener(mClickListener);
//		mListView1.addHeaderView(headView);
		mTextView3.setText("333");
		pages.add(mView0);
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
		mAdapter = new SimpleAdapter(ScrollListViewHintActivity.this, data, R.layout.item_views1, new String[]{"str"}, new int[]{R.id.textView1});
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
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
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
		parentViewPager = (ParentViewPager2) findViewById(R.id.viewPager_parent);
		ptv1 = (TextView) findViewById(R.id.ptv1);
		BottomLy = (LinearLayout) findViewById(R.id.Bottom_ly);
		ptv1.setBackgroundColor(getResources().getColor(R.color.color_blue));
		ptv2 = (TextView) findViewById(R.id.ptv2);
		ptv3 = (TextView) findViewById(R.id.ptv3);
		ptv4 = (TextView) findViewById(R.id.ptv4);
		parentViewPager.setOnPageChangeListener(mChangeListener);
	}

	public void btnClick(View v){
		switch (v.getId()) {
			case R.id.ptv1:
				parentViewPager.setCurrentItem(0);
				break;
			case R.id.ptv2:
				parentViewPager.setCurrentItem(1);
//			Toast.makeText(ScrollListViewHintActivity.this, "2", 1).show();
				break;
			case R.id.ptv3:
				parentViewPager.setCurrentItem(2);
//			Toast.makeText(ScrollListViewHintActivity.this, "3", 1).show();
				break;
			case R.id.ptv4:
				parentViewPager.setCurrentItem(3);
//			Toast.makeText(ScrollListViewHintActivity.this, "4", 1).show();
				break;
			case R.id.tv1:
//			Toast.makeText(ScrollListViewHintActivity.this, "1", 1).show();
				viewPagerSon.setCurrentItem(0);
				break;
			case R.id.tv2:
				viewPagerSon.setCurrentItem(1);
//			Toast.makeText(ScrollListViewHintActivity.this, "2", 1).show();
				break;
			case R.id.tv3:
				viewPagerSon.setCurrentItem(2);
//			Toast.makeText(ScrollListViewHintActivity.this, "3", 1).show();
				break;
			case R.id.tv4:
				viewPagerSon.setCurrentItem(3);
//			Toast.makeText(ScrollListViewHintActivity.this, "4", 1).show();
				break;
			case R.id.btn19:
				Intent mIntent = new Intent(ScrollListViewHintActivity.this, PhotoAnimActivity.class);
				startActivity(mIntent);
				break;
			case R.id.btn18:
				break;
			case R.id.btn17:
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
//			if(null != mHandler){
//				mHandler.sendEmptyMessageDelayed(123, 2000);
//			}
			Log.d("TAG", "onPageSelected "+arg0);
			ptv1.setBackgroundColor(getResources().getColor(R.color.transparent));
			ptv2.setBackgroundColor(getResources().getColor(R.color.transparent));
			ptv3.setBackgroundColor(getResources().getColor(R.color.transparent));
			ptv4.setBackgroundColor(getResources().getColor(R.color.transparent));
			parentViewPager.setIndex(arg0);
			switch (arg0) {
				case 0:
					ptv1.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 1:
					if(null != viewPagerSon){
						if(viewPagerSon.getCurrentItem() == 0){
							parentViewPager.setScrollFrist(true);
							parentViewPager.setScrollEnd(false);
						}
					}
					ptv2.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 2:
					ptv3.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 3:
					ptv4.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				default:
					break;
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			Log.d("TAG", "onPageScrolled "+arg0);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			Log.d("TAG", "onPageScrollStateChanged "+arg0);
		}
	};
	private OnPageChangeListener mChangeListener2 = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
//			viewPagerSon.setEnd(false);//内部拦截时用
			parentViewPager.setScrollFrist(false);
			parentViewPager.setScrollEnd(false);
			tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
			tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
			tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
			tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
			switch (arg0) {
				case 0:
					parentViewPager.setScrollFrist(true);
					tv1.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 1:
					tv2.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 2:
					tv3.setBackgroundColor(getResources().getColor(R.color.color_blue));
					break;
				case 3:
//				viewPagerSon.setEnd(true);//内部拦截时用
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

	/**
	 *
	 * @param flag true 不显示到显示
	 */
	private void daelView(boolean flag) {
		mListView0.setCanScrollH(flag);
		if(flag){
			BottomLy.setVisibility(View.VISIBLE);
			//平移Y
			ObjectAnimator translationY = ObjectAnimator.ofFloat(topTitle, "translationY", -200,0);
			translationY.setDuration(duration);
			translationY.start();
			//透明度
			ObjectAnimator alpha = ObjectAnimator.ofFloat(topTitle, "alpha", 0.5f,1);
			alpha.setDuration(duration);
			alpha.start();
			//平移Y
			ObjectAnimator translationY2 = ObjectAnimator.ofFloat(BottomLy, "translationY", 200,0);
			translationY2.setDuration(duration);
			translationY2.start();
			//透明度
			ObjectAnimator alpha2 = ObjectAnimator.ofFloat(BottomLy, "alpha", 0.5f,1);
			alpha2.setDuration(duration);
			alpha2.start();
//			ArrayList<Animator> mAnimators = new ArrayList<Animator>();
//			mAnimators.add(translationY);
//			mAnimators.add(alpha);
//			AnimatorSet mSet = new AnimatorSet();
//			mSet.setDuration(duration);
//			mSet.playTogether(mAnimators);
//			mHandler.sendEmptyMessageDelayed(1, duration);
		}else{
			BottomLy.setVisibility(View.GONE);
			//平移Y
			ObjectAnimator translationY = ObjectAnimator.ofFloat(topTitle, "translationY", 0,-200);
			translationY.setDuration(duration);
			translationY.start();
			//透明度
			ObjectAnimator alpha = ObjectAnimator.ofFloat(topTitle, "alpha", 1,0.5f);
			alpha.setDuration(duration);
			alpha.start();
			//平移Y
			ObjectAnimator translationY2 = ObjectAnimator.ofFloat(BottomLy, "translationY", 0,200);
			translationY2.setDuration(duration);
			translationY2.start();
			//透明度
			ObjectAnimator alpha2 = ObjectAnimator.ofFloat(BottomLy, "alpha", 1,0.5f);
			alpha2.setDuration(duration);
			alpha2.start();
//			ArrayList<Animator> mAnimators = new ArrayList<Animator>();
//			mAnimators.add(translationY);
//			mAnimators.add(alpha);
//			AnimatorSet mSet = new AnimatorSet();
//			mSet.setDuration(duration);
//			mSet.playTogether(mAnimators);
//			mHandler.sendEmptyMessageDelayed(0, duration);
		}

	}
	private float downX = -1;
	private float downY = -1;
	OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = arg1.getX();
					downY = arg1.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					float nowDownX = arg1.getX();
					float nowDownY = arg1.getY();
					float dX = Math.abs(nowDownX-downX);
					float dY = Math.abs(nowDownY-downY);
					//隐藏的时候横着滑动
					if(BottomLy.getVisibility() != View.VISIBLE  && dX > dY && dX>5){
						Log.d("TAG", BottomLy.getVisibility()+"onTouch"+nowDownY+"  "+downY);
//					isVisiable = true;
						downX = arg1.getX();
						downY = arg1.getY();
						//下滑
						daelView(true);
					}else if(BottomLy.getVisibility() != View.VISIBLE  && nowDownY - downY > 30){
						Log.d("TAG", BottomLy.getVisibility()+"onTouch"+nowDownY+"  "+downY);
//					isVisiable = true;
						downX = arg1.getX();
						downY = arg1.getY();
						//下滑
						daelView(true);
					}else if(BottomLy.getVisibility() == View.VISIBLE && nowDownY - downY < -30){
						Log.d("TAG", BottomLy.getVisibility()+"onTouch"+nowDownY+"  "+downY);
//					isVisiable = false;
						downX = arg1.getX();
						downY = arg1.getY();
						//上滑（隐藏）
						daelView(false);
					}
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					downX = arg1.getX();
					downY = arg1.getY();
					break;
				default:
					break;
			}
			return false;
		}
	};
	OnItemClickListener mClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
								long arg3) {
			Toast.makeText(ScrollListViewHintActivity.this, "position:"+position, Toast.LENGTH_LONG).show();

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
	@Override
	public void onBackPressed() {
		if(parentViewPager.getCurrentItem() == 0 && mListView0.getFirstVisiblePosition() > 0){
//			mListView0.setSelection(0);
			mListView0.setSelectionFromTop(mListView0.getFirstVisiblePosition(), 0);
			//下滑
			daelView(true);
			int x = 0;
			int d = duration/mListView0.getFirstVisiblePosition();
			for (int i = mListView0.getFirstVisiblePosition(); i >= 0; i--) {
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				msg.arg1 = i;
				mHandler.sendMessageDelayed(msg, x*d);
				x ++;
			}
//			isVisiable = true;
		}else{
			super.onBackPressed();
		}
	}
}
