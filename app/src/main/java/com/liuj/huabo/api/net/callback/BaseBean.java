package com.liuj.huabo.api.net.callback;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by liujun on 2020/4/9.
 */
public class BaseBean {


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
