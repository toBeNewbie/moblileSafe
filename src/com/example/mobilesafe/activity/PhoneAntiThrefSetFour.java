package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;


public class PhoneAntiThrefSetFour extends PhoneAntiThrefBaseActivity {
	@Override
	protected void initView() {
		setContentView(R.layout.phone_antithref_guideseting_04);
	}

	@Override
	
	public void startNext() {
		// TODO Auto-generated method stub
		startPageType(PhoneAntiTheft.class);
	}
}
