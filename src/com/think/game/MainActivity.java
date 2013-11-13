package com.think.game;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.think.game.f.FGame;

public class MainActivity extends AndroidApplication {

	private FGameAct fAct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fAct = new FGameAct();
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
		case R.id.action_exit:
			finish();
			System.exit(0);
		case R.id.action_start_end:
			boolean isStart = item.getTitle().equals(
					getString(R.string.action_start));
			if (isStart) {
				// 点击为开始
				fAct.initGame(new FGame());
				item.setTitle(getString(R.string.action_end));
			} else {
				// 点击为结束
				item.setTitle(getString(R.string.action_start));
			}
			break;
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
}
