package com.example.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mobilesafe.db.LockedDataDB;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-10-6
 *@des 程序锁数据库的dao层
 */
public class LockedDataDao {
	
	private LockedDataDB mLockedDataDB;
	
	private Context mContext;
	public LockedDataDao(Context context){
		mLockedDataDB = new LockedDataDB(context);
		this.mContext=context;
	}
	
	/**
	 * 向程序锁数据库里添加数据，对一个App完成加锁的操作
	 * 
	 * @param packName
	 * 			要添加数据的包名
	 */
	public void addLockedPackName(String packName){
		
		SQLiteDatabase writableDatabase = mLockedDataDB.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(LockedDataDB.PACKAGE_NAME, packName);
		writableDatabase.insert(LockedDataDB.LOCKED_TABLE_NAME, null, values);
		writableDatabase.close();
		
		
		//发送通知数据库改变
		mContext.getContentResolver().notifyChange(LockedDataDB.URI, null);
		
	}
	
	/**
	 * 向程序锁数据库里删除数据，对一个App完成移除加锁功能操作
	 * 
	 * @param packName
	 * 			要删除的应用包名
	 */
	public void removeLockedPackName(String packName){

		SQLiteDatabase writableDatabase = mLockedDataDB.getWritableDatabase();
		writableDatabase.delete(LockedDataDB.LOCKED_TABLE_NAME, LockedDataDB.PACKAGE_NAME+"=?", new String[]{packName});
		writableDatabase.close();
		
		//发送通知数据库改变
		mContext.getContentResolver().notifyChange(LockedDataDB.URI, null);
	}
	
	
	/**
	 * 判断app是否已经加锁
	 * 
	 * @param packName
	 * 			所判断的应用包名
	 * @return
	 * 		true : 应用程序已经加锁     		false : 应用程序未加锁
	 */
	public boolean appIfLocked(String packName){
		
		boolean ifLocked=false;
		SQLiteDatabase readableDatabase = mLockedDataDB.getReadableDatabase();
		Cursor cursor = readableDatabase.rawQuery("select 1 from "+LockedDataDB.LOCKED_TABLE_NAME+
									" where "+LockedDataDB.PACKAGE_NAME+" = ?", 
									new String[]{packName});
		if (cursor.moveToNext()) {
			ifLocked=true;
		}

		return ifLocked;
	
	}
	
	/**
	 * 获取所有的已经加锁的app
	 * @return
	 * 		返回所有加锁App应用的包名
	 */
	public List<String> getAllLockedApp(){
		
		List<String> allLockedApp=new ArrayList<String>();
		SQLiteDatabase database = mLockedDataDB.getReadableDatabase();
		Cursor cursor = database.rawQuery("select "+LockedDataDB.PACKAGE_NAME+" from "+LockedDataDB.LOCKED_TABLE_NAME, null);
		while (cursor.moveToNext()) {
			allLockedApp.add(cursor.getString(0));
		}
		return allLockedApp;
	}
	
}
