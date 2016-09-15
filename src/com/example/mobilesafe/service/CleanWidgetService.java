package com.example.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.mobilesafe.R;
import com.example.mobilesafe.receiver.MyAppWidgetProvider;
import com.example.mobilesafe.utils.TastInfosUtils;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-9-15
 *@des 清理进程的widget服务
 */
public class CleanWidgetService extends Service {

	private AppWidgetManager myWidgetManager;
	private Timer timer;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void onCreate() {

		myWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		timer = new Timer();
		
		TimerTask task =new TimerTask() {
			
			@Override
			public void run() {
				updateWidgetUI();
			}
		};
		timer.schedule(task , 0, 1000*2);
		super.onCreate();
		
	}
	
	protected void updateWidgetUI() {
		ComponentName provider=new ComponentName(getApplicationContext(), MyAppWidgetProvider.class);
		RemoteViews views=new RemoteViews(getPackageName(), R.layout.process_widget);
		views.setTextViewText(R.id.tv_process_count, "运行中的软件："+TastInfosUtils.getAllRunningInfos(getApplicationContext()).size());
		views.setTextViewText(R.id.tv_process_memory, "可用内存："+Formatter.formatFileSize(getApplicationContext(), TastInfosUtils.getAvailMem(getApplicationContext())));
	
		//启动一个广播
		Intent intent = new Intent();
		intent.setAction("www.clean.background");
		PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), 0, intent , 0);
		views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
		myWidgetManager.updateAppWidget(provider, views);
	}


	@Override
	public void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}
	
}
