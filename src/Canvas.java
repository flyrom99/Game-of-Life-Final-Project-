import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Canvas extends JPanel {

    Square[][] squares;
    int w;
    int h;
    static int num;
    public int getH() {
        return h;
    }
    public int getW() {
        return w;
    }

    public Canvas(int w, int h, int numSquares) {
        this.w = w;
        this.h = h;
        squares = new Square[numSquares][numSquares];
        this.num = numSquares;
        int xInc = (this.w / num);
        int yInc = (this.h / num);
        for (int x = 0; x < this.w; x += xInc) {
            for (int y = 0; y < this.h; y += yInc) {
                squares[y/yInc][x/xInc] = new Square(x,y,xInc,yInc,false);
            }
        }
    }
    public static void main(String[] args)
    {

    }

    public  void addListeners(Canvas GUI)
    {
        GUI.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int xInc = (GUI.getW()/num);
                int yInc = (GUI.getH()/num);
                int xSquare = (x/xInc);
                int ySquare = (y/yInc);
                Square square = squares[ySquare][xSquare];
                if(square.isFilled())
                    square.setFilled(false);
                else
                    square.setFilled(true);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

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
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        for (int r = 0; r < squares[0].length; r++) {
            for (int c = 0; c < squares.length; c++) {
                Square s = squares[r][c];
                if (s.isFilled()) {
                    g.fillRect(s.getX(), s.getY(), s.getW(), s.getH());
                } else {
                    g.drawRect(s.getX(), s.getY(), s.getW(), s.getH());
                }
            }
        }
    }

}
