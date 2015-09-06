package reversi2;

import javax.swing.JPanel;

public class UIView extends JPanel {
    
    public void performSegue(UIViewEnum v){
        this.firePropertyChange("segueTo", UIViewEnum.NULL, v);
    }
    
}
