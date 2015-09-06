package game;

/**
 * 
 */
/** 
 * @author 作者 E-mail: 
 * @version 创建时间：Jun 9, 2015 1:10:53 PM 
 * 类说明 
 */
/**
 * @author fengxiangli
 *
 */
// 客户端UI上实现的函数，负责将服务器的信息通知UI. Socket不需要关心这些如何实现。
// UI实现的函数都是SC方向的函数。这些函数会触发UI内部属性变化，UI通过设置属性监听可以及时捕捉变化并作出反应。
// socket调用函数的方式：暂定为Window.ui.function();
// ui将会是Window类的一个public static实例，其他类可以不必实例化Window对象就可以访问ui.
// 但是这样做数据不安全，也可能会出现一系列其他问题。如果我想到了更好的方案就再告诉你好了。

// 由于UI内部只要调用就一定会触发属性变化，不需要返回值。

public interface GameInterface {
	
	/**
	 * socket通知客户端对方下了哪步棋
	 * <p>SETPS</p>
	 * @param x
	 * @param y
	 */
	public void makeStep(int x, int y);

	/**
	 * socket通知客户端服务器对注册
	 * <p>RREGS</p>
	 * @param code
	 * @param msg
	 */
	public void regMsg(int code, String msg);
	/**
	 * socket通知客户端服务器对登陆
	 * <p>RLOGI</p>
	 * @param code
	 * @param msg
	 */
	public void logMsg(int code, String msg);
	
	/**
	 * 服务器通知客户端可以开始游戏了，并告诉客户端棋子颜色是什么。
	 * @param color
	 * @param name
	 * @param rank
	 */
	public void startGame(int color, String name, String rank);

	/**
	 * 服务器告诉客户端正在等待对手或等待对方再来一局（GWAIT或GWTAG）
	 * <p>GWAIT/GWTAG</p>
	 */
	public void waitingForGame();

	/**
	 * 不知道这是怎么回事。。我看熊典写了但我没明白。。这个函数应该对应于RSTEP
	 * <p>RSETP</p>
	 * @param code
	 * @param msg
	 */
	public void response(int code, String msg);

	/**
	 * 服务器通知客户端游戏结束&谁赢了 
	 * <p>GOVER</p>
	 * @param winner 
	 */
	public void gameOver(int winner, int your, int opp);

	/**
	 * 服务器通知客户端对方申请悔棋
	 * <p>RGRET</p>
	 */
	public void undoRequest();
	
	/**
	 * 服务器告诉客户端对方是否接受你的悔棋申请
	 * <p>RRGRE</p>
	 * @param code 规定code = 0 同意，code = 1拒绝
	 */
	public void oppConfirm(int code);

	/**
	 * 服务器告诉客户端对方认输了
	 * <p>ADDFT</p>
	 */
	public void oppSurrendered();

	/**
	 * 服务器告诉客户端对方发了GQUIT，不想再玩了
	 * <p>GQUIT</p>
	 */
	public void oppQuit();

	/**
	 * 服务器告诉客户端对方GOVER后还想来一局。客户端有拒绝对方请求的权利。
	 * <p>GAGAN</p>
	 */
	public void oppRequestNewGame();

	/**
	 * 服务器通知客户端对方掉线。
	 * <p>GDISC</p>
	 */
	public void oppDisconnected();
	
	/**
	 * 无效的命令
	 * <p>IVCMD</p>
	 */
	public void inValidCommand(int code, String msg);
	/**
	 * 改昵称的回复
	 * <p>RCNCH</p>
	 * @param code
	 * @param msg
	 */
	public void changeNickReply(int code, String msg);
	/**
	 * 服务器通知客户端对手中途离场
	 * <p>GFQUT</p>
	 * <p>实际调用oppQuit(GQUIT)</p>
	 */
	public void oppTerminateGame();


	public void getStats(String nick, int grades, int win, int lose, int draw);

	public void getScore(int yourScore, int oppositeScore);

	/**
	 * 下面是客户端与socket之间规定的代号，用public static final变量表示。
	 * 需要增加新的代号的时候就在下面加一个变量。
	 */
	/**
	 * 黑棋=-1
	 */
	public static final int BLACKCOLOR = -1;
	/**
	 * 白棋=1
	 */
	public static final int WHITECOLOR = 1;
	/**
	 * 能撤销 = 0
	 */
	public static final int CAN_UNDO = 0;
	/**
	 * 不能撤销 = 1
	 */
	public static final int CANNOT_UNDO = 1;
	/**
	 * 无效命令终止 = 0
	 */
	public static final int IVCMD_ALERT = 0;
	/**
	 * 无效命令中途结束对局 = 1
	 */
	public static final int IVCMD_GQUIT = 1;
	/**
	 * 无效命令中途登出 = 2 
	 */
	public static final int IVCMD_LOGOUT = 2;
	/**
	 * 无效命令中途退出 = 3
	 */
	public static final int IVCMD_EXIT = 3;
    public static final int YES = 0;
    public static final int NO = 1;
    
    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_BAD = 1;
    
    public static final int LOG_OK = 0;
    public static final int LOG_BAD = 1;
    public static final int REG_OK = 0;
    public static final int REG_BAD = 1;
	// ----重要！调用实例：----
	// 由于暂定调用方式是通过Window.ui, 如果需要调用startGame()函数并且给客户端分配棋子颜色为黑棋的时候应该这样：
	// Window.ui.startGame(Window.ui.BLACKCOLOR);
	// 这样能尽量让代码可扩充行强一些

}