package com.liuj.huabo.log;


import android.text.TextUtils;
import android.util.Log;


/**
 * Created by liujun on 2020/4/9.
 */
public class LogPrinter {
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;


    private static final String TAG = "LogPrinter";
    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    public synchronized static void d(String tag, String message) {
        logTopBorder(DEBUG, tag);
        logContent(DEBUG, tag, message);
        logBottomBorder(DEBUG, tag);
    }

    public synchronized static void e(String tag, String message) {
        logTopBorder(ERROR, tag);
        logContent(ERROR, tag, message);
        logBottomBorder(ERROR, tag);
    }


    public synchronized static void json(int logType, String tag, Object obj) {
        String jsonStr = "invalid json";
        if (obj != null) {
            String formatStr = com.alibaba.fastjson.JSONObject.toJSONString(obj);
            jsonStr = JsonFormatter.format(JsonFormatter.convertUnicode(formatStr));
        }
        logContent(logType, tag, jsonStr);
    }


    public synchronized static void printRequest(int logType, String tag, String url, Object params) {
        logTopBorder(logType, tag);
        log(logType, tag, HORIZONTAL_DOUBLE_LINE + " request : " + url);
        logDivider(logType, tag);
        json(logType, tag, params);
        logBottomBorder(logType, tag);
    }


    public synchronized static void printResponse(int logType, String tag, String url, Object params) {
        logTopBorder(logType, tag);
        log(logType, tag, HORIZONTAL_DOUBLE_LINE + " response : " + url);
        logDivider(logType, tag);
        json(logType, tag, params);
        logBottomBorder(logType, tag);
    }


    private static void logTopBorder(int logType, String tag) {
        log(logType, tag, TOP_BORDER);
    }


    private static void logBottomBorder(int logType, String tag) {
        log(logType, tag, BOTTOM_BORDER);
    }

    private static void logDivider(int logType, String tag) {
        log(logType, tag, MIDDLE_BORDER);
    }

    private static void logContent(int logType, String tag, String content) {
        if (!TextUtils.isEmpty(content)) {
            String[] lines = content.split(System.getProperty("line.separator"));
            for (String line : lines) {
                log(logType, tag, HORIZONTAL_DOUBLE_LINE + " " + line);
            }
        }
    }

    private static void log(int logType, String tag, String line) {
//        if (TextUtils.isEmpty(tag)) {
//            tag = "LogPrinter";
//        }
        tag = TAG + "-" + tag;
        switch (logType) {
            case ERROR:
                Log.e(tag, line);
                break;
            case INFO:
                Log.i(tag, line);
                break;
            case VERBOSE:
                Log.v(tag, line);
                break;
            case WARN:
                Log.w(tag, line);
                break;
            case ASSERT:
                Log.wtf(tag, line);
                break;
            case DEBUG:
            default:
                Log.d(tag, line);
                break;
        }
    }

}
