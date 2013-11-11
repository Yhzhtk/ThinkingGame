package com.think.game;

import android.os.Bundle;
import android.view.Menu;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.think.game.f.FGameUtil;

public class MainActivity extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize(new FGame(), false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}

class FGame implements ApplicationListener {

	// 存储所有颜色对象
	static Texture[] textures;

	// 绘图用的SpriteBatch
	private SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch(); // 实例化

		int length = FGameUtil.getAllColors().length;
		textures = new Texture[length];

		for (int i = 0; i < length; i++) {
			Pixmap p = new Pixmap(2, 2, Format.RGBA8888);
			p.setColor(FGameUtil.getAllColors()[i]);
			p.fillRectangle(0, 0, 2, 2);
			textures[i] = new Texture(p, false);
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1); //背景色
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(textures[0], 0, 0, 20, 20);
		batch.draw(textures[1], 40, 20, 20, 20);
		batch.draw(textures[2], 20, 40, 20, 20);
		batch.draw(textures[3], 40, 60, 20, 20);
		batch.draw(textures[4], 60, 40, 20, 20);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}
}
