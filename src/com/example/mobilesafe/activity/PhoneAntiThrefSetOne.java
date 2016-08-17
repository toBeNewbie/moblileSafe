package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

public class PhoneAntiThrefSetOne extends PhoneAntiThrefBaseActivity {

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		setContentView(R.layout.phone_antithref_guidseting_01);
	}

	@Override
	protected void startNext() {
		// TODO Auto-generated method stub
		startPageType(PhoneAntiThrefSetTwo.class);
	}

}
