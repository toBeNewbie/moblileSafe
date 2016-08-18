package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;


public class PhoneAntiThrefSetTwo extends PhoneAntiThrefBaseActivity {
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.phone_antithref_guidseting_02);
	}

	@Override
	public void startNext() {
		// TODO Auto-generated method stub
		startPageType(PhoneAntiThrefSetThree.class);
	}

	@Override
	protected void startPre() {

		startPageType(PhoneAntiThrefSetOne.class);
	}
}
