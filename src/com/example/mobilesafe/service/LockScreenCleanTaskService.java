package com.example.mobilesafe.service;

import java.util.List;

import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.utils.TastInfosUtils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-9-14
 *@des 锁屏清理内存服务类
 */
public class LockScreenCleanTaskService extends Service {

	private CleanTaskReceiver mTaskReceiver;
	private ActivityManager mActivityManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class CleanTaskReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			List<AppInforBean> allRunningInfos = TastInfosUtils.getAllRunningInfos(context);
			for (AppInforBean appInforBean : allRunningInfos) {
				mActivityManager.killBackgroundProcesses(appInforBean.getPackageName());
			}
			
			System.out.println("清理进程..............");
		}
		
	}
	
	@Override
	public void onCreate() {
		System.out.println("锁屏服务开启,,,,,,,,,,,,,,,");
		mTaskReceiver = new CleanTaskReceiver();
		IntentFilter filter=new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mTaskReceiver, filter);
		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		super.onCreate();
		
	}
	
	@Override
	public void onDestroy() {
		System.out.println("锁屏服务关闭.........");
		//取消注册
		unregisterReceiver(mTaskReceiver);
		super.onDestroy();
	}

}
