package game;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

class UIMultiPlayerView extends UIGameView {
    
    private JPanel p_left, p_right; 
    
    private JLabel opponent, oppname, opprank; 
    private ImageIcon whiteIc, blackIc;
    
    public static final int UNDO = 0;
    public static final int REJECT = 1;
    public static final int AGREE = 2;
    public static final int NEW = 3;
    public static final int HOME = 6;
    public static final int MUSIC = 4;
    public static final int SURRENDER = 5;
    
    public static final int ICONS = 7;
    

    public UIMultiPlayerView() throws IOException{
        
        // init board with 7 components
        super(7);
        
        setBackground(Color.black);
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        
        loadIcons();
        initFrame();
        

    }
    
    
    private void initFrame() throws IOException {
       // Initialize panels
        
        p_left = new JPanel(new GridLayout((int)Math.ceil((double)iconNum/2.0),1));
        p_right = new JPanel(new GridLayout((int)Math.floor((double)iconNum/2.0),1));
        
        p_left.setBackground(Color.black);
        p_right.setBackground(Color.black);
        
        whiteIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/opponent_white.png")));
        blackIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/opponent_black.png")));
        opponent = new JLabel(blackIc);
        oppname = new JLabel();
        opprank = new JLabel();
        
        // set subcomponents here
        setMessageBoard();
        setScoreBoard();
        //setOpponent("name", "rank");
        setIcons();
        
        Box b_down = Box.createVerticalBox();
        b_down.add(Box.createVerticalGlue());
        b_down.add(opponent);
        b_down.add(Box.createVerticalGlue());
        b_down.add(scoreboard);
        b_down.add(Box.createVerticalGlue());
        b_down.setAlignmentX(CENTER_ALIGNMENT);
        opponent.setAlignmentX(CENTER_ALIGNMENT);
        scoreboard.setAlignmentX(CENTER_ALIGNMENT);
        
        Box b = Box.createHorizontalBox();
        b.add(Box.createHorizontalGlue());
        b.add(p_left);
        b.add(board);
        b.add(p_right);
        b.add(Box.createHorizontalGlue());
        
        add(Box.createVerticalGlue());
        add(messageboard);
        messageboard.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(b);
        add(Box.createVerticalGlue());
        this.add(b_down);
        
        add(Box.createVerticalGlue());
    }
    
    
    private void setMessageBoard() throws IOException {
        messageIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/label3.png")));
        messageboard = new JLabel(messageIc);
        messageboard.setLayout(new BoxLayout(messageboard,BoxLayout.PAGE_AXIS));
       
        messageboard.add(Box.createVerticalGlue());
        messageboard.add(Box.createVerticalGlue());
        message = new JLabel("Welcome to Reversi");
        message.setAlignmentX(CENTER_ALIGNMENT);
        message.setForeground(Color.getHSBColor(0.05f, 0.9f, 0.10f));
        message.setFont(new Font("Arial",Font.BOLD,22));
        messageboard.add(message);
        messageboard.add(Box.createVerticalGlue());
    }
    
    private void setScoreBoard() throws IOException {
        scoreIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/label.png")));
        scoreboard = new JLabel(scoreIc);
        
        scoreboard.setLayout(new BoxLayout(scoreboard, BoxLayout.LINE_AXIS));
        
        wmsg = new JLabel(String.valueOf(0),SwingConstants.LEFT);
        bmsg = new JLabel(String.valueOf(0),SwingConstants.RIGHT);
        wmsg.setForeground(Color.getHSBColor(0.08f, 0.9f, 0.2f));
        wmsg.setFont(new Font("Arial",Font.BOLD,40));
        bmsg.setForeground(Color.getHSBColor(0.09f, 0.9f, 0.2f));
        bmsg.setFont(new Font("Arial",Font.BOLD,40));
        
        scoreboard.add(Box.createHorizontalGlue());
        scoreboard.add(bmsg);
        scoreboard.add(Box.createHorizontalGlue());
        scoreboard.add(wmsg);
        scoreboard.add(Box.createHorizontalGlue());
    }
    
    private void setIcons(){
        for (int i = 0; i < iconNum; ++i){
            label[i] = new JLabel(icon[i]);
            if (i < iconNum/2){
                p_right.add(label[i]);
            } else {
                p_left.add(label[i]);
            }
        }
    }
    
    private void setOpponent(String name, String rank){
        
        opponent.setAlignmentX(CENTER_ALIGNMENT);
        opponent.setLayout(new BoxLayout(opponent, BoxLayout.PAGE_AXIS));
        
        oppname.setText(name);
        opprank.setText(rank);
        oppname.setForeground(Color.black);
        oppname.setFont(new Font("Arial",Font.BOLD,20));
        opprank.setForeground(Color.black);
        opprank.setFont(new Font("Arial",Font.BOLD,20));
        oppname.setAlignmentX(CENTER_ALIGNMENT);
        opprank.setAlignmentX(CENTER_ALIGNMENT);
        
        opponent.add(Box.createVerticalGlue());
        opponent.add(oppname);
        opponent.add(Box.createVerticalGlue());
        opponent.add(opprank);
        opponent.add(Box.createVerticalGlue());
        
    }  

    
    
    private void loadIcons() throws IOException {
        // Load label imageicons
        icon = new ImageIcon[iconNum];
        icon2 = new ImageIcon[iconNum];
        
        boardIc = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/board.png")));
        icon[UNDO] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/undo.png")));
        icon[NEW] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/new.png")));
        icon[REJECT] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/reject.png")));
        icon[AGREE] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/agree.png")));
        icon[MUSIC] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/music.png")));
        icon[HOME] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/back_small.png")));
        icon[SURRENDER] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/surrender.png")));
        icon2[UNDO] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/undo2.png")));
        icon2[REJECT] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/reject2.png")));
        icon2[NEW] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/new2.png")));
        icon2[AGREE] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/agree2.png")));
        icon2[MUSIC] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/music2.png")));
        icon2[HOME] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/back_small2.png")));
        icon2[SURRENDER] = new ImageIcon(ImageIO.read(getClass().getResource("/graphics/surrender2.png")));
        
    }
    

    public void initOpponent(int color, String name, String rank) {
        
        if (color == GameInterface.BLACKCOLOR){
            opponent.setIcon(whiteIc);
        } else if (color == GameInterface.WHITECOLOR){
            opponent.setIcon(blackIc);
        }
        setOpponent(name, rank);
        repaint();
    }
    public void setOpponent(int oppScore){
        opprank.setText(oppScore + "");
        revalidate();
    }
    
}
