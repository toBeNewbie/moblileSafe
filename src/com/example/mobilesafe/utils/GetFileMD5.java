package com.example.mobilesafe.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-9-17
 *@des 获取文件的MD5特征码
 */
public class GetFileMD5 {
	
	public static String fileMD5(String filePath){
		
		StringBuffer buffer = new StringBuffer();
		try {
			
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			//读取文件
			FileInputStream inputStream=new FileInputStream(filePath);
			
			byte[] bytes=new byte[1024*10];
			int length = inputStream.read(bytes);
			while (length!=-1) {
				messageDigest.update(bytes, 0, length);//不断读取文件内容
				
				//继续读取
				length=inputStream.read(bytes);
				
			}
			
			//读取文件结束
			byte[] digest = messageDigest.digest();
			
			for (byte b : digest) {
				int number=b&0xff;
				String numberstr = Integer.toHexString(number);
				if (numberstr.length()==1) {
					buffer.append("0");
				}
				buffer.append(numberstr);
			}
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (buffer.toString()).toUpperCase();
	}
}
