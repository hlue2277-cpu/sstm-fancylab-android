package com.liuj.huabo.api.net.callback;

import android.content.Context;

/**
 * Created by liujun on 2020/4/9.
 */
public abstract class PostFilesCallback extends JSONCallback {
    private static final String TAG = "PostFilesCallback";

    public PostFilesCallback(Context context, boolean showProgressBar) {
        super(context, showProgressBar);
    }


    public abstract void onProgress(long totalBytes, long remainingBytes, int index);

}
