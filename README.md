ThinkingGame
============

基于LibGDX的Android小游戏，使用Stage(舞台)-Actor(演员)的方式呈现游戏。

###代码说明

**con.think.game**

- **DataUtil.java** 使用key-value存储获取记录得分
- **FGameAct.java** 界面控制，游戏状态逻辑处理，Stage-Actor展示
- **FGameInputProcessor.java** 自定义事件处理，根据坐标计算格子坐标
- **FGameParameter.java** 处理各项参数如屏幕大小，格子数，颜色等

**com.think.game.f**

- **FGame.java** 游戏算法，消除判断，得分控制
- **FGameUtil.java** FGame的静态方法类

###游戏规则：

点击空格子，空格子上下左右最近一个“非空”格子（最多四个，边缘处可能小于四个）中有两个或两个以上相同的格子，则可消除得分。

###得分提示：

点空格子一次消除2个加12分，3个加27分，4个加48分。游戏剩余时间会加分，每剩一秒加12分，无上限。游戏结束时不剩有色格子加100分，剩一个加80，依次递减。
