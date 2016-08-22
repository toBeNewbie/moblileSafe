package com.example.mobilesafe.bean;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-21
 *@des 封装黑名单数据信息。
 */
public class BlcakListBean {
	private String phone;
	
	private int mode;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "BlcakListBean [phone=" + phone + ", mode=" + mode + "]";
	}
	
	
	
}
