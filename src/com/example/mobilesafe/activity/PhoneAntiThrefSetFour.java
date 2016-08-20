package com.example.mobilesafe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.receiver.MyDeviceAdminReceiver;
import com.example.mobilesafe.service.AntiThrefService;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;
import com.example.mobilesafe.utils.ShowToastUtils;


public class PhoneAntiThrefSetFour extends PhoneAntiThrefBaseActivity {
	
	private CheckBox cb_antithref;
	private TextView tv_anti_thref;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;


	/**
	 * 初始化界面。
	 */
	@Override
	protected void initView() {
		setContentView(R.layout.phone_antithref_guideseting_04);
		cb_antithref = (CheckBox) findViewById(R.id.cb_on_antithref);
		tv_anti_thref = (TextView) findViewById(R.id.tv_anti_thref);
		
		mDeviceAdminSample = new ComponentName(this, MyDeviceAdminReceiver.class);
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
	}

	@Override
	protected void initEvent() {
		
		//给复选框添加点击事件。
		cb_antithref.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				splashUtils.putBoolean(getApplicationContext(), myConstantValue.dISPLAY_SETTING, isChecked);
				if (isChecked) {
					
					//判断是否激活设备管理器界面。
					if (!mDPM.isAdminActive(mDeviceAdminSample)) {
						
						 activeDevices();
					}else {
						
						//启动服务。
						Intent intent = new Intent(PhoneAntiThrefSetFour.this, AntiThrefService.class);
						startService(intent);
						tv_anti_thref.setText("防盗服务已经开启");
					}
					
					
					
				}else {
					Intent intent = new Intent(PhoneAntiThrefSetFour.this, AntiThrefService.class);
					startService(intent);
					tv_anti_thref.setText("防盗服务已经关闭");
					stopService(intent);
					//关闭服务。
					
				}
			}

			public void activeDevices() {
				// Launch the activity to have the user enable our admin.
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
				intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				       "激活设备管理器");
				startActivityForResult(intent, 1);
			}
		});
		super.initEvent();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//根据是否开启设备管理器选择是否开启服务。
		if (mDPM.isAdminActive(mDeviceAdminSample)) {
			tv_anti_thref.setText("防盗服务已经开启");

			//启动服务。
			Intent intent = new Intent(PhoneAntiThrefSetFour.this, AntiThrefService.class);
			startService(intent);
			cb_antithref.setChecked(true);
		}else {
			//取消复选框。
			cb_antithref.setChecked(false);
			ShowToastUtils.showToast("你需要先激活设备管理器，才能启动防盗服务", this);
		}
		super.onActivityResult(requestCode, resultCode, data);
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
			splashUtils.putBoolean(getApplicationContext(), myConstantValue.BOOT_COMPLETE,true);

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
