
package simpleUI;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.Box;
import reversi2.*;


public class UIHello extends UIView {

    private JLabel logo, login, signup, single;
    private ImageIcon logoIc, loginIc, signupIc, singleIc;
      
    public UIHello() throws IOException {
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.black);
        
        initFrame();
        initComponents();
        
        setVisible(true);
    }

    
    private void initFrame() throws IOException{
        // add logo
        logoIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/logo2.png")));
        logo = new JLabel(logoIc);
        logo.setAlignmentX(CENTER_ALIGNMENT);
        
        loginIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/login.png")));
        login = new JLabel(loginIc);
        signupIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/signup.png")));
        signup = new JLabel(signupIc);
        singleIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/single_player.png")));
        single = new JLabel(singleIc);

        login.setAlignmentX(CENTER_ALIGNMENT);
        signup.setAlignmentX(CENTER_ALIGNMENT);
        single.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(logo);
        add(login);
        add(signup);
        add(single);
        add(Box.createVerticalGlue());
        
    }
    
    private void initComponents(){
        login.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Reversi.connected){
                    performSegue(UIViewEnum.LOGIN);
                } else {
                   JOptionPane.showMessageDialog(null, "You are not connected. Please check your connection and restart the game.");
                }
            }
        });
        signup.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Reversi.connected){
                    performSegue(UIViewEnum.SIGNUP);
                } else {
                    JOptionPane.showMessageDialog(null, "You are not connected. Please check your connection and restart the game.");
                }
            }
        });
        single.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                Reversi.loggedIn = false;
                performSegue(UIViewEnum.SINGLEPLAYER);
            }
        });
    }
    
}
