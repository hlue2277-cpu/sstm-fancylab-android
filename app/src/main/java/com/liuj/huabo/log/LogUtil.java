package com.liuj.huabo.log;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.liuj.huabo.common.Constants;
import com.liuj.huabo.util.ListUtil;


import java.util.List;


/**
 * 日志控制
 *
 * @author ZL
 */
public final class LogUtil {


    public static void d(String tag, String msg) {
        if (Constants.IS_DEBUG_MODE) {
//            LogPrinter.d(tag, msg);
            LogUtils.dTag("LogPrinter" + "-" + tag, msg);
        }

    }


    public static void e(String tag, String msg) {
        if (Constants.IS_DEBUG_MODE) {
//            LogPrinter.e(tag, msg);
            LogUtils.eTag("LogPrinter" + "-" + tag, msg);
        }

    }


    public static void json(String tag, Object object) {
        if (Constants.IS_DEBUG_MODE) {
//            LogPrinter.json(LogPrinter.ERROR, tag, object);
            String jsonStr = "invalid json";
            if (object != null) {
                String formatStr = JSONObject.toJSONString(object);
                jsonStr = JsonFormatter.format(JsonFormatter.convertUnicode(formatStr));
            }
            LogUtils.json("LogPrinter" + "-" + tag, jsonStr);
        }
    }


    public static <T> void d(String tag, List<T> list) {
        if (ListUtil.isEmpty(list)) {
            LogUtil.d("LogPrinter" + "-" + tag, "list is empty");
        } else {
            StringBuilder sb = new StringBuilder();
            for (T t : list) {
                sb.append(t.toString()).append("\n");
            }
            LogUtil.d("LogPrinter" + "-" + tag, sb.toString());
        }
    }


    public static void printRequest(String tag, String url, Object params) {
        if (Constants.IS_DEBUG_MODE) {
//            LogPrinter.printRequest(LogPrinter.DEBUG, tag, url, params);
//            LogUtils.dTag("LogPrinter" + "-" + tag, url);
            String jsonStr = "invalid json";
            if (params != null) {
                String formatStr = JSONObject.toJSONString(params);
                jsonStr = JsonFormatter.format(JsonFormatter.convertUnicode(formatStr));
            }
            LogUtils.json("LogPrinter" + "-" + tag, jsonStr);
        }
    }


    public static void printResponse(String tag, String url, Object params) {
        if (Constants.IS_DEBUG_MODE) {
//            LogPrinter.printResponse(LogPrinter.DEBUG, tag, url, params);
//            LogUtils.dTag("LogPrinter" + "-" + tag, url);
            String jsonStr = "invalid json";
            if (params != null) {
                String formatStr = JSONObject.toJSONString(params);
                jsonStr = JsonFormatter.format(JsonFormatter.convertUnicode(formatStr));
            }
            LogUtils.json("LogPrinter" + "-" + tag,url+"\n"+ jsonStr);
        }

    }
}
