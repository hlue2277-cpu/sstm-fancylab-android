package com.liuj.huabo.api.net.callback;

/**
 * Created by liujun on 2020/4/9.
 * 服务器响应数据
 */
public class BaseResponseBean extends BaseBean {


    public static final String CODE_SUCCESS = "0000";
    /**
     * 状态，true-成功；false-失败
     */
    public boolean success = false;

    /**
     * 错误码，status为0时给出，错误码参考:错误码
     */
    public String errcode;

    /**
     * 错误详细消息，status为0时给出。
     */
    public String msg;

    /**
     * 业务数据，status为0时，此字段可以为空
     */
    public String data;

    @Override
    public String toString() {
        return "BaseResponseBean{" +
                "success=" + success +
                ", errcode='" + errcode + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
