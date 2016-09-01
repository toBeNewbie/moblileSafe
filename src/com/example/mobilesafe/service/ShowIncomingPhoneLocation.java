package com.example.mobilesafe.service;

import com.example.mobilesafe.dao.AddressPhoneLocationDao;
import com.example.mobilesafe.difineView.MyToast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class ShowIncomingPhoneLocation extends Service {

	private TelephonyManager telephonyManager;
	private PhoneStateListener listener;
	private MyToast myToast;
	private OutCallReceiver outCallReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class OutCallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			//显示外拨手机号码
			String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			String phoneLocation = AddressPhoneLocationDao.getPhoneMessage(phoneNumber);
			myToast.show(phoneLocation);
			
		}
	
	}
	
	// 服务创建开始，
	@Override
	public void onCreate() {

		myToast = new MyToast(getApplicationContext());

		// 注册显示来电归属地显示服务。
		registerShowPhoneLocation();
		
		//注册外拨电话归属地服务。
		registeOutCallReceiver();
		
		super.onCreate();
	}

	public void registeOutCallReceiver() {
		outCallReceiver = new OutCallReceiver();
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(outCallReceiver, intentFilter);
	}

	public void registerShowPhoneLocation() {
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		listener = new PhoneStateListener() {

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {

				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:// 停止状态。
					myToast.hiden();
					break;

				case TelephonyManager.CALL_STATE_RINGING:// 响铃状态。
					showToastPhoneLocation(incomingNumber);
					break;

				case TelephonyManager.CALL_STATE_OFFHOOK:// 通话状态
//					myToast.hiden();
					break;
				default:
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}

		};

		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	// 显示电话归属地的信息。
	protected void showToastPhoneLocation(String incomingNumber) {

		String phoneLocation = AddressPhoneLocationDao
				.getPhoneMessage(incomingNumber);
		myToast.show(phoneLocation);
	}

	// 服务已经销毁。
	@Override
	public void onDestroy() {
		// 取消注册电话来电监听。
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		
		//取消外拨电话监听。
		unregisterReceiver(outCallReceiver);
		super.onDestroy();
	}

}
