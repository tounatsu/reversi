package game;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import reversi2.*;
import reversi2.Reversi;
import simpleUI.UISettings;


public class UIMultiPlayerController extends UIView implements UIMultiPlayerControllerInterface {
    
    private final UIMultiPlayerView viewPanel;
    private final JLabel[][] btn;
    private final int tileLength;
    private final int gifFrames;
    public final int frameWait = 30;
    
    // game controls
    private int opponentColor;
    private int bNum, wNum;
    public boolean inGame, canUndo, oppHasUndo;
    private boolean oppRequestUndo, requestUndo, oppRequestNew, requestNew;
    public boolean canMove;
    
    public UIMultiPlayerController() throws IOException{
        
        this.setLayout(new BorderLayout());
        viewPanel = new UIMultiPlayerView();
        
        // initialize variables
        tileLength = viewPanel.tileLength;
        gifFrames = viewPanel.gifFrames;
        bNum = 0;
        wNum = 0;
        inGame = false;
        requestUndo = oppRequestUndo = requestNew = oppRequestNew = false;
        canMove = false;

        if(UISettings.getIsMusicOn()){
            GameMainModel.load_music();
        }
        
        btn = new JLabel[tileLength][tileLength];
        initComponents(); 
        
        this.add(viewPanel);
        this.setVisible(true);

    }
    
    private void initComponents(){
        // Initialize game board
        for (int i = 0; i != tileLength; i++) {
            for (int j = 0; j != tileLength; j++) {
                btn[i][j] = new JLabel();
                viewPanel.p_board.add(btn[i][j]);
                btn[i][j].setOpaque(false);
                btn[i][j].setVisible(true);
                
                final int _i = i, _j = j;
                btn[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) { 
                        //System.out.println(_i + " " +_j);
                        if (canMove) {
                            if (make_step(_i, _j, GameMainModel.myColor) && inGame){
                                Reversi.socket.makeStep(_i, _j);
                                if (GameStrategyModel.solution(GameMainModel.oppColor).x == -1) {
                                    canMove = true;
                                    message(GameControlCommand.OPPONENT_STUCK);
                                }
                                if (GameStrategyModel.solution(GameMainModel.myColor).x == -1) {
                                    canMove = false;
                                    message(GameControlCommand.USER_STUCK);
                                }
                                check_end();
                            }
                        } 
                    }
                });
            }
        }

        // button Mouselisteners
        for (int i = 0; i < viewPanel.iconNum; ++i){
            final int _i = i;
            viewPanel.label[i].addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    switch (_i){
                        case UIMultiPlayerView.UNDO:button_undo();break;
                        case UIMultiPlayerView.MUSIC:button_music();break;
                        case UIMultiPlayerView.NEW: button_new();break;
                        case UIMultiPlayerView.HOME: button_home();break;
                        case UIMultiPlayerView.AGREE:button_agree();break;
                        case UIMultiPlayerView.REJECT:button_reject();break;
                        case UIMultiPlayerView.SURRENDER:button_surrender();break;
                        default: break;
                    }
                }
                @Override
                public void mousePressed(MouseEvent e) { viewPanel.setPressed(_i);}
                @Override
                public void mouseReleased(MouseEvent e) { viewPanel.setReleased(_i);}
                @Override
                public void mouseEntered(MouseEvent e) {    // send message: DONE
                    switch (_i){
                        case UIMultiPlayerView.UNDO:
                            message(GameControlCommand.UNDO);
                            break;
                        case UIMultiPlayerView.MUSIC: 
                            message(GameControlCommand.MUSIC);
                            break;
                        case UIMultiPlayerView.NEW: 
                            message(GameControlCommand.NEW);
                            break;
                        case UIMultiPlayerView.HOME:
                            message(GameControlCommand.HOME);
                            break;
                        case UIMultiPlayerView.AGREE:
                            message(GameControlCommand.AGREE);
                            break;
                        case UIMultiPlayerView.REJECT:
                            message(GameControlCommand.REJECT);
                            break;
                        case UIMultiPlayerView.SURRENDER:
                            message(GameControlCommand.SURRENDER);
                            break;
                        default: break;
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    message(GameControlCommand.EXIT);
                }
            });
        }
        
    }
    
    // Mousing over the label-buttons makes a message show up in the messgeboard
    @Override
    public void message(GameControlCommand msg) { 
        switch (msg){
            case UNDO: viewPanel.changeMsg("Undo move.");break;
            case MUSIC:viewPanel.changeMsg("Next song");break;
            case NEW:  viewPanel.changeMsg("Start a new game"); break;
            case HOME: viewPanel.changeMsg("Back to menu"); break;
            case INVALID: viewPanel.changeMsg("Invalid move!"); break;
            case OPPONENT_STUCK: viewPanel.changeMsg("Make another move!"); break;
            case USER_STUCK: viewPanel.changeMsg("You can't move now!"); break;
            case OPP_REJECT:
                viewPanel.changeMsg("The opponent rejected the request.");
                break;
            case OPP_AGREE:
                viewPanel.changeMsg("The opponent accepted the request.");
                break;
            case AGREE: viewPanel.changeMsg("Accept opponent's request.");
                break;
            case REJECT: viewPanel.changeMsg("Refuse opponent's request.");
                break;
            case SURRENDER: viewPanel.changeMsg("Admit defeat."); break;
            case OPP_SURRENDER: viewPanel.changeMsg("The opponent admits defeat! You win."); break;
            default:
                if ("".equals(GameMainModel.music)) break;
                String prefix = "music/";
                String name = GameMainModel.music.substring(prefix.length());
                viewPanel.changeMsg("Now playing: "+ name);
                break;
        }
    }
    
    @Override
     public void message(String msg) {
         viewPanel.changeMsg(msg);
     }
    
    public void set_last(int i, int j, int color){      // sets last move
        switch (color){
            case GameInterface.BLACKCOLOR: btn[i][j].setIcon(viewPanel.black2);break;
            case GameInterface.WHITECOLOR: btn[i][j].setIcon(viewPanel.white2); break;
            default: btn[i][j].setIcon(null);break;
        }
    }
    
    public void set_btn(){    // sets color of all buttons
        for (int i = 0; i < tileLength;i++){
            for (int j = 0;j < tileLength;j++){
                btn[i][j].setVisible(true);
                switch (GameMainModel.get_board(i,j)){
                    case GameInterface.BLACKCOLOR: btn[i][j].setIcon(viewPanel.black); break;
                    case GameInterface.WHITECOLOR: btn[i][j].setIcon(viewPanel.white); break;
                    default: btn[i][j].setIcon(null);break;
                }
            }
        }
        //System.out.print("here");
    }
    
    private void set_num() {        // sets the scoreboard
        bNum = wNum = 0;
        for (int i = 0; i < tileLength; i++) {
            for (int j = 0; j < tileLength; j++) {
                if (GameMainModel.get_board(i, j) == GameInterface.BLACKCOLOR) {
                    bNum += 1;
                }
                if (GameMainModel.get_board(i, j) == GameInterface.WHITECOLOR) {
                    wNum += 1;
                }
            }
        }
        viewPanel.setScore(String.valueOf(wNum), GameInterface.WHITECOLOR);
        viewPanel.setScore(String.valueOf(bNum), GameInterface.BLACKCOLOR);
        //System.out.print("here");
    }
    
    private void refresh() {
        set_btn();
        set_num();
        viewPanel.repaint();
    }

    public void change_color(ArrayList<Integer> i, ArrayList<Integer> j, int color) throws InterruptedException {
        if (i.size() != j.size()) {
            return;
        }
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    if (color == GameInterface.BLACKCOLOR) {
                        for (int n = 0; n < gifFrames; ++n) {
                            for (int _i = 0; _i < i.size(); ++_i) {
                                btn[i.get(_i)][j.get(_i)].setIcon(viewPanel.wtob[n]);
                            }
                            this.sleep(frameWait);
                        }
                    } else if (color == GameInterface.WHITECOLOR) {
                        for (int n = 0; n < gifFrames; ++n) {
                            for (int _i = 0; _i < i.size(); ++_i) {
                                btn[i.get(_i)][j.get(_i)].setIcon(viewPanel.btow[n]);
                            }
                            this.sleep(frameWait);
                        }
                    }
                } catch (InterruptedException ex) {
                }
            }
        };
        th.start();
    }


    public void check_end() {
        boolean all_stuck = false;
        if (GameStrategyModel.solution(GameMainModel.myColor).x == -1 && GameStrategyModel.solution(-1 * GameMainModel.myColor).x == -1) {
            all_stuck = true;
        }
        if (all_stuck || bNum + wNum == tileLength * tileLength || inGame && (bNum == 0 || wNum == 0)) {
            inGame = false;
        }
    }
    
    @Override
    public void initOpponent(int color, String name, String rank){
        opponentColor = -1*color;
        viewPanel.initOpponent(color, name, rank);
    }

    @Override
    public void setOppScore(int score){
        viewPanel.setOpponent(score);
    }

    @Override
    public boolean isInGame() {
        return inGame;
    }

    @Override
    public void initGame() {
        inGame = true;
        canUndo = oppHasUndo = false;
        requestUndo = oppRequestUndo = requestNew = oppRequestNew = false;
        GameMainModel.oppColor = opponentColor;
        GameMainModel.myColor = -1* opponentColor;
        canMove = (opponentColor == GameInterface.WHITECOLOR);
        GameMainModel.init(UISettings.getIsMusicOn());
        refresh();
    }

    @Override
    public int getOppColor() {
        return opponentColor;
    }

    @Override
    public boolean make_step(int x, int y, int color) {
        if (!inGame && !canMove) { return false; }
        
        if (GameMainModel.make_step(new GameMainModel.Point(x, y, color))) {
            refresh();
            try {
                change_color(GameMainModel.get_x(), GameMainModel.get_y(), color);
            } catch (InterruptedException ie) { }
            set_last(x, y, color);

            message(GameControlCommand.EXIT);
            
            if (color == opponentColor){
                canMove = true;
                canUndo = true;
                oppHasUndo = false;
            } else {
                canMove = false;
                canUndo = false;
            }
            
            return true;
        } else {
            message(GameControlCommand.INVALID);
            Timer timer = new Timer(2000, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Runnable doRun = () -> {
                        message(GameControlCommand.EXIT);
                    };
                    SwingUtilities.invokeLater(doRun);
                }
            });
            timer.start();
            timer.setRepeats(false);
            return false;
        }
    }

    @Override
    public void gameOverCleanup() {
        inGame = false;
        canMove = false;
        canUndo = false;
        oppHasUndo = false;
        requestUndo = oppRequestUndo = requestNew = oppRequestNew = false;
    }

    @Override
    public void undoMove(int color) {
        GameMainModel.back_step(color);
        requestUndo = oppRequestUndo = requestNew = oppRequestNew = false;
        canUndo = false;
        canMove = true;
        if (color == opponentColor){
            oppHasUndo = true;
        }
        refresh();
    }

    @Override
    public void oppRequestUndo() {
        oppRequestUndo = true;
        requestUndo = false;
        canMove = false;
        canUndo = false;
        oppHasUndo = false;
    }
    
    @Override
    public void oppRequestNew() {
        oppRequestNew = true;
        requestNew = false;
        oppRequestUndo = requestUndo = false;
        canMove = false;
        canUndo = false;
        oppHasUndo = false;
    }

    // opp tells user that user cannot undo, so user must make a move.
    @Override
    public void cantUndoMove() {
        requestUndo = false;
        oppRequestUndo = false;
        canMove = true;
        canUndo = false;
        oppRequestNew = false;
        requestNew = false;
        oppHasUndo = false;
    }
    
    @Override
    public boolean isUndoRequest() {
        return requestUndo;
    }

    @Override
    public boolean isNGRequest() {
        return requestNew;
    }
    
    
    private void button_new() {
        if(!canMove){
            message("Please wait for your opponent!");
            return;
        }

        if (!inGame) {
            Reversi.socket.newGame(SocketInterface.GAME_AGAIN);
        } else {
            Reversi.socket.surrender();
            gameOverCleanup();
            Reversi.socket.newGame(SocketInterface.GAME_AGAIN);    
        }
        message("Starting a new game.");
        initGame();
    }

    private void button_undo() {
         if (canMove && canUndo && !requestUndo && !oppHasUndo) {
            message("Undo request sent. Waiting...");
            Reversi.socket.undo();
            canMove = false;
            canUndo = false;
            requestUndo = true;
            oppRequestUndo = false;
        } else {
             message("You cannot undo.");
         }
    }

    private void button_music() {
        if (UISettings.getIsMusicOn()) {
            GameMainModel.change_music();
        } else {
            message("Music is not on!");
        }
    }

    private void button_home() {
        if (inGame) {
            Reversi.socket.quit(SocketInterface.QUIT_WHEN_IN_GAME);
        } else {
            Reversi.socket.quit(SocketInterface.QUIT_WHEN_GAME_OVER);
        }
        performSegue(UIViewEnum.MENU);
    }

    private void button_surrender() {
        if (inGame && canMove) {
            Reversi.socket.surrender();
            gameOverCleanup();
            message(GameControlCommand.SURRENDER);
            message("You have surrendered.");
        } else {
            message("You cannot surrender!");
        }
    }

    private void button_agree() {
        if (oppRequestUndo) {
            Reversi.socket.confirm(SocketInterface.CAN_UNDO);
            message("You have accepted opponent's request");
            this.undoMove(opponentColor);
            oppRequestUndo = false;
        }  else {
            message("The opponent has not made a request!");
        }
        
    }

    private void button_reject() {
        if (oppRequestUndo) {
            Reversi.socket.confirm(SocketInterface.CANNOT_UNDO);
            message("You have denied opponent's request");
            oppRequestUndo = false;
        }  else {
            message("The opponent has not made a request!");
        }
    }

    
    
}
