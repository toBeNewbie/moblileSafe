package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.dao.AddressPhoneLocationDao;
import com.example.mobilesafe.utils.ShowToastUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneLocationQuery extends Activity {
	private EditText et_queryNumber;
	private TextView tv_locationMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		
		
	}

	private void initView() {
		
		setContentView(R.layout.phone_location_query);
		et_queryNumber = (EditText) findViewById(R.id.et_input_query_number);
	
		tv_locationMessage = (TextView) findViewById(R.id.tv_phone_location_message);
	}
	
	
	public void queryLocation(View view){
		
		String phoneNumber = et_queryNumber.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			
			//给文本框添加振动效果。
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_queryNumber.startAnimation(shake);
			
			//给手机添加振动效果。
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(new long[]{200,300,200,300}, 3);
			return;
		}
		String phoneStr = AddressPhoneLocationDao.getPhoneMessage(phoneNumber);
		tv_locationMessage.setText("归属地信息：\n"+phoneStr);
	}
}
