package com.think.game;

import java.lang.reflect.Method;

import android.util.DisplayMetrics;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

	// 标志系统像素等
	private DisplayMetrics metric;

	// 存储所有颜色对象
	private static String[] imgPaths;
	private static Texture[] texts;
	private static Image[] rects;
	private static TextureRegionDrawable btnUpDraw;
	private static TextureRegionDrawable btnDownDraw;
	private static TextureRegionDrawable msgDraw;
	private static Texture backText;
	private static Image processBar;
	private static Table gameTab;
	private static Table controlTab;

	private static Music loopMusic;
	private static Sound startSound;
	private static Sound scoreSound;
	private static Sound errorSound;
	private static Sound overSound;
	private static Sound cationSound;

	// 游戏参数
	public FGameParameter para;

	// 游戏时间，如果正玩着表示开始时间，如果暂停的话表示已经玩的时间
	private long playTime = 0;
	private long lastTime = 0;

	// 游戏状态
	public final static int NOTSTART = 0;
	public final static int START = 1;
	public final static int PAUSE = 2;
	public final static int RESUME = 3;
	public final static int END = 4;
	public int playState = END;

	// 绘图资源
	private SpriteBatch batch;
	private Stage stage;
	private BitmapFont font;
	// private Skin skin;

	// 游戏实例
	private FGame fgame;

	// 控件
	private Label lab; // 得分标签
	private TextButton btn1; // 按钮1
	private TextButton btn2; // 按钮2
	private Image[][] gameArea; // 存储每个区块

	private Image[] disAppearImg;// 消失的区块

	private boolean msgShow; // 消息是否正在显示
	private TextButton msgBox; // 消息Label
	private long msgShowTime; // 需要显示的时间
	private long msgStartTime; // 消息开始显示时间

	private boolean updateLevel; // 记录当前是否在更新等级，如果是不更新界面
	
	private FGameInputProcessor inputPro; // 事件处理器
	
	// 标志是否第一次加载
	private boolean isInit = false;

	public FGameAct(DisplayMetrics metric) {
		this.metric = metric;
		// 初始化游戏实例
		fgame = new FGame(6, 6);
	}

	/**
	 * 初始化资源信息，只需一次，在第一次加载时
	 */
	private void initProperties() {
		loopMusic = Gdx.audio.newMusic(Gdx.files.internal("music/back.mp3"));
		loopMusic.setLooping(true);
		loopMusic.setVolume(0.3f);
		loopMusic.play();

		startSound = Gdx.audio.newSound(Gdx.files.internal("music/start.wav"));
		scoreSound = Gdx.audio.newSound(Gdx.files.internal("music/score.wav"));
		errorSound = Gdx.audio.newSound(Gdx.files.internal("music/error.wav"));
		cationSound = Gdx.audio
				.newSound(Gdx.files.internal("music/cation.wav"));
		overSound = Gdx.audio.newSound(Gdx.files.internal("music/over.wav"));

		// 初始化颜色
		int length = FGameUtil.getAllColors().length;
		imgPaths = new String[length];
		texts = new Texture[length];
		rects = new Image[length];

		disAppearImg = new Image[4];

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
		msgDraw = new TextureRegionDrawable(new TextureRegion(new Texture(
				Gdx.files.internal("msg.png"))));

		para = FGameParameter.getParaInstance(1, metric);

		batch = new SpriteBatch();
		stage = new Stage(para.getScreenWidth(), para.getScreenHeight(), true,
				batch);

		Image back = new Image(backText);
		back.setSize(para.getScreenWidth(), para.getScreenHeight());
		stage.addActor(back);

		// 添加按钮和标签
		controlTab = new Table();
		setBound(controlTab, para.getControlBound());
		stage.addActor(controlTab);

		processBar = new Image(new Texture(Gdx.files.internal("process.png")));
		controlTab.add(processBar).colspan(3).height(para.getProcessHeight())
				.expandX().fillX().spaceBottom(para.getProcessBottomSpace());

		controlTab.row();

		TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle(
				btnUpDraw, btnDownDraw, btnDownDraw, font);

		btn1 = new TextButton("开始", btnStyle);
		controlTab.add(btn1).expandX().height(para.getBtnHeight()).bottom()
				.spaceLeft(para.getBtnLeftSpace()).fillX();

		btn2 = new TextButton("暂停", btnStyle);
		btn2.setDisabled(true);
		controlTab.add(btn2).expandX().height(para.getBtnHeight()).bottom()
				.spaceLeft(para.getBtnLeftSpace()).fillX();

		lab = new Label("得分: 0", new LabelStyle(font, null));
		controlTab.add(lab).height(para.getBtnHeight()).bottom().expandX()
				.center();

		TextButton.TextButtonStyle msgStyle = new TextButton.TextButtonStyle(
				msgDraw, msgDraw, msgDraw, font);
		msgBox = new TextButton("Ready!", msgStyle);

		// 添加按钮事件
		stage.addListener(new EventListener() {
			@Override
			public boolean handle(Event arg0) {
				if (arg0.getTarget() == btn1) {
					if (playState == END || playState == NOTSTART) {
						playTime = 0;
						startSound.play();
						updateScore(0);
						// 更新状态
						updateState(START);
						initGame(null);
					} else {
						updateState(END);
					}
					Log.d("Event", "Btn1 Click");
					return true;
				} else if (arg0.getTarget() == btn2) {
					if (playState == START) {
						updateState(PAUSE);
					} else {
						updateState(RESUME);
					}
					Log.d("Event", "Btn2 Click");
					return true;
				} else if (arg0.getTarget() == msgBox) {
					if (System.currentTimeMillis() - msgStartTime > msgShowTime) {
						hideMsg();
					}
					return true;
				} else if (arg0.getTarget() == lab) {
					showMsg("RECORD " + DataUtil.getMaxScore(), 2000);
					return true;
				}
				return false;
			}
		});

		// 默认简单级别
		initLevel(1, false);
	}

	public void initLevel(int level, boolean judge) {
		if (judge && para.getLevel() == level) {
			return;
		}
		updateLevel = true;
		
		// 加载参数
		para = FGameParameter.getParaInstance(level, metric);
		fgame = new FGame(para.getRows(), para.getCols());

		if(inputPro != null){
			inputPro.updateAttri(this);
		}
		
		updateState(NOTSTART);
		
		stage.getActors().removeValue(gameTab, false);
		
		// 初始化绘图区域
		gameTab = new Table();
		
		setBound(gameTab, para.getGameBound());
		stage.addActor(gameTab);

		gameArea = new Image[para.getRows()][para.getCols()];
		for (int j = para.getCols() - 1; j >= 0; j--) {
			for (int i = 0; i < para.getRows(); i++) {
				gameArea[i][j] = getImageNotPos(i, j);
				gameTab.add(gameArea[i][j]).expand().fill()
						.space(para.getNodeSpace());
			}
			gameTab.row();
		}
		msgBox.toFront();
		
		updateLevel = false;
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

			// 判断是否结束
			if (FGameUtil.checkGameOver(fgame.getRcs())) {
				Log.i("IsEnd", "check ok");
				// 结束时如果还剩时间则加分数
				long remainTime = para.getGameTime()
						- (System.currentTimeMillis() - playTime);
				fgame.setRemainTime(remainTime);
				updateState(END);
			} else {
				Log.i("IsEnd", "check false");
			}
		} else if (res == 1) {
			errorSound.play();
		} else {
			cationSound.play();
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
				fgame = new FGame(para.getRows(), para.getCols());
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
		inputPro = new FGameInputProcessor(this);
		multi.addProcessor(inputPro);
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
		// 暂停置playTime为已玩时间
		if (playState == START) {
			updateState(PAUSE);
		}
		Log.d("GdxEvent", "pause");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Log.i("rend", playState + "");
		// 每超过100ms，更新进度条
		if (playState == START && System.currentTimeMillis() - lastTime > 500) {
			lastTime = System.currentTimeMillis();
			updateProcess(lastTime - playTime);
		}

		if (msgShow && msgShowTime > 0
				&& System.currentTimeMillis() - msgStartTime > msgShowTime) {
			hideMsg();
		}
		
		if(!updateLevel){
			stage.act(Gdx.graphics.getDeltaTime());
			// 绘图
			stage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		Log.d("GdxEvent", "resize " + width + " " + height);
	}

	@Override
	public void resume() {
		if (playState == RESUME) {
			return;
		}
		// 重置开始时间
		// updateState(RESUME);

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
		int[][] xyv = fgame.getRemoveInfos();
		if(xyv == null){
			return;
		}
		for (int[] xy : xyv) {
			updateImage(xy[0], xy[1]);
		}
		disAppear(xyv);
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
	 * 根据x,y获取Image(位置和大小改用table布局，不需要了)
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Image getImageNotPos(int x, int y) {
		int index = fgame.getRcs()[x][y];
		// 从text中生成一个Image对象
		return new Image(texts[index]);
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
		lab.setText("得分: " + score);
	}

	/**
	 * 设置进度条，返回当前是否结束
	 * 
	 * @param time
	 * @return
	 */
	private boolean updateProcess(long time) {
		float process = (float) ((double) time / para.getGameTime());
		process = 1 - process;
		if (process < 0) {
			process = 0;
			// 时间到，游戏结束
			showMsg("对不起时间到了", 1000);
			updateState(END);
		}
		// controlTab的宽度即为进度条宽度
		float width = para.getControlBound().getWidth() * process;
		// Log.i("Width", width + " " + time + " " + process);
		processBar.setWidth(width);
		return process > 0;
	}

	/**
	 * 更新状态
	 * 
	 * @param state
	 */
	private void updateState(int state) {
		Log.i("UpdateState", state + "");
		switch (state) {
		case NOTSTART:
			playState = NOTSTART;
			btn1.setText("开始");
			btn2.setDisabled(true);
			break;
		case START:
			playState = START;
			if (playTime <= para.getGameTime()) {
				playTime = System.currentTimeMillis() - playTime;
			}
			btn1.setText("结束");
			btn2.setText("暂停");
			btn2.setDisabled(false);
			loopMusic.play();
			showMsg("开始游戏", 1000);
			break;
		case PAUSE:
			playState = PAUSE;
			if (playTime > para.getGameTime()) {
				playTime = System.currentTimeMillis() - playTime;
			}
			btn2.setText("继续");
			showMsg("游戏暂停", 0);
			// loopMusic.pause();
			break;
		case RESUME:
			Log.i("State", "Resume");
			// 重新开始就是start
			playState = START;
			playTime = System.currentTimeMillis() - playTime;
			btn2.setText("暂停");
			loopMusic.play();
			showMsg("游戏继续", 1000);
			break;
		case END:
			playState = END;
			btn1.setText("开始");
			btn2.setDisabled(true);
			overSound.play();
			// 显示成绩
			showScore(fgame.getScore());
			break;
		}
	}

	/**
	 * 显示消息
	 * 
	 * @param msg
	 * @param time
	 *            时间大于0，到时会消失，否则一直显示，直到点击
	 */
	public void showMsg(String msg, long time) {
		msgBox.setText(msg);
		int[] centerGame = para.getCenterGameBound();
		float width = msgBox.getLabel().getTextBounds().width + 30;
		float height = msgBox.getLabel().getTextBounds().height + 60;
		float x = centerGame[0] - width / 2;
		float y = centerGame[1] - height / 2;
		msgBox.setBounds(x, y, width, height);
		msgBox.setPosition(x, y);
		msgStartTime = System.currentTimeMillis();
		msgShow = true;
		msgShowTime = time;
		stage.addActor(msgBox);
	}

	/**
	 * 隐藏消息
	 */
	private void hideMsg() {
		msgShow = false;
		msgBox.remove();
	}

	private void showScore(int score) {
		int max = DataUtil.getMaxScore();
		if (score > max) {
			showMsg("游 戏 结 束\n恭喜New Record!\n你的得分" + fgame.getScore(), 0);
			DataUtil.putScore(score);
		} else {
			showMsg("游 戏 结 束\n你的得分" + fgame.getScore(), 0);
		}
	}

	/**
	 * 移除的动态效果
	 * 
	 * @param removeNodes
	 */
	public void disAppear(int[][] removeNodes) {
		int i = 0;
		for (int[] xy : removeNodes) {
			disAppearImg[i] = new Image(texts[xy[2]]);
			disAppearImg[i].setBounds(para.getGameBound().getX()
					+ gameArea[xy[0]][xy[1]].getX(), para.getGameBound().getY()
					+ gameArea[xy[0]][xy[1]].getY(),
					gameArea[xy[0]][xy[1]].getWidth(),
					gameArea[xy[0]][xy[1]].getHeight());
			disAppearImg[i].addAction(Actions.fadeOut(1.5f));
			disAppearImg[i].addAction(Actions.moveTo(para.getScreenWidth() / 2,
					para.getScreenHeight(), 1f));
			disAppearImg[i].addAction(Actions.scaleTo(0.1f, 0.1f, 1f));
			disAppearImg[i].addAction(Actions.rotateTo(720, 1f));
			stage.addActor(disAppearImg[i]);
			i++;
		}
	}
}