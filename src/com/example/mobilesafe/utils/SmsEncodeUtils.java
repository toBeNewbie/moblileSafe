package com.example.mobilesafe.utils;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-9-4
 *@des 对文本进行位运算，进行加密。
 */
public class SmsEncodeUtils {
	
	/**
	 * 
	 * @param originalStr
	 * 			原来的字符串。
	 * @param seed
	 * 			加密的种子。
	 * @return
	 */
	public static String encodeStr(String originalStr,byte seed){
		
		try {
		
			byte[] bytes = originalStr.getBytes("gbk");
			for (int i = 0; i < bytes.length; i++) {
				bytes[i]^=seed;
			}
		
			return new String(bytes, "gbk");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
}
