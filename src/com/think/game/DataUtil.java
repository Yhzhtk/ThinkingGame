package com.think.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 记录游戏数据
 * @author gudh
 * @date 2013-11-15
 */
public class DataUtil {

	private static Activity context;

	private final static String fMaxScore1 = "fMaxScore1";
	private final static String fMaxScore2 = "fMaxScore2";
	private final static String fMaxScore3 = "fMaxScore3";

	public static void initDataUtil(Activity ctx) {
		DataUtil.context = ctx;
	}

	/**
	 * 记录最高分
	 * 
	 * @param score
	 */
	public static void putScore(int level, int score) {
		SharedPreferences sharedPref = context
				.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getLevelKey(level), score);
		editor.commit();
	}

	/**
	 * 获取最高分
	 * 
	 * @return
	 */
	public static int getMaxScore(int level) {
		SharedPreferences sharedPref = context
				.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getInt(getLevelKey(level), 0);
	}
	
	private static String getLevelKey(int level){
		switch(level){
		case 1:
			return fMaxScore1;
		case 2:
			return fMaxScore2;
		case 3:
			return fMaxScore3;
		}
		return "";
	}
}
