package com.example.mobilesafe.service;

import com.example.mobilesafe.activity.EnterPasswordActivity;
import com.example.mobilesafe.dao.LockedDataDao;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.accessibility.AccessibilityEvent;

/**
 * 
 * @author Administrator
 * @company Newbie
 * @date 2016-10-9
 * @des 	辅助功能电子狗服务
 */
public class MyAccessibilityService extends AccessibilityService {

	private LockedDataDao mLockedDataDao;
	private MasterAppReceiver masterAppReceiver;
	private String masterApp;

	private class MasterAppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//获得应用包名
			masterApp = intent.getStringExtra("master");
		}

	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

		if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

			String packageName = event.getPackageName().toString();
			if (mLockedDataDao.appIfLocked(packageName)) {
				// 程序加锁，阻止
				if (packageName.equals(masterApp)) {
					// 放行

				} else {

					Intent passwordiIntent = new Intent(
							getApplicationContext(),
							EnterPasswordActivity.class);
					passwordiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					passwordiIntent.putExtra("packageName", packageName);

					startActivity(passwordiIntent);
				}
			} else {
				// 程序未加锁，放行
			}
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mLockedDataDao = new LockedDataDao(getApplicationContext());
		masterAppReceiver = new MasterAppReceiver();
		IntentFilter filter = new IntentFilter("newbie.com.cn");
		// 注册广播
		registerReceiver(masterAppReceiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 取消注册
		unregisterReceiver(masterAppReceiver);
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

}
