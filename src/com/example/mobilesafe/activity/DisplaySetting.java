package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.BlacklistInterceptService;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;
/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-28
 *@des 显示设置中心的界面。
 */
public class DisplaySetting extends Activity {
	private RelativeLayout rlAutoUpdate;
	private RelativeLayout rlBlacklistIntercept;
	private ImageView ivAutoUpdate;
	private ImageView ivBlackView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		initView();
		initDate();
		initEvent();
	}

	private void initDate() {
		
		//初始化黑名单电话拦截服务。
		{
			if (AntiThrefServiceUtils.serviceRunning(getApplicationContext(), "com.example.mobilesafe.service.BlacklistInterceptService")) {
				
				ivBlackView.setImageResource(R.drawable.on);
			}else {
				ivBlackView.setImageResource(R.drawable.off);
			}
		}
		
		//初始化自动更新代码块。
		{
			Boolean autoUpdate = splashUtils.getbBoolean(getApplicationContext(), myConstantValue.AUTO_VERSION_UPDATE, false);
			 
			
			if (autoUpdate) {
				ivAutoUpdate.setImageResource(R.drawable.on);
			}else {
				ivAutoUpdate.setImageResource(R.drawable.off);
			}
		}
		
	}

	private void initEvent() {
		
		//给自动更新布局添加点击事件。
		rlAutoUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				 
				Boolean autoUpdate = splashUtils.getbBoolean(getApplicationContext(), myConstantValue.AUTO_VERSION_UPDATE, false);
						 
				splashUtils.putBoolean(getApplicationContext(),myConstantValue.AUTO_VERSION_UPDATE, !autoUpdate);
				
				if (!autoUpdate) {
					ivAutoUpdate.setImageResource(R.drawable.on);
				}else {
					ivAutoUpdate.setImageResource(R.drawable.off);
				}
			}
		});
		
		
		//给黑名单拦截服务添加数据。
		rlBlacklistIntercept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (AntiThrefServiceUtils.serviceRunning(getApplicationContext(), "com.example.mobilesafe.service.BlacklistInterceptService")) {
					
					Intent intent=new Intent(DisplaySetting.this, BlacklistInterceptService.class);
					stopService(intent);
					ivBlackView.setImageResource(R.drawable.off);
				}else {
					Intent intent=new Intent(DisplaySetting.this, BlacklistInterceptService.class);
					startService(intent);
					ivBlackView.setImageResource(R.drawable.on);
				}
				
			}
		});
		
	}

	private void initView() {
		setContentView(R.layout.activity_setting_display);
		
		rlAutoUpdate = (RelativeLayout) findViewById(R.id.rl_setting_auto_update_intercept);
		rlBlacklistIntercept = (RelativeLayout) findViewById(R.id.rl_setting_blacklist_intercept);
	
		ivAutoUpdate = (ImageView) findViewById(R.id.iv_autoupdate_display);
		ivBlackView = (ImageView) findViewById(R.id.iv_blacklist_intercept);
	}
}
