package com.liuj.huabo.util;



import java.util.Map;
import java.util.TreeMap;

public class SignUtils {
    /**
     * 签名值
     */
    public static final String PARAM_SIGN = "sign";
    /**
     * API平台分配给consumer的appkey的参数名称
     */
    public static final String PARAM_APPKEY = "appkey";
    /**
     * 时间戳
     */
    public static final String PARAM_TIMESTAMP = "timestamp";
    /**
     * api版本号
     */
    public static final String PARAM_V = "v";

    /**
     * 对请求参数集进行MD5签名
     * @param params 待签名的请求参数集
     * @param secretCode 签名密码
     * @return 返回null 或 32位16进制大写字符串
     */
    public static String signMD5(Map<String, String> params, String secretCode){
        if(params instanceof TreeMap){
            return signMD5((TreeMap<String, String>) params, secretCode, false).getHexResult().toUpperCase();
        }else{
            TreeMap<String, String> treeMap = new TreeMap<>();
            treeMap.putAll(params);
            return signMD5(treeMap, secretCode, false).getHexResult().toUpperCase();
        }
    }
    /**
     * 将请求参数按key=value&key=valuesecretCode拼接
     * <br/>排除key为sign和signmethod的key-value
     * @param params 请求参数
     * @param secretCode 签名密码
     * @return 返回32位16进制大写字符串
     */
    public static Md5Builder signMD5(TreeMap<String, String> params, String secretCode, boolean startAppend){
        Md5Builder md5 = new Md5Builder();
        if(startAppend) {
            md5.append(secretCode);
        }
        String value = "";
        boolean start = true;
        for(String name : params.keySet()){
            value = params.get(name);
            if(StringUtils.isEmpty(value)){
                value = "";
            }
            if(!start) {
                md5.append("&");
            }else {
                start = false;
            }
            md5.append(name).append("=").append(value);
        }
        if(!startAppend) {
            md5.append(secretCode);
        }
        return md5;
    }
}
