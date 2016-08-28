package com.example.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BlacklistInterceptService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// 开启服务。
		System.out.println("服务已经开启.............");
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// 销毁服务
		System.out.println("服务已经销毁...............");
		super.onDestroy();
	}

}
