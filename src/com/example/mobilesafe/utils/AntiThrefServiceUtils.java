package com.example.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-19
 *@des 监听系统正在运行的服务的工具类。
 */
public class AntiThrefServiceUtils {
	
	private static boolean condition;

	public static boolean serviceRunning(Context context,String serviceName){
		
		ActivityManager serviceManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceInfos = serviceManager.getRunningServices(50);
		for (RunningServiceInfo runningServiceInfo : serviceInfos) {
			condition = runningServiceInfo.service.getClassName().equals(serviceName);
			if (condition) {
				break;
			}
		}
		return condition;
	}
}
