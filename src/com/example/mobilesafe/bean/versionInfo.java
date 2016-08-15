package com.example.mobilesafe.bean;

/**
 * 
 * 
 * @author Administrator
 * @company Newbie
 * @date 2016-8-15
 * @des 封装版本信息的类。
 */
public class versionInfo {


	private String version_name;
	private int version_code;
	private String desc;
	private String downloadUrl;
	public String getVersion_name() {
		return version_name;
	}
	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	public int getVersion_code() {
		return version_code;
	}
	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
	

}
