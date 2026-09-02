package com.liuj.huabo.util;

import android.os.Build;


/**
 * Created by liujun on 2020/4/8.
 */
public class DeviceInfoUtil {


    public static String getSerialNum() {
//Build.getSerial()
        return Build.SERIAL;
    }

    public static String getDeviceName(){
        return Build.BRAND +"-"+ Build.MODEL;
    }


}
