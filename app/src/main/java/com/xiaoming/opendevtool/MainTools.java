package com.xiaoming.opendevtool;

import android.app.Activity;
import android.os.Build;

public class MainTools {
	public static void exitApp(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			activity.finishAffinity();
		} else {
			activity.finish();
		}
		MyApplication.mHandler.postDelayed(() -> System.exit(0),700);
	}
}
