package com.liuj.huabo.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Date;

/**
 * @author ：chezi008 on 2018/8/2 0:02
 * @description ：
 * @email ：chezi008@163.com
 */
public class FileUtils {
    public static final String TAG = FileUtils.class.getSimpleName();
    private static int BUFFER_LEN = 1024*16;

    public static Date parseDate(File file) {
        ExifInterface exif = null;
        Date date1 = null;
        try {
            exif = new ExifInterface(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String date = exif.getAttribute(ExifInterface.TAG_DATETIME);
        try {
            if (!TextUtils.isEmpty(date)) {
                date1 = DateUtils.convertToDate(date);
            } else {
//                date1 = DateUtils.convertToDate("1995:03:13 22:38:20");
                date1 = new Date(file.lastModified());
            }
            Log.i("date", date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static boolean isImageFile(String fName) {
        boolean re;
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        return (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp"));
    }

    public static String obtainFileName(String path) {
        File file = new File(path);
        return file.getName();
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
        } else if (file.isFile()) {
            return deleteFile(fileName);
        }
        return false;
    }



    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.d("FileUtils","删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    public static boolean deleteFile(File file) {
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.d("FileUtils","删除单个文件" + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + "不存在！");
            return false;
        }
    }


    public static void writeBytesToFile(byte[] srcbyte, Context mContext,String name) throws IOException{
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable())
            path = mContext.getExternalFilesDir(null).getAbsolutePath() + "/crash/log";
        else
            path = mContext.getFilesDir().getAbsolutePath() + "/crash/log";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        OutputStream out = new FileOutputStream(dir+name+"aaa.jpg");
        InputStream is = new ByteArrayInputStream(srcbyte);
        byte[] buff = new byte[1024];
        int len = 0;
        while((len=is.read(buff))!=-1){
            out.write(buff, 0, len);
        }
        is.close();
        out.close();
    }


    /**
     * 保存内容到文件
     * @param mContext  上下文
     * @param content   要保存的内容
     * @param isAppend  是否追加
     * @param filename  文件名
     */
    public static void saveLog(Context mContext,String content,boolean isAppend,String filename) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/coco_app_log/";
        FileWriter fwriter = null;
        try {
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(path+filename+".txt");
            if(!file.exists()){
                file.createNewFile();
            }
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(file.getPath(), isAppend);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(fwriter!=null) {
                    fwriter.flush();
                    fwriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }



    public static void writeFile(Context mContext,String content,boolean isAppend,String filename,String path) {
        FileWriter fwriter = null;
        try {
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(path+filename+".json");
            if(!file.exists()){
                file.createNewFile();
            }
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(file.getPath(), isAppend);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(fwriter!=null) {
                    fwriter.flush();
                    fwriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }




    //发送文件改变广播
    public static void notifySystemToScan(Context context, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }


    /**
     * 保存云台日志到本地   不追加
     * @param byteFile  云台二进制数据
     * @param filename  log名
     */
    public static void saveFileByBinary(byte[] byteFile,String filename){
        saveFileByBinary(byteFile,filename,false);
    }

    /**
     * 保存云台日志到本地   追加
     * @param byteFile  云台二进制数据
     * @param filename  log名
     */
    public static void saveFileByBinaryAppend(byte[] byteFile,String filename){
        saveFileByBinary(byteFile,filename,true);
    }

    /**
     *  根据指定的二进制流字符串保存文件并返回保存路径
     */
    private static void saveFileByBinary(byte[] byteFile,String filename,boolean isAppend){                   /***加载附件***/

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/coco_cp_log/";

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(path+filename+".log");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            InputStream is = new ByteArrayInputStream(byteFile);
            FileOutputStream os = new FileOutputStream(file,isAppend);
            byte[] b = new byte[1024];
            int len = 0;
            //开始读取

            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            //完毕关闭所有连接
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String md5Checksum(File file) {
        byte[] md5 = doMd5(file);
        return Md5Builder.getHex(md5);
    }
    public static String md5CheckSum(byte[] content) {


        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(content, 0, content.length);

            return Md5Builder.getHex(md.digest());
        } catch (Exception e) {
        }
        return null;
    }
    public static byte[] doMd5(File file) {
        byte[] buffer = new byte[BUFFER_LEN];
        MessageDigest md;
        try(InputStream is = new BufferedInputStream(new FileInputStream(file), BUFFER_LEN);) {
            int numRead;
            md = MessageDigest.getInstance("MD5");
            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    md.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            return md.digest();
        } catch (Exception ignore) {
        }
        return new byte[0];
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


}
