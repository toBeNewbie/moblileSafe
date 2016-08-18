package com.example.mobilesafe.bean;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-18
 *@des 封装联系人的基本信息。
 */
public class ContactBean {
	
	private String phoneNumber;
	private String contactName;
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	@Override
	public String toString() {
		return "ContactBean [phoneNumber=" + phoneNumber + ", contactName="
				+ contactName + "]";
	}
	
	
}
