package com.liuj.huabo.common;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.liuj.huabo.api.net.RequestConfig;
import com.liuj.huabo.util.AppInfoUtil;
import com.liuj.huabo.util.AppUtil;
import com.liuj.huabo.util.CrashHandler;
import com.liuj.huabo.util.DeviceIdUtil;
import com.liuj.huabo.util.ScanUtil;
import com.tencent.bugly.crashreport.CrashReport;

public class MyApplication  extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.init(this);
        CrashHandler.getInstance().init(this);
        init();
//        setNewLandOutPutMode();
        CrashHandler.getInstance().init(this);
        SharedPreferences sp =getSharedPreferences(Constants.App_Config,MODE_PRIVATE);
        //RequestConfig.Url.URL_SERVER= sp.getString(Constants.Server_Url,"http://sstm.lengliwh.com");
        //RequestConfig.Url.URL_SERVER= sp.getString(Constants.Server_Url,"http://192.168.2.203");//暂时
        //RequestConfig.Url.URL_SERVER= sp.getString(Constants.Server_Url,"http://172.18.17.31:86");
        RequestConfig.Url.URL_SERVER= sp.getString(Constants.Server_Url,"http://172.18.19.31:85");
        RequestConfig.Url.refreshParentUrl();
        ScanUtil.getInstance().init(this);
        CrashReport.initCrashReport(getApplicationContext(), "f882e6ecd0", false);
        Constants.APP_VERSION = AppInfoUtil.getAppVersion();
    }

    private void init(){
        Constants.DEVICE_ID = DeviceIdUtil.getDeviceUniqueId(this);
    }

    public void  setNewLandOutPutMode(){
        Intent intent =new  Intent("ACTION_BAR_SCANCFG");
        intent.putExtra("EXTRA_SCAN_MODE",3);
        intent.putExtra("EXTRA_SCAN_AUTOEND",1);
        sendBroadcast(intent);
    }

}
