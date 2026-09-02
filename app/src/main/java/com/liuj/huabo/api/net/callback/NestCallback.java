package com.liuj.huabo.api.net.callback;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.liuj.huabo.log.LogUtil;


import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liujun on 2020/4/9.
 * 将解析出来的{@link BaseResponseBean}对象再进行一次封装
 * 主要针对嵌套的json数据
 * 集中处理弹出/关闭对话框,错误等情况
 * 实现类只需要考虑成功情况{@link #onSuccess(Object)}
 */
public abstract class NestCallback<T> implements ICallback<BaseResponseBean> {
    protected static final String ERROR_CODE_TIMEOUT = "E00060001";
    private static final String TAG = "ResponseCallback";
//    protected BaseActivity mBaseActivity;
    private int loginType;
    private boolean showProgressBar;

    public NestCallback() {
        this.showProgressBar = false;
    }

    public NestCallback(Context context, boolean showProgressBar) {

        this.showProgressBar = showProgressBar;
    }

    /**
     * @param context
     * @param loginType
     * @param showProgressBar
     */
    public NestCallback(Context context, int loginType, boolean showProgressBar) {
        this(context, showProgressBar);
        this.loginType = loginType;
    }

    @Override
    public BaseResponseBean parseResponseOnWorkThread(Call call, Response response) {
        String url = call.request().url().toString();
        url = url.split("\\?")[0];
        if (!response.isSuccessful()) {
            LogUtil.e(TAG, "url : " + url + "\nerrorcode : " + response.code());
            response.body().close();
            return null;
        }
        BaseResponseBean baseResponseBean = new BaseResponseBean();
        try {
            String string = response.body().string();
            //todo 数据加密过 需要解密
//            if (!BuildConfig.DEBUG_MODE || RequestConfig.getRequestConfig().needEncrypt) {
//                string = EncryptUtil.decryptByAESAndRSA(string);
//            }
            baseResponseBean = JSONObject.parseObject(string, BaseResponseBean.class);
            if (baseResponseBean == null) {
                LogUtil.printResponse(TAG, url, string);
            } else {
                LogUtil.printResponse(TAG, url, string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.body().close();
        }
        return baseResponseBean;
    }


    @Override
    public void onResponse(BaseResponseBean baseResponseBean) {
        // todo loading finish
//        if (mBaseActivity != null) {
//            mBaseActivity.dismissProgressDialog();
//        }
        if (baseResponseBean != null && baseResponseBean.success && !TextUtils.isEmpty(baseResponseBean.data)) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            Class<T> clazz = (Class) params[0];
            T t = JSONObject.parseObject(baseResponseBean.data, clazz);
            if (t != null) {
                onSuccess(t);
            } else {
                onFailed(null);
            }
        } else {
            onFailed(baseResponseBean);
        }
    }

    /**
     * status == 1
     *
     * @param t
     */
    public abstract void onSuccess(@NonNull T t);

    /**
     * status == 0
     *
     * @param bean
     */
    public void onFailed(BaseResponseBean bean) {
        if (bean == null) {
//            ToastUtils.showShort("数据解析错误");
            return;
        }

        if (TextUtils.isEmpty(bean.data)) {
//            ToastUtils.showShort("未获取到有效数据");
        }
    }


    @Override
    public void onError(Call call, IOException e) {
        if (e != null) {
            LogUtil.e(TAG, e.getMessage());
        }
//        if (mBaseActivity != null) {
//            mBaseActivity.dismissProgressDialog();
//        }
//        ToastUtils.showShort("网络连接错误,请重试");
    }

    @Override
    public void onPre() {
//        if (showProgressBar && mBaseActivity != null && !mBaseActivity.isFinishing()) {
//            mBaseActivity.showProgressDialog();
//        }
    }

    /**
     * 跳转到登陆页面
     */
    private void reLogin() {

    }
}
