package com.liuj.huabo.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

/**
 * Created by liujun on 2020/4/8.
 */
public class AppUtil {

    private static Context mContext;

    public static void init(Context context){
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }


    /**
     * 判断SDK是否存在
     * @return
     */
    public static  boolean isSDCardExist(){
        return !Environment.isExternalStorageRemovable() || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void sleep(long millon){
        try {
            Thread.sleep(millon);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void restartApplication(){
        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //与正常页面跳转一样可传递序列化数据,在Launch页面内获得
        intent.putExtra("REBOOT","reboot");
        getContext().startActivity(intent);
    }




}
