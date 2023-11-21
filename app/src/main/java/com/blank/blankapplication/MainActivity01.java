package com.blank.blankapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity01 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.TetherSettings"));
		startActivity(intent);
		finish();
	}
}
