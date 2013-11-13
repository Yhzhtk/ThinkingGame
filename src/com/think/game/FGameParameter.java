package com.think.game;

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

	private int screenWidth;
	private int screenHeight;

	private Rectangle btn1Bound;
	private Rectangle btn2Bound;
	private Rectangle labelBound;

	private Rectangle gameBound;
	private int[] nodeSize;
	private int[] rectSize;

	private static FGameParameter para;

	private FGameParameter() {
	}

	public static FGameParameter getParaInstance(int rows, int cols) {
		if (para == null) {
			para = new FGameParameter();

			para.rows = rows;
			para.cols = cols;

			para.screenWidth = Gdx.graphics.getWidth();
			para.screenHeight = Gdx.graphics.getHeight();

			para.btn1Bound = new Rectangle(20, 20, 140, 80);
			para.btn2Bound = new Rectangle(180, 20, 140, 80);
			para.labelBound = new Rectangle(340, 20, 120, 80);

			para.gameBound = new Rectangle(25, 130, 420, 672);
			para.nodeSize = new int[] { 40, 40 };
			para.rectSize = new int[] { 43, 43 };
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

	public Rectangle getGameBound() {
		return gameBound;
	}

	public int[] getNodeSize() {
		return nodeSize;
	}
	
	public int[] getRectSize() {
		return rectSize;
	}
}
