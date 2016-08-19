package com.example.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-19
 *@des 手机防盗的服务。
 */
public class AntiThrefService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d("SERVICE", "防盗服务已经开启");
		super.onCreate();
	}
	
	
	@Override
	public void onDestroy() {
		Log.d("SERVICE", "防盗服务已经关闭");
		super.onDestroy();
	}

}
