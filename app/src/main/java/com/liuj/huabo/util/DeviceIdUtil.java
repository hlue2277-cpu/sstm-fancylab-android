package com.liuj.huabo.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by liujun on 2020/5/23.
 */
public class DeviceIdUtil {
    //deviceid ->  mac -> android -> serial -> fakeimei

    public static String head = "";



    public static String getDeviceUniqueId(Context paramContext) {
        String str = getDeviceId(paramContext);
        head = "deviceId";
        if (TextUtils.isEmpty(str)) {
            str = getFormatMac();
            head = "mac";
            if (TextUtils.isEmpty(str)) {
                str = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
                head = "androidId";
                if (TextUtils.isEmpty(str)) {
                    str = getSerial();
                    head= "serial";
                    if (TextUtils.isEmpty(str)) {
                        head = "fackImei";
                        return getFakeImei();
                    }
                }

            }
        }
        if(TextUtils.isEmpty(str)){
            return "";
        }else {
            return head+str;
        }
    }



    private static String getMac() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                String str = getMacMoreThanM(AppUtil.getContext());
                if (!TextUtils.isEmpty(str))
                    return str;
            }
            // 6.0以下手机直接获取wifi的mac地址即可
            WifiManager wifiManager = (WifiManager) AppUtil.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            assert wifiManager != null;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null)
                return wifiInfo.getMacAddress();
        } catch (Throwable throwable) {
        }
        return "";
    }

    private static String getFormatMac(){
        return getMac().replace(":","");
    }


    private static String getMacMoreThanM(Context paramContext) {
        try {
            //获取本机器所有的网络接口
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                //获取硬件地址，一般是MAC
                byte[] arrayOfByte = networkInterface.getHardwareAddress();
                if (arrayOfByte == null || arrayOfByte.length == 0) {
                    continue;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (byte b : arrayOfByte) {
                    //格式化为：两位十六进制加冒号的格式，若是不足两位，补0
                    stringBuilder.append(String.format("%02X:", new Object[]{Byte.valueOf(b)}));
                }
                if (stringBuilder.length() > 0) {
                    //删除后面多余的冒号
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                String str = stringBuilder.toString();
                // wlan0:无线网卡 eth0：以太网卡
                if (networkInterface.getName().equals("wlan0")) {
                    return str;
                }
            }
        } catch (SocketException socketException) {
            return null;
        }
        return null;
    }


    private static String getSerial() {
        String str = "";
        if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT < 26) {
            str = Build.SERIAL;
        } else if (Build.VERSION.SDK_INT >= 26) {
            try {
                Class clazz = Class.forName("android.os.Build");
                Method method = clazz.getMethod("getSerial", new Class[0]);
                str = (String) method.invoke(clazz, new Object[0]);
            } catch (Throwable throwable) {
            }
        }
        return str;
    }

    private static String getDeviceId(Context paramContext) {
        String str = "";

        return str;
    }


    private static String getFakeImei() {
        return "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
    }

}
