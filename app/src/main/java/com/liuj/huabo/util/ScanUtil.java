package com.liuj.huabo.util;

import android.content.Context;

import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;

public class ScanUtil {

    public static ScanUtil scanUtil;

    public ScanInterface scanInterface;

    private long timeStamp;

    public static ScanUtil getInstance(){
        if(scanUtil == null ){
            synchronized (ScanUtil.class){
                if(scanUtil==null){
                    scanUtil = new ScanUtil();
                }
            }
        }
        return scanUtil;
    }

    public void init(Context context){
        scanInterface = new ScanDecode(context);
        scanInterface.initService("true");
    }

    public void getBarCode(ScanFilterResult listener){
        scanInterface.getBarCode(new ScanInterface.OnScanListener() {
            @Override
            public void getBarcode(String s) {
                if(System.currentTimeMillis() - timeStamp >= 150){
                    timeStamp = System.currentTimeMillis();
                    if(listener!=null) listener.result(s);
                }
            }

            @Override
            public void getBarcodeByte(byte[] bytes) {

            }
        });
    }

    public void startScan(){
//        scanInterface.starScan();
    }

    public void stopScan(){
//        scanInterface.stopScan();
    }

    public interface ScanFilterResult{
        void result(String result);
    }

}
