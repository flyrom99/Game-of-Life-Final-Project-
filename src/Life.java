import java.util.*;

public class Life {
    TreeMap<Square,Integer> numNeighbors = new TreeMap<>();
    TreeSet<Square> allCells;
    TreeSet<Square> alive = new TreeSet<>();
    TreeSet<Square> extraneousAlive = new TreeSet<>();
    TreeSet<Square> outOfBounds = new TreeSet<>();
    Square[][] board;
    boolean[][] statArr;
    int xTranspose = 0;
    int yTranspose = 0;
    public TreeSet<Square> getExtraneousAlive()
    {
        return extraneousAlive;
    }
    public Life(Square[][] board,TreeSet<Square> allAlive)
    {
        this.allCells = allAlive;
        this.board = board;
        statArr = new boolean[board.length][board[0].length];
    }
    public void setBoard(Square[][] board)
    {
        this.board = board;
    }
    public void printImportantStuff(TreeMap<Square, Boolean> changed)
    {
        printBoard(board);
    }
    public void printImportantStuff()
    {
        printBoard(board);
    }
    public void setOutOfBounds(TreeSet<Square> s)
    {
        this.outOfBounds = s;
    }
    public void reset(int xInc, int yInc)
    {
        statArr = new boolean[board.length][board[0].length];
        board = new Square[board.length][board[0].length];
        alive.clear();
        outOfBounds.clear();
        numNeighbors.clear();
        for(int r = 0;r<board.length;r++)
        {
            for(int col = 0;col<board[0].length;col++)
            {
                board[r][col] = new Square(col*xInc,r*yInc,col,r,xInc,yInc,false,board,outOfBounds);

            }
        }
    }


    public boolean[][] getStatArr()
    {
        return this.statArr;
    }

    public Square getPoint(int x, int y)
    {
        return board[y][x];
    }
    public void animateCell(Square p) {
        //statArr[p.getArrayY()][p.getArrayX()] = true;
        alive.add(p);
        p.setFilled(true);
        for (Square n : p.getNeighbors(board)) {
            if(n==null)
            {
                p.setCalculatedNeighbors(false);
                animateCell(p);
            }
            else {
                if (numNeighbors.keySet().contains(n)) {
                    numNeighbors.remove(n);
                }
                numNeighbors.put(n, n.getNumAliveNeighbors(alive));
            }
        }
        if (numNeighbors.keySet().contains(p))
            numNeighbors.remove(p);
        numNeighbors.put(p, p.getNumAliveNeighbors(alive));
    }

    public TreeSet<Square> getAlive()
    {
        return alive;
    }
    public void killCell(Square p)
    {
        //statArr[p.getArrayY()][p.getArrayX()] = false;
       alive.remove(p);
       p.setFilled(false);
       if(numNeighbors.containsKey(p))
           if(p.getNumAliveNeighbors(alive) == 0 || !p.isFilled())
               numNeighbors.remove(p);
       for(Square n: p.getNeighbors(board))
       {
           numNeighbors.remove(n);
           if(n.getNumAliveNeighbors(alive) != 0 || n.isFilled())
               numNeighbors.put(n,n.getNumAliveNeighbors(alive));
       }

        p.setNumNeighbors(p.getNumAliveNeighbors(alive));
       numNeighbors.put(p,p.getNumAliveNeighbors(alive));
    }
    public void printBoard(Square[][] board)
    {
        String[][]arr = new String[board.length][board[0].length];
        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {
                if(board[r][c].isFilled())
                    arr[r][c] = ("*(" + board[r][c].getArrayX() + ", " + board[r][c].getArrayY() + ")*");
                else
                    arr[r][c] = " (" + board[r][c].getArrayX() + ", " + board[r][c].getArrayY() + ") ";
            }
        }

        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {

                System.out.print(arr[r][c] + calcSpaces(arr[r][c]));
            }
            System.out.println();
        }


    }

    public void printNeighborBoard(Square[][] board)
    {

        String[][]arr = new String[board.length][board[0].length];
        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {
                if(board[r][c].isFilled())
                    arr[r][c] = ("(" + board[r][c].getNumAliveNeighbors(alive) + ")");
                else
                    arr[r][c] = ""+ board[r][c].getNumAliveNeighbors(alive);
            }
        }

        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {

                System.out.print(arr[r][c] + calcSpaces(arr[r][c]));
            }
            System.out.println();
        }
    }
    public String calcSpaces(String s)
    {
        String result = "";
        int n = 8-s.length();
        for(int i = 0;i<n;i++)
        {
            result+=" ";
        }
        return result;
    }
    public void printGraphicsArr(Square[][] board)
    {
        String[][] arr = new String[board.length][board[0].length];
        for(int r = 0;r<board[0].length;r++)
        {
            for(int c = 0;c<board.length;c++)
            {
                int x = board[r][c].getX();
                int y = board[r][c].getY();
                if(board[r][c].isFilled())
                    arr[r][c] = ("*(" + x + ", " + y + ")*");
                else
                arr[r][c] = " (" + x + ", " + y + ") ";
            }
        }

        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {

                System.out.print(arr[r][c] + calcSpaces(arr[r][c]));
            }
            System.out.println();
        }
    }

    public void updateAllNumNeighborValues()
    {
        ArrayList<Square> updateThese = new ArrayList<>(numNeighbors.keySet());
        for(Square s: updateThese)
        {
            numNeighbors.put(s,s.getNumAliveNeighbors());
        }
    }
    public Square[][] getBoard()
    {
        return board;
    }
    public TreeMap<Square,Boolean> doCycle(int i)
    {
        //returns the Squares that changed
        double start = System.currentTimeMillis();
        TreeMap<Square,Integer> results = new TreeMap<>();
        TreeMap<Square,Boolean> changed = new TreeMap<>();
        //-1 == kill  1 == live
        /*
        System.out.println("before");

        printNeighborBoard(board);
        printBoard(board);
        System.out.println("numNeighbors before: " + numNeighbors);
        */
        for(Square p: numNeighbors.keySet()) {
            int neighbors = p.getNumAliveNeighbors();
                if (p.isFilled() && alive.contains(p)) {
                    if (neighbors == 3 || neighbors == 2) {
                            results.put(p, 0); //alive cell stays alive
                    } else {
                        results.put(p, -1);
                        changed.put(p,false);
                    }
                } else //is dead rn
                {
                    if (neighbors == 3) {

                            results.put(p, 1);
                            changed.put(p, true);

                    } else {
                        results.put(p, 0);
                    }
                }
            }

        for(Square p:results.keySet())
        {
            if(results.get(p) == 1)
            {
                animateCell(p);
                p.setFilled(true);
            }
            else if(results.get(p) == -1)
            {
                killCell(p);
                p.setFilled(false);
            }
            else
            {
            }
        }

        return changed;
    }


}
