import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;

public class Canvas extends JPanel {

    Square[][] squares;
    int w;
    int h;
    int xNum;
    int yNum;
    int xInc;
    int yInc;
    Life life;
    Life previousLife;
    ArrayList<Square> liveCells = new ArrayList<>();
    ArrayList<Square> justChanged = new ArrayList<>();
    HashMap<Square,Boolean> changed;
    BufferedImage image;

    public Canvas(int w, int h, int numXSq, int numYSq) {
        this.w = w;
        this.h = h;
        changed = new HashMap<>();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        squares = new Square[numYSq][numXSq];
        this.xNum = numXSq;
        this.yNum = numYSq;
        xInc = w / (numXSq);
        yInc = h / (numYSq);
        int xCounter = 0;
        for (int x = 0; x < this.w; x += xInc) {
            int yCounter = 0;
            for (int y = 0; y < this.h; y += yInc) {
                squares[yCounter][xCounter] = new Square(x, y, xCounter, yCounter, xInc, yInc, false,squares);
                yCounter++;
            }
            xCounter++;
        }
        life = new Life(squares);
        for (int r = 0; r < squares.length; r++) {
            for (int col = 0; col < squares[0].length; col++) {
                updatePixelsOfSquare(squares[r][col]);
            }
        }
        this.setSize(w, h);
        addListeners(this);
    }

    public ArrayList<Square> getLiveCells() {
        return liveCells;
    }

    public Life getLife() {
        return life;
    }

    public HashMap<Square,Boolean> getChanged() {
        return changed;
    }
    public int getxNum()
    {
        return xNum;
    }
    public int getyNum()
    {
        return yNum;
    }
    public void updatePixelsOfSquare(Square s) {
        //always black in first/last row and column
        boolean status = s.isFilled();
        int firstColumn = s.getX();
        int firstRow = s.getY();
        int lastColumn = firstColumn + s.getW() - 1;
        int lastRow = firstRow + s.getH() - 1;
        for (int y = firstRow; y <= lastRow; y++) {
            for (int x = firstColumn; x <= lastColumn; x++) {
                if (x == firstColumn || y == firstRow)
                    image.setRGB(x, y, 0);
                else if (status)
                    image.setRGB(x, y, 0);
                else
                    image.setRGB(x, y, 0xffffff);
            }
        }
    }

    public void makeFreshGrid() {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Square s = squares[0][0];
        int firstColumn = s.getX();
        int firstRow = s.getY();
        for (int y = firstRow; y < h; y++) {
            for (int x = firstColumn; x < w; x++) {
                if(x%s.getW()==0 || y%s.getH()==0)
                    image.setRGB(x,y,0);
                else
                    image.setRGB(x,y,0xffffff);
            }
        }
    }

    public HashMap<Square,Boolean> calcNewPositions(Life life) {
        changed = life.doCycle(1);
        boolean[][] board = life.getStatArr();
        for (Square s : changed.keySet()) {

            if (changed.get(s))
                s.setFilled(true);
            else
                s.setFilled(false);
            updatePixelsOfSquare(s);
        }

        return changed;
    }

    public void reset() {
        life.reset();
        this.squares = life.getBoard();
        for(int r = 0;r<yNum;r++)
        {
            for(int c =0 ;c<xNum;c++)
            {
                updatePixelsOfSquare(squares[r][c]);
            }
        }
        changed.clear();
    }

    public Square[][] getSquares() {
        return squares;
    }

    public static void main(String[] args) {

    }
    public int getXInc()
    {
        return xInc;
    }
    public int getYInc()
    {
        return yInc;
    }
    public int getW()
    {
        return w;
    }
    public int getH()
    {
        return h;
    }
    public void addListeners(Canvas GUI) {
        GUI.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();
                xInc = w / xNum;
                yInc = h / yNum;
                int xSquare = (int) (x / xInc);
                int ySquare = (int) (y / yInc);
                Square square = squares[ySquare][xSquare];
                if (!square.isClickedOn()) {
                    System.out.println("in here");
                    square.toggleFilled();
                    changed.put(square,square.isFilled());
                    square.setClickedOn(true);
                    life.getStatArr()[ySquare][xSquare] = square.isFilled();
                    updatePixelsOfSquare(square);
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

    public void calcSquareLocations() {
        xInc = w / xNum;
        yInc = h / yNum;
        int x = 0;

        int y = 0;
        int yC = 0;
        while (y < h) {
            int xC = 0;
            while (x < w)
                System.out.println(y / yInc);
            System.out.println(x / xInc);
            squares[yC][xC].setX(x);
            squares[yC][xC].setY(y);
            xC++;
        }
        yC++;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double start = System.currentTimeMillis();
        ArrayList<Square> actual = new ArrayList<>(changed.keySet());

        for (int i = 0; i < changed.size(); i++) {
            Square s = actual.get(i);
            if (changed.get(s)) {
                {
                    life.animateCell(s);
                }
            } else {
                life.killCell(s);
            }
            squares[s.getArrayY()][s.getArrayX()].setClickedOn(false);
        }
        System.out.println("loop time: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        g.drawImage(image, 0, 0, null);

        System.out.println("draw time: " + (System.currentTimeMillis() - start));

    }
}
