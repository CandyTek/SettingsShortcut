package com.xiaoming.opendevtool;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;

@SuppressLint("ExportedPreferenceActivity")
@SuppressWarnings("deprecation")
public class HomeActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_setting1);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(!hasFocus){
			MainTools.exitApp(this);
		}
	}
}
