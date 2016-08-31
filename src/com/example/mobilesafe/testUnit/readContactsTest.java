package com.example.mobilesafe.testUnit;

import java.util.List;

import com.example.mobilesafe.bean.BlcakListBean;
import com.example.mobilesafe.dao.AddressPhoneLocationDao;
import com.example.mobilesafe.dao.BlackListDao;
import com.example.mobilesafe.db.BlackListDB;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;

import android.test.AndroidTestCase;

public class readContactsTest extends AndroidTestCase {
	public void testBlackDB(){
		 BlackListDao dao=new BlackListDao(getContext());
		 for (int i = 0; i < 100; i++) {
			
			 dao.add("131414"+i, BlackListDB.PHONE_MODE);
		}
	}
	
	public void testLocation(){
		System.out.println(AddressPhoneLocationDao.getPhoneMessage("18339450881"));
	}
	
	public void testGetAll(){
		BlackListDao dao=new BlackListDao(getContext());
		List<BlcakListBean> beans = dao.getBlackList();
		System.out.println(beans);
	}
	
	public void testGetPage(){
		BlackListDao dao=new BlackListDao(getContext());
		System.out.println(dao.getPageData(1, 5));
	}
}
