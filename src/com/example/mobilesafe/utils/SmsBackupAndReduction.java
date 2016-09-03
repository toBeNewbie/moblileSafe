package com.example.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import com.example.mobilesafe.utils.SmsBackupAndReduction.GsonData.Sms;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;

public class SmsBackupAndReduction {

	private static File file;

	/**
	 * 
	 * @param charSet
	 *            带有特殊字符json格式数据。
	 * @return 原来的字符串。
	 */
	public static String convertToOriginalStr(String charSet) {

		String resource = null;

		for (int i = 0; i < charSet.length(); i++) {

			resource += convertToOriginalChar(charSet.charAt(i));
		}

		return resource;

	}

	/**
	 * 
	 * @param charSet
	 *            要转义的字符串。
	 * @return 转义后的特殊字符json格式数据。
	 */
	public static String convertToSpecialStr(String charSet) {

		String resource = null;

		for (int i = 0; i < charSet.length(); i++) {

			resource += convertToSpecialChar(charSet.charAt(i));
		}

		return resource;

	}
	
	
	public interface SmsBackupReductionListener{
		void show();
		void dismiss();
		void setMax(int max);
		void setProgress(int currentProgress);
	}
	

	/**
	 * 
	 * @param c
	 *            要转义的特殊字符。
	 * @return 原数据的字符。
	 */
	public static char convertToOriginalChar(char c) {

		char res = '\u0000';
		// { ■ } ▲ " ◆ :☑ ,∮ [€ ]☂

		switch (c) {
		case '■':
			res = '{';
			break;

		case '▲':
			res = '}';
			break;

		case '◆':
			res = '"';
			break;

		case '☑':
			res = ':';
			break;

		case '∮':
			res = ',';
			break;

		case '€':
			res = '[';
			break;

		case '☂':
			res = ']';
			break;

		default:
			res = c;
			break;
		}
		return res;

	}

	/**
	 * 
	 * @param c
	 *            要转义的字符。
	 * @return json格式要转义的字符。
	 */
	public static char convertToSpecialChar(char c) {

		char res = '\u0000';
		// { ■ } ▲ " ◆ :☑ ,∮ [€ ]☂

		switch (c) {
		case '{':
			res = '■';
			break;

		case '}':
			res = '▲';
			break;

		case '"':
			res = '◆';
			break;

		case ':':
			res = '☑';
			break;

		case ',':
			res = '∮';
			break;

		case '[':
			res = '€';
			break;

		case ']':
			res = '☂';
			break;

		default:
			res = c;
			break;
		}
		return res;

	}

	public static void backupSms(final Activity context,final SmsBackupReductionListener progressDialog) {

		class DataNum{
			int progressNum;
		}
		
		final DataNum dataNum = new DataNum();
		// 判断sd卡是否挂载。
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			// 判断SD卡是否空间充足。
			long freeSpace = Environment.getExternalStorageDirectory()
					.getFreeSpace();
			if (freeSpace <= 1024 * 1024 * 5) {

				throw new RuntimeException("sd卡空间不足，请清理之后备份");
			} else {
				file = new File(Environment.getExternalStorageDirectory(),
						"smses.json");
			}

		} else {
			throw new RuntimeException("SD卡没有挂载");
		}

		try {

			// 写入字符串，用PrintWriter比较方便。
			final PrintWriter printWriter = new PrintWriter(file);
			printWriter.println("{\"smses\":[");
			Uri uri = Uri.parse("content://sms");
			final Cursor cursor = context.getContentResolver().query(uri,
					new String[] { "address", "date", "body", "type" }, null,
					null, null);

			//设置进度条进度，并显示。
			progressDialog.setMax(cursor.getCount());
			progressDialog.show();
			
			
			// 往文件中写数据
			new Thread() {
				private String sms = null;
				public void run() {
					while (cursor.moveToNext()) {
						sms = "{";
						sms += "\"address\":\"" + cursor.getString(0) + "\"";
						sms += ",\"date\":\"" + cursor.getString(1) + "\"";
						sms += ",\"body\":\""
								+ convertToSpecialStr(cursor.getString(2))
								+ "\"";
						sms += ",\"type\":\"" + cursor.getString(3) + "\"}";

						// 判断是否是最后一条信息
						if (cursor.isLast()) {
							sms += "]}";
						} else {
							sms += ",";
						}
						printWriter.println(sms);
						// 刷新缓存。
						printWriter.flush();
						
						//模拟加载。
						SystemClock.sleep(500);
						//更新对话框。
						dataNum.progressNum++;
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								progressDialog.setProgress(dataNum.progressNum);
							}
						});

					}

					cursor.close();
					printWriter.close();
					
					
					//关闭对话框。
					context.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressDialog.dismiss();
						}
					});
					
				};

			}.start();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * 短信的还原。
	 * @param context
	 * @param progressDialog
	 */
	public static void reductionSms(final Activity context,final SmsBackupReductionListener progressDialog) {
		
		class progressCount{
			int progressNum;
		}
		
		final progressCount progressCount=new progressCount();
		
		// 判断sd卡是否挂载。
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			// 判断SD卡是否空间充足。
			long freeSpace = Environment.getExternalStorageDirectory()
					.getFreeSpace();

			file = new File(Environment.getExternalStorageDirectory(),
					"smses.json");

		} else {
			throw new RuntimeException("SD卡没有挂载");
		}
		
		//解析json格式数据。
		try {
			String jsonData = getDataStr(new FileInputStream(file));
			Gson mGson = new Gson();
			final GsonData gsonData = mGson.fromJson(jsonData, GsonData.class);
			final Uri uri=Uri.parse("content://sms/");
			
			//设置进度条，并显示对话框。
			progressDialog.setMax(gsonData.smses.size());
			progressDialog.show();
			
			//子线程写数据。
			new Thread(){

				public void run() {
					 ContentValues values=null;
					for (Sms sms : gsonData.smses) {
						
						values = new ContentValues();
						
						values.put("address", sms.address);
						values.put("body",convertToOriginalStr(sms.body));
						values.put("date", Long.parseLong(sms.date));
						values.put("type", Integer.parseInt(sms.type));
						
						context.getContentResolver().insert(uri, values);
						
						
						progressCount.progressNum++;
						
						//模拟加载数据。
						SystemClock.sleep(300);
						
						
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {

								progressDialog.setProgress(progressCount.progressNum);
							}
						});
					}
					
					//关闭进度框。
					context.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {

							progressDialog.dismiss();
						}
					});

					
				};
			}.start();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		
		
		
	}
	
	/**
	 * 
	 * @param is   输入流。
	 * 
	 * @return  输入流里的字符串。
	 */
	public static String getDataStr(InputStream is){
		StringBuilder mStringBuilder = new StringBuilder();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			while ((line=reader.readLine()) != null) {

				mStringBuilder.append(line);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				is.close();
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return mStringBuilder.toString();
	}
	
	
	
	public class GsonData{
		
		public List<Sms> smses;
		
		class Sms{
		String address;	
		String body;
		String date;
		String type;
		}
	}

}
