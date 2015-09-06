
package reversi2;

import game.GameControlCommand;
import game.GameInterface;
import game.UIMultiPlayerController;
import game.UIMultiPlayerControllerInterface;
import javax.swing.JOptionPane;
import simpleUI.UILogin;
import simpleUI.UIMenu;
import simpleUI.UISignup;
import simpleUI.UIWaiting;

public class GameTrigger implements GameInterface {
    
    @Override
    public void makeStep(int x, int y) {
        if (Reversi.ui instanceof UIMultiPlayerController){
             UIMultiPlayerController temp = (UIMultiPlayerController) Reversi.ui;
             temp.make_step(x, y, temp.getOppColor());
        }
    }

    @Override
    public void regMsg(int code, String msg) {
        if (Reversi.ui instanceof UISignup){
        //System.out.print("register2");
        switch (code) {
            case GameInterface.LOG_OK:
                Reversi.ui.performSegue(UIViewEnum.MENU);
                break;
            case GameInterface.LOG_BAD: 
                JOptionPane.showMessageDialog(null, msg);
                break;
            default: break;
        }
        
        }
    }

    @Override
    public void logMsg(int code, String msg) {
        if (Reversi.ui instanceof UILogin){
        //System.out.print("login2");
        switch (code) {
            case GameInterface.REG_OK:
                // success. go to menu page 
                Reversi.ui.performSegue(UIViewEnum.MENU);
                break;
            case GameInterface.REG_BAD: 
                // fail. alert.
                JOptionPane.showMessageDialog(null, msg);
                break;
            default: break;
        }
        
        }
    }

    @Override
    public void startGame(int color, String name, String rank) {
        if (Reversi.ui instanceof UIWaiting || Reversi.ui instanceof UIMenu){
            
            // server distributes a color to user.
            // user segues to the interface.
            // after interface is constructed, initialize opponent info and start a new game.
            if (color == GameInterface.BLACKCOLOR || color == GameInterface.WHITECOLOR){
                Reversi.ui.performSegue(UIViewEnum.MULTIPLAYER);
                if (Reversi.ui instanceof UIMultiPlayerController) {
                    UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
                    temp.initOpponent(color, name, rank);
                    //Reversi.socket.getScore();
                    temp.initGame();
                }
            } 
        }
    }

    @Override
    public void waitingForGame() {
            System.out.println("waiting");
            Reversi.ui.performSegue(UIViewEnum.WAITING);
        Reversi.ui.revalidate();
        Reversi.ui.repaint();
    }

    @Override
    public void response(int code, String msg) {
        // server response to user's step.
        if (Reversi.ui instanceof UIMultiPlayerController){
            if(code == GameInterface.RESPONSE_OK){
                // last step was ok, no need to do anything.
            } else if (code == GameInterface.RESPONSE_BAD){
                // has bug: tell user, tell server, and quit game.
                JOptionPane.showMessageDialog(null, msg);
                //Reversi.socket.hasBug(code, msg);
                
                //Reversi.socket.quit(SocketInterface.QUIT_WHEN_GAME_OVER);
                //Reversi.ui.performSegue(UIViewEnum.MENU);
            }
        }
    }

    @Override
    public void gameOver(int color, int your, int opp) {
        if (Reversi.ui instanceof UIMultiPlayerController){
            UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
            // game over, show winner and clean up statuses
            temp.gameOverCleanup();
            if (color == temp.getOppColor()){
                temp.message("You lose. Your score is " + your);
            } else if (color == -1*temp.getOppColor()){
                temp.message("You win. Your score is " + your);
            } else {
                temp.message("It's a draw. Your score is " + your);
            }
            temp.setOppScore(opp);
        }
    }

    @Override
    public void undoRequest() {
        // opponent asks to undo.
        if (Reversi.ui instanceof UIMultiPlayerController){
            UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
            // tells user opp request
            // enables response
            temp.message("Opponent wants to undo. Do you agree?");
            temp.oppRequestUndo();
        }
    }

    @Override
    public void oppConfirm(int code) {
        // opponent agrees user's request.
        if (Reversi.ui instanceof UIMultiPlayerController){
            UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
            if (code == GameInterface.YES){
                temp.message(GameControlCommand.OPP_AGREE);
                temp.undoMove(-1*temp.getOppColor());
            } else if (code == GameInterface.NO){
                temp.message(GameControlCommand.OPP_REJECT);
                temp.cantUndoMove();
            }
        }
    }

    // opponent surrendered
    @Override
    public void oppSurrendered() {
        if (Reversi.ui instanceof UIMultiPlayerController){
            UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
            // clean up board, show message. do not do anything else to board.
            temp.gameOverCleanup();
            temp.message(GameControlCommand.OPP_SURRENDER);
        }
    }

    @Override
    public void oppQuit() {
        if (Reversi.ui instanceof UIMultiPlayerController){
            UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
            
            // opp quit: go back to menu.
            temp.message(GameControlCommand.OPP_QUIT);
            JOptionPane.showMessageDialog(null, "Opponent has quit the game!");
            
        }
        Reversi.ui.performSegue(UIViewEnum.MENU);
    }

    @Override
    public void oppRequestNewGame() {
        if (Reversi.ui instanceof UIMultiPlayerController){
            UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
            // tells user opp has requested new game. user can accept or reject.
            if (temp.isInGame()){
                oppSurrendered();  
            } 
            temp.message(GameControlCommand.OPP_REQUEST_NEW);
            temp.oppRequestNew();
        }
    }

    @Override
    public void oppDisconnected() {
        if (Reversi.ui instanceof UIMultiPlayerController){
            UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
            
            temp.message(GameControlCommand.OPP_DISCONNECTED);
            JOptionPane.showMessageDialog(null, "Opponent is disconnected!");
        }
        Reversi.ui.performSegue(UIViewEnum.MENU);
    }

    @Override
    public void inValidCommand(int code, String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public void changeNickReply(int code, String msg) {
        if (code == 0) return;
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public void oppTerminateGame() {
        oppQuit();
    }

    @Override
    public void getStats(String nick, int grades, int win, int lose, int draw) {
        
        int total = win + lose + draw;
        Reversi.label[0] = nick;
        Reversi.label[1] = grades + "";
        Reversi.label[2] = total + "";
        Reversi.label[3] = win + "";
        Reversi.label[4] = lose + "";
        Reversi.label[5] = draw + "";

        System.out.println(nick + " " + grades + " " + total + " " + win + " " + lose + " " + draw);
        
    }

    @Override
    public void getScore(int yourScore, int oppositeScore){
        if (Reversi.ui instanceof UIMultiPlayerController) {
            UIMultiPlayerControllerInterface temp = (UIMultiPlayerController) Reversi.ui;
            temp.setOppScore(oppositeScore);
            // TBC
        }
    }

}
