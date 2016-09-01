package com.example.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.mobilesafe.bean.PhoneServiceNameBean;
import com.example.mobilesafe.bean.PhoneServiceNumberBean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressPhoneLocationDao {

	public static final String DB_FILE_PATH = "/data/data/com.example.mobilesafe/files/address.db";

	public static final String DB_SERVICE_NAME="/data/data/com.example.mobilesafe/files/commonnum.db";
	
	
	
	//获取号码的类型的具体信息。
	public static List<PhoneServiceNumberBean> getPhoneNumber(PhoneServiceNameBean serviceNameBean){
		List<PhoneServiceNumberBean> serviceNumberBeans = new ArrayList<PhoneServiceNumberBean>();
		
		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(
				DB_SERVICE_NAME, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqLiteDatabase.rawQuery("select number,name from table"+serviceNameBean.getId(),null);
		
		PhoneServiceNumberBean serviceNumberBean=null;
		while (cursor.moveToNext()) {

			serviceNumberBean =new PhoneServiceNumberBean();
			serviceNumberBean.setServiceNumber(cursor.getString(0));
			serviceNumberBean.setServiceName(cursor.getString(1));
			
			serviceNumberBeans.add(serviceNumberBean);
		}
		cursor.close();
		return serviceNumberBeans;
	}
	
	
	
	//获取服务号码类型的信息。
	public static List<PhoneServiceNameBean> getPhoneName(){
		
		List<PhoneServiceNameBean> serviceNameBeans=new ArrayList<PhoneServiceNameBean>();
		
		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(
				DB_SERVICE_NAME, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqLiteDatabase.rawQuery("select * from classlist",null);
		PhoneServiceNameBean serviceNameBean=null;
		while (cursor.moveToNext()) {
			serviceNameBean=new PhoneServiceNameBean();
			serviceNameBean.setName(cursor.getString(0));
			serviceNameBean.setId(cursor.getInt(1));
			serviceNameBeans.add(serviceNameBean);
		}
		cursor.close();
		return serviceNameBeans;
	}
	
	
	
	/**
	 * 获取移动电话归属地的信息。
	 * 
	 * @param phoneNumber
	 *            限制为手机号码的前七位
	 * 
	 * @return 移动手机号码的归属地信息。
	 */
	private static String getMoblePhoneLocation(String phoneNumber) {
		String locationMess = "未知号码";

		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(
				DB_FILE_PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select location from data2 where "
						+ "id=(select outkey from data1 where id=?)",
				new String[] { phoneNumber });

		if (cursor.moveToNext()) {
			// 获取当地归属地信息。
			locationMess = cursor.getString(0);
		}

		return locationMess;
	}

	
	/**
	 *获取固定电话的位置信息
	 * @param fixedNumber   固定电话的区号
	 * @return   固定电话的位置信息。
	 */
	private static String getFixedPhoneLocation(String fixedNumber) {
		String locationMess = "未知号码";

		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(
				DB_FILE_PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = sqLiteDatabase.rawQuery(
				"select location from data2 where " + "area=?",
				new String[] { fixedNumber });

		if (cursor.moveToNext()) {
			// 获取当地归属地信息。
			locationMess = cursor.getString(0);
		}

		return locationMess;
	}
	
	
	/**
	 * 
	 * @param phoneString  要查询的手机号码。
	 * @return
	 */
	public static String getPhoneMessage(String phoneString){
		
		String location="未知截断";
		//18437925095
		 Pattern p = Pattern.compile("1[34578]{1}[1-9]{9}");
		 Matcher m = p.matcher(phoneString);
		 boolean b = m.matches();
		
		 if (b) {
			location=getMoblePhoneLocation(phoneString.substring(0, 7));
		}else {
			
			//2位区号。
			if (phoneString.charAt(1)=='1' || phoneString.charAt(1)=='2') {
				
				location = getFixedPhoneLocation(phoneString.substring(1, 3));
			}else {
				//3位区号。
				location = getFixedPhoneLocation(phoneString.substring(1, 4));
			}
		}
		 
		return location.substring(0, location.length()-2);
	}
}
