package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;


public class PhoneAntiThrefSetThree extends PhoneAntiThrefBaseActivity {
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.phone_antithref_guidseting_03);
	}

	@Override
	public void startNext() {
		// TODO Auto-generated method stub
		startPageType(PhoneAntiThrefSetFour.class);
	}
}
