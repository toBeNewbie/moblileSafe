package com.example.mobilesafe.dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressPhoneLocationDao {

	public static final String DB_FILE_PATH = "/data/data/com.example.mobilesafe/files/address.db";

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
	public static String getFixedPhoneLocation(String fixedNumber) {
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
