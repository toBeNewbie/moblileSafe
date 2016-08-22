package com.example.mobilesafe.db;

import android.R.integer;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-21
 *@des 黑名单数据库的创建。
 */
public class BlackListDB extends SQLiteOpenHelper {

	//版本信息
	public static final int VERSION=1;
	//表名
	public static final String BLACKTB_NAME="blacktb";
	
	public static final String PHONE_COLUMN = "phone";
	public static final String MODE_COLUMN ="mode";
	//短信拦截模式
	public static final int SMS_MODE = 1<<0;
	//电话拦截模式
	public static final int PHONE_MODE = 1<<1;
	//全部拦截模式
	public static final int ALL_MODE = SMS_MODE | PHONE_MODE;
	

	public BlackListDB(Context context) {
		super(context, "blacklist.db", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacktb(_id integer primary key autoincrement,phone text,mode integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table blacktb");
		onCreate(db);
	}

}
