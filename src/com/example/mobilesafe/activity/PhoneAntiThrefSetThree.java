package com.example.mobilesafe.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.mobilesafe.R;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.example.mobilesafe.utils.ShowToastUtils;


public class PhoneAntiThrefSetThree extends PhoneAntiThrefBaseActivity {
	private EditText anti_phone_number;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.phone_antithref_guidseting_03);
		anti_phone_number = (EditText) findViewById(R.id.et_anti_thref_phone_number);
	}

	/**
	 * 初始化数据,回显页面数据。
	 */
	
	@Override
	protected void initData() {
		String antiPhoneNnuber = splashUtils.getString(getApplicationContext(), myConstantValue.MOBILE_SAFE_NUMBER, "").trim();
		anti_phone_number.setText(antiPhoneNnuber);
	 
		anti_phone_number.setSelection(antiPhoneNnuber.length());
		super.initData();
	}
	
	
	/**
	 * 跳转下一个界面。
	 */
	@Override
	public void startNext() {
		
		String antiPhoneNumber = anti_phone_number.getText().toString().trim();
		if (TextUtils.isEmpty(antiPhoneNumber)) {
			ShowToastUtils.showToast("安全号码不能为空", this);
		}else {
			
			String antiNumber = anti_phone_number.getText().toString().trim();
			
			splashUtils.putString(getApplicationContext(), myConstantValue.MOBILE_SAFE_NUMBER, antiNumber);
			
			startPageType(PhoneAntiThrefSetFour.class);
		}
	}

	@Override
	protected void startPre() {
		// TODO Auto-generated method stub
		startPageType(PhoneAntiThrefSetTwo.class);
	}
	
	/**
	 * 点击选择绑定手机号处理逻辑
	 * @param view
	 */
	public void selectContacts(View view){
		Intent intent=new Intent(this, ContactList.class);
		startActivityForResult(intent, 0);
	}
	
 
	/**
	 * 接收被启动联系人返回的数据。
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data!=null) {
			
			String safeNumber = data.getStringExtra(myConstantValue.MOBILE_SAFE_NUMBER);
			anti_phone_number.setText(safeNumber);
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	 
}
