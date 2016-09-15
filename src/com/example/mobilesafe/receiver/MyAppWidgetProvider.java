package com.example.mobilesafe.receiver;

import com.example.mobilesafe.service.CleanWidgetService;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {
		Intent serviceIntent=new Intent(context, CleanWidgetService.class);
		context.startService(serviceIntent);
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		Intent serviceIntent=new Intent(context, CleanWidgetService.class);
		context.stopService(serviceIntent);
		super.onDisabled(context);
	}

}
