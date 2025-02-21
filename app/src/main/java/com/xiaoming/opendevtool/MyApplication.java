package com.xiaoming.opendevtool;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

public class MyApplication extends Application {
	private static Application instance;
	@SuppressLint("CheckResult")
	public static final Handler mHandler = new Handler(Looper.getMainLooper());
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		float dpi = displayMetrics.densityDpi;
		if (dpi < 640) {
			// Apply the screen density adjustment
			ScreenAdapterUtils.setDensity(this, 640f, true, true);
		}
	}

	public static Application getInstance() {
		return instance;
	}
}
