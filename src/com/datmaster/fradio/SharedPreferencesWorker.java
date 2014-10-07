package com.datmaster.fradio;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWorker {
	
	private static final String PREFERENCES = "radio_settings";
	private static final String SETTINGS_STATION = "selected_station";
	
	private static SharedPreferences sharedPreferences;
	
	public static void init(Context context) {
		sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
	}
	
	public static void setStationId(int station) {
		sharedPreferences.edit().putInt(SETTINGS_STATION, station).commit();
	}
	
	public static int getStationId() {
		return sharedPreferences.getInt(SETTINGS_STATION, 0);
	}
}
