package com.example.mobilesafe.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.AppInforBean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
	public static long getRomTotalSpace(){
		File storageDirectory = Environment.getDataDirectory();
		return storageDirectory.getTotalSpace();
	} 
	
	
	/**
	 * 获取SD卡的总大小。
	 * @return
	 */
	public static long getSdTotalSpace(){
		File file = Environment.getExternalStorageDirectory();
		return file.getTotalSpace();
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
	 * 设置所有的应用进程
	 * 
	 * @param inforBean  需要设置的应用进程类
	 * 						需要先设置该进程的包名。
	 * @param context
	 * 				上下文。
	 * @throws NameNotFoundException 
	 */
	public static void setAppInfos(AppInforBean inforBean,Context context) throws NameNotFoundException{
		
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo=packageManager.getApplicationInfo(inforBean.getPackageName(), 0);
		
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
		
		if ((applicationInfo.flags & applicationInfo.FLAG_EXTERNAL_STORAGE)!=0) {
			//安装在sd卡的App.
			inforBean.setInstallSD(true);
		}
		
		
		
		//安装路径。
		inforBean.setSourceDir(applicationInfo.sourceDir);
		
		//安装文件的大小。
		inforBean.setAppTakeUpSize(new File(applicationInfo.sourceDir).length());
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
			inforBean.setPackageName(applicationInfo.packageName);
			 try {
				setAppInfos(inforBean, context);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			inforBeans.add(inforBean);
		}
		
		return inforBeans;
	}
	
	
	
	
}
