package com.blank.blankapplication;

import android.annotation.SuppressLint;
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
}
