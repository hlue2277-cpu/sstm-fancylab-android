package com.liuj.huabo.api.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.liuj.huabo.api.net.callback.BaseResponseBean;
import com.liuj.huabo.api.net.callback.ICallback;
import com.liuj.huabo.api.net.callback.PostFilesCallback;
import com.liuj.huabo.api.net.request.ProgressRequestBody;
import com.liuj.huabo.common.Constants;
import com.liuj.huabo.log.LogUtil;
import com.liuj.huabo.util.DateUtils;
import com.liuj.huabo.util.DeviceInfoUtil;
import com.liuj.huabo.util.DynamicStats;
import com.liuj.huabo.util.ListUtil;
import com.liuj.huabo.util.SignUtils;


import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liujun on 2020/4/9.
 */
public class HttpEngine {

    private static final String TAG = "HttpEngine";
    private static final String STATS_HTTP = "httpRequest";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
    private static HttpEngine mhHttpEngine;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;


    private HttpEngine() {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static HttpEngine getInstance() {
        if (mhHttpEngine == null) {
            synchronized (HttpEngine.class) {
                if (mhHttpEngine == null) {
                    mhHttpEngine = new HttpEngine();
                }
            }
        }
        return mhHttpEngine;
    }

    public static HttpUtil post(String url) {
        return new HttpUtil(url, true);
    }

    public static HttpUtil get(String url) {
        return new HttpUtil(url, false);
    }

    /**
     * 发送请求
     */
    public static Request request(String url, String method, JSONObject jsonParams, final ICallback callback, boolean sync) {
        if (TextUtils.isEmpty(url)) {
            if (callback != null) {
                callback.onError(null, null);
            }
            return null;
        }
        if (!url.startsWith("http")) {
            if (callback != null) {
                callback.onError(null, null);
            }
            return null;
        }
        if (null != callback) {
            callback.onPre();
        }
        Request.Builder builder = new Request.Builder();

        //TODO 构建公共的请求参数
        TreeMap<String, String> treeMapParam = buildParams();

        for (Map.Entry<String, Object> entry : jsonParams.entrySet()) {
            treeMapParam.put(entry.getKey(), entry.getValue().toString());
        }

        /*-----------构建请求头-----------*/
        Map<String, String> headerMap = buildHeader(treeMapParam);
//        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
//            builder.addHeader(entry.getKey(), entry.getValue());
//        }
        /*----------------------------*/
        if (RequestConfig.Method.GET.equalsIgnoreCase(method)) {
            if (treeMapParam.size() > 0) {
                StringBuilder paramBuilder = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : treeMapParam.entrySet()) {
                    if (first) {
                        first = false;
                        paramBuilder.append("?");
                    } else {
                        paramBuilder.append("&");
                    }

                    paramBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                }

                for (Map.Entry<String, String> entry2 : headerMap.entrySet()) {
                    paramBuilder.append("&");
                    paramBuilder.append(entry2.getKey()).append("=").append(entry2.getValue());
                }


                url += paramBuilder.toString();
            }
            builder.get();
        } else if (RequestConfig.Method.POST.equalsIgnoreCase(method)) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if (treeMapParam.size() > 0) {
                for (Map.Entry<String, String> entry : treeMapParam.entrySet()) {
                    bodyBuilder.add(entry.getKey(), entry.getValue());
                }

                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    bodyBuilder.add(entry.getKey(), entry.getValue());
                }
            }
            builder.post(bodyBuilder.build());
        }
        LogUtil.d(TAG, "url : " + url);

        /*-----------打印请求信息--------------------*/
        JSONObject logObj = new JSONObject();
        logObj.put("url", url.split("\\?")[0]);
        logObj.put("param", treeMapParam);
        logObj.put("sign", headerMap);
        LogUtil.printRequest(TAG, url.split("\\?")[0], logObj.toJSONString());
        /*-------------------------------*/
        final Request request = builder.url(url).build();
        if (!sync) {
            enqueue(request, callback);
        }
        return request;
    }

    /**
     * 异步请求
     */
    public static void request(String url, String method, JSONObject jsonParams, final ICallback callback) {
        request(url, method, jsonParams, callback, false);
    }

    /**
     * 同步请求 返回单个对象
     */
    public static <T> T requestSync(String url, String method, JSONObject jsonParams, Class<T> clazz) {
        BaseResponseBean baseResponseBean = requestSync(url, method, jsonParams);
        if (baseResponseBean != null && baseResponseBean.success && !TextUtils.isEmpty(baseResponseBean.data)) {
            return JSONObject.parseObject(baseResponseBean.data, clazz);
        } else {
            return null;
        }
    }

    /**
     * 同步请求返回集合
     */
    public static <T> List<T> requestSyncList(String url, String method, JSONObject jsonParams, Class<T> clazz) {
        BaseResponseBean baseResponseBean = requestSync(url, method, jsonParams);
        if (baseResponseBean != null && baseResponseBean.success && !TextUtils.isEmpty(baseResponseBean.data)) {
            if (baseResponseBean == null) {
                LogUtil.printResponse(TAG, url, baseResponseBean);
            } else {
                LogUtil.printResponse(TAG, url,  baseResponseBean);
            }
            return JSONObject.parseArray(baseResponseBean.data, clazz);
        } else {
            return null;
        }
    }
    /**
     * 同步请求返回集合
     */
    public static BaseResponseBean requestSync(String url, String method, JSONObject jsonParams) {
        Request request = request(url, method, jsonParams, null, true);
        BaseResponseBean baseResponseBean = null;
        String response = executeRequest(request);
        if(response!=null){
            baseResponseBean = JSONObject.parseObject(response, BaseResponseBean.class);
            if (baseResponseBean == null) {
                LogUtil.printResponse(TAG + "ResponseCallBack", url, baseResponseBean);
            }else {
                LogUtil.printResponse(TAG + "ResponseCallBack", url, response);
            }
        }
        return baseResponseBean;
    }

    private static String executeRequest(Request request){
        Response response = null;
        long lasttime = System.currentTimeMillis();
        boolean error = false;
        String responseText = null;
        try {
            response = getInstance().mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                error = true;
                LogUtil.e(TAG, "url : " + request.url() + "\nerrorcode : " + response.code());
            }else {
                responseText = response.body().string();
            }
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } finally{
            if (response != null) {
                try{response.close();}catch (Exception ignore){}
            }
        }
        DynamicStats.getLogCounter(STATS_HTTP).addCountAndTime(lasttime, error);
        return responseText;
    }

    public static void downloadFile(String url, String method, JSONObject jsonParams, Callback callback) {
        Request request = request(url, method, jsonParams, null, true);
        if (request == null) {
            return;
        }
        getInstance().getOkHttpClient().newCall(request).enqueue(callback);
    }

    /**
     * 发送异步请求
     */
    private static void enqueue(Request request, final ICallback callback) {
        final long lasttime = System.currentTimeMillis();
        getInstance().mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getInstance().postFailureResult(callback, call, e);
                DynamicStats.getLogCounter(STATS_HTTP).addCountAndTime(lasttime, true);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try {
                    if (callback != null) {
                        Object o = callback.parseResponseOnWorkThread(call, response);
                        getInstance().postSuccessResult(callback, call, o);
                    }
                }finally {
                    if (response != null) {
                        try{response.close();}catch (Exception ignore){}
                    }
                }
                DynamicStats.getLogCounter(STATS_HTTP).addCountAndTime(lasttime, false);
            }
        });
    }

    public static void multipartRequest(String url, JSONObject jsonObject, List<File> files, final PostFilesCallback callback) {
        if (jsonObject == null) {
            return;
        }
        LogUtil.e(TAG, "url : " + url + " , params : " + jsonObject.toJSONString());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // builder.setType(MultipartBody.FORM).addFormDataPart(KEY_PARAMS, jsonObject.toJSONString());
        if (!ListUtil.isEmpty(files)) {
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                if (file != null) {
                    builder.addFormDataPart("image", file.getName(), new ProgressRequestBody(RequestBody.create(MEDIA_TYPE_PNG, file), callback));
                }
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        enqueue(request, callback);
    }

    public static void multipartRequest(String url, JSONObject jsonObject, File image, final ICallback callback) {
        if (jsonObject == null || image == null) {
            return;
        }
        LogUtil.e(TAG, "url : " + url + " , params : " + jsonObject.toJSONString());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // builder.setType(MultipartBody.FORM).addFormDataPart(KEY_PARAMS, formatData(jsonObject.toJSONString(), needEncrypted));
        LogUtil.d(TAG, "fileName : " + image.getName());
        builder.addFormDataPart("image", image.getName(), RequestBody.create(MEDIA_TYPE_PNG, image));
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        enqueue(request, callback);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private void postFailureResult(final ICallback cCallBack, final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (cCallBack != null) {
                    cCallBack.onError(call, e);
                }
            }
        });

    }

    private void postSuccessResult(final ICallback cCallBack, final Call call, final Object response) {
        mHandler.post(() -> {
            if (cCallBack != null) {
                cCallBack.onResponse(response);
            }
        });
    }

    private static TreeMap<String, String> buildParams() {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("timestamp", DateUtils.getCurFullTimestampStr());
        params.put("appkey", Constants.APP_KEY);
        params.put("deviceId", Constants.DEVICE_ID);
        params.put("v", Constants.APP_VERSION);
        return params;
    }

    private static Map<String, String> buildHeader(TreeMap<String, String> params) {
        String sign = SignUtils.signMD5(params, Constants.SECRET_CODE, false).getHexResult();
        Map<String, String> header = new LinkedHashMap<>();
        header.put("sign", sign);
        return header;
    }

}
