package com.example.mobilesafe.service;

import com.example.mobilesafe.dao.BlackListDao;
import com.example.mobilesafe.db.BlackListDB;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BlacklistInterceptService extends Service {

	private SmsReceiver mSmsReceiver;
	private BlackListDao mBlackListDao;
	private TelephonyManager telephonyManager;
	private PhoneStateListener stateListener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @author Administrator
	 * @company Newbie
	 * @date 2016-8-28
	 * @des 监听短信的收发。
	 */
	class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// 获取短信内容。
			Object[] smsDatas = (Object[]) intent.getExtras().get("pdus");

			for (Object object : smsDatas) {
				SmsMessage smsMessage = SmsMessage
						.createFromPdu((byte[]) object);
				String messageBody = smsMessage.getDisplayOriginatingAddress();

				int mode = mBlackListDao.getMode(messageBody);
				if ((mode & BlackListDB.SMS_MODE) != 0) {
					abortBroadcast();
				}
			}
		}

	}

	@Override
	public void onCreate() {

		mBlackListDao = new BlackListDao(getApplicationContext());

		// 注册短信拦截服务。
		registerSmsIntercept();

		// 注册电话拦截服务。
		registerTelIntercept();

		// 开启服务。
		System.out.println("服务已经开启.............");
		super.onCreate();
	}

	// 注册电话拦截服务。
	private void registerTelIntercept() {

		//获取电话管理器。
		telephonyManager = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);

		//注册电话监听器。
		stateListener = new PhoneStateListener() {

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// 监听电话状态的改变。
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:
					phoneIntercept(incomingNumber);
					break;

				default:
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}

			//电话拦截
			private void phoneIntercept(String incomingNumber) {
			 
				int mode = mBlackListDao.getMode(incomingNumber);
				if ((mode & BlackListDB.PHONE_MODE)!=0) {
					endPhone();
				}
			}
			
		};

		telephonyManager.listen(stateListener,
				PhoneStateListener.LISTEN_CALL_STATE);// 监听电话状态。
	}

	protected void endPhone() {
		// 挂断电话。
		System.out.println("电话已经挂断。。。。");
	}

	/**
	 * 注册短信拦截服务。
	 */
	public void registerSmsIntercept() {
		mSmsReceiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mSmsReceiver, filter);
	}

	@Override
	public void onDestroy() {

		// 取消短信拦截服务。
		unregisterReceiver(mSmsReceiver);

		// 取消注册电话拦截服务。
		telephonyManager.listen(stateListener, PhoneStateListener.LISTEN_NONE);
		// 销毁服务
		System.out.println("服务已经销毁...............");
		super.onDestroy();
	}

}
