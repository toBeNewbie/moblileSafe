package com.example.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-10-6
 *@des 封装关于程序锁应用的数据库
 */
public class LockedDataDB extends SQLiteOpenHelper {

	public static final String LOCKED_TABLE_NAME="locked_tb";
	public static final String PACKAGE_NAME="pack_name";
	
	public static final Uri URI=Uri.parse("content://newbie.com.cn");
	
	public LockedDataDB(Context context) {
		super(context, "locked_tb", null, 1, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table locked_tb(_id integer primary key autoincrement,pack_name text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("drop table locked_tb");
		onCreate(db);
		
	}

}
