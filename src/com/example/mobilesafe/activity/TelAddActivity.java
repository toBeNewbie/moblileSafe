package com.example.mobilesafe.activity;

import java.util.List;

import com.example.mobilesafe.bean.ContactBean;
import com.example.mobilesafe.dao.ReadContactDao;

public class TelAddActivity extends BaseSmsTelActivity {

	@Override
	public List<ContactBean> getDatas() {
		// TODO Auto-generated method stub
		return ReadContactDao.getTelData(getApplicationContext());
	}

}
