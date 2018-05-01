import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI {
    final int fps = 24;

    static Timer timer;
    static boolean timerActive = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game of Life");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(650, 650);

        Life life = new Life(70);

        Canvas c = new Canvas(600, 600, 70, life);

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem reset = new JMenuItem("Reset");

        reset.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                life.reset();
                c.reset();
                c.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        menu.add(reset);
        bar.add(menu);

        c.setPreferredSize(frame.getSize());
        c.setBackground(Color.WHITE);
        c.setVisible(true);
        c.addListeners(c);

        frame.setResizable(false);
        frame.setVisible(true);
        timer = new Timer(500, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                for(int i = 0;i<1;i++) {
                    c.calcNewPositions(life);
                }

                    c.repaint();
            }
        });
      //  timer.start();

        JButton b = new JButton("Start/Stop");
        b.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(timerActive) {
                    timer.stop();
                    timerActive = false;
                }
                else {
                    timer.start();
                    timerActive = true;
                }

            }


            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.setBounds(40, 80, 601, 601);
        frame.add(c, BorderLayout.CENTER);
        frame.add(b, BorderLayout.PAGE_END);
        frame.add(bar, BorderLayout.BEFORE_FIRST_LINE);
        Square[][] rc = c.getSquares();

    }

}
