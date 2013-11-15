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

	private final static String fMaxScore = "fMaxScore";

	public static void initDataUtil(Activity ctx) {
		DataUtil.context = ctx;
	}

	/**
	 * 记录最高分
	 * 
	 * @param score
	 */
	public static void putScore(int score) {
		SharedPreferences sharedPref = context
				.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(fMaxScore, score);
		editor.commit();
	}

	/**
	 * 获取最高分
	 * 
	 * @return
	 */
	public static int getMaxScore() {
		SharedPreferences sharedPref = context
				.getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getInt(fMaxScore, 0);
	}
}
