package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.example.mobilesafe.service.AntiThrefService;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-19
 *@des 系统启动完成的广播接受者
 */
public class BootCompleteReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {

		//监听Sim卡是否变动。
		
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = telephony.getSimSerialNumber();
		
		if (phoneNumber.equals(splashUtils.getString(context, myConstantValue.SIM_NUMBER, ""))) {
		
			android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
			smsManager.sendTextMessage(splashUtils.getString(context, myConstantValue.MOBILE_SAFE_NUMBER, "110"), null, "I am thref who you want to meet!!!", null, null);
			
		}
		
		//系统开机自动启动服务。
		
		if (splashUtils.getbBoolean(context, myConstantValue.BOOT_COMPLETE, false)) {
			
			//启动服务。
			intent = new Intent(context, AntiThrefService.class);
			context.startService(intent);
			
		}
	}

}
