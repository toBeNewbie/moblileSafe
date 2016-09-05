package com.example.mobilesafe.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.AppInforBean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-9-4
 *@des 获取应用程序的基本信息。
 */
public class GetAppInfoUtils {
	
	
	 /**
	  * 获取手机的总内存大小。
	  * @return
	  */
	public static long getTotalSpace(){
		File storageDirectory = Environment.getDataDirectory();
		return storageDirectory.getTotalSpace();
	} 
	
	
	 /**
	  * 获取手机的可用内存空间。
	  * @return
	  */
	public static long getFreeSpace(){
		File storageDirectory = Environment.getDataDirectory();
		return storageDirectory.getFreeSpace();
	} 
	
	
	/**
	 * 获取sd卡的可用空间。
	 * @return
	 */
	public static long getSdFreeSpace(){
		File file = Environment.getExternalStorageDirectory();
		return file.getFreeSpace();
	}
	
	
	
	/**
	 * 获取所有应用程序的信息。
	 * 
	 * @param context
	 * @return
	 */
	public static List<AppInforBean> getInstallAppInfos(Context context){
		
		PackageManager packageManager = context.getPackageManager();
		
		List<AppInforBean> inforBeans=new ArrayList<AppInforBean>();
		
		List<ApplicationInfo> applications = packageManager.getInstalledApplications(0);
		AppInforBean inforBean = null;
		for (ApplicationInfo applicationInfo : applications) {
			inforBean=new AppInforBean();
			
			//包名。
			inforBean.setPackageName(applicationInfo.packageName);
			
			//图标。
			inforBean.setIcon(applicationInfo.loadIcon(packageManager));
			
			//App的名字。
			inforBean.setAppName(applicationInfo.loadLabel(packageManager)+"");
			
			if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM)!=0) {
				
				//安装在系统App
				inforBean.setSystem(true);
				
			}
			
			if ((applicationInfo.flags&applicationInfo.FLAG_EXTERNAL_STORAGE)!=0) {
				//安装在sd卡的App.
				inforBean.setInstallSD(true);
			}
			
			
			
			//安装路径。
			inforBean.setSourceDir(applicationInfo.sourceDir);
			
			//安装文件的大小。
			inforBean.setAppTakeUpSize(new File(applicationInfo.sourceDir).length());
			
			inforBeans.add(inforBean);
		}
		
		return inforBeans;
	}
	
	
	
	
}
