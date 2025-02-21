package com.xiaoming.opendevtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent() != null && getIntent().getData() != null) {
			Intent intent = new Intent(getIntent().getData().toString());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		MainTools.exitApp(this);
		// Settings.ACTION_WIFI_SETTINGS;
	}
}
