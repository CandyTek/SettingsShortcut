package com.blank.blankapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取元数据
		try {
			ActivityInfo appInfo = getPackageManager().getActivityInfo(this.getComponentName(),
					PackageManager.GET_META_DATA);
			Bundle bundle = appInfo.metaData;

			String flags = bundle.getString("action");
			Intent intent =  new Intent(flags);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (PackageManager.NameNotFoundException ignored) {}
		finish();
		// Settings.ACTION_ACCESSIBILITY_SETTINGS
		// Settings.ACTION_LOCALE_SETTINGS
		// Settings.ACTION_SETTINGS
		// Settings.ACTION_MANAGE_WRITE_SETTINGS
		Settings.ACTION_MANAGE_OVERLAY_PERMISSION
	}
}
