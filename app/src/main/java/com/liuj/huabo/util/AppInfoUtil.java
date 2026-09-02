package com.liuj.huabo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;

/**
 * Created by liujun on 2020/4/8.
 */
public class AppInfoUtil {


    //获取app版本
    public static String getAppVersion(){
        Context context = AppUtil.getContext();
        String versionName;
        PackageManager pManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = pManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "Unknown";
            e.printStackTrace();
        }
        return versionName;
    }







}
