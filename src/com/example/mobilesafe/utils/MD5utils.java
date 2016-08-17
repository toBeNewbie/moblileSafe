package com.example.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-17
 *@des 数据MD5加密算法加密算法。
 *
 *算法思想：
 *
 *1 用每个byte去和11111111做与运算 并且得到的是一个int类型的值 byte&11111111
 *
 * 2 把int类型转换成16进制并返回String类型
 *
 * 3 不满8个2进制位就补全
 */
public class MD5utils {

	public static String MD5(String str){
		
		StringBuffer buffer = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			//获得密文
			byte[] encryption = digest.digest(str.getBytes());
			for (byte b : encryption) {
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
		}
		return buffer.toString();
		
	}
	
}
