package com.example.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.AppInforBean;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-9-6
 *@des  	获取运行程序的信息。
 */
public class TastInfosUtils {

	
	/**
	 * 
	 * @param context
	 * 
	 * @return   获取系统可用内存。
	 */
	public static long getAvailMem(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		activityManager.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	
	
	/**
	 * 	获取总内存大小。
	 * 
	 * @return
	 */
	public static long getTotalMem(){
		
		File file=new File("/proc/meminfo");
		try {
			
			BufferedReader mBufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = mBufferedReader.readLine();
			String totalSize=line.substring(line.indexOf(':')+1, line.length()-2).trim();
			
			long totalMem=Long.parseLong(totalSize)*1024;
			
			return totalMem;
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	 
	/**
	 *  获取总运行进程的数目。
	 * @param context
	 * @return
	 */
	public static int getRunningTotalProcessNum(Context context){
		
		List<AppInforBean> inforBeans=new ArrayList<AppInforBean>();
		
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//获取正在运行进程的信息。
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		return appProcesses.size();
	}
	
	/**
	 * 获取所有运行程序的进程信息
	 * 
	 * @return
	 */
	public static List<AppInforBean> getAllRunningInfos(Context context){
		
		List<AppInforBean> inforBeans=new ArrayList<AppInforBean>();
		
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//获取正在运行进程的信息。
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		
		AppInforBean mInforBean=null;
		for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
			
			mInforBean=new AppInforBean();
			
			//获取运行进程的基本信息。
			android.os.Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
			//获取占用的内存。
			long privateDirty = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
			//设置占用的内存。
			mInforBean.setAppRunningSize(privateDirty);
		
			mInforBean.setPackageName(runningAppProcessInfo.processName);
			try {
				//设置属性。
				GetAppInfoUtils.setAppInfos(mInforBean, context);
				//将对象添加到集合中。
				inforBeans.add(mInforBean);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return inforBeans;
	}
	
}
