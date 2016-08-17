package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public abstract class PhoneAntiThrefBaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();

		initEvent();
		
		initData();
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
	
	/**
	 * 跳转页面时的动画。
	 */
	private void nextPageAnimation() {
		// TODO Auto-generated method stub
		
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

}
