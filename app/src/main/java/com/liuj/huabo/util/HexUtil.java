package com.liuj.huabo.util;

/**
 * Created by liujun on 2020/6/28.
 */
public class HexUtil {


    /**
     * 字节数组转换为十六进制字符串
     * @param b   byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public  static   String byte2hexSimple(byte b[]) {
        try {
            if (b == null) {
                throw new IllegalArgumentException(
                        "Argument b ( byte array ) is null! ");
            }
            StringBuilder sb = new StringBuilder();
            String stmp = "";
            for (int n = 0; n < b.length; n++) {
                stmp = Integer.toHexString(b[n] & 0xff);
                if (stmp.length() == 1) {
                    sb.append("0").append(stmp).append("");
                } else {
                    sb.append(stmp).append("");
                }
            }
            return sb.toString().toLowerCase();
        }catch (Exception e){
            return "";
        }
    }

}
