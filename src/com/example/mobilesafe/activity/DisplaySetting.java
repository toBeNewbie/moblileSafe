package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mobilesafe.R;
import com.example.mobilesafe.difineView.SettingCustomView;
import com.example.mobilesafe.difineView.SettingCustomView.onToggleChangeListener;
import com.example.mobilesafe.service.BlacklistInterceptService;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;

/**
 * 
 * @author Administrator
 * @company Newbie
 * @date 2016-8-28
 * @des 显示设置中心的界面。
 */
public class DisplaySetting extends Activity {
	private SettingCustomView customView_auto_update;
	private SettingCustomView customView_blacklist_intercept;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initView();
		initDate();
		initEvent();
	}

	private void initDate() {
		
		
		// 初始化自动更新代码块。
		customView_auto_update.setToggleOn(splashUtils.getbBoolean(
				getApplicationContext(),
				myConstantValue.AUTO_VERSION_UPDATE, false));
		
		// 初始化黑名单电话拦截服务。
		customView_blacklist_intercept.setToggleOn(AntiThrefServiceUtils.serviceRunning(getApplicationContext(),
				"com.example.mobilesafe.service.BlacklistInterceptService"));
		
		
	}
		
		

		

	private void initEvent() {
		
		onToggleChangeListener toggleChangeListener=new onToggleChangeListener() {
			
			@Override
			public void toggleChange(View view, boolean isOpen) {
				switch (view.getId()) {
				case R.id.custom_view_autoupdate:
				{
					splashUtils.putBoolean(getApplicationContext(),
							myConstantValue.AUTO_VERSION_UPDATE, isOpen);
					
				}
					break;

				case R.id.custom_view_blacklist_intercept:
				{
					
					if (isOpen) {
						
						Intent intent = new Intent(DisplaySetting.this,
								BlacklistInterceptService.class);
						startService(intent);
						
					} else {
						
						Intent intent = new Intent(DisplaySetting.this,
								BlacklistInterceptService.class);
						stopService(intent);
						
					}
				}
					break;
				default:
					break;
				}
			}
		};
		
		customView_auto_update.setOnToggleListener(toggleChangeListener);
		customView_blacklist_intercept.setOnToggleListener(toggleChangeListener);

		
	}

	private void initView() {
		setContentView(R.layout.activity_setting_display);

		customView_auto_update = (SettingCustomView) findViewById(R.id.custom_view_autoupdate);
		customView_blacklist_intercept = (SettingCustomView) findViewById(R.id.custom_view_blacklist_intercept);
	}
}
