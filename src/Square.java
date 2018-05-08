import java.util.ArrayList;

public class Square {
    int x;
    int y;
    int arrayX;
    int arrayY;
    int w;
    int h;
    boolean filled;
    boolean clickedOn;
    int numNeighbors;

    public Square(int x, int y,int aX, int aY, int w, int h, boolean filled)
    {
        this.x = x;
        this.arrayX = aX;
        this.arrayY = aY;
        this.y = y;
        this.w = w;
        this.h = h;
        this.filled = filled;
        this.clickedOn = false;
    }
    public int getArrayX() {
        return arrayX;
    }

    public void setArrayX(int arrayX) {
        this.arrayX = arrayX;
    }

    public int getArrayY() {
        return arrayY;
    }

    public void setArrayY(int arrayY) {
        this.arrayY = arrayY;
    }
    public boolean isClickedOn() {
        return clickedOn;
    }

    public void setClickedOn(boolean repainted) {
        this.clickedOn = repainted;
    }
    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void toggleFilled()
    {
        if(filled)
            this.filled = false;
        else
            this.filled = true;
    }
    public Square[] getNeighbors(Square[][] board)
    {
        ArrayList<Square> points = new ArrayList<>();
        if((arrayY-1>=0 && arrayX-1>=0) && (arrayY-1<board.length && arrayX-1<board[0].length))
            points.add(board[arrayY-1][arrayX-1]);
        if((arrayY-1>=0 && arrayX>=0) && (arrayY-1<board.length && arrayX<board[0].length))
            points.add(board[arrayY-1][arrayX]);
        if((arrayY-1>=0 && arrayX+1>=0)&& (arrayY-1<board.length && arrayX+1<board[0].length))
            points.add(board[arrayY-1][arrayX+1]);
        if((arrayY>=0 && arrayX-1>=0) && (arrayY<board.length && arrayX-1<board[0].length))
            points.add(board[arrayY][arrayX-1]);
        if((arrayY>=0 && arrayX+1>=0)&& (arrayY<board.length && arrayX+1<board[0].length))
            points.add(board[arrayY][arrayX+1]);
        if((arrayY+1>=0 && arrayX-1>=0)&& (arrayY+1<board.length && arrayX-1<board[0].length))
            points.add(board[arrayY+1][arrayX-1]);
        if((arrayY+1>=0 && arrayX>=0) && (arrayY+1<board.length && arrayX<board[0].length))
            points.add(board[arrayY+1][arrayX]);
        if((arrayY+1>=0 && arrayX+1>=0)&& (arrayY+1<board.length && arrayX+1<board[0].length))
            points.add(board[arrayY+1][arrayX+1]);
        Square[] result = new Square[points.size()];
        for(int i = 0;i<points.size();i++)
        {
            result[i] = points.get(i);
        }
        return result;
    }

    public int getNumAliveNeighbors(boolean[][] stat, Square[] neighbors)
    {
        ArrayList<Square> result = new ArrayList<>();
        for(Square p : neighbors)
        {
            //System.out.println("pX: " + p.getArrayX() + " pY: " + p.getArrayY() + " gX: " + p.getX() + " gY: " + p.getY());
            if(stat[p.getArrayY()][p.getArrayX()])
                result.add(p);
        }
        return result.size();
    }
    public void setNumNeighbors(int i)
    {
        this.numNeighbors = i;
    }
    public int getNumNeighbors()
    {
        return this.numNeighbors;
    }

    public int hashCode()
    {
        return 3*x + 5*y;
    }

    public static void printArr(Square[][] arr)
    {
        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {
                System.out.print(arr[r][c].isFilled() + " ");
            }
            System.out.println();
        }
    }

    public String toString()
    {
        return "(" + arrayX + ", " + arrayY + ") s: " + filled;
    }


}
