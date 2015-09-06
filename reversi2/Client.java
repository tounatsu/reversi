
package reversi2;

import game.GameInterface;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import org.json.JSONObject;
/** 
 * @author 作者 E-mail: 
 * @version 创建时间：Jun 9, 2015 1:39:42 PM 
 * 类说明 
 */
/**
 * @author fengxiangli
 *
 */
public class Client implements Runnable{
	public static void main(String argv[]) {
		Client myClient = new Client();
		GameInterface myImplemet = new GameTrigger();
		myClient.gameDelegate = myImplemet;
		myClient.GFQUT();
	}
	/**
	 * 成员变量GameInterfacedelegate实例
	 */
	public GameInterface gameDelegate = new GameTrigger();
	/**
	 * socket存储位置
	 */
	public Socket sock ;
	/**
	 * 判断是否连接
	 */
	public Boolean Connected;
	/**
	 * 字段
	 */
	/**
	 * 输入流
	 */
	private DataInputStream in;
	/**
	 * 输入的长度
	 */
	private int contentLength;
	/**
	 * jsonContent当前字段的长度
	 */
	private String jsonContent;
	/**
	 * 构造函数
	 */
	public Client() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * running函数
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			in = new DataInputStream(sock.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			byte[] header = new byte[5];
			try {
                            in.readFully(header);
			}  catch (EOFException e2){} 
                        catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
			}
			String s = new String(header);
			System.out.println(s);
			switch (s) {
//				对手下棋位置回复
			case "SETPS":
				try {
					SETPS(in);
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
//				注册的回复
			case "RREGS":
				try {
					RREGS(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
//				登陆的回复
			case "RLOGI":
				try {
					RLOGI(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
//				游戏开始
			case "GSTRT":
				try {
					GSTRT(in);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
//				等待玩家
			case "GWAIT":
				GWAIT();
				break;
//				下棋是否合法的回复
			case "RSETP":
				try {
					RSETP(in);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				break;
//				游戏结束
			case "GOVER":
				try {
					GOVER(in);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				break;
//				对手接收rival悔棋请求
			case "RGRET":
				RGRET();
				break;
//				对手是否同意悔棋请求
			case "RRGRE":
				try {
					RRGRE(in);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				break;
//				对手认输请求
			case "ADDFT":
				ADDFT();
				break;
//				对手结束对局请求
			case "GQUIT":
				GQUIT();
				break;
//				对手再来一局请求
			case "GAGAN":
				GAGAN();
				break;
//				等待对方再来一局
			case "GWTAG":
				GWTAG();
				break;
//				对方掉线
			case "GDISC":
				GDISC();
				break;
//				无效命令
			case "IVCMD":
				try {
					IVCMD(in);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
//				改昵称的回复
			case "RCNCH":
				try {
					RCNCH(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
//				中途离场的回复
			case "GFQUT":
				GFQUT();
				break;
			case "RSTAT":
			try {
				//System.out.println("stats here");
				RSTAT(in);
			} catch (IOException e233){
				e233.printStackTrace();
			}
				break;
				case "RSCOR":
			try {
				//System.out.println("stats here");
				RSCOR(in);
			} catch (IOException e23){
				e23.printStackTrace();
			}
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 
	 */
	private void GFQUT() {
		// TODO Auto-generated method stub
		gameDelegate.oppTerminateGame();
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void RCNCH(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
		contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int code = myJsonObject.getInt("code");
		String msg = myJsonObject.getString("msg");

		System.out.println(msg);
		gameDelegate.changeNickReply(code, msg);
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void IVCMD(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
		contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int code = myJsonObject.getInt("code");
		String msg = myJsonObject.getString("msg");
		gameDelegate.inValidCommand(code, msg);
	}
	/**
	 * 
	 */
	private void GDISC() {
		// TODO Auto-generated method stub
		gameDelegate.oppDisconnected();
	}
	/**
	 * 
	 */
	private void GWTAG() {
		// TODO Auto-generated method stub
		gameDelegate.waitingForGame();
	}
	/**
	 * 
	 */
	private void GAGAN() {
		// TODO Auto-generated method stub
		gameDelegate.oppRequestNewGame();
	}
	/**
	 * 
	 */
	private void GQUIT() {
		// TODO Auto-generated method stub
		gameDelegate.oppQuit();
	}
	/**
	 * 
	 */
	private void ADDFT() {
		// TODO Auto-generated method stub
		gameDelegate.oppSurrendered();
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void RRGRE(DataInputStream in2) throws IOException {
		// sdafsafsaf
		contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int code = myJsonObject.getInt("code");
		gameDelegate.oppConfirm(code);
	}
	/**
	 * 
	 */
	private void RGRET() {
		// TODO Auto-generated method stub
		gameDelegate.undoRequest();
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void GOVER(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
		contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int winner = myJsonObject.getInt("winner");


		int your = myJsonObject.getInt("yourScore");
		int opp = myJsonObject.getInt("oppositeScore");

		gameDelegate.gameOver(winner, your, opp);
		//gameDelegate.getScore(your, opp);
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void RSETP(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
		contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int code = myJsonObject.getInt("code");
		String msg = myJsonObject.getString("msg");
		gameDelegate.response(code, msg);
		
	}
	/**
	 * 
	 */
	private void GWAIT() {
		// TODO Auto-generated method stub
		gameDelegate.waitingForGame();
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void GSTRT(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
		contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int color = myJsonObject.getInt("color");
		String name = myJsonObject.getString("opnick");
		int rank =myJsonObject.getInt("opscore");
		gameDelegate.startGame(color, name, rank + "");
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void RLOGI(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
		contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int code = myJsonObject.getInt("code");
		String msg = myJsonObject.getString("msg");
		gameDelegate.logMsg(code, msg);
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void SETPS(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
		contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int x = myJsonObject.getInt("x");
		int y = myJsonObject.getInt("y");
                System.out.println(x + "" + y);
		gameDelegate.makeStep(x, y);
	}
	/**
	 * @param in2
	 * @throws IOException 
	 */
	private void RREGS(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
                contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);
		int code = myJsonObject.getInt("code");
		String msg = myJsonObject.getString("msg");
		gameDelegate.regMsg(code, msg);	
            
            
	}

	private void RSTAT(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
        contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);

		String nick = myJsonObject.getString("nick");
		int grades = myJsonObject.getInt("grades");
		int win = myJsonObject.getInt("win");
		int lose = myJsonObject.getInt("lose");
		int draw = myJsonObject.getInt("draw");
		
		gameDelegate.getStats(nick, grades, win, lose, draw);	
            
	}

	private void RSCOR(DataInputStream in2) throws IOException {
		// TODO Auto-generated method stub
        contentLength = in2.readInt();
		byte[] jsonContentByte = new byte[contentLength];
		in2.read(jsonContentByte);
		jsonContent = new String(jsonContentByte);
		JSONObject myJsonObject= new JSONObject(jsonContent);

		int your = myJsonObject.getInt("yourScore");
		int opp = myJsonObject.getInt("oppositeScore");
		
		gameDelegate.getScore(your, opp);	
            
	}
        
        
	
}
 


