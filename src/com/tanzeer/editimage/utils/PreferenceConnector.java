package com.tanzeer.editimage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceConnector {
	public static final String PREF_NAME = "CASHONE";
	@SuppressWarnings("deprecation")
	public static final int MODE = Context.MODE_WORLD_WRITEABLE;


	public static final String SAVED_PASSWORD = "SAVED_PASSWORD";
	public static final String Hi_Hunny_Account_Number = "Hi_Hunny_Account_Number";
	public static final String FORGET_PASSWORD_EMAIL = "FORGET_PASSWORD_EMAIL";
	public static final String CLOUD_USERNAME = "CLOUD_USERNAME";
	public static final String CLOUD_PASSWORD = "CLOUD_PASSWORD";

	 public static final String NO_OF_CAM = "NO_OF_CAM";
	 public static final String SELCTED_CAM_FACE = "SELCTED_CAM_FACE";

	public static final String SMTP = "SMTP";
	public static final String PORT_NUMBER = "PORT_NUMBER";
	public static final String EMAIL_USERNAME = "EMAIL_USERNAME";
	public static final String EMAIL_PASS = "EMAIL_PASS";
	public static final String BCC_to = "BCC_to";
	public static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
	public static final String EMAIL_BODY = "EMAIL_BODY";
//	public static final String CUSTOMER_SECURE_KEY = "CUSTOMER_SECURE_KEY";
////	secure_key
//	
//	public static final String ORDER_ID_BLANK = "ORDER_ID_BLANK";

	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();

	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();

	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);

	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static void remove(Context context, String key) {
		getEditor(context).remove(key);

	}

}
