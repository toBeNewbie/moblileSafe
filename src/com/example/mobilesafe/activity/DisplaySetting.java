package com.example.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobilesafe.R;
import com.example.mobilesafe.difineView.CustomDialogView;
import com.example.mobilesafe.difineView.SettingCustomView;
import com.example.mobilesafe.difineView.SettingCustomView.onToggleChangeListener;
import com.example.mobilesafe.service.BlacklistInterceptService;
import com.example.mobilesafe.service.ShowIncomingPhoneLocation;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;
import com.example.mobilesafe.utils.ShowToastUtils;

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
	private SettingCustomView coutomView_phone_location_display;
	private SettingCustomView customView_phone_display_style;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initView();
		initDate();
		initEvent();
	}

	private void initDate() {
		
		//初始化电话归属地样式
		customView_phone_display_style.setText("归属地显示样式("+CustomDialogView.locationStyleNames[splashUtils.getInt(getApplicationContext(), myConstantValue.PHONE_LOCATION_STYLE_INDEX, 0)]+")");
		

		// 初始化自动更新代码块。
		customView_auto_update.setToggleOn(splashUtils.getbBoolean(
				getApplicationContext(), myConstantValue.AUTO_VERSION_UPDATE,
				false));

		// 初始化黑名单电话拦截服务。
		customView_blacklist_intercept
				.setToggleOn(AntiThrefServiceUtils
						.serviceRunning(getApplicationContext(),
								"com.example.mobilesafe.service.BlacklistInterceptService"));

		// 初始化归属地显示服务。
		coutomView_phone_location_display
				.setToggleOn(AntiThrefServiceUtils
						.serviceRunning(getApplicationContext(),
								"com.example.mobilesafe.service.ShowIncomingPhoneLocation"));

	}

	private void initEvent() {

		onToggleChangeListener toggleChangeListener = new onToggleChangeListener() {

			@Override
			public void toggleChange(View view, boolean isOpen) {
				switch (view.getId()) {

				// 是否启动自动更新
				case R.id.custom_view_autoupdate: {
					splashUtils.putBoolean(getApplicationContext(),
							myConstantValue.AUTO_VERSION_UPDATE, isOpen);

				}
					break;

				// 是否启动黑名单拦截服务。
				case R.id.custom_view_blacklist_intercept: {

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
				// 开启电话归属地显示服务。
				case R.id.custom_phone_number_display_location_service: {
					if (isOpen) {

						Intent showIncoming = new Intent(DisplaySetting.this,
								ShowIncomingPhoneLocation.class);
						startService(showIncoming);

					} else {

						Intent showIncoming = new Intent(DisplaySetting.this,
								ShowIncomingPhoneLocation.class);
						stopService(showIncoming);

					}
				}
					break;
					
				case R.id.custom_phone_location_display_style://设置归属地显示样式。
				{
					CustomDialogView dialogView= new CustomDialogView(DisplaySetting.this,customView_phone_display_style);
					dialogView.show();
				}
					break;
				default:
					break;
				}
			}
		};

		customView_auto_update.setOnToggleListener(toggleChangeListener);
		customView_blacklist_intercept
				.setOnToggleListener(toggleChangeListener);
		coutomView_phone_location_display
				.setOnToggleListener(toggleChangeListener);
		
		customView_phone_display_style.setOnToggleListener(toggleChangeListener);

	}

	private void initView() {
		setContentView(R.layout.activity_setting_display);

		customView_auto_update = (SettingCustomView) findViewById(R.id.custom_view_autoupdate);
		customView_blacklist_intercept = (SettingCustomView) findViewById(R.id.custom_view_blacklist_intercept);

		coutomView_phone_location_display = (SettingCustomView) findViewById(R.id.custom_phone_number_display_location_service);

		customView_phone_display_style = (SettingCustomView) findViewById(R.id.custom_phone_location_display_style);
	
	}
}
