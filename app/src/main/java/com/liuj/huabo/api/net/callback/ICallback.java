package com.liuj.huabo.api.net.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liujun on 2020/4/9.
 * 自定义响应接口
 */
public interface ICallback<T> {

    /**
     * 发送请求之前调用
     * MainThread
     */
    void onPre();

    /**
     * 网络请求失败
     * MainThread
     *
     * @param call
     * @param e
     */
    void onError(Call call, IOException e);

    /**
     * 响应
     * MainThread
     *
     * @param t
     */
    void onResponse(T t);

    /**
     * 解析响应流
     * WorkThread
     *
     * @param call
     * @param response
     * @return
     */
    T parseResponseOnWorkThread(Call call, Response response);



}
