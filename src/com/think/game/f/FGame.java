package com.think.game.f;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.util.Log;
import android.util.SparseArray;

/**
 * @author gudh
 * @date 2013-11-11
 */
public class FGame {

	private int rows = 10;
	private int cols = 10;
	private int colorCount = 5;
	private int rcs[][] = new int[rows][cols];

	private int emptyCount = 10;
	private int score = 0;

	public FGame(int rows, int cols, int colorCount, int emptyCount) {
		this.rows = rows;
		this.cols = cols;
		this.rcs = new int[rows][cols];
		this.colorCount = colorCount;
		this.emptyCount = emptyCount;
		// 初始化游戏布局
		initGame();

		this.score = 0;
	}

	public void printLayout(){
		System.out.println("=======================");
		for(int j = rows - 1; j >= 0; j--){
			for(int i = 0; i < cols; i++){
				System.out.print(rcs[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("=======================");
	}
	
	/**
	 * 初始化游戏布局，空白和非空白等
	 */
	private void initGame() {
		Set<String> emptySet = FGameUtil.getEmptyPosSet(rows, cols, emptyCount);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (emptySet.contains(new StringBuilder().append(i).append("_")
						.append(j).toString())) {
					rcs[i][j] = 0;
					Log.d("InitColor", FGameUtil.getColor(0).toString());
				} else {
					rcs[i][j] = FGameUtil.getNextColorIndex(colorCount);
					Log.d("InitColor", FGameUtil.getColor(rcs[i][j]).toString());
				}
			}
		}
	}

	/**
	 * 点击X，Y位置，处理并返回结果
	 * 
	 * @param x
	 * @param y
	 * @return -1 不可玩游戏(或不正确的索引)，0点击为非空处，1点击没有任何消除，2点击有消除并得分
	 */
	public int click(int x, int y) {
		if (x < 0 || x >= rows || y < 0 || y > cols) {
			return -1;
		}
		if (rcs[x][y] != 0) {
			return 0;
		}

		// 获取最近非空节点集
		List<int[]> nearNodes = getNearNotEmptyNodes(x, y);
		// 计算可以被消除的节点
		List<int[]> clearNodes = getNeedClearNodes(nearNodes);

		if (clearNodes == null) {
			return 1;
		}

		// 有需要消除的节点
		clearMatchedNodes(clearNodes);

		return 2;
	}

	/**
	 * 获取节点x,y处上下左右最近的一个非空节点集，如果是边缘的话就不添加到结果集
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private List<int[]> getNearNotEmptyNodes(int x, int y) {
		List<int[]> nodes = new ArrayList<int[]>(4);
		int tx = x;
		int ty = y;

		// 左边的节点
		do {
			tx--;
		} while (tx > 0 && rcs[tx][ty] == 0);
		if (tx >= 0) {
			nodes.add(new int[] { tx, ty });
		}

		// 右边的节点
		tx = x;
		ty = y;
		do {
			tx++;
		} while (tx < rows - 1 && rcs[tx][ty] == 0);
		if (tx < rows) {
			nodes.add(new int[] { tx, ty });
		}

		// 下边的节点
		tx = x;
		ty = y;
		do {
			ty--;
		} while (ty > 0 && rcs[tx][ty] == 0);
		if (ty >= 0) {
			nodes.add(new int[] { tx, ty });
		}

		// 上边的节点
		tx = x;
		ty = y;
		do {
			ty++;
		} while (ty < cols - 1 && rcs[tx][ty] == 0);
		if (ty < cols) {
			nodes.add(new int[] { tx, ty });
		}

		return nodes;
	}

	/**
	 * 计算哪些节点可以被消除，并返回可以被消除的节点，如果没有可被消除的返回null
	 * 
	 * @param nodes
	 * @return
	 */
	private List<int[]> getNeedClearNodes(List<int[]> nodes) {

		// 节点分类，将相同颜色的节点分类到同一个KEY下面
		SparseArray<List<int[]>> classNodes = new SparseArray<List<int[]>>(4);
		// 按key(颜色)，value(存储此颜色的所有坐标int[]的list)
		for (int[] node : nodes) {
			int color = getRC(node);
			if (classNodes.get(color) == null) {
				classNodes.put(color, new ArrayList<int[]>(4));
			}
			classNodes.get(color).add(node);
		}

		// 根据分类判断哪些节点可以被消除
		List<int[]> needClearNodes = null;
		for (int i = 1; i < colorCount + 1; i++) {
			if (classNodes.indexOfKey(i) >= 0) {
				if (classNodes.get(i).size() > 1) {
					// 如果为null则初始化一下
					if (needClearNodes == null) {
						needClearNodes = new ArrayList<int[]>(4);
					}
					needClearNodes.addAll(classNodes.get(i));
				}
			}
		}

		return needClearNodes;
	}

	/**
	 * 消除指定的节点数据
	 * 
	 * @param clearNodes
	 */
	private void clearMatchedNodes(List<int[]> clearNodes) {
		int base = 1;
		for(int[] node : clearNodes){
			setRC(node, 0);
			score += base;
			base++;
		}
	}
	
	private void setRC(int[] rc, int value) {
		rcs[rc[0]][rc[1]] = value;
	}

	private int getRC(int[] rc) {
		return rcs[rc[0]][rc[1]];
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getColorCount() {
		return colorCount;
	}

	public int[][] getRcs() {
		return rcs;
	}

	public int getEmptyCount() {
		return emptyCount;
	}

	public int getScore() {
		return score;
	}
}
