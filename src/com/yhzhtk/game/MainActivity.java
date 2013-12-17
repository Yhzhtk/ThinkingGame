package com.yhzhtk.game;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.think.game.DataUtil;
import com.think.game.FGameAct;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends AndroidApplication {

	private FGameAct fAct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DataUtil.initDataUtil(this);
		
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels;  // 屏幕宽度（像素）
//        int height = metric.heightPixels;  // 屏幕高度（像素）
//        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        
        fAct = new FGameAct(metric);
		// 初始化游戏
		initialize(fAct, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 菜单事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_level1:
			fAct.initLevel(1, true);
			break;
		case R.id.action_level2:
			fAct.initLevel(2, true);
			break;
		case R.id.action_level3:
			fAct.initLevel(3, true);
			break;
		case R.id.action_exit:
			finish();
			System.exit(0);
			break;
		case R.id.action_about:
			String aboutStr = "开源项目，基于LibGDX框架开发的小游戏\n\n开发人：Yhzhtk、OsBelief\n\n项目地址：https://github.com/Yhzhtk/ThinkingGame";
			new AlertDialog.Builder(this).setTitle("关于").setMessage(aboutStr)
					.setPositiveButton("确定", null).show();
			break;
		case R.id.action_help:
			String helpStr = "FGame规则：\n\n  点击空格子，空格子上下左右最近一个非空格子（最多四个，边缘处可能小于四个）中有两个或两个以上相同的格子，则可消除得分。\n\n得分提示：\n\n  点空格子一次消除2个加12分，3个加27分，4个加48分。游戏剩余时间会加分，每剩一秒加12分，无上限。游戏结束时不剩有色格子加100分，剩一个加80，依次递减。";
			new AlertDialog.Builder(this).setTitle("游戏帮助").setMessage(helpStr)
					.setPositiveButton("确定", null).show();
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	// 实现按两次返回键退出
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 友盟统计
	 */
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	/**
	 * 友盟统计
	 */
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
