package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ShowToastUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-10-9
 *@des 输入解锁密码的界面
 */
public class EnterPasswordActivity extends Activity {
	private ImageView iv_appIcon;
	private EditText et_password;
	private Button bt_enterApp;
	private String packageName;
	private HomeReceiver mHomeReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
		initData();
		
		initHomeReceiver();
	}

	private class HomeReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().contains(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				//进入主界面
				enterMainActivity();
			}
		}
		
	}
	private void initHomeReceiver() {
		
		mHomeReceiver = new HomeReceiver();
		IntentFilter filter=new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		registerReceiver(mHomeReceiver, filter);
	
	}

	//监听返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			//进入主界面
			enterMainActivity();
		}
		return super.onKeyDown(keyCode, event);
		
	}
	
	private void enterMainActivity() {

		/**
		 *  <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.MONKEY"/>
            </intent-filter>
		 */
		
		Intent mainIntent=new Intent("android.intent.action.MAIN");
		mainIntent.addCategory("android.intent.category.HOME");
		mainIntent.addCategory("android.intent.category.DEFAULT");
		mainIntent.addCategory("android.intent.category.MONKEY");
		//进入主界面
		startActivity(mainIntent);
		
		//关闭自己
		finish();
	}

	private void initData() {
		packageName = getIntent().getStringExtra("packageName");
		PackageManager mPackageManager = getPackageManager();
		try {
			//根据包名获取应用程序的图标
			iv_appIcon.setImageDrawable(mPackageManager.getApplicationIcon(packageName));
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mHomeReceiver);
		super.onDestroy();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_enter_password);
		
		iv_appIcon = (ImageView) findViewById(R.id.iv_locked_app_icon);
		et_password = (EditText) findViewById(R.id.et_locked_app_password);
	}
	
	
	//点击确定进入加锁app
	public void enterLockedApp(View view){
		String password = et_password.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			ShowToastUtils.showToast("密码不能为空", this);
			return;
		}
		
		if (password.equals("123")) {
			
			//发送广播
			Intent intent=new Intent("newbie.com.cn");
			intent.putExtra("master", packageName);
			sendBroadcast(intent);
			
			//关闭自己
			finish();
			
		}else {
			ShowToastUtils.showToast("密码错误", this);
		}
	}
}
