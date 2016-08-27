package com.example.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mobilesafe.bean.BlcakListBean;
import com.example.mobilesafe.db.BlackListDB;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-21
 *@des 对数据库进行操作。
 */
public class BlackListDao {
	
	private BlackListDB mBlackListDB;
	
	
	public BlackListDao(Context context){
		
		mBlackListDB = new BlackListDB(context);
	}
	
	
	//添加黑名单数据对象整体信息。
	public void add(BlcakListBean listBean){
		add(listBean.getPhone(), listBean.getMode());
	}
	
	/**
	 * 给黑名单列表添加数据。
	 * @param phone 黑名单号码。
	 * @param mode  拦截模式。 SMS_MODE 短信拦截 、  PHONE_MODE 电话拦截、  ALL_MODE 全部拦截。
	 */
	public void add(String phone,int mode){
		SQLiteDatabase liteDatabase = mBlackListDB.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(BlackListDB.PHONE_COLUMN, phone);
		values.put(BlackListDB.MODE_COLUMN, mode);
		liteDatabase.insert(BlackListDB.BLACKTB_NAME, null, values);
		liteDatabase.close();
	}
	
	
	//获得所有的黑名单列表信息。
	public List<BlcakListBean> getBlackList(){
		
		List<BlcakListBean> beans = new ArrayList<BlcakListBean>();
		SQLiteDatabase liteDatabase = mBlackListDB.getReadableDatabase();
		Cursor cursor = liteDatabase.rawQuery("select "+BlackListDB.PHONE_COLUMN+","
												+BlackListDB.MODE_COLUMN+
												" from "+BlackListDB.BLACKTB_NAME+
												" order by _id desc", null);
		
		BlcakListBean listBean = null;
		while (cursor.moveToNext()) {

			listBean = new BlcakListBean();
			
			listBean.setPhone(cursor.getString(0));
			listBean.setMode(cursor.getInt(1));
			
			beans.add(listBean);
		}
		liteDatabase.close();
		cursor.close();
		return beans;
	}
	
	
	/**
	 * 
	 * @param phone 要删除的手机号码。
	 * @return true 删除成功  false 删除失败。
	 */
	public boolean deleteBlacklist(String phone){
		SQLiteDatabase liteDatabase = mBlackListDB.getReadableDatabase();
		int deleteCount = liteDatabase.delete(BlackListDB.BLACKTB_NAME, BlackListDB.PHONE_COLUMN+"=?", new String[]{phone});
		
		liteDatabase.close();
		return deleteCount>0?true:false;
	}
	
	public void update(BlcakListBean bean){
		update(bean.getPhone(), bean.getMode());
	}
	
	/**
	 * 更新数据库。
	 * @param phone 要更新黑名单数据的号码
	 * @param mode  要更新黑名单数据的拦截模式。
	 */
	public void update(String phone,int mode){
		
		deleteBlacklist(phone);
		add(phone, mode);
	}
	
	
	/**
	 * 获取当前页的数据。
	 */
	public List<BlcakListBean> getPageData(int pageNumber,int countPage){
		List<BlcakListBean> listBeans = new ArrayList<BlcakListBean>();
		int startIndex = (pageNumber-1) * countPage;
		SQLiteDatabase database = mBlackListDB.getReadableDatabase();
		Cursor cursor = database.rawQuery("select phone,mode from blacktb order by _id desc limit ?,?",new String[]{startIndex+"",countPage+""});
		
		BlcakListBean listBean = null;
		while (cursor.moveToNext()) {

			listBean = new BlcakListBean();
			
			listBean.setPhone(cursor.getString(0));
			listBean.setMode(cursor.getInt(1));
			
			listBeans.add(listBean);
		}
		cursor.close();
		return listBeans;
	}
	
	
	/**
	 * 获得要显示的行数。
	 */
	public int getTotalRaws(){
		int totalPage = 0;
		SQLiteDatabase database = mBlackListDB.getReadableDatabase();
		Cursor cursor = database.rawQuery("select count(1) from blacktb", null);
		if (cursor.moveToNext()) {
			totalPage=cursor.getInt(0);
		}
		return totalPage;
	}
	
	
	
	
}
