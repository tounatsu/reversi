package reversi2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;
/** 
 * @author 作者 E-mail: 
 * @version 创建时间：Jun 9, 2015 1:27:00 PM 
 * 类说明 
 */
/**
 * @author fengxiangli
 */
 public class SocketInterfaceImplementer implements SocketInterface {
	/**
	 * 属性
	 */
	public Client clientDelegate = new Client(); 
	public Socket sock;
	public boolean Connected;
	/**
	 * 字段
	 */
	private DataOutputStream out;
	private String ip;
	private int port;
	private final static int DEFAULT_PORT = 12345;
	private final static String DEFAULT_IP = "112.124.118.137";
//	字段方法
	 /**
	  * int转byte数组
	  * @param a
	  * @return
	  */
	 private byte[] intToByteArray(int a) // turn int to byte array
		{  
	    	return new byte[] 
	    	{  
		        (byte) ((a >> 24) & 0xFF),  
		        (byte) ((a >> 16) & 0xFF),     
		        (byte) ((a >> 8) & 0xFF),     
		        (byte) (a & 0xFF)
	    	};  
		}
	 /**
	  * pack封装
	  * @param head
	  * @param json
	  * @return
	  */
	 private byte[] pack(String head,JSONObject json) // pack info into byte array
		{
			byte[] pack_header = head.getBytes();
			if (json == null)
			{
				return pack_header;
			}
			byte[] pack_content = json.toString().getBytes();
			byte[] pack_length = intToByteArray(pack_content.length);
			byte[] packed = new byte[pack_header.length+pack_length.length+pack_content.length];

			System.arraycopy(pack_header,0,packed,0,pack_header.length);
			System.arraycopy(pack_length,0,packed,pack_header.length,pack_length.length);
			System.arraycopy(pack_content,0,packed,pack_header.length+pack_length.length,pack_content.length);

			return packed;
		}
	 /**
	  * 发送数据
	  * @param msg
	  * @return
	  */
	    private boolean send(byte[] msg) // send byte data
     {
         if (!sock.isConnected()) {
             init(ip, port);
         }
         try {
             out.write(msg);
         } catch (IOException e) {
             return false;
         }
         return true;
     }
//	构造函数
	/**
	 * 构造函数1
	 */
	public SocketInterfaceImplementer() {
		init(DEFAULT_IP,DEFAULT_PORT);
		clientDelegate.sock = sock;
		clientDelegate.Connected = Connected;
		
	}
	/**
	 * 构造函数2
	 * @param _ip
	 * @param _port
	 */
	public SocketInterfaceImplementer(String _ip, int _port) {
		// TODO Auto-generated constructor stub
		init(_ip, _port);
		clientDelegate.sock = sock;
		clientDelegate.Connected = Connected;
	}
	
//属性方法
	
	@Override
	public boolean register(String uid, String pwd, String nick) {
		// TODO Auto-generated method stub
		JSONObject sendmsg = new JSONObject();
		sendmsg.put("uid", uid);
		sendmsg.put("pwd", pwd);
		sendmsg.put("nick", nick);
		byte[] msg = pack("REGST", sendmsg);
		return send(msg);
	}

	/* (non-Javadoc)
	 * @see SocketInterface#login(java.lang.String, java.lang.String)
	 */

	@Override
	public boolean login(String uid, String pwd) {
		// TODO Auto-generated method stub
		JSONObject sendmsg = new JSONObject();
		sendmsg.put("uid", uid);
		sendmsg.put("pwd", pwd);
		byte[] msg = pack("LOGIN",sendmsg);
		return send(msg);
	}

	/* (non-Javadoc)
	 * @see SocketInterface#changeNickname(java.lang.String)
	 */
	@Override
	public boolean changeNickname(String nick) {
		// TODO Auto-generated method stub
		JSONObject sendmsg = new JSONObject();
		sendmsg.put("nick", nick);
		byte[] msg = pack("NCCHG", sendmsg);
		return send(msg);
	}

	/* (non-Javadoc)
	 * @see SocketInterface#newGame(int)
	 */
	/**
	 * 通知服务器开始新游戏。返回布尔值表示是否发送成功。
	 * 统一规定，code = 0 表示NGAME（申请进入新游戏）
	 * code = 1 表示GAGAN（结束了一局游戏之后还想再来一局）
	 */
	@Override
	public boolean newGame(int code) {
		// TODO Auto-generated method stub
		switch (code) {
		case GAME_NEW:
			byte[] msg = pack("NGAME", null);
			return send(msg);
		case GAME_AGAIN:
			msg= pack("GAGAN", null);
			return send(msg);
		default:
			break;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see SocketInterface#makeStep(int, int)
	 */
	@Override
	public boolean makeStep(int x, int y) {
		// TODO Auto-generated method stub
		JSONObject sendmsg = new JSONObject();
		sendmsg.put("x", x);
		sendmsg.put("y", y);
		byte[] msg = pack("SETPS", sendmsg);
                
                System.out.println("SETPS");
                
		return send(msg);
	}

	/* 悔棋
	 * @see SocketInterface#undo()
	 */
	@Override
	public boolean undo() {
		// TODO Auto-generated method stub
		byte[] msg = pack("RGRET", null);
                
                System.out.println("RGRET");
		return send(msg);
	}

	/**
	 * 悔棋的回复
	 */
	@Override
	public boolean confirm(int code) {
		// TODO Auto-generated method stub
		JSONObject sendmsg = new JSONObject();
		sendmsg.put("code", code);
		byte[] msg = pack("RRGRE", sendmsg);
                System.out.println("RRGRE");
		return send(msg);
	}
	/**
	 * 投降
	 */
	@Override
	public boolean surrender() {
		// TODO Auto-generated method stub
		byte[] msg = pack("ADDFT", null);
		return send(msg);
	}
	/**
	 *  客户端点击“返回”退出游戏时调用的函数。传递的参数表示退出情况。 
	 *  统一规定一下，code = 0 表示GQUIT（GOVER之后不想玩了）
	 *  code = 1 表示GFQUT中途离场（客户端在游戏进行时退出游戏，就自动认输）
	 *  code = 2 表示NCACL（客户端没有开始游戏，正在等待其他用户进局，但是没耐心等下去了取消等待）
	 * @param code
	 * @return
	 */
	@Override
	public boolean quit(int code) {
		// TODO Auto-generated method stub
		JSONObject sendmsg = new JSONObject();
		switch (code) {
		case QUIT_WHEN_GAME_OVER:
			byte[] msg = pack("GQUIT", null);
			return send(msg);
		case QUIT_WHEN_IN_GAME:
			msg = pack("GFQUT", null);
			return send(msg);
		case QUIT_WHEN_WAITING:
			msg = pack("NCACL", null);
			return send(msg);
		default:
			break;
		}
		return false;
	}

	/**
	 * 初始化
	 */
	public boolean init(String _ip, int _port) {
		// TODO Auto-generated method stub
		ip = _ip;
		port = _port;
		try
		{
			sock = new Socket(ip,port);
			out = new DataOutputStream(sock.getOutputStream());
			Connected = true;
			return true;
		}
		catch (Exception e)
		{
			System.out.println(e);
			Connected = false;
			return false;
		}
	}
	/**
	 * 回复
     * @param message
	 */
	@Override
	public boolean response(int code, String message) {
		// TODO Auto-generated method stub
		JSONObject sendmsg = new JSONObject();
		sendmsg.put("code", code);
		sendmsg.put("message", message);
		byte[] msg = pack("RSETP", sendmsg);
		return send(msg);
	}
	public static void main(String argv[]) {
		SocketInterfaceImplementer mySocketImplementer = new SocketInterfaceImplementer();
		mySocketImplementer.register("sfd", "1111", "sdfsf");
		mySocketImplementer.clientDelegate.run();
	}

    @Override
	public boolean LGOUT() {
		// TODO Auto-generated method stub
		byte[] msg = pack("LGOUT", null);
		return send(msg);
	}

	@Override
	public boolean getStats() {
		// TODO Auto-generated method stub
		byte[] msg = pack("STATS", null);
                
        //System.out.println("STATS");
		return send(msg);
	}

	@Override
	public boolean getScore(){
		byte[] msg = pack("SCORE", null);
		return send(msg);
	}
}
 

 
