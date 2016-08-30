package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.os.Bundle;
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
}
