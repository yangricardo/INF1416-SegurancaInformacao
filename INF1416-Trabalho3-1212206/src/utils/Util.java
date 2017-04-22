package utils;

import java.io.UnsupportedEncodingException;

public class Util {

	public static void printError(String msg,Exception e) {
		System.err.println(msg);
		System.err.println(" Message: "+e);
		System.err.println(" CAUSE: "+e.getCause());
		e.printStackTrace();
	}
	
	public static String convertToHex(byte[] data){
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(0x0100 + (data[i] & 0x00FF)).substring(1);
			buf.append((hex.length() < 2 ? "0" : "") + hex);
		}
		return buf.toString();
	}
	
	public static String convertToUTF8(byte[]data){
		String utf8Data = null;
		try {
			utf8Data = new String(data,"UTF8");
		} catch (UnsupportedEncodingException e) {
			printError("Erro ao converter bytes para UT8", e);
		}
		return utf8Data;
	}
	
	public static String convertToASCII(byte[]data){
		String utf8Data = null;
			utf8Data = new String(data);
		return utf8Data;
	}
		
}
