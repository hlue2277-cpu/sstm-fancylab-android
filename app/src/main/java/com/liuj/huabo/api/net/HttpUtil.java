package com.liuj.huabo.api.net;

import com.alibaba.fastjson.JSONObject;
import com.liuj.huabo.api.net.callback.BaseResponseBean;
import com.liuj.huabo.api.net.callback.ICallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * Created by liujun on 2020/4/9.
 */

public class HttpUtil {
    private String url;
    private Object tag;
    private JSONObject jsonParams;
    private boolean methodPost = true;

    public HttpUtil(String url, boolean methodPost) {
        this.url = url;
        this.methodPost = methodPost;
    }

    public HttpUtil tag(Object tag) {
        this.tag = tag;
        return this;
    }

    public HttpUtil params(JSONObject jsonParams) {
        this.jsonParams = jsonParams;
        return this;
    }

    public void execute(ICallback callback) {
        if (null == jsonParams) {
            jsonParams = new JSONObject();
        }
        if (methodPost) {
            HttpEngine.request(url, RequestConfig.Method.POST, jsonParams, callback);
        } else {
            HttpEngine.request(url, RequestConfig.Method.GET, jsonParams, callback);
        }
    }

    public <T> T executeSync(Class<T> clazz) {
        if (methodPost) {
            return HttpEngine.requestSync(url, RequestConfig.Method.POST, jsonParams, clazz);
        } else {
            return HttpEngine.requestSync(url, RequestConfig.Method.GET, jsonParams, clazz);
        }
    }

    public BaseResponseBean executeSync() {
        if (methodPost) {
            return HttpEngine.requestSync(url, RequestConfig.Method.POST, jsonParams);
        } else {
            return HttpEngine.requestSync(url, RequestConfig.Method.GET, jsonParams);
        }
    }

    public void downloadFile(Callback callback){
         HttpEngine.downloadFile(url, RequestConfig.Method.POST, jsonParams,callback);
    }


    public <T> List<T> executeSyncList(Class<T> clazz) {
        if (methodPost) {
            return HttpEngine.requestSyncList(url, RequestConfig.Method.POST, jsonParams, clazz);
        } else {
            return HttpEngine.requestSyncList(url, RequestConfig.Method.GET, jsonParams, clazz);
        }
    }



    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : HttpEngine.getInstance().getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : HttpEngine.getInstance().getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        for (Call call : HttpEngine.getInstance().getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : HttpEngine.getInstance().getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
