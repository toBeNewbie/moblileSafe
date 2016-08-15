package com.example.mobilesafe.spUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-15
 *@des splash工具类，检测版本更新。
 */
public class splashUtils {

	
	public static void putBoolean(Context context,String key, Boolean value){
		SharedPreferences sharedPreferences = context.getSharedPreferences(myConstantValue.SPL_FILE_CONFIGUE_NAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(key, value).commit();
		}
	
	
	public static Boolean getbBoolean(Context context,String key, Boolean defValue){
		SharedPreferences sharedPreferences = context.getSharedPreferences(myConstantValue.SPL_FILE_CONFIGUE_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defValue);
	}


	 

 
	
}
