package com.think.game.f;

import java.util.Scanner;

/**
 * ²âÊÔ·½·¨
 * @author gudh
 * @date 2013-11-11
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FGame game = new FGame(10, 10, 3, 10);
		game.printLayout();

		String c = "";
		Scanner scan = new Scanner(System.in);
		do {
			System.out.print("> ");
			c = scan.nextLine();
			System.out.println("input: " + c);
			String infos[] = c.split(" ");
			if (infos.length == 2) {
				int r = game.click(Integer.parseInt(infos[0]),
						Integer.parseInt(infos[1]));
				System.out.println("click status:" + r);
			} else {
				System.out.println("input error");
			}
			game.printLayout();
		} while (!c.equals("exit"));
		scan.close();
	}
}
