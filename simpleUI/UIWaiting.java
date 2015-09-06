package simpleUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import reversi2.Reversi;
import reversi2.SocketInterface;
import reversi2.UIView;
import reversi2.UIViewEnum;


public class UIWaiting extends UIView {
    
    private JLabel waiting, cancel, fail;
    private ImageIcon waitingIc, cancelIc, failIc;
    
    public UIWaiting() throws IOException {
        
        setBackground(Color.black);
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        
        initFrame();
        System.out.println("UIwait");
        
        setVisible(true);
    }

    private void initFrame() throws IOException{
  
        waitingIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/waiting.png")));
        cancelIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/cancel.png")));
        failIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/connection_fail.png")));
        waiting = new JLabel(waitingIc);
        cancel = new JLabel(cancelIc);
        fail = new JLabel(failIc);
        waiting.setAlignmentX(CENTER_ALIGNMENT);
        cancel.setAlignmentX(CENTER_ALIGNMENT);
        fail.setAlignmentX(CENTER_ALIGNMENT);
        
        add(Box.createVerticalGlue());
        add(waiting);
        add(cancel);
        add(Box.createVerticalGlue());
        
        
        
        cancel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Reversi.socket.quit(SocketInterface.QUIT_WHEN_WAITING);
                    performSegue(UIViewEnum.MENU);
                }
        });
        
    }
}
