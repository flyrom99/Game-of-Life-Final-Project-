import javax.swing.*;
import java.awt.*;

public class GUI {

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Game of Life");
        frame.setSize(500,500);
        Canvas c = new Canvas(500,500,100);
        c.setBackground(Color.WHITE);
        c.setVisible(true);
        c.addListeners(c);
        frame.setVisible(true);
        frame.setBounds(40,80,500,500);
        frame.add(c);
    }
}
