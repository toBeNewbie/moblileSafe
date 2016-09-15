package com.example.mobilesafe.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.utils.TastInfosUtils;

public class CleanTaskReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<AppInforBean> allRunningInfos = TastInfosUtils.getAllRunningInfos(context);
		for (AppInforBean appInforBean : allRunningInfos) {
			mActivityManager.killBackgroundProcesses(appInforBean.getPackageName());
		}
		
	}

}
