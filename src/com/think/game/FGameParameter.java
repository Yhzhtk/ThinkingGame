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

	private int level;
	
	private Rectangle gameBound;
	private Rectangle controlBound;

	private float[] rectSize;

	private int btnHeight;
	private int processHeight;

	private int nodeSpace;
	private int buttonLeftSpace;
	private int processBottomSpace;

	private int[] centerGameBound;

	private static FGameParameter para;

	// 游戏时间
	private long gameTime;

	private FGameParameter() {
	}

	/**
	 * 根据游戏等级和屏幕信息获取布局
	 * 
	 * @param level
	 *            游戏级别
	 * @param metric
	 * @return
	 */
	public static FGameParameter getParaInstance(int level,
			DisplayMetrics metric) {
		// 根据游戏级别和屏幕信息计算行列
		int rows = 8;
		int cols = 12;
		
		switch(level){
		case 1:
			rows = 6;
			cols = 10;
			break;
		case 2:
			rows = 8;
			cols = 12;
			break;
		case 3:
			rows = 10;
			cols = 15;
			break;
		default:
			rows = 8;
			cols = 15;
		} 
		return getParaInstance(level, rows, cols, metric);
	}

	/**
	 * 根据指定的行列获取自适应的游戏布局
	 * 
	 * @param rows
	 * @param cols
	 * @param metric
	 * @return
	 */
	private static FGameParameter getParaInstance(int level, int rows, int cols,
			DisplayMetrics metric) {
		if (para == null || para.rows != rows || para.cols != cols
				|| para.metric != metric) {
			para = new FGameParameter();
			
			para.level = level;

			para.rows = rows;
			para.cols = cols;
			para.metric = metric;

			para.screenWidth = Gdx.graphics.getWidth();
			para.screenHeight = Gdx.graphics.getHeight();

			int space = (int) (para.screenWidth * 0.08);

			int s_width = para.screenWidth - (2 * space);
			int s_height = para.screenHeight - (2 * space);

			int control_x = space;
			int control_y = space;
			int control_width = s_width;
			int control_height = (int) (s_height * 0.15);

			int game_x = space;
			int game_y = space + (int) (s_height * 0.17);
			int game_width = s_width;
			int game_height = (int) (s_height * 0.83);

			// para.controlBound = new Rectangle(25, 20, 420, 120);
			// para.gameBound = new Rectangle(25, 130, 420, 672);
			para.controlBound = new Rectangle(control_x, control_y,
					control_width, control_height);
			para.gameBound = new Rectangle(game_x, game_y, game_width,
					game_height);

			para.rectSize = new float[] {
					para.gameBound.getWidth() / para.rows,
					para.gameBound.getHeight() / para.cols };

			para.btnHeight = (int) (control_height * 0.7);
			para.processHeight = (int) (control_height * 0.15);

			para.nodeSpace = (int) (para.rectSize[0] * 0.06);
			para.buttonLeftSpace = (int) (s_width * 0.02);
			para.processBottomSpace = (int) (control_height * 0.1);

			para.centerGameBound = new int[] {
					(int) (para.getGameBound().getX() + para.getGameBound()
							.getWidth() / 2),
					(int) (para.getGameBound().getY() + para.getGameBound()
							.getHeight() / 2) };

			para.gameTime = (long) (para.rows * para.cols * 0.6) / 10 * 10000;
		}
		return para;
	}

	public int[] getCenterGameBound() {
		return centerGameBound;
	}

	public int getLevel(){
		return level;
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

	public int getBtnHeight() {
		return btnHeight;
	}

	public int getProcessHeight() {
		return processHeight;
	}
}
