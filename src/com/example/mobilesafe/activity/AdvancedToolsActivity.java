package com.example.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mobilesafe.R;
import com.example.mobilesafe.difineView.SettingCustomView;
import com.example.mobilesafe.difineView.SettingCustomView.onToggleChangeListener;
import com.example.mobilesafe.service.WatchDogThreadService;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;
import com.example.mobilesafe.utils.SmsBackupAndReduction;
import com.example.mobilesafe.utils.SmsBackupAndReduction.SmsBackupReductionListener;
import com.example.mobilesafe.utils.TastInfosUtils;

public class AdvancedToolsActivity extends Activity {

	private SettingCustomView customPhoneLocation;
	private SettingCustomView customPhoneService;
	private SettingCustomView smsReduction;
	private SettingCustomView smsBackup;
	private ProgressBar pbSmsProgress;
	private SettingCustomView sci_appLocked;
	private SettingCustomView sci_doorDog_fir;
	private SettingCustomView sci_doorDog_sec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
		initEvent();
		
		initData();
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		sci_doorDog_fir.setToggleOn(AntiThrefServiceUtils.serviceRunning(getApplicationContext(),
									"com.example.mobilesafe.service.MyAccessibilityService"));
		
		
		sci_doorDog_sec.setToggleOn(AntiThrefServiceUtils.serviceRunning(getApplicationContext(),
							"com.example.mobilesafe.service.WatchDogThreadService"));
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
					
				case R.id.custom_advanced_tools_sms_backup://短信备份。
					System.out.println("短信备份》。。。。。。。。。");
					backupSms();
					break;
					
				case R.id.custom_advanced_tools_sms_reduction://短信还原。
					System.out.println("短信还原》。。。。。。。。。");
					reductionSms();

					break;
					
				case R.id.custom_advanced_tools_app_locked://程序锁服务
					
					Intent applockedIntent=new Intent(AdvancedToolsActivity.this, AppLockedActivity.class);
					startActivity(applockedIntent);
					
					break;
					
				case R.id.custom_advanced_tools_dag_door_first://看门狗服务（1）
					break;
					
				case R.id.custom_advanced_tools_dag_door_second://看门狗服务（2）
					if (isOpen) {
						//开启服务
						Intent watchDog=new Intent(getApplicationContext(), WatchDogThreadService.class);
						startService(watchDog);
					}else {
						//关闭服务
						Intent watchDog=new Intent(getApplicationContext(), WatchDogThreadService.class);
						stopService(watchDog);
					}
					break;
				default:
					break;
				}
			}

			
			 
		};
		
		customPhoneLocation.setOnToggleListener(toggleChangeListener);
		customPhoneService.setOnToggleListener(toggleChangeListener);
		smsBackup.setOnToggleListener(toggleChangeListener);
		smsReduction.setOnToggleListener(toggleChangeListener);
		
		sci_appLocked.setOnToggleListener(toggleChangeListener);
		sci_doorDog_fir.setOnToggleListener(toggleChangeListener);
		sci_doorDog_sec.setOnToggleListener(toggleChangeListener);
		
	}

	//还原短信。
	protected void reductionSms() {
		final ProgressDialog progressDialog=new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		SmsBackupAndReduction.reductionSms(this, new SmsBackupReductionListener() {
			
			@Override
			public void show() {
				// TODO Auto-generated method stub
				progressDialog.show();
				
			}
			
			@Override
			public void setProgress(int currentProgress) {
				// TODO Auto-generated method stub
				progressDialog.setProgress(currentProgress);
				
			}
			
			@Override
			public void setMax(int max) {
				// TODO Auto-generated method stub
				progressDialog.setMax(max);
				
			}
			
			@Override
			public void dismiss() {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				
			}
		});
	}

	//短信备份。
	protected void backupSms() {

		final ProgressDialog progressDialog = new ProgressDialog(this);
		//设置水平进度条。
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		SmsBackupAndReduction.backupSms(this,new SmsBackupReductionListener() {
			
			@Override
			public void show() {
				// TODO Auto-generated method stub
				progressDialog.show();
			}
			
			@Override
			public void setProgress(int currentProgress) {
				// TODO Auto-generated method stub
				progressDialog.setProgress(currentProgress);
				
			}
			
			@Override
			public void setMax(int max) {
				// TODO Auto-generated method stub
				progressDialog.setMax(max);
			}
			
			@Override
			public void dismiss() {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
			}
		});
	}

	private void initView() {
		setContentView(R.layout.activity_advanced_tools);
		
		customPhoneLocation = (SettingCustomView) findViewById(R.id.custom_advanced_tools_phonenumber_location);
	
		customPhoneService = (SettingCustomView) findViewById(R.id.custom_advanced_tools_phoneservice_location);
	
		smsBackup = (SettingCustomView) findViewById(R.id.custom_advanced_tools_sms_backup);
	
		smsReduction = (SettingCustomView) findViewById(R.id.custom_advanced_tools_sms_reduction);
		
		
		//程序锁条目
		
		sci_appLocked = (SettingCustomView) findViewById(R.id.custom_advanced_tools_app_locked);
		
		//看门狗服务组件
		
		sci_doorDog_fir = (SettingCustomView) findViewById(R.id.custom_advanced_tools_dag_door_first);
		sci_doorDog_sec = (SettingCustomView) findViewById(R.id.custom_advanced_tools_dag_door_second);
	}
}
