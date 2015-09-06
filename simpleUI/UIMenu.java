package simpleUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import game.*;
import reversi2.*;


public class UIMenu extends UIView {
    
    private final int OPTION_NUM = 6;
    private JLabel[] menu;
    private ImageIcon[] menuIc;
    
    public UIMenu() throws IOException {
        
        setBackground(Color.black);
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        
        initFrame();

        Reversi.socket.getStats();
        
        setVisible(true);
    }

    private void initFrame() throws IOException{
  
        menu = new JLabel[OPTION_NUM +2];
        menuIc = new ImageIcon[OPTION_NUM +2];
        
        add(Box.createVerticalGlue());
        for (int i = 0; i < OPTION_NUM + 2; ++i){
            menuIc[i] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/menu/menu"+i+".png")));
            menu[i] = new JLabel(menuIc[i]);
            menu[i].setAlignmentX(CENTER_ALIGNMENT);
            add(menu[i]);
            
            final int _i = i;
            menu[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    switch(_i){
                        case 1: performSegue(UIViewEnum.SINGLEPLAYER); break;
                        case 2: 
                            Reversi.socket.newGame(SocketInterface.GAME_NEW);
                            performSegue(UIViewEnum.CONNECT);
                            break;
                        case 3: performSegue(UIViewEnum.SETTINGS); break;
                        case 4: 
                            //Reversi.socket.getStats();
                            performSegue(UIViewEnum.PROFILE); 
                            break;
                        case 5: performSegue(UIViewEnum.INSTRUCTIONS); break;
                        case 6: 
                            // TBC
                            Reversi.socket.LGOUT();
                            Reversi.loggedIn = false;
                            GameMainModel.stop_music();
                            performSegue(UIViewEnum.HELLO); break;
                    }
                }
            });

        }
        add(Box.createVerticalGlue());
    }
    
}
