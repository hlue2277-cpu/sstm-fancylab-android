package com.liuj.huabo.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Base64;


import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;



public class BitmapUtil {


    public static Bitmap scaleBitmap(Bitmap origin, int newW, int newH, boolean isRation) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();

        Matrix matrix = new Matrix();
        float ratioW = (newW * 1f) / (width * 1f);
        float ratioH = (newH * 1f) / (height * 1f);
        if (isRation) {
            float ratio;
            if (width > height) {
                ratio = ratioW;
            } else {
                ratio = ratioH;
            }
            if (ratio >= 1) {
                matrix.preScale(1, 1);
            } else {
                matrix.preScale(ratio, ratio);
            }
        } else {
            if (ratioW >= 1) {
                ratioW = 1;
            }
            if (ratioH >= 1) {
                ratioH = 1;
            }
            matrix.preScale(ratioW, ratioH);
        }
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        /*
        if (newBM.equals(origin)) {
            return newBM;
        }
         */
        origin.recycle();
        return newBM;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String bitmapToBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] bytes = baos.toByteArray();
         return Base64.encodeToString(bytes,Base64.DEFAULT);//
//        return Base64.getEncoder().encodeToString(bytes);
    }


}
