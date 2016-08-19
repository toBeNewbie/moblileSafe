package com.example.mobilesafe.activity;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.AntiThrefService;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;
import com.example.mobilesafe.utils.ShowToastUtils;


public class PhoneAntiThrefSetFour extends PhoneAntiThrefBaseActivity {
	
	private CheckBox cb_antithref;
	private TextView tv_anti_thref;


	/**
	 * 初始化界面。
	 */
	@Override
	protected void initView() {
		setContentView(R.layout.phone_antithref_guideseting_04);
		cb_antithref = (CheckBox) findViewById(R.id.cb_on_antithref);
		tv_anti_thref = (TextView) findViewById(R.id.tv_anti_thref);
	}

	@Override
	protected void initEvent() {
		
		//给复选框添加点击事件。
		cb_antithref.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			private Intent intent;

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				splashUtils.putBoolean(getApplicationContext(), myConstantValue.dISPLAY_SETTING, isChecked);
				if (isChecked) {
					tv_anti_thref.setText("防盗服务已经开启");
					
					//启动服务。
					intent = new Intent(PhoneAntiThrefSetFour.this, AntiThrefService.class);
					startService(intent);
					
					
				}else {
					tv_anti_thref.setText("防盗服务已经关闭");
					stopService(intent);
					//关闭服务。
					
				}
			}
		});
		super.initEvent();
	}
	
	
	@Override
	protected void initData() {
		if (AntiThrefServiceUtils.serviceRunning(getApplicationContext(), "com.example.mobilesafe.service.AntiThrefService")) {
			//服务正在运行。
			cb_antithref.setChecked(true);
		}else {
			//服务关闭运行。
			cb_antithref.setChecked(false);
		}
	}
	
	
	/**
	 * 跳转到下一个界面。
	 */
	@Override
	
	public void startNext() {
		
		if (splashUtils.getbBoolean(getApplicationContext(), myConstantValue.dISPLAY_SETTING, false)) {
			
			startPageType(PhoneAntiTheft.class);
		}else {
			ShowToastUtils.showToast("必须开启防盗保护", this);
		}
	}

	
	/**
	 * 跳转到上一个界面。
	 */
	@Override
	protected void startPre() {
		// TODO Auto-generated method stub
		startPageType(PhoneAntiThrefSetThree.class);
	}
}
