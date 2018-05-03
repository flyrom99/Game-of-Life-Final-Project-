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
    int xNum;
    int yNum;
    int xInc;
    int yInc;
    Life life;
    ArrayList<Square> liveCells = new ArrayList<>();
    public double getH() {
        return h;
    }
    public double getW() {
        return w;
    }

    public Canvas(int w, int h, int numXSq, int numYSq, Life life) {
        this.w = w;
        this.h = h;
        this.life = life;
        squares = new Square[numYSq][numXSq];
        this.xNum = numXSq;
        this.yNum = numYSq;
        xInc = w/(numXSq);
        yInc = h/(numYSq);
        int xCounter = 0;
        for (int x = 0; x < this.w; x += xInc) {
            int yCounter = 0;
            for (int y = 0; y < this.h; y += yInc) {
                int xSquare = x/xInc;
                int ySquare = y/yInc;
                //System.out.println("xInc: " + xInc + "    yInc: " + yInc);
                //System.out.println("xSq: " + xSquare + "    ySq: " + ySquare);
                //System.out.println("x: " + x + "   y: " + y);
                squares[yCounter][xCounter] = new Square(x,y,xInc,yInc,false);
                yCounter++;
            }
            xCounter++;
        }

        this.setSize(w,h);
        addListeners(this);
    }
    public ArrayList<Square> getLiveCells()
    {
        return liveCells;
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
                xInc = w/xNum;
                yInc = h/yNum;
                int xSquare = (int)(x/xInc);
                int ySquare = (int)(y/yInc);
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
        xInc = w/xNum;
        yInc = h/yNum;
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
        g.setColor(Color.black);
        for (int r = 0; r < squares.length; r++) {
            for (int c = 0; c < squares[0].length; c++) {
                Square s = squares[r][c];
                    if (s.isFilled()) {
                        {
                            //if(life.getBoard()[r][c].isRedrawThis())
                                g.fillRect(s.getX(), s.getY(), s.getW(), s.getH());
                            life.animateCell(life.getPoint(c, r));
                        }
                    } else {
                        //if(life.getBoard()[r][c].isRedrawThis())
                            g.drawRect(s.getX(), s.getY(), s.getW(), s.getH());
                        life.killCell(life.getPoint(c, r));
                    }
                    squares[r][c].setClickedOn(false);

            }
        }
    }
}
