package com.example.mobilesafe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Process;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-10-10
 *@des 异常的补救
 */
public class CatchErrorApplication extends Application {

	@Override
	public void onCreate() {
		
		// 所有功能执行之前执行   ； 监控异常的状态
		super.onCreate();
		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				
				StringBuilder mess=new StringBuilder();
				
				// 反射求出手机机型信息
				Class type = Build.class;
				Field[] declaredFields = type.getDeclaredFields();
				for (Field field : declaredFields) {
					try {
						
						 Object values = field.get(null);
						 mess.append(field.getName()+":"+values+"\n");
						 
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				//捕获任何线程抛出的异常，错误信息保存到sd卡中
				mess.append(ex.toString());
				writeExceptionToFile(mess.toString(), new File("/sdcard/mobilesafe_error.txt"));
				
				//重启程序，进入主界面
				Intent forPackageIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
				startActivity(forPackageIntent);
				
				//杀掉异常进程
				Process.killProcess(Process.myPid());
			}
		});
	}

	/**
	 * 
	 * @param mess  文件内容
	 * @param file  文件名
	 */
	private void writeExceptionToFile(String mess,File file){
		try {
			
			PrintWriter mPrintWriter=new PrintWriter(file);
			mPrintWriter.write(mess);
			
			//刷新缓存，到硬盘
			mPrintWriter.flush();
			mPrintWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	
	
}
