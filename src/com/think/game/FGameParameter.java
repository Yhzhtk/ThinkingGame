package com.think.game;

import android.util.DisplayMetrics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * 存储FGame的游戏参数
 * 
 * @author gudh
 * @date 2013-11-12
 */
public class FGameParameter {

	private int rows;
	private int cols;
	private DisplayMetrics metric;

	private int screenWidth;
	private int screenHeight;

	private Rectangle gameBound;
	private Rectangle controlBound;

	private float[] rectSize;

	private int nodeSpace;
	private int buttonLeftSpace;
	private int processBottomSpace;

	private static FGameParameter para;

	// 游戏时间
	private long gameTime;

	private FGameParameter() {
	}

	/**
	 * 根据游戏等级和屏幕信息获取布局
	 * 
	 * @param level 游戏级别
	 * @param metric
	 * @return
	 */
	public static FGameParameter getParaInstance(int level,
			DisplayMetrics metric) {
		// 根据游戏级别和屏幕信息计算行列
		int rows = 8;
		int cols = 12;

		return getParaInstance(rows, cols, metric);
	}

	/**
	 * 根据指定的行列获取自适应的游戏布局
	 * 
	 * @param rows
	 * @param cols
	 * @param metric
	 * @return
	 */
	private static FGameParameter getParaInstance(int rows, int cols,
			DisplayMetrics metric) {
		if (para == null || para.rows != rows || para.cols != cols
				|| para.metric != metric) {
			para = new FGameParameter();

			para.rows = rows;
			para.cols = cols;
			para.metric = metric;

			para.screenWidth = Gdx.graphics.getWidth();
			para.screenHeight = Gdx.graphics.getHeight();

			para.controlBound = new Rectangle(25, 20, 420, 120);

			para.gameBound = new Rectangle(25, 130, 420, 672);

			para.rectSize = new float[] { para.gameBound.getWidth() / para.rows,
					para.gameBound.getHeight() / para.cols };

			para.nodeSpace = 3;
			para.buttonLeftSpace = 10;
			para.processBottomSpace = 2;

			para.gameTime =  (long)(para.rows * para.cols * 0.6) / 10 * 10000;
		}
		return para;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public Rectangle getGameBound() {
		return gameBound;
	}

	public Rectangle getControlBound() {
		return controlBound;
	}

	public long getGameTime() {
		return gameTime;
	}

	public int getNodeSpace() {
		return nodeSpace;
	}

	public int getBtnLeftSpace() {
		return buttonLeftSpace;
	}

	public int getProcessBottomSpace() {
		return processBottomSpace;
	}

	public float[] getRectSize() {
		return rectSize;
	}
}
