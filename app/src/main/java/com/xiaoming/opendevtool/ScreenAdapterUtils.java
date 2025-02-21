package com.xiaoming.opendevtool;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Realmo
 * @version 1.0.0
 * @name 屏幕适配工具类
 * @email momo.weiye@gmail.com
 * @time 2020/4/8 9:20
 * @describe 简易今日头条 屏幕适配方案
 * https://github.com/feisher/ScreenAdapter
 */
public class ScreenAdapterUtils {
	private static float appDensity;
	private static float appScaledDensity;
	private static DisplayMetrics appDisplayMetrics;
	/**
	 * 用来参照的的width 对应单位dp
	 */
	private static float WIDTH;
	private static float HEIGHT;

	private static boolean isCalcHeight = false;

	/**
	 * @param application
	 * @param value       对应屏幕切图宽度的dp值 如：1920px hdpi  则width = 1920F/1.5
	 *                    <p>
	 *                    使用固定横屏的情况来计算
	 *                    示例 ScreenAdapterUtils.setDensity(this,768f,true,true);
	 */
	public static void setDensity( final Application application,float value,boolean isCalcHeight,boolean autoEffectActivity) {
		appDisplayMetrics = application.getResources().getDisplayMetrics();
		HEIGHT = value;
		WIDTH = value;
		ScreenAdapterUtils.isCalcHeight = isCalcHeight;
		if (autoEffectActivity) {
			registerActivityLifecycleCallbacks(application);
		}
		if (appDensity == 0) {
			//初始化的时候赋值
			appDensity = appDisplayMetrics.density;
			appScaledDensity = appDisplayMetrics.scaledDensity;

			//添加字体变化的监听
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				application.registerComponentCallbacks(new ComponentCallbacks() {
					@Override
					public void onConfigurationChanged( Configuration newConfig) {
						//字体改变后,将appScaledDensity重新赋值
						if (newConfig.fontScale > 0) {
							appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
						}
					}
	
					@Override
					public void onLowMemory() {
					}
				});
			}
		}
	}

	public static void setDefault(Activity activity) {
		float targetDensity = 0;
		try {
			if (isCalcHeight) {
				targetDensity = Math.min(appDisplayMetrics.heightPixels,appDisplayMetrics.widthPixels) / HEIGHT;
			} else {
				targetDensity = Math.max(appDisplayMetrics.heightPixels,appDisplayMetrics.widthPixels) / WIDTH;
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}

		float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
		int targetDensityDpi = (int) (160 * targetDensity);

		/**
		 *
		 * 最后在这里将修改过后的值赋给系统参数
		 *
		 * 只修改Activity的density值
		 */

		DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
		activityDisplayMetrics.density = targetDensity;
		activityDisplayMetrics.scaledDensity = targetScaledDensity;
		activityDisplayMetrics.densityDpi = targetDensityDpi;
	}

	/**
	 * 主要用与WindowManger悬浮窗的ui适配
	 *
	 * @param context
	 */
	public static void setOtherScreenAdapter( Context context) {

		float targetDensity = 0;
		try {
			targetDensity = appDisplayMetrics.widthPixels / WIDTH;
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}

		float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
		int targetDensityDpi = (int) (160 * targetDensity);

		DisplayMetrics activityDisplayMetrics = context.getResources().getDisplayMetrics();
		activityDisplayMetrics.density = targetDensity;
		activityDisplayMetrics.scaledDensity = targetScaledDensity;
		activityDisplayMetrics.densityDpi = targetDensityDpi;
	}

	private static void registerActivityLifecycleCallbacks(Application application) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
				@Override
				public void onActivityCreated(Activity activity,Bundle savedInstanceState) {
					setDefault(activity);
				}
				@Override
				public void onActivityStarted(Activity activity) {
				}
				@Override
				public void onActivityResumed(Activity activity) {
				}
				@Override
				public void onActivityPaused(Activity activity) {
				}
				@Override
				public void onActivityStopped(Activity activity) {
				}
				@Override
				public void onActivitySaveInstanceState(Activity activity,Bundle outState) {
				}
				@Override
				public void onActivityDestroyed(Activity activity) {
	
				}
			});
		}

	}

	private static List<Field> sMetricsFields;

	private ScreenAdapterUtils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * Adapt for the horizontal screen, and call it in {@link Activity#getResources()}.
	 */
	public static Resources adaptWidth(final Resources resources,final int designWidth) {
		float newXdpi = (resources.getDisplayMetrics().widthPixels * 72f) / designWidth;
		applyDisplayMetrics(resources,newXdpi);
		return resources;
	}

	/**
	 * Adapt for the vertical screen, and call it in {@link Activity#getResources()}.
	 */
	public static Resources adaptHeight(final Resources resources,final int designHeight) {
		return adaptHeight(resources,designHeight,false);
	}

	/**
	 * Adapt for the vertical screen, and call it in {@link Activity#getResources()}.
	 */
	public static Resources adaptHeight(final Resources resources,final int designHeight,final boolean includeNavBar) {
		int height = Math.min(resources.getDisplayMetrics().heightPixels,resources.getDisplayMetrics().widthPixels);
		float screenHeight = (height + (includeNavBar ? getNavBarHeight(resources) : 0)) * 72f;
		float newXdpi = screenHeight / designHeight;
		applyDisplayMetrics(resources,newXdpi);
		return resources;
	}
	/**
	 * Adapt for the vertical screen, and call it in {@link Activity#getResources()}.
	 */
	// public static Resources adaptHeight(final Resources resources,final int designHeight,final boolean includeNavBar) {
	// 	float screenHeight = (resources.getDisplayMetrics().heightPixels
	// 			+ (includeNavBar ? getNavBarHeight(resources) : 0)) * 72f;
	// 	float newXdpi = screenHeight / designHeight;
	// 	applyDisplayMetrics(resources,newXdpi);
	// 	return resources;
	// }
	private static int getNavBarHeight(final Resources resources) {
		int resourceId = resources.getIdentifier("navigation_bar_height","dimen","android");
		if (resourceId != 0) {
			return resources.getDimensionPixelSize(resourceId);
		} else {
			return 0;
		}
	}

	/**
	 * @param resources The resources.
	 * @return the resource
	 */
	public static Resources closeAdapt(final Resources resources) {
		float newXdpi = Resources.getSystem().getDisplayMetrics().density * 72f;
		applyDisplayMetrics(resources,newXdpi);
		return resources;
	}

	/**
	 * Value of pt to value of px.
	 *
	 * @param ptValue The value of pt.
	 * @return value of px
	 */
	public static int pt2Px(final float ptValue) {
		DisplayMetrics metrics = MyApplication.getInstance().getResources().getDisplayMetrics();
		return (int) (ptValue * metrics.xdpi / 72f + 0.5);
	}

	/**
	 * Value of px to value of pt.
	 *
	 * @param pxValue The value of px.
	 * @return value of pt
	 */
	public static int px2Pt(final float pxValue) {
		DisplayMetrics metrics = MyApplication.getInstance().getResources().getDisplayMetrics();
		return (int) (pxValue * 72 / metrics.xdpi + 0.5);
	}

	private static void applyDisplayMetrics(final Resources resources,final float newXdpi) {
		resources.getDisplayMetrics().xdpi = newXdpi;
		MyApplication.getInstance().getResources().getDisplayMetrics().xdpi = newXdpi;
		applyOtherDisplayMetrics(resources,newXdpi);
	}

	private static void applyOtherDisplayMetrics(final Resources resources,final float newXdpi) {
		if (sMetricsFields == null) {
			sMetricsFields = new ArrayList<>();
			Class resCls = resources.getClass();
			Field[] declaredFields = resCls.getDeclaredFields();
			while (declaredFields != null && declaredFields.length > 0) {
				for (Field field : declaredFields) {
					if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
						field.setAccessible(true);
						DisplayMetrics tmpDm = getMetricsFromField(resources,field);
						if (tmpDm != null) {
							sMetricsFields.add(field);
							tmpDm.xdpi = newXdpi;
						}
					}
				}
				resCls = resCls.getSuperclass();
				if (resCls != null) {
					declaredFields = resCls.getDeclaredFields();
				} else {
					break;
				}
			}
		} else {
			applyMetricsFields(resources,newXdpi);
		}
	}

	private static void applyMetricsFields(final Resources resources,final float newXdpi) {
		for (Field metricsField : sMetricsFields) {
			try {
				DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
				if (dm != null) {
					dm.xdpi = newXdpi;
				}
			}
			catch (Exception e) {
				Log.e("AdaptScreenUtils","applyMetricsFields: " + e);
			}
		}
	}

	private static DisplayMetrics getMetricsFromField(final Resources resources,final Field field) {
		try {
			return (DisplayMetrics) field.get(resources);
		}
		catch (Exception e) {
			Log.e("AdaptScreenUtils","getMetricsFromField: " + e);
			return null;
		}
	}
}

