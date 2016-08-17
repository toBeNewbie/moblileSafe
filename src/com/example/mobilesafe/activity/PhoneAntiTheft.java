package com.example.mobilesafe.activity;

import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-17
 *@des 手机防盗界面。
 */
public class PhoneAntiTheft extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (splashUtils.getbBoolean(getApplicationContext(), myConstantValue.dISPLAY_SETTING, false)) {
			
			//设置向导完成。
			initView();
		}else{
			//设置向导未完成。
			Intent intent = new Intent(PhoneAntiTheft.this, PhoneAntiThrefSetOne.class);
			startActivity(intent);
			finish();
		}
		 
	}

	private void initView() {
		// TODO Auto-generated method stub
		
	}

	 
}
