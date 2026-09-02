package com.liuj.huabo.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class DensityUtil {
	
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

	/**
	 * 获取屏幕的密度
	 * @param context
	 * @return
	 */
	public static int getDensityDpi(Context context) {
		return getDisplayMetrics(context).densityDpi;
	}
	
	/**
	 * 获取屏幕的宽度
	 * @param context
	 * @return
	 */
	public static int getDisplayWidth(Context context) {
		return getDisplayMetrics(context).widthPixels;
	}
	
	/**
	 * 获取屏幕的高度
	 * @param context
	 * @return
	 */
	public static int getDisplayHeight(Context context) {
		return getDisplayMetrics(context).heightPixels;
	}
	
	
	public static float getScaledDensity(Context context){
		return getDisplayMetrics(context).scaledDensity;
	}
	
	/**
	 * 获取DisplayMetrics对象
	 * @param context
	 * @return
	 */
	private static DisplayMetrics getDisplayMetrics(Context context){
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wManager.getDefaultDisplay().getMetrics(metric);
		return metric;
	}

	/**
	 * 获取状态栏的高度
	 * @param context
	 * @return
	 */
	public static int getStateBarHeight(Context context){
		Class clazz;
		try {
			clazz = Class.forName("com.android.internal.R$dimen");
			Field field = clazz.getField("status_bar_height");
			Object object = clazz.newInstance();
			int id = Integer.parseInt(field.get(object).toString());
			return context.getResources().getDimensionPixelOffset(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
		
	}
}
