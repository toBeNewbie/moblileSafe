package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.difineView.SettingCustomView;
import com.example.mobilesafe.difineView.SettingCustomView.onToggleChangeListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdvancedToolsActivity extends Activity {

	private SettingCustomView customPhoneLocation;
	private SettingCustomView customPhoneService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
		initEvent();
		
	}

	private void initEvent() {
		 onToggleChangeListener toggleChangeListener=new onToggleChangeListener() {
			
			@Override
			public void toggleChange(View view, boolean isOpen) {
				switch (view.getId()) {
				//电话归属地查询。
				case R.id.custom_advanced_tools_phonenumber_location:
					Intent phoneLocation=new Intent(AdvancedToolsActivity.this, PhoneLocationQuery.class);
					startActivity(phoneLocation);
					break;
				//服务电话归属地查询。
				case R.id.custom_advanced_tools_phoneservice_location:
					Intent Location=new Intent(AdvancedToolsActivity.this, PhoneServiceNumber.class);
					startActivity(Location);
					break;
				default:
					break;
				}
			}
		};
		
		customPhoneLocation.setOnToggleListener(toggleChangeListener);
		customPhoneService.setOnToggleListener(toggleChangeListener);
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_advanced_tools);
		customPhoneLocation = (SettingCustomView) findViewById(R.id.custom_advanced_tools_phonenumber_location);
	
		customPhoneService = (SettingCustomView) findViewById(R.id.custom_advanced_tools_phoneservice_location);
	}
}
