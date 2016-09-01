package com.example.mobilesafe.service;

import com.example.mobilesafe.dao.AddressPhoneLocationDao;
import com.example.mobilesafe.difineView.MyToast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class ShowIncomingPhoneLocation extends Service {

	private TelephonyManager telephonyManager;
	private PhoneStateListener listener;
	private MyToast myToast;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// 服务创建开始，
	@Override
	public void onCreate() {

		myToast = new MyToast(getApplicationContext());

		// 注册显示来电归属地显示服务。
		registerShowPhoneLocation();

		super.onCreate();
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
					myToast.hiden();
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
		super.onDestroy();
	}

}
