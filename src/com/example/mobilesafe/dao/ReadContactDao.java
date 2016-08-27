package com.example.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.ContactBean;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-18
 *@des 读取联系人信息。
 */
public class ReadContactDao {
	
	private static Cursor cursor2;
	private static Cursor cursor;

	//电话添加。
	public static List<ContactBean> getTelData(Context context){
		List<ContactBean> contactBeans=new ArrayList<ContactBean>();
		Uri uri=Uri.parse("content://call_log/calls");
		Cursor cursor3 = context.getContentResolver().query(uri, new String[]{"name","number"},null, null, null);
		ContactBean contactBean=null;
		while (cursor3.moveToNext()) {

			contactBean=new ContactBean();
			contactBean.setContactName(cursor3.getString(0));
			contactBean.setPhoneNumber(cursor3.getString(1));
			contactBeans.add(contactBean);
		}
		cursor3.close();
		return contactBeans;
	}
	
	
	//短信添加。
	public static List<ContactBean> getSmsData(Context context){
		List<ContactBean> contactBeans=new ArrayList<ContactBean>();
		Uri uri=Uri.parse("content://sms");
		Cursor cursor4 = context.getContentResolver().query(uri, new String[]{"address"},null, null, null);
		ContactBean contactBean=null;
		while (cursor4.moveToNext()) {

			contactBean=new ContactBean();
			contactBean.setContactName("sms");
			contactBean.setPhoneNumber(cursor4.getString(0));
			contactBeans.add(contactBean);
		}
		cursor4.close();
		return contactBeans;
	}
	
	
	//获取好友联系人信息。
	public static List<ContactBean> readContacts(Context context){
		
		List<ContactBean> contactBeans=new ArrayList<ContactBean>();
		String uriContact = "content://com.android.contacts/contacts";
		String uriData = "content://com.android.contacts/data";
		
		cursor = context.getContentResolver().query(Uri.parse(uriContact), new String[]{"name_raw_contact_id"}, null, null, null);
		
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				String contact_id = cursor.getString(cursor.getColumnIndex("name_raw_contact_id"));
				ContactBean contact = new ContactBean();
				 
					
					cursor2 = context.getContentResolver().query(Uri.parse(uriData), new String[]{"data1","mimetype"}, "raw_contact_id=?",new String[]{contact_id}, null);
					while (cursor2.moveToNext()) {
						//获得联系人的基本信息。
		 
						if (cursor2.getString(1).equals("vnd.android.cursor.item/name")) {
							contact.setContactName(cursor2.getString(0));
						}else if (cursor2.getString(1).equals("vnd.android.cursor.item/phone_v2")) {
							contact.setPhoneNumber(cursor2.getString(0));
						}
						
					}
					
					contactBeans.add(contact);
				
				cursor2.close();
				}
		}
		cursor.close();
		return contactBeans;
	}
}









