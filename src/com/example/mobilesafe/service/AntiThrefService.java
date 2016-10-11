package com.example.mobilesafe.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.mobilesafe.R;
import com.example.mobilesafe.receiver.MyDeviceAdminReceiver;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-19
 *@des 手机防盗的服务。
 */
public class AntiThrefService extends Service {

	private SmsReceiver mSmsReceiver;
	private ComponentName mDeviceAdminSample;
	private DevicePolicyManager mDPM;


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
						
						MediaPlayer player=MediaPlayer.create(getApplicationContext(), R.raw.confession);
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
						player.start();
						isPlaying=true;
					}
					abortBroadcast();
				}else if (messageBody.equals("#*lockscreen*#")) {
					mDPM.resetPassword("110", 0);
					mDPM.lockNow();
					abortBroadcast();
				}else if (messageBody.equals("#*wipedata*#")) {
					mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
					abortBroadcast();
				}else if (messageBody.equals("#*gps*#")) {
					getLocationMessage();
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
		
		
		mDeviceAdminSample = new ComponentName(this, MyDeviceAdminReceiver.class);
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
	}
	
	
	public void getLocationMessage() {
		// 获取定位信息。
		final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		locationManager.requestLocationUpdates("gps", 0, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				//坐标发生改变时调用。
				float accuracy = location.getAccuracy();
				double altitude = location.getAltitude();
				double latitude = location.getLatitude();//纬度
				double longitude = location.getLongitude();//经度
				String message = "精度是"+accuracy+"\n海拔是"+ altitude+"\n纬度是："+latitude+"\n经度是："+longitude;
				
				//给安全号码发送消息。
				android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
				smsManager.sendTextMessage(splashUtils.getString(getApplicationContext(), myConstantValue.MOBILE_SAFE_NUMBER, "110"), null, message, null, null);
				
				if (locationManager!=null) {
					//发送短信时停止监听。保证只在发送短信时定位。
					locationManager.removeUpdates(this);
				}
			}
		});
		
	}


	@Override
	public void onDestroy() {
		//取消注册。
		super.onDestroy();
		
		unregisterReceiver(mSmsReceiver);
	}

}
