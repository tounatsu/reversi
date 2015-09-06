package reversi2;

import game.*;
import simpleUI.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Reversi extends JFrame {
    
    public static boolean connected = false;
    public static boolean loggedIn = true;
    public static boolean isMusicOn = false;

    public static String label[] = {"Nickname","rank","100","20","70","10"}; 
    public static UIView ui;
    public static final GameTrigger game = new GameTrigger();
    public static SocketInterfaceImplementer socket;
    
    public Reversi() throws IOException{
        setSize(550, 730);
        setLocation(50, 50);
        setMinimumSize(new Dimension(550,730));
        setMaximumSize(new Dimension(1000,900));
        setTitle("Reversi!");
        myfunc();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        
        setIconImage(ImageIO.read(getClass().getResource("/graphics/icon.png")));
        
        try{
            socket = new SocketInterfaceImplementer();
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        
        ui = new UIHello();
        getContentPane().add(ui);
        
        listenForSegue();
    }
    
    private void listenForSegue(){
        ui.addPropertyChangeListener("segueTo", (PropertyChangeEvent evt) -> {
            try {
                this.getContentPane().remove(ui);
                this.revalidate();
                switch(evt.getNewValue().toString()){
                    case "HELLO": ui = new UIHello(); listenForSegue(); break;
                    case "LOGIN": ui = new UILogin(); listenForSegue(); break;
                    case "SIGNUP": ui = new UISignup(); listenForSegue(); break;
                    case "MENU": ui = new UIMenu(); listenForSegue(); break;
                    case "PROFILE": ui = new UIProfile(); listenForSegue(); break;
                    case "SETTINGS": ui = new UISettings(); listenForSegue(); break;
                    case "INSTRUCTIONS": ui = new UIInstructions(); listenForSegue(); break;
                    case "SINGLEPLAYER": ui = new UISinglePlayerController(); listenForSegue(); break;
                    case "CONNECT": break;
                    case "WAITING": ui = new UIWaiting(); listenForSegue(); break;
                    case "MULTIPLAYER": ui = new UIMultiPlayerController(); listenForSegue(); break;
                    default: break;
                }
            } catch (IOException ex) {
                Logger.getLogger(Reversi.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getContentPane().add(ui);
            this.revalidate();
        });
    }
    
    public static void main(String[] s){
        try {
            Reversi w = new Reversi();
            w.setVisible(true);
            Reversi.connected = true;
            Reversi.socket.clientDelegate.run();
        } catch(java.lang.NullPointerException ex1){
            JOptionPane.showMessageDialog(null, "Connection to server failed. You will not be able to log in to your account." +
                "You can still play against the computer.");
            Reversi.connected = false;
        }

        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Reversi exception: " + ex.toString());
            ex.printStackTrace();
        }
        
    }
    
}
