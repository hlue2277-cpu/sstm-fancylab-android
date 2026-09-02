package com.liuj.huabo.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;


import java.util.List;

/**
 * Created by liujun on 2020/5/13.
 */
public class UIUtil {

    public static void expandTouchArea(final View view, final int size) {
        final View parentView = (View) view.getParent();
        parentView.post(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                view.getHitRect(rect);

                rect.top -= size;
                rect.bottom += size;
                rect.left -= size;
                rect.right += size;

                parentView.setTouchDelegate(new TouchDelegate(rect, view));
            }
        });
    }

    /**
     * 判断是否是有效点击
     */
    public static long timeStamp = 0;
    public static boolean isValidClick(){
        if(System.currentTimeMillis() -timeStamp >2000){
            timeStamp = System.currentTimeMillis();
            return true;
        }else {
            return false;
        }
    }

    public static boolean isValidClick(int interval){
        if(System.currentTimeMillis() -timeStamp >interval){
            timeStamp = System.currentTimeMillis();
            return true;
        }else {
            return false;
        }
    }

    private static int intIsBatChk=0;
    public static void setBatCheckOrNot(int iIsBat){
        intIsBatChk=iIsBat;
    }

    public static int getBatCheckOrNot(){
        return intIsBatChk;
    }


    /**
     * 判断某个Activity 界面是否在前台
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean  isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if( am == null)
            return false;
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            return cpn != null && className.equals(cpn.getClassName());
        }
        return false;
    }


    /**
     *
     * @param context
     * @return　　　false 前台　　ture 后台
     */
    public static boolean isAppBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance + ",context.getClass().getName()=" + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    //2026.3.19
    private static String  LastHttpServerErr = "";
    public static void setLastHttpServerErr(String strErr)
    {
        LastHttpServerErr=strErr;
    }

    public static String getLastHttpServerErr()
    {
        return LastHttpServerErr;
    }


    //2026.8.7
    private static String  LastCertType = "";
    public static void setLastCertType(String strTyp)
    {
        LastCertType=strTyp;
    }

    public static String getLastCertType()
    {
        return LastCertType;
    }

}
