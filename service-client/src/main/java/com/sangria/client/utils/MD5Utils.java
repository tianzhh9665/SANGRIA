package com.sangria.client.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5Utils {

		public static String getMD5(String source) {
	        String s = null;
	        char hexChar[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
	                'E', 'F' };
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(source.getBytes());
	            byte[] hashCalc = md.digest();
	            char result[] = new char[16 * 2];
	            int k = 0;
	            for (int i = 0; i < 16; i++) {
	                byte everyByte = hashCalc[i];
	                result[k++] = hexChar[everyByte >>> 4 & 0xf];
	                result[k++] = hexChar[everyByte & 0xf];
	            }
	            s = new String(result);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return s;
	    }

	    public static void main(String[] args) {
	        System.out.println(getMD5("1234qwer"));
	    }

	    public static String getMD5Str(String inStr) {
	        MessageDigest md5 = null;
	        try{
	            md5 = MessageDigest.getInstance("MD5");
	        }catch(Exception e){
	            System.out.println(e.toString());
	            e.printStackTrace();
	            return "";
	        }
	        byte[] byteArray = null;
	        try {
	            byteArray = inStr.getBytes("UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        byte[] md5Bytes = md5.digest(byteArray);
	        StringBuffer hexValue = new StringBuffer();
	        for(int i=0;i<md5Bytes.length;i++){
	            int val = md5Bytes[i] & 0xff;
	            if(val<16){
	                hexValue.append("0");
	            }
	            hexValue.append(Integer.toHexString(val));
	        }
	        return hexValue.toString();
	    }
}
