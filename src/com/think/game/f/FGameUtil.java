package com.think.game.f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.annotation.SuppressLint;

import com.badlogic.gdx.graphics.Color;

/**
 * 
 * @author gudh
 * @date 2013-11-11
 */
public class FGameUtil {

	final static Color colors[] = { Color.CLEAR, Color.RED, Color.YELLOW,
			Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.PINK };

	private final static Random random = new Random();

	public static Color[] getAllColors() {
		return colors;
	}

	/**
	 * 获取下一个随机颜色下标，不包括0
	 * 
	 * @return
	 */
	static int getNextColorIndex(int colorCount) {
		return random.nextInt(colorCount) + 1;
	}

	static Color getColor(int index) {
		return colors[index];
	}

	/**
	 * 在rows行和cols列中，随机取出count个节点位置，以{row, col}表示
	 * 
	 * @param rows
	 * @param cols
	 * @param count
	 * @return
	 */
	static List<int[]> getEmptyPos(int rows, int cols, int count) {
		List<int[]> emptyPos = new ArrayList<int[]>(count);
		int[] temp;
		for (int i = 0; i < count; i++) {
			boolean exist;
			do {
				exist = false;
				temp = new int[] { random.nextInt(rows), random.nextInt(cols) };
				// 判断是否已经存在
				for (int[] th : emptyPos) {
					if (th[0] == temp[0] && th[1] == temp[1]) {
						// 如果已经存在则重新生成随机
						exist = true;
						break;
					}
				}
			} while (exist);
			emptyPos.add(temp);
		}
		return emptyPos;
	}

	/**
	 * 在rows行和cols列中，随机取出count个节点位置，以row_col字符串表示
	 * 
	 * @param rows
	 * @param cols
	 * @param count
	 * @return
	 */
	static Set<String> getEmptyPosSet(int rows, int cols, int count) {
		Set<String> sets = new HashSet<String>(count);

		while (sets.size() < count) {
			sets.add(new StringBuilder().append(random.nextInt(rows))
					.append("_").append(random.nextInt(cols)).toString());
		}

		return sets;
	}

	/**
	 * 判断是否有可以消除的小方块,true没有,false有
	 * 
	 * @param rcs
	 * @return
	 */
	@SuppressLint("UseSparseArrays")
	public static boolean checkGameOver(int[][] rcs) {
		int rows = rcs.length;
		int cols = rcs[0].length;
		HashSet<Integer> exitHash = new HashSet<Integer>(3);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (rcs[i][j] == 0) {
					int tx = j;
					int ty = i;
					// 左边的节点
					do {
						--tx;
					} while (tx > 0 && rcs[ty][tx] == 0);
					if (tx >= 0 && rcs[ty][tx] != 0) {
						exitHash.add(rcs[ty][tx]);
					}

					// 右边的节点
					tx = j;
					ty = i;
					do {
						++tx;
					} while (tx < cols - 1 && rcs[ty][tx] == 0);
					if (tx < cols && rcs[ty][tx] != 0) {
						if (exitHash.contains(rcs[ty][tx])) {
							return false;
						} else {
							exitHash.add(rcs[ty][tx]);
						}
					}
					// 上边的节点
					tx = j;
					ty = i;
					do {
						--ty;
					} while (ty > 0 && rcs[ty][tx] == 0);
					if (ty >= 0 && rcs[ty][tx] != 0) {
						if (exitHash.contains(rcs[ty][tx])) {
							return false;
						} else {
							exitHash.add(rcs[ty][tx]);
						}
					}
					// 下边的节点
					tx = j;
					ty = i;
					do {
						++ty;
					} while (ty < rows - 1 && rcs[ty][tx] == 0);
					if (ty < rows && rcs[ty][tx] != 0) {
						if (exitHash.contains(rcs[ty][tx])) {
							return false;
						}
					}
					exitHash.clear();
				}
			}
		}
		return true;
	}
}
