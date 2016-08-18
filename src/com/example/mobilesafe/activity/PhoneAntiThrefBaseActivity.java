package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class PhoneAntiThrefBaseActivity extends Activity {

	private GestureDetectorCompat mGDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();

		initEvent();
		
		initData();
		
		initGesture();
	}
	
	

	/**
	 * 初始化界面。
	 */
	protected void initView() {
		
	}

	public void startPageType(Class type){
		Intent intent = new Intent(this, type);
		startActivity(intent);
		
		finish();
	}
	
	protected abstract void startNext();
	protected abstract void startPre();
	/**
	 * 跳转到下一个页面。
	 * @param view
	 */
	public void nextAntiThrefPage(View view){
		
		//跳转到下一页面。
		startNext();
		//跳转到下一页面动画。
		nextPageAnimation();
	}
	
	
    public void preAntiThrefPage(View view){
    	
    	startPre();
    	
    	prePageAnimation();
    }
	
	
    /**
     * 跳转到上一页面的动画。
     */
    public void prePageAnimation(){
    	overridePendingTransition(R.anim.pre_enter_animation, R.anim.pre_exit_animation);
    }
    
	/**
	 * 跳转页面时的动画。
	 */
	private void nextPageAnimation() {
		overridePendingTransition(R.anim.next_enter_animation, R.anim.next_exit_animation);
	}

	/**
	 * 完成数据的显示。
	 */
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 初始化事件。
	 */
	protected void initEvent() {

	}
	
	/**
	 * 初始化手势监听器。
	 */
	private void initGesture() {
		mGDetector = new GestureDetectorCompat(this, new MyGestureListener());
	}
	
	/**
	 * 
	 * @author Administrator
	 *@company Newbie
	 *@date 2016-8-18
	 *@des 手势监听器。
	 */
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			if (Math.abs(e1.getX()-e2.getX())>Math.abs(e1.getY()-e2.getY())) {
				//x轴滑动。判断滑动速度。
				if (Math.abs(velocityX)>50) {
					//判断向左滑动，还是向右滑动。
					if (velocityX>0) {
						preAntiThrefPage(null);
					}else {
						nextAntiThrefPage(null);
					}
					
				}
				
			}else {
				//y轴滑动。
			}
			
			return true;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mGDetector!=null) {
			mGDetector.onTouchEvent(event);
			//消费掉事件。
			return true;
		}
		return super.onTouchEvent(event);
	}

}
