package demo.xy.com.xytdcq.demo1;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import demo.xy.com.xytdcq.R;

/**
 * 照片动画
 */
public class PhotoAnimActivity extends Activity {

	private ImageView iv1;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView iv4;
	private ImageView iv5;
	private ImageView iv6;
	private  Animation animation1 ;
	private  Animation animation2 ;
	private  Animation animation3 ;
	private  Animation animation4 ;
	private  Animation animation5 ;
	private  Animation animation6 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv4 = (ImageView) findViewById(R.id.iv4);
		iv5 = (ImageView) findViewById(R.id.iv5);
		iv6 = (ImageView) findViewById(R.id.iv6);
		
		
	}
	private void startXmlAnim() {
		// 加载动画 
		Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.animation1); 
        animation2 = AnimationUtils.loadAnimation(this, R.anim.animation2); 
        animation3 = AnimationUtils.loadAnimation(this, R.anim.animation3); 
        animation4 = AnimationUtils.loadAnimation(this, R.anim.animation4); 
        animation5 = AnimationUtils.loadAnimation(this, R.anim.animation5); 
        animation6 = AnimationUtils.loadAnimation(this, R.anim.animation6); 
        animation1.setFillAfter(true);//停留在动画结束出，在xml中设置无效
        animation2.setFillAfter(true);//停留在动画结束出，在xml中设置无效
        animation3.setFillAfter(true);//停留在动画结束出，在xml中设置无效
        animation4.setFillAfter(true);//停留在动画结束出，在xml中设置无效
        animation5.setFillAfter(true);//停留在动画结束出，在xml中设置无效
        animation6.setFillAfter(true);//停留在动画结束出，在xml中设置无效
        // 动画开始 
        this.iv1.startAnimation(animation1); 
        
        animation1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				//启动第二个
				iv2.setVisibility(View.VISIBLE);
				iv2.startAnimation(animation2); 
			}
		});
        animation2.setAnimationListener(new AnimationListener() {
        	@Override
        	public void onAnimationStart(Animation arg0) {
        	}
        	
        	@Override
        	public void onAnimationRepeat(Animation arg0) {
        	}
        	
        	@Override
        	public void onAnimationEnd(Animation arg0) {
        		//启动第二个
        		iv3.setVisibility(View.VISIBLE);
        		iv3.startAnimation(animation3); 
        	}
        });
        animation3.setAnimationListener(new AnimationListener() {
        	@Override
        	public void onAnimationStart(Animation arg0) {
        	}
        	
        	@Override
        	public void onAnimationRepeat(Animation arg0) {
        	}
        	
        	@Override
        	public void onAnimationEnd(Animation arg0) {
        		//启动第二个
        		iv4.setVisibility(View.VISIBLE);
        		iv4.startAnimation(animation4); 
        	}
        });
        animation4.setAnimationListener(new AnimationListener() {
        	@Override
        	public void onAnimationStart(Animation arg0) {
        	}
        	
        	@Override
        	public void onAnimationRepeat(Animation arg0) {
        	}
        	
        	@Override
        	public void onAnimationEnd(Animation arg0) {
        		//启动第二个
        		iv5.setVisibility(View.VISIBLE);
        		iv5.startAnimation(animation5); 
        	}
        });
        animation5.setAnimationListener(new AnimationListener() {
        	@Override
        	public void onAnimationStart(Animation arg0) {
        	}
        	
        	@Override
        	public void onAnimationRepeat(Animation arg0) {
        	}
        	
        	@Override
        	public void onAnimationEnd(Animation arg0) {
        		//启动第二个
        		iv6.setVisibility(View.VISIBLE);
        		iv6.startAnimation(animation6); 
        	}
        });
		
	}
	private void startCodeAnim(){
		AnimationSet animationSet = new AnimationSet(true);
		//图片透明度由0变为1，也就是由不可见到可见
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
//		alphaAnimation.setStartOffset(10000);
		//scale缩放比从2倍到1（原始大小）
		ScaleAnimation scale = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(1000);//动画播放间隔
		animationSet.addAnimation(scale);
		//rotate反方向旋转20度
		RotateAnimation rotate =new RotateAnimation(0f,-20f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
		rotate.setDuration(1000);
		animationSet.addAnimation(rotate);
		//translate从原始位置向x和y的正方向移动原始图片大小的10%距离
		TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.1f);
		translate.setDuration(1300);
		animationSet.addAnimation(translate);
		//animationSet.setStartOffset(10000);
//		animationSet.setFillBefore(false);
		//设置不动画播放完后不回到原始位置
		animationSet.setFillAfter(true);
		//添加并播放动画
		iv1.startAnimation(animationSet);
	}
	private void startCodeAnim2(){
		//透明度
		ObjectAnimator alpha = ObjectAnimator.ofFloat(iv1, "alpha", 0,1,1);
		int duration = 5000;
		alpha.setDuration(duration);
		alpha.start();
		//旋转2
		ObjectAnimator rotation2 = ObjectAnimator.ofFloat(iv1, "rotationX", 0,180,0);
		rotation2.setDuration(duration);
		rotation2.start();
		//旋转3
		ObjectAnimator rotation3 = ObjectAnimator.ofFloat(iv1, "rotationY", 0,180,0);
		rotation3.setDuration(duration);
		rotation3.start();
		//平移X
		ObjectAnimator translationX = ObjectAnimator.ofFloat(iv1, "translationX", 0,180,0);
		translationX.setDuration(duration);
		translationX.start();
		//平移Y
		ObjectAnimator translationY = ObjectAnimator.ofFloat(iv1, "translationY", 0,180,0);
		translationY.setDuration(duration);
		translationY.start();
		//缩放X
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv1, "scaleX", 2,1,1);
		scaleX.setDuration(duration);
		scaleX.start();
		//缩放Y
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv1, "scaleY", 2,1,1);
		scaleY.setDuration(duration);
		scaleY.start();
	}

	public void btnClick(View v){
		initAnim();
		iv1.setVisibility(View.VISIBLE);
		if(v.getId() == R.id.buttonCode){
			startCodeAnim();
		}else if(v.getId() == R.id.buttonXml){
			startXmlAnim();
		}else if(v.getId() == R.id.buttonCode2){
			startCodeAnim2();
		}
		
	}
	private void initAnim() {
		iv1.clearAnimation();
		iv2.clearAnimation();
		iv3.clearAnimation();
		iv4.clearAnimation();
		iv5.clearAnimation();
		iv6.clearAnimation();
		iv1.setVisibility(View.GONE);
		iv2.setVisibility(View.GONE);
		iv3.setVisibility(View.GONE);
		iv4.setVisibility(View.GONE);
		iv5.setVisibility(View.GONE);
		iv6.setVisibility(View.GONE);
	}
	

}
