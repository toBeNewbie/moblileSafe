package com.example.mobilesafe.testUnit;

import java.util.List;

import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.bean.BlcakListBean;
import com.example.mobilesafe.bean.PhoneServiceNameBean;
import com.example.mobilesafe.dao.AddressPhoneLocationDao;
import com.example.mobilesafe.dao.BlackListDao;
import com.example.mobilesafe.db.BlackListDB;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;
import com.example.mobilesafe.utils.GetAppInfoUtils;
import com.example.mobilesafe.utils.SmsBackupAndReduction;
import com.example.mobilesafe.utils.TastInfosUtils;

import android.test.AndroidTestCase;

public class readContactsTest extends AndroidTestCase {
	public void testBlackDB(){
		 BlackListDao dao=new BlackListDao(getContext());
		 for (int i = 0; i < 100; i++) {
			
			 dao.add("131414"+i, BlackListDB.PHONE_MODE);
		}
	}
	
	public void testLocation(){
		List<PhoneServiceNameBean> phoneName = AddressPhoneLocationDao.getPhoneName();
		
		for (PhoneServiceNameBean phoneServiceNameBean : phoneName) {
			
			System.out.println(AddressPhoneLocationDao.getPhoneNumber(phoneServiceNameBean));
		}
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
	
	public void testSms(){
//		SmsBackupAndReduction.reductionSms(getContext());
		List<AppInforBean> appInfos = GetAppInfoUtils.getInstallAppInfos(getContext());
		for (AppInforBean appInforBean : appInfos) {
			System.out.println(appInforBean);
		}
	}
	
	
	public void testSpace(){
			System.out.println(TastInfosUtils.getAllRunningInfos(getContext()));
	}
	
}
