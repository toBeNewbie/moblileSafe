package com.example.mobilesafe.bean;



/**
 * @author Administrator
 *@company Newbie
 *@date 2016-8-16
 *@des 用于封装主页面功能描述。
 */
public class homeToolsBean {

	private String title;
	private String desc;
	private int iconId;
	
	
	public homeToolsBean() {
		super();
		// TODO Auto-generated constructor stub
	}


	public homeToolsBean(String title, String desc, int iconId) {
		super();
		this.title = title;
		this.desc = desc;
		this.iconId = iconId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public int getIconId() {
		return iconId;
	}


	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	
	
	
}
