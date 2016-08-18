package com.example.mobilesafe.activity;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;


public class PhoneAntiThrefSetTwo extends PhoneAntiThrefBaseActivity {
	private ImageView bind_sim_image;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.phone_antithref_guidseting_02);
		bind_sim_image = (ImageView) findViewById(R.id.bind_sim_image);
	}

	/**
	 * 进入下一个界面。
	 */
	@Override
	public void startNext() {
		String phoneNumber=splashUtils.getString(getApplicationContext(), myConstantValue.SIM_NUMBER, null);
		
		if (TextUtils.isEmpty(phoneNumber)) {
			Toast.makeText(getApplicationContext(), "你必须首先绑定Sim卡", 0).show();
		}else {
			
			startPageType(PhoneAntiThrefSetThree.class);
		}
	}

	
	/**
	 * 进入上一个界面。
	 */
	@Override
	protected void startPre() {

		startPageType(PhoneAntiThrefSetOne.class);
	}

	/**
	 * 初始化界面。
	 */
	@Override
	protected void initData() {
		String phoneNumber = splashUtils.getString(getApplicationContext(), myConstantValue.SIM_NUMBER, null);
		if (TextUtils.isEmpty(phoneNumber)) {
			bind_sim_image.setImageResource(R.drawable.unlock);
		}else {
			bind_sim_image.setImageResource(R.drawable.lock);
		}
	}
	
	/**
	 * 绑定Sim卡逻辑编码。
	 * @param view
	 */
	public void bindSIM(View view){
		String simNumber = splashUtils.getString(getApplicationContext(), myConstantValue.SIM_NUMBER, null);
		
		if (TextUtils.isEmpty(simNumber)) {
			//没有绑定。
			TelephonyManager telephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			String phoneNumber = telephony.getSimSerialNumber();
			//保存Sim卡号。
			splashUtils.putString(getApplicationContext(), myConstantValue.SIM_NUMBER, phoneNumber);
			bind_sim_image.setImageResource(R.drawable.lock);
		}else {
			
			//取消绑定。
			splashUtils.putString(getApplicationContext(), myConstantValue.SIM_NUMBER, "");
			//已经绑定。
			bind_sim_image.setImageResource(R.drawable.unlock);
		}
	}
}













