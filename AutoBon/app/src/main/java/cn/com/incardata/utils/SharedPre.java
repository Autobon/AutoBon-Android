package cn.com.incardata.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPre {
	private final static String SHARED_PRE_NAME = "AutoBon";

	public static String getString(Context context, String key){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		return shared.getString(key, "");
	}
	
	public static float getFloat(Context context, String key){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		return shared.getFloat(key, 0.0f);
	}
	
	public static int getInt(Context context, String key){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		return shared.getInt(key, -1);
	}
	
	public static boolean getBoolean(Context context, String key, boolean defValue){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		return shared.getBoolean(key, defValue);
	}
	
	public static String getString(Context context, String key, String defValue){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		return shared.getString(key, defValue);
	}
	
	public static float getFloat(Context context, String key, float defValue){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		return shared.getFloat(key, defValue);
	}
	
	public static int getInt(Context context, String key, int defValue){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		return shared.getInt(key, defValue);
	}
	
	public static void setSharedPreferences(Context context, String key, String value){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void setSharedPreferences(Context context, String key, float value){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	public static void setSharedPreferences(Context context, String key, int value){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static void setSharedPreferences(Context context, String key, boolean value){
		SharedPreferences shared = context.getSharedPreferences(SHARED_PRE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
}
