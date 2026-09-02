package com.liuj.huabo.api.net;

/**
 * Created by liujun on 2020/4/9.
 */

public class RequestConfigBean {
    public static int TYPE_RELEASE = 1;
    public static int TYPE_DEBUG = 2;
    public static int TYPE_C12 = 3;

    public String url;
    public String rsaPublicKey;
    public boolean needEncrypt;

    public RequestConfigBean(boolean needEncrypt, String url, String rsaPublicKey) {
        this.url = url;
        this.rsaPublicKey = rsaPublicKey;
        this.needEncrypt = needEncrypt;
    }
}
