package com.liuj.huabo.api.net.callback;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by liujun on 2020/4/9.
 */
public abstract class NestListCallback<T> extends NestCallback<BaseResponseBean> {
    private static final String TAG = "NestListCallback";

    public NestListCallback() {
        super();
    }

    public NestListCallback(Context context, boolean showProgressBar) {
        super(context, showProgressBar);
    }

    @Override
    public void onResponse(BaseResponseBean baseResponseBean) {
//        if (mBaseActivity != null) {
//            mBaseActivity.dismissProgressDialog();
//        }
        if (baseResponseBean != null && baseResponseBean.success && !TextUtils.isEmpty(baseResponseBean.data)) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            Class<T> clazz = (Class) params[0];
            List<T> list = JSONObject.parseArray(baseResponseBean.data, clazz);
            onSuccess(list);
        } else {
            onFailed(baseResponseBean);
        }
    }


    @Override
    public void onSuccess( BaseResponseBean bean) {

    }

    public abstract void onSuccess(List<T> list);
}
