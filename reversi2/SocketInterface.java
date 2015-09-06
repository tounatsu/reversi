package reversi2;
import org.omg.IOP.Codec;
/** 
 * @author 作者 E-mail: 
 * @version 创建时间：Jun 9, 2015 1:10:28 PM 
 * 类说明 
 */
/**
 * @author fengxiangli
 *
 */
// socket 需要实现的接口, 负责把客户端的指令、信息、状态变化通知服务器。
// socket实现的函数都是CS方向的。
// 客户端程序调用时，客户端socket负责将命令或信息发送给服务器，给客户端返回是否成功发送。
// 客户端调用方式：SocketInterface.function();

public interface SocketInterface {

	// （以下返回boolean的函数如果socket觉得没有必要有返回值就可以都只返回void.）
	// 返回消息代码表示成功，失败或其他。
	/**
	 * 注册
	 * @param uid
	 * @param pwd
	 * @param nick
	 * @return
	 */
	public boolean register(String uid, String pwd, String nick);	
	/**
	 * 登陆
	 * @param uid
	 * @param pwd
	 * @return
	 */
	public boolean login(String uid, String pwd);
	/**
	 * 改变昵称姓名
	 * @param nick
	 * @return
	 */
	public boolean changeNickname(String nick);

	/**
	 * 通知服务器开始新游戏。返回布尔值表示是否发送成功。
	 * 统一规定，code = 0 表示NGAME（申请进入新游戏）
	 * code = 1 表示GAGAN（结束了一局游戏之后还想再来一局）
	 * @param code
	 * @return
	 */
	public boolean newGame(int code);

	/**
	 * 客户端下棋（下棋检查在客户端上完成；保证调用这个函数时只要双方棋盘数据一致，一定可以下棋）
	 * CS方向下棋的回复始终是code 0 msg ok, 所以RSETP这部分功能应当包含在这个函数中。
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean makeStep(int x, int y);
	/**
	 * 客户端悔棋
	 * @return 
	 */
	public boolean undo();

	/**
	 * 对方提出悔棋申请，客户端需要确认是否接受对方悔棋。参数为0表示同意悔棋，为1表示拒绝。
	 * @param code 
	 * @return 
	 */
	public boolean confirm(int code);

	// 客户端认输，ADDFT. 是用surrender()还是quit(1)取决于认输是否一定意味着要退出棋局返回菜单页。
	public boolean surrender();
	/**
	 * 
	 * @param _ip
	 * @param _port
	 * @return
	 */
	public boolean init(String _ip, int _port); 
	
	/**
	 *  客户端点击“返回”退出游戏时调用的函数。传递的参数表示退出情况。 
	 *  统一规定一下，code = 0 表示GQUIT（GOVER之后不想玩了）
	 *  code = 1 表示GFQUT中途离场（客户端在游戏进行时退出游戏，就自动认输）
	 *  code = 2 表示NCACL（客户端没有开始游戏，正在等待其他用户进局，但是没耐心等下去了取消等待）
	 * @param code
	 * @return
	 */
	// 因为数据都在客户端上，不知道有没有必要设置心跳函数。。这个函数每隔一段时间把棋盘信息发给服务器，确认保持棋盘信息一致性。。
	// 跟熊典确认一下需不需要吧。。
	public boolean quit(int code);
	/**
	 * 下棋的回复
	 * @param code
	 * @param msg
	 * @return
	 */
	public boolean response(int code , String message);
        
        /**
	 * 登出
	 * @return
	 */
	public boolean LGOUT();

	public boolean getStats();
    public boolean getScore();
        
        
	// 下面是客户端与socket之间规定的code msg 代号，用public static final变量表示。
	// 需要增加新的代号的时候就在下面加一个变量。
	/**
	 * 同意撤销 为0
	 */
	public static final int CAN_UNDO = 0;
	/**
	 * 不同意撤销
	 */
	public static final int CANNOT_UNDO = 1;
	/**
	 * 游戏结束 退出
	 */
	public static final int QUIT_WHEN_GAME_OVER = 0;
	/**
	 * 游戏结束 在游戏中
	 */
	public static final int QUIT_WHEN_IN_GAME = 1;
	/**
	 * 游戏结束 等待
	 */
	public static final int QUIT_WHEN_WAITING = 2;
	/**
	 * 新游戏
	 */
	public static final int GAME_NEW = 0;
	/**
	 * 游戏再次开始
	 */
	public static final int GAME_AGAIN = 1;

	
	
	
}
 

 
