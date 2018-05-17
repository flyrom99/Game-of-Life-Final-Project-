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
    TreeSet<Square> allCells = new TreeSet<>();
    Life previousLife;
    ArrayList<Square> justChanged = new ArrayList<>();
    TreeMap<Square, Boolean> changed;
    BufferedImage image;

    public Canvas(int w, int h, int numXSq, int numYSq) {
        this.w = w;
        this.h = h;
        changed = new TreeMap<>();
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
                squares[yCounter][xCounter] = new Square(x, y, xCounter, yCounter, xInc, yInc, false, squares, allCells);
                allCells.add(squares[yCounter][xCounter]);
                yCounter++;
            }
            xCounter++;
        }
        life = new Life(squares, allCells);
        for (int r = 0; r < squares.length; r++) {
            for (int col = 0; col < squares[0].length; col++) {
                updatePixelsOfSquare(squares[r][col]);
            }
        }
        this.setSize(w, h);
        addListeners(this);
    }

    public TreeSet<Square> getAllCells() {
        return allCells;
    }

    public Life getLife() {
        return life;
    }

    public TreeMap<Square, Boolean> getChanged() {
        return changed;
    }

    public int getxNum() {
        return xNum;
    }

    public int getyNum() {
        return yNum;
    }

    public void updatePixelsOfSquare(Square s) {
        //always black in first/last row and column
        if(!allCells.contains(s))
            allCells.add(s);
        if(s.getArrayX()>=squares[0][0].getArrayX() && s.getArrayX()<(squares[0].length+squares[0][0].getArrayX()) && s.getArrayY()>= squares[0][0].getArrayY() && s.getArrayY()<(squares.length+squares[0][0].getArrayY())) {
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
            //System.out.println("updated " + s);
            s.setClickedOn(false);
        }
    }
    public void updateAllPixels(Square[][] board)
    {
        for(int r = 0;r<board.length;r++)
        {
            for(int c = 0;c<board[0].length;c++)
            {
                updatePixelsOfSquare(board[r][c]);
            }
        }
    }
    public void newTranslate(int xShift, int yShift, Square[][] board)
    {
        double time = System.currentTimeMillis();
        Square startOfRow = board[0][0].getShiftedSquare(xShift,yShift,board);
        int startingX = startOfRow.getArrayX();
        int startingY = startOfRow.getArrayY();
        Square[][] newBoard = new Square[board.length][board[0].length];
        TreeMap<Square, Boolean> oldValues = new TreeMap<Square, Boolean>();
        if(xShift == 0 & yShift == 0)
            return;
        for(int r = 0;r<board.length;r++)
        {
            Square current = startOfRow;
            double rs = System.currentTimeMillis();
            for(int c = 0;c<board[0].length;c++)
            {
                Square[] neighbors = current.getNeighbors(board);
                int numN = current.getNumAliveNeighbors();
                newBoard[r][c] = current;
                life.outOfBounds.remove(current);
                if(numN>0)
                    life.numNeighbors.put(current,numN);
                current.setX((c)*xInc);
                current.setY((r)*yInc);
                double start = System.currentTimeMillis();
                current = neighbors[4]; //go east
            }
            startOfRow = startOfRow.getNeighbors(board)[6]; //getSouth (aka go to nextRow)
        }
        Square[][] oldBoard = board;
        for(int r = 0;r<board.length;r++)
        {
            for(int c =0;c<board[0].length;c++)
            {
                Square current = oldBoard[r][c];
                if(!oldBoard[r][c].isInBounds(newBoard))
                {
                    current.setX(current.getX()*-1);
                    oldBoard[r][c].setY(current.getY()*-1);
                    life.outOfBounds.add(current);
                }
            }
        }

        TreeSet<Square> newOut = new TreeSet<>();
        for(int r = 0;r<newBoard.length;r++)
        {
            for(int c = 0;c<newBoard[0].length;c++)
            {
                Square[] neighbors = newBoard[r][c].getNeighbors(newBoard);
                updatePixelsOfSquare(newBoard[r][c]);
                if(newBoard[r][c].isFilled() || newBoard[r][c].getNumAliveNeighbors()>0)
                    life.numNeighbors.put(newBoard[r][c],newBoard[r][c].getNumAliveNeighbors());
                for(Square s: neighbors)
                {
                    if(!s.isInBounds(newBoard))
                        life.outOfBounds.add(s);
                }
            }
        }
        for(Square s: life.outOfBounds)
        {
            if(!s.isInBounds(newBoard))
                newOut.add(s);
        }
        life.setBoard(newBoard);
        setSquares(newBoard);
        updateAllPixels(board);
        life.setOutOfBounds(newOut);
        newTranslate(0,0,board);
        repaint();
        System.out.println("translating: " + (System.currentTimeMillis()-time));
    }

    public void reCalcAllNeighbors() {
        for (int r = 0; r < squares.length; r++) {
            for (int c = 0; c < squares[0].length; c++) {
                squares[r][c].reCalcNeighbors(squares, life.getAlive());
            }
        }
    }

    public void setSquares(Square[][] board) {
        this.squares = board;
    }

    public void makeFreshGrid() {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Square s = squares[0][0];
        int firstColumn = s.getX();
        int firstRow = s.getY();
        for (int y = firstRow; y < h; y++) {
            for (int x = firstColumn; x < w; x++) {
                if (x % s.getW() == 0 || y % s.getH() == 0)
                    image.setRGB(x, y, 0);
                else
                    image.setRGB(x, y, 0xffffff);
            }
        }
    }

    public TreeMap<Square, Boolean> calcNewPositions(Life life) {
        changed = life.doCycle(1);
        for (Square s : changed.keySet()) {

            if (changed.get(s))
                s.setFilled(true);
            else
                s.setFilled(false);
            if (s.getArrayX() >= 0 && s.getArrayX() < squares[0].length && s.getArrayY() >= 0 && s.getArrayY() < squares.length)
                updatePixelsOfSquare(s);
            s.setClickedOn(false);
        }
        setFocusable(true);
        requestFocusInWindow();
        return changed;
    }

    public void reset() {
        life.reset(xInc,yInc);
        this.squares = life.getBoard();
        for (int r = 0; r < yNum; r++) {
            for (int c = 0; c < xNum; c++) {
                updatePixelsOfSquare(squares[r][c]);
            }
        }
        changed.clear();
        setFocusable(true);
        requestFocusInWindow();
    }

    public Square[][] getSquares() {
        return squares;
    }

    public static void main(String[] args) {

    }

    public int getXInc() {
        return xInc;
    }

    public int getYInc() {
        return yInc;
    }

    public void setXInc(int x) {
        xInc = x;
    }

    public void setYInc(int y) {
        yInc = y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void addListeners(Canvas GUI) {

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

    public void toggleClickedOnForAll() {
        for (int r = 0; r < squares.length; r++) {
            for (int c = 0; c < squares[0].length; c++) {
                squares[r][c].setClickedOn(false);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double start = System.currentTimeMillis();
        //System.out.println("loop time: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        g.drawImage(image, 0, 0, null);
        //System.out.println("draw time: " + (System.currentTimeMillis() - start));
        setFocusable(true);
        requestFocusInWindow();

    }
}
