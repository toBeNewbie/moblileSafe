package com.example.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.mobilesafe.activity.EnterPasswordActivity;
import com.example.mobilesafe.dao.LockedDataDao;

public class WatchDogThreadService extends Service {

	
	private LockedDataDao mLockedDataDao;
	private MasterAppReceiver masterAppReceiver;
	private String masterApp;
	private boolean isRunning=false;
	private ActivityManager mAm;
	private List<String> allLockedAppPackageNames;

	private class MasterAppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			masterApp = intent.getStringExtra("master");
		}

	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
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
		
		mAm = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		
		allLockedAppPackageNames = mLockedDataDao.getAllLockedApp();
		
		//监控线程
		startDogThreadService();
		
		
	}

	private void startDogThreadService() {
		// TODO Auto-generated method stub
		new Thread(){
			private List<RunningTaskInfo> runningTasks;
			private RunningTaskInfo taskInfo;
			private String packageName;

			public void run() {
				isRunning=true;
				while (isRunning) {
					runningTasks = mAm.getRunningTasks(1);
					taskInfo = runningTasks.get(0);
					
					packageName = taskInfo.topActivity.getPackageName();
					
					if (allLockedAppPackageNames.contains(packageName)){//mLockedDataDao.appIfLocked(packageName)) {
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
					SystemClock.sleep(200);
				}
			};
		}.start();
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 取消注册
		unregisterReceiver(masterAppReceiver);
		isRunning=false;
	}

}
