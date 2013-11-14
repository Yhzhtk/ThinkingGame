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

	private Rectangle btn1Bound;
	private Rectangle btn2Bound;
	private Rectangle labelBound;
	private Rectangle processBound;

	private Rectangle gameBound;
	private int[] nodeSize;
	private int[] rectSize;

	private static FGameParameter para;

	// 游戏时间
	private long gameTime;

	private FGameParameter() {
	}
	
	/**
	 * 根据游戏等级和屏幕信息获取布局
	 * @param level
	 * @param metric
	 * @return
	 */
	public static FGameParameter getParaInstance(int level, DisplayMetrics metric){
		// 根据游戏级别和屏幕信息计算行列
		int rows = 10;
		int cols = 16;
		
		return getParaInstance(rows, cols, metric);
	}

	/**
	 * 根据指定的行列获取自适应的游戏布局
	 * @param rows
	 * @param cols
	 * @param metric
	 * @return
	 */
	public static FGameParameter getParaInstance(int rows, int cols,
			DisplayMetrics metric) {
		if (para == null || para.rows != rows || para.cols != cols
				|| para.metric != metric) {
			para = new FGameParameter();
			
			para.rows = rows;
			para.cols = cols;
			para.metric = metric;
			
			para.screenWidth = Gdx.graphics.getWidth();
			para.screenHeight = Gdx.graphics.getHeight();

			para.btn1Bound = new Rectangle(20, 20, 140, 60);
			para.btn2Bound = new Rectangle(180, 20, 140, 60);
			para.labelBound = new Rectangle(340, 20, 120, 60);
			para.processBound = new Rectangle(25, 100, 420, 10);

			para.gameBound = new Rectangle(25, 130, 420, 672);
			para.nodeSize = new int[] { 40, 40 };
			para.rectSize = new int[] { 43, 43 };

			para.gameTime = 600000;
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

	public Rectangle getBtn1Bound() {
		return btn1Bound;
	}

	public Rectangle getBtn2Bound() {
		return btn2Bound;
	}

	public Rectangle getLabelBound() {
		return labelBound;
	}

	public Rectangle getProcessBound() {
		return processBound;
	}

	public Rectangle getGameBound() {
		return gameBound;
	}

	public int[] getNodeSize() {
		return nodeSize;
	}

	public int[] getRectSize() {
		return rectSize;
	}

	public long getGameTime() {
		return gameTime;
	}
}
