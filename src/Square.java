import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;

public class Square implements Comparable {
    int x;
    int y;
    int arrayX;
    int arrayY;
    int w;
    int h;
    boolean filled;
    boolean clickedOn;
    int numNeighbors;
    boolean calculatedNeighbors;
    Square[][] board;
    Square[] neighbors = new Square[8];
    TreeSet<Square> outOfBounds;
    Square N;
    Square NW;
    Square NE;
    Square E;
    Square W;
    Square SW;
    Square S;
    Square SE;
    public Square(int x, int y,int aX, int aY, int w, int h, boolean filled,Square[][] board,TreeSet<Square> outOfBounds)
    {
        this.outOfBounds = outOfBounds;
        this.x = x;
        this.arrayX = aX;
        this.arrayY = aY;
        this.y = y;
        this.w = w;
        this.h = h;
        this.filled = filled;
        this.clickedOn = false;
        this.calculatedNeighbors = false;
        this.board = board;
    }
    public int getArrayX() {
        return arrayX;
    }
    public void setCalculatedNeighbors(boolean s)
    {
        calculatedNeighbors = s;
    }
    public int slowerGetNumAliveNeighbors()
    {
        Square[] n =getNeighbors(board);
        int count = 0;
        for(Square s: n)
        {
            if(s.isFilled())
                count++;
        }
        return count;
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
    public Square[] getNeighborsNoCalc()
    {
        return neighbors;
    }
    public void toggleFilled()
    {
        if(filled)
            this.filled = false;
        else
            this.filled = true;
    }
    public boolean isInBounds(Square[][] board)
    {
        return (arrayX>=board[0][0].getArrayX() && arrayX<(board[0][0].getArrayX()+board[0].length) && arrayY>=board[0][0].getArrayY() && arrayY<(board.length+board[0][0].getArrayY()));
    }
    public void setNeighbors(Square[] n)
    {
        this.neighbors = n;
    }

    public Square[] getNeighbors(Square[][] board) {

        // 0 = NW, 1 = N, 2 = NE, 3 = W, 4 = E, 5 = SW, 6 = S, 7 = SE
        if (!calculatedNeighbors) {
            neighbors = new Square[8];
            int count = 0;
            // NW
            if ((arrayY - 1 >= 0 && arrayX - 1 >= 0) && (arrayY - 1 < board.length && arrayX - 1 < board[0].length))
                neighbors[0] = board[arrayY - 1][arrayX - 1];
            else {
                count++;
                Square s = new Square(x + w, y - h, arrayX - 1, arrayY - 1, w, h, false, board, outOfBounds);
                if (!outOfBounds.contains(s)) {
                    neighbors[0] = s;
                    outOfBounds.add(s);
                } else {
                    Square p = outOfBounds.subSet(s,true,s,true).floor(s);
                    if(p == null)
                    {
                        neighbors[0] = s;
                        outOfBounds.add(s);
                    }
                    else
                    {
                        neighbors[0] = p;
                        outOfBounds.add(p);
                    }
                }
            }
            //N
            if ((arrayY - 1 >= 0 && arrayX >= 0) && (arrayY - 1 < board.length && arrayX < board[0].length))
                neighbors[1] = board[arrayY - 1][arrayX];
            else {
                count++;
                Square s = new Square(x + w, y - h, arrayX, arrayY - 1, w, h, false, board, outOfBounds);
                if (!outOfBounds.contains(s)) {
                    neighbors[1] = s;
                    outOfBounds.add(s);
                } else {
                    Square p = outOfBounds.subSet(s,true,s,true).floor(s);
                    if(p == null)
                    {
                        neighbors[1] = s;
                        outOfBounds.add(s);
                    }
                    else
                    {
                        neighbors[1] = p;
                        outOfBounds.add(p);
                    }
                }
            }
            //NE
            if ((arrayY - 1 >= 0 && arrayX + 1 >= 0) && (arrayY - 1 < board.length && arrayX + 1 < board[0].length))
                neighbors[2] = board[arrayY - 1][arrayX + 1];
            else {
                count++;
                Square s = new Square(x + w, y - h, arrayX + 1, arrayY - 1, w, h, false, board, outOfBounds);
                if(!outOfBounds.contains(s)) {
                    neighbors[2] = s;
                    outOfBounds.add(s);
                }
                else
                {
                    Square p = outOfBounds.subSet(s,true,s,true).floor(s);
                    if(p == null)
                    {
                        neighbors[2] = s;
                        outOfBounds.add(s);
                    }
                    else
                    {
                        neighbors[2] = p;
                        outOfBounds.add(p);
                    }
                    }
                }

            //W
            if ((arrayY >= 0 && arrayX - 1 >= 0) && (arrayY < board.length && arrayX - 1 < board[0].length))

                neighbors[3] = board[arrayY][arrayX - 1];
            else {
                count++;
                Square s = new Square(x + w, y - h, arrayX - 1, arrayY, w, h, false, board, outOfBounds);
                Square p = outOfBounds.subSet(s,true,s,true).floor(s);
                if(p == null)
                {
                    neighbors[3] = s;
                    outOfBounds.add(s);
                }
                else
                {
                    neighbors[3] = p;
                    outOfBounds.add(p);
                }
            }
            //E
            if ((arrayY >= 0 && arrayX + 1 >= 0) && (arrayY < board.length && arrayX + 1 < board[0].length))
                neighbors[4] = board[arrayY][arrayX + 1];
            else {
                count++;
                Square s = new Square(x + w, y - h, arrayX + 1, arrayY, w, h, false, board, outOfBounds);
                if(!outOfBounds.contains(s)) {
                    neighbors[4] = s;
                    outOfBounds.add(s);
                }
                else {
                    Square p = outOfBounds.subSet(s,true,s,true).floor(s);
                    if(p == null)
                    {
                        neighbors[4] = s;
                        outOfBounds.add(s);
                    }
                    else
                    {
                        neighbors[4] = p;
                        outOfBounds.add(p);
                    }
                }
            }
            //SW
            if ((arrayY + 1 >= 0 && arrayX - 1 >= 0) && (arrayY + 1 < board.length && arrayX - 1 < board[0].length))
                neighbors[5] = board[arrayY + 1][arrayX - 1];
            else {
                count++;
                Square s = new Square(x + w, y - h, arrayX - 1, arrayY + 1, w, h, false, board, outOfBounds);
                if(!outOfBounds.contains(s)) {
                    neighbors[5] = s;
                    outOfBounds.add(s);
                }
                else {
                    Square p = outOfBounds.subSet(s,true,s,true).floor(s);
                    if(p == null)
                    {
                        neighbors[5] = s;
                        outOfBounds.add(s);
                    }
                    else
                    {
                        neighbors[5] = p;
                        outOfBounds.add(p);
                    }
                }
            }
            //S
            if ((arrayY + 1 >= 0 && arrayX >= 0) && (arrayY + 1 < board.length && arrayX < board[0].length))
                neighbors[6] = board[arrayY + 1][arrayX];
            else {
                count++;
                Square s = new Square(x + w, y - h, arrayX, arrayY + 1, w, h, false, board, outOfBounds);
                if(!outOfBounds.contains(s)) {
                    neighbors[6] = s;
                    outOfBounds.add(s);
                }
                else {
                    Square p = outOfBounds.subSet(s,true,s,true).floor(s);
                    if(p == null)
                    {
                        neighbors[6] = s;
                        outOfBounds.add(s);
                    }
                    else
                    {
                        neighbors[6] = p;
                        outOfBounds.add(p);
                    }
                }
            }
            //SE
            if ((arrayY + 1 >= 0 && arrayX + 1 >= 0) && (arrayY + 1 < board.length && arrayX + 1 < board[0].length))
                neighbors[7] = board[arrayY + 1][arrayX + 1];
            else {
                count++;
                Square s = new Square(x + w, y - h, arrayX + 1, arrayY + 1, w, h, false, board, outOfBounds);
                if(!outOfBounds.contains(s)) {
                    neighbors[7] = s;
                    outOfBounds.add(s);
                }
                else {
                    Square p = outOfBounds.subSet(s,true,s,true).floor(s);
                    if(p == null)
                    {
                        neighbors[7] = s;
                        outOfBounds.add(s);
                    }
                    else
                    {
                        neighbors[7] = p;
                        outOfBounds.add(p);
                    }
                }
            }
            calculatedNeighbors = true;
            NW = neighbors[0];
            N = neighbors[1];
            NE = neighbors[2];
            W = neighbors[3];
            E = neighbors[4];
            SW = neighbors[5];
            S = neighbors[6];
            SE = neighbors[7];
        }
            return neighbors;
    }


    public Square getShiftedSquare(int xShift, int yShift, Square[][] board)
    {
        // positive xShift means go right, negative xShift goes left
        // positive yShift means go up, negative yShift means go down


        Square current = this;
        Square orig = current;
        if(xShift < 0) {
            for (int i = 0;i<Math.abs(xShift);i++) {
                current = current.getNeighbors(board)[3]; //go west
            }
        }
        else if(xShift > 0)
        {
            for(int i = 0;i<Math.abs(xShift);i++)
            {
                current = current.getNeighbors(board)[4]; //go east
            }
        }
        if(yShift < 0)
        {
            for(int i = 0;i<Math.abs(yShift);i++)
            {
                current = current.getNeighbors(board)[6]; //go south
            }
        }
        else if(yShift > 0)
        {
            for(int i = 0;i<Math.abs(yShift);i++)
            {
                current = current.getNeighbors(board)[1]; //go north
            }
        }
        else
        {
        }
        return current;
    }
    public ArrayList<Square> getArrayListNeighbors()
    {
        ArrayList<Square> list = new ArrayList<>();
        list.add(NW);
        list.add(N);
        list.add(NE);
        list.add(W);
        list.add(E);
        list.add(SW);
        list.add(S);
        list.add(SE);
        return list;
    }
    public void reCalcNeighbors(Square[][] board, TreeSet<Square> alive)
    {
        calculatedNeighbors = false;
        neighbors = getNeighbors(board);
        for(Square s:neighbors)
            if(s.isFilled())
                alive.add(s);

    }
    public int getNumAliveNeighbors(TreeSet<Square> alive)
    {
        int sum = 0;
        if(!calculatedNeighbors)
            getNeighbors(board);
        for(Square p : neighbors)
        {
           if(p!=null) {
               if (alive.contains(p))
                   sum++;
           }
        }
        return sum;
    }
    public int getNumAliveNeighbors()
    {
        int sum = 0;
        Square[] n = getNeighbors(board);
        for(Square p : n)
        {
            if(p!=null) {
                if (p.isFilled())
                    sum++;
            }
        }
        return sum;
    }
    public void setNumNeighbors(int i)
    {
        this.numNeighbors = i;
    }
    public int getNumNeighbors()
    {
        if(!calculatedNeighbors)
            neighbors = getNeighbors(board);
        return this.numNeighbors;
    }

    public int HashCode()
    {
        return (int)(Math.pow(3*arrayX,arrayY) + Math.pow(5*arrayY,arrayY));
    }


    public static void printArr(Square[][] arr)
    {
        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {
                System.out.print(arr[r][c].isFilled() + " ");
            }
        }
    }

    public String toString()
    {
        return "(" + arrayX + ", " + arrayY + ") s: " + filled + " " + getNumAliveNeighbors();
    }


    @Override
    public int compareTo(Object o) {
        Square other = (Square)o;
        Integer oX = other.getArrayX();
        Integer oY = other.getArrayY();
        Integer tX = arrayX;
        Integer tY = arrayY;
        if(oX.equals(tX))
            return tY.compareTo(oY);
        else
            return tX.compareTo(oX);
    }
}
