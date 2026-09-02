package com.liuj.huabo.api.net.callback;

import android.content.Context;

public abstract class JSONCallback extends NestCallback {

    private static final String TAG = "JSONCallback";

    public JSONCallback() {
        super();
    }

    public JSONCallback(Context context, boolean showProgressBar) {
        super(context, showProgressBar);
    }

    @Override
    public void onResponse(BaseResponseBean baseResponseBean) {
        //todo 处理加载框
        if (null != baseResponseBean && baseResponseBean.success) {
            onSuccess(baseResponseBean);
        } else {
            onFailed(baseResponseBean);
        }
    }

    /**
     * status == 1
     */
    public abstract void onSuccess( BaseResponseBean baseResponseBean);


    @Override
    public void onSuccess(Object o) {

    }

}
