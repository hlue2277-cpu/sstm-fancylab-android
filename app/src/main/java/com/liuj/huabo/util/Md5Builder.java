package com.liuj.huabo.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class Md5Builder {
	private MessageDigest md;
	private byte[] result;
	
	public Md5Builder() {
		try {
			md = MessageDigest.getInstance("MD5");
			md.reset();
		} catch (Exception e) {
		}
	}
	public Md5Builder append(String str) {
		try {
			md.update(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return this;
	}
	public Md5Builder append(byte[] data) {
		md.update(data);
		return this;
	}
	public byte[] getResult() {
		if(result == null) {
			result = md.digest();
		}
		return result;
	}
	public String getHexResult() {
		return getHex(getResult());
	}
	public static String getHex(byte[] bytes){
		StringBuilder buf = new StringBuilder(bytes.length*2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}
}
