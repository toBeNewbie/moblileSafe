package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.nineoldandroids.animation.ObjectAnimator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

public class HomeActivity extends Activity {

	private ImageView logo_home;
	private ImageView setting;
	private GridView gv_tools;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//初始化界面
		initView();
		//给主界面logo设置主界面。
		startAnimationForLogo();
		
		initAnimation();
		
		initDate();
		
		initEvent();
	}
	
	/**
	 * 给主界面logo设置动画。
	 */
	private void startAnimationForLogo() {
		
		ObjectAnimator animator = ObjectAnimator.ofFloat(logo_home, "rotationY", 0,60,120,180,240,300,360);
		animator.setDuration(2000);
		animator.setRepeatCount(ObjectAnimator.INFINITE);
		animator.start();
 
	}

	/**
	 * 初始主化界面
	 */
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_home);
		logo_home = (ImageView) findViewById(R.id.logo);
		setting = (ImageView) findViewById(R.id.setting);
		gv_tools = (GridView) findViewById(R.id.gv_home_tools);
	}

	
	private void initEvent() {
		// TODO Auto-generated method stub
		
	}

	
	private void initDate() {
		// TODO Auto-generated method stub
		
	}

	private void initAnimation() {
		// TODO Auto-generated method stub
		
	}

}
