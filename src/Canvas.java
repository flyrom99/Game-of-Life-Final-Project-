import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Canvas extends JPanel{

    Square[][] squares;
    int w;
    int h;
    static int num;
    int xInc;
    int yInc;
    Life life;
    ArrayList<Square> liveCells = new ArrayList<>();
    public int getH() {
        return h;
    }
    public int getW() {
        return w;
    }

    public Canvas(int w, int h, int numSquares, Life life) {
        this.w = w;
        this.h = h;
        this.life = life;
        squares = new Square[numSquares][numSquares];
        this.num = numSquares;
        xInc = w/num;
        yInc = h/num;
        for (int x = 0; x < this.w; x += xInc) {
            for (int y = 0; y < this.h; y += yInc) {
                //System.out.println("y/yInc: " + y/yInc + "  x/xInc: " + x/xInc);
                squares[y/yInc][x/xInc] = new Square(x,y,xInc,yInc,false);
            }
        }

        this.setSize(w,h);
        addListeners(this);
    }
    public ArrayList<Square> getLiveCells()
    {
        return liveCells;
    }
    public void bruh()
    {
        repaint();
    }
    public void calcNewPositions(Life life)
    {
        life.doCycle(1);
        boolean[][] board = life.getStatArr();
        for (int r = 0; r < board[0].length; r++) {
            for (int col = 0; col < board.length; col++) {
                if (board[r][col])
                    squares[r][col].setFilled(true);
                else
                    squares[r][col].setFilled(false);
            }
        }
    }
    public void reset()
    {
        for(int r = 0;r<squares[0].length;r++)
        {
            for(int c = 0;c<squares.length;c++)
            {
                squares[r][c].setFilled(false);
            }
        }
        liveCells.clear();
    }
    public Square[][] getSquares()
    {
        return squares;
    }
    public static void main(String[] args)
    {

    }

    public  void addListeners(Canvas GUI)
    {
        GUI.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();
                xInc = w/num;
                yInc = h/num;
                int xSquare = (x/xInc);
                int ySquare = (y/yInc);
                Square square = squares[ySquare][xSquare];
                if(!square.isClickedOn()) {
                    square.toggleFilled();
                    square.setClickedOn(true);
                    repaint();
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
    }

    public void calcSquareLocations()
    {
        xInc = w/num;
        yInc = h/num;
        int x = 0;

        int y = 0;
        int yC = 0;
        while(y<h)
        {
            int xC = 0;
            while(x<w)
                System.out.println(y/yInc);
                System.out.println(x/xInc);
                squares[yC][xC].setX(x);
                squares[yC][xC].setY(y);
                xC++;
            }
            yC++;
        }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("here");
        g.setColor(Color.black);
        for (int r = 0; r < squares[0].length; r++) {
            for (int c = 0; c < squares.length; c++) {
                Square s = squares[r][c];
                if (s.isFilled()) {
                    {
                        g.fillRect(s.getX(), s.getY(), s.getW(), s.getH());
                        life.animateCell(life.getPoint(c,r));
                    }
                } else {
                    g.drawRect(s.getX(), s.getY(), s.getW(), s.getH());
                    life.killCell(life.getPoint(c,r));
                }
                squares[r][c].setClickedOn(false);
            }
        }
    }
}
