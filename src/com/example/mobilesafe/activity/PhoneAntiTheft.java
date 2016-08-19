package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-17
 *@des 手机防盗界面。
 */
public class PhoneAntiTheft extends Activity {

	private TextView tvSafeNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (splashUtils.getbBoolean(getApplicationContext(), myConstantValue.dISPLAY_SETTING, false)) {
			
			//设置向导完成。
			initView();
			
			initData();
			
			initEvent();
		}else{
			//设置向导未完成。
			Intent intent = new Intent(PhoneAntiTheft.this, PhoneAntiThrefSetOne.class);
			startActivity(intent);
			finish();
		}
		
		
		 
	}

	private void initEvent() {
		
		
	}

	 public void to_antithref_guide01(View view){
		Intent intent = new Intent(this,PhoneAntiThrefSetOne.class );
		startActivity(intent);
		finish();
	}
	
	private void initData() {
		
		tvSafeNumber.setText(splashUtils.getString(getApplicationContext(), myConstantValue.MOBILE_SAFE_NUMBER, ""));
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.phone_antithref_done);
		tvSafeNumber = (TextView) findViewById(R.id.tv_safe_number);
	}
	
	
	

	 
}
