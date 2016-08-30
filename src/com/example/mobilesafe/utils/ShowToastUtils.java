package com.example.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;



/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-18
 *@des 弹出土司...Toast。
 */
public class ShowToastUtils {

	public static void showToast(final String mess ,final Activity context){
		
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 Toast.makeText(context, mess, 0).show();
			}
		});
	}
}
