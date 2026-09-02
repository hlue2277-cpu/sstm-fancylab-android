package com.liuj.huabo.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoHelper {


    /**
     * @param path 本地视频路径
     * @param frame  第几帧
     * @return Bitmap
     */
    public static Bitmap getVideoFrame(String path , int frame){
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }

    public static Bitmap getVideoFirstFrame(String path){
        return getVideoFrame(path,1);
    }


    /**
     * 获取视频时长
     * @param path  视频路径
     * @return  返回视频的时长
     */
    public static int getVideoDuration(String path){
        int duration = 0;
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(AppUtil.getContext(), Uri.parse(path));
            mp.prepare();
            duration = mp.getDuration();
            Log.d("onVideoTaken",duration + "");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("onVideoTaken",e.getMessage());
        }finally {
            mp.release();
        }
        return duration;
    }

    /**
     * 调用系统的视频播放
     */
    public static boolean callSystemVideoPlayer(Activity context,String path){
        try {
            Intent intent =new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://"+path);
            intent.setDataAndType(uri, "video/*");
            List<ResolveInfo> resolveInfoList = queryPlayerPackageNameLst(path);
            if(!ListUtil.isEmpty(resolveInfoList)){
                intent.setPackage(resolveInfoList.get(0).activityInfo.packageName);
                intent.setClass(context,resolveInfoList.get(0).activityInfo.getClass());
            }
            context.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(context, "no default player", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    /**
     * 获取支持播放视频的应用名
     * @return
     */
    private static  List<ResolveInfo>  queryPlayerPackageNameLst(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "video/*");
        PackageManager pm = AppUtil.getContext().getPackageManager();
        //所有安装的应用(包括已卸载的但目录还存在的)
        List<ResolveInfo> infos = pm.queryIntentActivities(intent,PackageManager.GET_UNINSTALLED_PACKAGES);
        List<String> packageNameLst = null;
        if (infos != null && infos.size() > 0) {
            packageNameLst = new ArrayList<>(infos.size());
            for(int i=0; i<infos.size(); i++) {
                ResolveInfo info = infos.get(i);
                packageNameLst.add(info.activityInfo.packageName);
                //info.activityInfo.name
            }
        }
        return infos;
    }
}
