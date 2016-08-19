package com.example.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-19
 *@des 手机防盗的服务。
 */
public class AntiThrefService extends Service {

	private SmsReceiver mSmsReceiver;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @author Administrator
	 *@company Newbie
	 *@date 2016-8-19
	 *@des 接收短息的广播接受者。
	 */
	private class SmsReceiver extends BroadcastReceiver{

		boolean isPlaying = false;
		@Override
		public void onReceive(Context context, Intent intent) {
			//获取短信内容。
			Object[] smsDatas=(Object[]) intent.getExtras().get("pdus");
			
			for (Object object : smsDatas) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object);
				String messageBody = smsMessage.getDisplayMessageBody();
				if (messageBody.equals("#*music*#")) {
					
					//播放报警音乐。
					if (!isPlaying) {
						
						MediaPlayer player=MediaPlayer.create(getApplicationContext(), com.example.mobilesafe.R.raw.confession);
						player.setVolume(1.0f, 1.0f);
						player.setLooping(true);
						player.start();
						player.setOnCompletionListener(new OnCompletionListener() {
							
							@Override
							public void onCompletion(MediaPlayer mp) {
								// TODO Auto-generated method stub
								isPlaying=false;
							}
						});
						isPlaying=true;
					}
					abortBroadcast();
				}else if (messageBody.equals("#*lockscreen*#")) {
					Log.d("message", "远程锁屏");
					abortBroadcast();
				}else if (messageBody.equals("#*wipedata*#")) {
					Log.d("message", "清除数据");
					abortBroadcast();
				}else if (messageBody.equals("#*gps*#")) {
					Log.d("message", "定位信息");
					abortBroadcast();

				}
			}
		}
		
	}
	
	
	@Override
	public void onCreate() {
		//注册短信拦截广播。
		
		super.onCreate();
		mSmsReceiver = new SmsReceiver();
		IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		
		registerReceiver(mSmsReceiver, filter);
	}
	
	
	@Override
	public void onDestroy() {
		//取消注册。
		super.onDestroy();
		
		unregisterReceiver(mSmsReceiver);
	}

}
