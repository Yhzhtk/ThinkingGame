package com.think.game;

import java.lang.reflect.Method;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.think.game.f.FGame;
import com.think.game.f.FGameUtil;

/**
 * FGame控制主类
 * 
 * @author gudh
 * @date 2013-11-12
 */
public class FGameAct implements ApplicationListener {

	// 存储所有颜色对象
	private static String[] imgPaths;
	private static Texture[] texts;
	private static Image[] rects;
	private static TextureRegionDrawable btnUpDraw;
	private static TextureRegionDrawable btnDownDraw;
	private static Texture backText;

	private static Music loopMusic;
	private static Sound startSound;
	private static Sound scoreSound;
	private static Sound errorSound;

	// 游戏参数
	public FGameParameter para;

	// 绘图资源
	private SpriteBatch batch;
	private Stage stage;
	private BitmapFont font;
	// private Skin skin;

	// 游戏实例
	private FGame fgame;

	// 控件
	Label lab; // 得分标签
	TextButton btn1; // 按钮1
	TextButton btn2; // 按钮2
	private Image[][] gameArea; // 存储每个区块

	// 标志是否第一次加载
	private boolean isInit = false;

	public FGameAct() {
		// 初始化游戏实例
		fgame = new FGame();
	}

	/**
	 * 初始化资源信息，只需一次，在第一次加载时
	 */
	private void initProperties() {
		loopMusic = Gdx.audio.newMusic(Gdx.files.internal("music/ten.mp3"));
		loopMusic.setLooping(true);
		loopMusic.setVolume(0.3f);
		loopMusic.play();
		

		startSound = Gdx.audio.newSound(Gdx.files.internal("music/start.wav"));
		scoreSound = Gdx.audio.newSound(Gdx.files.internal("music/score.wav"));
		errorSound = Gdx.audio.newSound(Gdx.files.internal("music/error.wav"));

		// 初始化颜色
		int length = FGameUtil.getAllColors().length;
		imgPaths = new String[length];
		texts = new Texture[length];
		rects = new Image[length];

		for (int i = 0; i < length; i++) {
			imgPaths[i] = "image/" + i + ".png";
			texts[i] = new Texture(Gdx.files.internal(imgPaths[i]));
			rects[i] = new Image(texts[i]);
		}

		// 初始化font和skin文件
		font = new BitmapFont(Gdx.files.internal("default.fnt"),
				Gdx.files.internal("default.png"), false);
		// skin = new Skin(Gdx.files.internal("uiskin.json"));

		// 初始化背景
		btnUpDraw = new TextureRegionDrawable(new TextureRegion(new Texture(
				Gdx.files.internal("btnUp.png"))));
		btnDownDraw = new TextureRegionDrawable(new TextureRegion(new Texture(
				Gdx.files.internal("btnDown.png"))));
		backText = new Texture(Gdx.files.internal("back.png"));

		// 加载参数
		para = FGameParameter.getParaInstance(fgame.getRows(), fgame.getCols());

		batch = new SpriteBatch();
		stage = new Stage(para.getScreenWidth(), para.getScreenHeight(), true,
				batch);

		Image back = new Image(backText);
		back.setSize(para.getScreenWidth(), para.getScreenHeight());
		stage.addActor(back);

		// 添加按钮和标签
		TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle(
				btnUpDraw, btnDownDraw, btnDownDraw, font);

		btn1 = new TextButton("Start", btnStyle);
		setBound(btn1, para.getBtn1Bound());
		stage.addActor(btn1);

		btn2 = new TextButton("Pause", btnStyle);
		setBound(btn2, para.getBtn2Bound());
		stage.addActor(btn2);

		lab = new Label("Come on!\n\nScore： 0", new LabelStyle(font, Color.RED));
		setBound(lab, para.getLabelBound());
		stage.addActor(lab);

		// 初始化绘图区域
		gameArea = new Image[para.getRows()][para.getCols()];
		for (int i = 0; i < para.getRows(); i++) {
			for (int j = 0; j < para.getCols(); j++) {
				gameArea[i][j] = getImageByPos(i, j);
				stage.addActor(gameArea[i][j]);
			}
		}

		// 添加按钮事件
		stage.addListener(new EventListener() {
			@Override
			public boolean handle(Event arg0) {
				if (arg0.getTarget() == btn1) {
					Log.d("Event", "Btn1 Click " + btn1.getText() + " Start");
					if (btn1.getText().toString().equals("Start")) {
						startSound.play();
						btn1.setText("End");
						initGame(null);
					} else {
						btn1.setText("Start");
					}
					return true;
				} else if (arg0.getTarget() == btn2) {
					Log.d("Event", "Btn2 Click");
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 玩一次游戏，参数为坐标（左下角为0，0）
	 * 
	 * @param x
	 * @param y
	 */
	public void playOnce(int x, int y) {
		int res = fgame.click(x, y);
		Log.i("PlayResult", "玩游戏结果：" + res);
		if (res == 2) {
			// 重绘界面
			repaint();
			scoreSound.play();
		} else {
			errorSound.play();
		}
		if (res > 0) {
			// 更新游戏得分
			updateScore(fgame.getScore());
		}
	}

	/**
	 * 以指定行列初始化游戏信息，开始游戏时初始化
	 * 
	 * @param fgame
	 *            以fgame初始化数据
	 */
	public void initGame(FGame fgame) {
		// 将fgame置为全局
		if (this.fgame != fgame) {
			if (fgame == null) {
				fgame = new FGame();
			}
			this.fgame = fgame;
		}

		// 初始化颜色
		for (int i = 0; i < para.getRows(); i++) {
			for (int j = 0; j < para.getCols(); j++) {
				updateImage(i, j);
			}
		}
	}

	@Override
	public void create() {
		if (!isInit) {
			// 第一次加载时初始化资源信息
			initProperties();
			isInit = true;
		}

		// 初始化游戏
		initGame(this.fgame);

		// 添加事件，先判断是否游戏点击，后判断按钮等
		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(new FGameInputProcessor(this));
		multi.addProcessor(stage);
		Gdx.input.setInputProcessor(multi);

		Log.d("GdxEvent", "create");
	}

	@Override
	public void dispose() {
		Log.d("GdxEvent", "dispose");
	}

	@Override
	public void pause() {
		Log.d("GdxEvent", "pause");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// 绘图
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		Log.d("GdxEvent", "resize " + width + " " + height);
	}

	@Override
	public void resume() {

		loopMusic.play();

		// 当pause时EGL的资源被销毁，resume时需要加载
		for (int i = 0; i < FGameUtil.getAllColors().length; i++) {
			texts[i] = new Texture(Gdx.files.internal(imgPaths[i]));
			rects[i] = new Image(texts[i]);
		}
		repaint();
		Log.d("GdxEvent", "resume");
	}

	/**
	 * 重绘界面
	 */
	private void repaint() {
		for (int i = 0; i < fgame.getRows(); i++) {
			for (int j = 0; j < fgame.getCols(); j++) {
				updateImage(i, j);
			}
		}
	}

	/**
	 * 利用反射设置控件的位置和大小
	 * 
	 * @param obj
	 * @param bound
	 */
	private void setBound(Object obj, Rectangle bound) {
		try {
			Method posMethod = obj.getClass().getMethod("setPosition",
					float.class, float.class);
			posMethod.invoke(obj, bound.getX(), bound.getY());

			Method sizeMethod = obj.getClass().getMethod("setSize",
					float.class, float.class);
			sizeMethod.invoke(obj, bound.getWidth(), bound.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据x,y获取Image，并设置好位置和大小
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Image getImageByPos(int x, int y) {
		int index = fgame.getRcs()[x][y];
		// 从text中生成一个Image对象
		Image m = new Image(texts[index]);
		float xx = para.getGameBound().getX() + x * para.getRectSize()[0];
		float yy = para.getGameBound().getY() + y * para.getRectSize()[1];
		m.setPosition(xx, yy);
		m.setSize(para.getNodeSize()[0], para.getNodeSize()[1]);
		return m;
	}

	/**
	 * 更新某个位置的颜色
	 * 
	 * @param x
	 * @param y
	 */
	private void updateImage(int x, int y) {
		int index = fgame.getRcs()[x][y];
		gameArea[x][y].setDrawable(new TextureRegionDrawable(new TextureRegion(
				texts[index])));
	}

	/**
	 * 更新得分
	 * 
	 * @param score
	 */
	private void updateScore(int score) {
		lab.setText("Come on!\n\nScore: " + score);
	}
}