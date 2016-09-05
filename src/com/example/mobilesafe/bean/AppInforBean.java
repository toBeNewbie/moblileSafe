package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class AppInforBean {
	private Drawable icon;         //应用程序图标。
	private String appName;	       //应用程序的名称。
	private boolean isSystem;      //是否是应用软件。
	private boolean isInstallSD;   //是否安装在sd卡中。
	private String packageName;		//应用程序包名。
	private long AppTakeUpSize;		//应用程序占用的空间。
	private String sourceDir;		//应用程序安装路径。
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public boolean isInstallSD() {
		return isInstallSD;
	}
	public void setInstallSD(boolean isInstallSD) {
		this.isInstallSD = isInstallSD;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public long getAppTakeUpSize() {
		return AppTakeUpSize;
	}
	public void setAppTakeUpSize(long appTakeUpSize) {
		AppTakeUpSize = appTakeUpSize;
	}
	public String getSourceDir() {
		return sourceDir;
	}
	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}
	@Override
	public String toString() {
		return "AppInforBean [icon=" + icon + ", appName=" + appName
				+ ", isSystem=" + isSystem + ", isInstallSD=" + isInstallSD
				+ ", packageName=" + packageName + ", AppTakeUpSize="
				+ AppTakeUpSize + ", sourceDir=" + sourceDir + "]";
	}
	
	
	
}
