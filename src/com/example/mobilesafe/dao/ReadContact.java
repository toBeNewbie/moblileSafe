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
public class ReadContact {
	
	private static Cursor cursor2;
	private static Cursor cursor;

	public static List<ContactBean> readContacts(Context context){
		
		List<ContactBean> contactBeans=new ArrayList<ContactBean>();
		String uriContact = "content://com.android.contacts/contacts";
		String uriData = "content://com.android.contacts/data";
		
		cursor = context.getContentResolver().query(Uri.parse(uriContact), new String[]{"name_raw_contact_id"}, null, null, null);
		
		if (cursor!=null) {
			while (cursor.moveToNext()) {
				String contact_id = cursor.getString(cursor.getColumnIndex("name_raw_contact_id"));
				ContactBean contact = new ContactBean();
				if (cursor2!=null) {
					
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
				}
				cursor2.close();
				}
		}
		cursor.close();
		return contactBeans;
	}
}









