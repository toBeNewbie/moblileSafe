package com.example.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.mobilesafe.dao.BlackListDao;
import com.example.mobilesafe.dao.ReadContactDao;
import com.example.mobilesafe.db.BlackListDB;

import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
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
					//删除电话日志。
					deletePhoneLog(incomingNumber);
					//挂断电话。
					endPhone();
				}
			}
			
		};

		telephonyManager.listen(stateListener,
				PhoneStateListener.LISTEN_CALL_STATE);// 监听电话状态。
	}

	//挂断电话。
	protected void endPhone() {
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//挂断电话被屏蔽啦。
		try {
			Class clazz = Class.forName("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			
			ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
			iTelephony.endCall();
			
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 挂断电话。
	}

	private void deletePhoneLog(final String incomingNumber) {
		
		Uri uri=Uri.parse("content://call_log/calls");
		getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
		
			@Override
			public void onChange(boolean selfChange) {
				// 日志发生变化。
				ReadContactDao.deleteLog(getApplicationContext(), incomingNumber);
				
				//取消注册。
				getContentResolver().unregisterContentObserver(this);
				
				super.onChange(selfChange);
			}
		});
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
