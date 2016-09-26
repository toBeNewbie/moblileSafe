package com.example.mobilesafe.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-9-17
 *@des 访问病毒数据库
 */
public class AntiVirusFileDao {
	
	
	public static final String DB_VIRUS_PATH = "/data/data/com.example.mobilesafe/files/antivirus.db";
	/**
	 * 判断文件是否是病毒
	 * @return
	 * 		返回false：不是病毒    true:是病毒
	 */
	public static boolean isVirus(String md5){
		
		boolean isVirus=false;
		SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(DB_VIRUS_PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = openDatabase.rawQuery("select 1 from datable where md5 =?", new String[]{md5});
		
		if (cursor.moveToNext()) {
			isVirus=true;
		}
		
		openDatabase.close();
		cursor.close();
		return isVirus;
	}
	
	/**
	 * 获取当前数据库的版本号
	 * @return
	 * 		返回最新数据库的版本号
	 */
	public static int getCurrentVersion(){
		int currentVersion=-1;
		SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(DB_VIRUS_PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = openDatabase.rawQuery("select subcnt from version", null);
		if (cursor.moveToNext()) {
			currentVersion=cursor.getInt(cursor.getColumnIndex("subcnt"));
		}
		
		openDatabase.close();
		cursor.close();
		return currentVersion;
	}
	
	
	/**
	 * 更新数据库的版本
	 * @param newVersion
	 * 			要更新的数据库版本
	 */
	public static void updateDBVersion(int newVersion){
		SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(DB_VIRUS_PATH, null, SQLiteDatabase.OPEN_READONLY);
	
		ContentValues values=new ContentValues();
		values.put("subcnt", newVersion);
		openDatabase.update("version", values, null, null);
		openDatabase.close();
	}
	
	 /**
	  * 更新病毒数据库内容
	  * @param MD5
	  * 		病毒文件的MD5值
	  * @param desc
	  * 		病毒描述信息
	  */
	public static void updateDBVirus(String MD5,String desc){
		SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(DB_VIRUS_PATH, null, SQLiteDatabase.OPEN_READONLY);
		
		ContentValues values=new ContentValues();
		values.put("desc", desc);
		values.put("md5", MD5);
		values.put("name", "Android.Troj.AirAD.a");
		values.put("type", 6);
		openDatabase.insert("datable", null, values);
	
		openDatabase.close();
	}
	
}
