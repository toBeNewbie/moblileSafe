package com.example.mobilesafe.activity;

import java.util.List;

import com.example.mobilesafe.bean.ContactBean;
import com.example.mobilesafe.dao.BlackListDao;
import com.example.mobilesafe.dao.ReadContactDao;

public class SmsAddActivity extends BaseSmsTelActivity {

	@Override
	public List<ContactBean> getDatas() {
		// TODO Auto-generated method stub
		return ReadContactDao.getSmsData(getApplicationContext());
	}

}
