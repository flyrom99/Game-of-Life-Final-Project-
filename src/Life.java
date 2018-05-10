import java.util.*;

public class Life {
    HashMap<Square,Integer> numNeighbors = new HashMap<>();
    HashSet<Square> alive = new HashSet<>();
    Square[][] board;
    boolean[][] statArr;
    int xTranspose = 0;
    int yTranspose = 0;
    public Life (int x, int y)
    {
        board = new Square[y][x];
        statArr = new boolean[y][x];
        for(int r = 0;r<board.length;r++)
        {
            for(int c = 0;c<board[0].length;c++)
            {
                board[r][c] = new Square(c,r,0,0,50,50,false,board);
                statArr[r][c] = false;
            }
        }
    }

    public Life(Square[][] board)
    {
        this.board = board;
        statArr = new boolean[board.length][board[0].length];
        //System.out.println("bW: " + board[0].length + " bH " + board.length + "  sW: " + statArr[0].length + " sH: " + statArr.length);
    }

    public void repositionArrays()
    {

    }

    public void reset()
    {
        for(int r = 0;r<board[0].length;r++)
        {
            for(int c = 0;c<board.length;c++)
            {
                statArr[r][c] = false;
                board[r][c].setFilled(false);
            }
        }
        numNeighbors.clear();
        alive.clear();
    }
    public boolean[][] getStatArr()
    {
        return this.statArr;
    }

    public Square getPoint(int x, int y)
    {
        return board[y][x];
    }
    public void animateCell(Square p)
    {

        statArr[p.getArrayY()][p.getArrayX()] = true;
        alive.add(p);
        for(Square n: p.getNeighbors(board))
        {
            if(numNeighbors.keySet().contains(n))
                numNeighbors.remove(n);
            numNeighbors.put(n,n.getNumAliveNeighbors(alive));
        }
        if(numNeighbors.keySet().contains(p))
            numNeighbors.remove(p);
        numNeighbors.put(p,p.getNumAliveNeighbors(alive));
    }

    public HashSet<Square> getAlive()
    {
        return alive;
    }
    public void killCell(Square p)
    {
        statArr[p.getArrayY()][p.getArrayX()] = false;
       alive.remove(p);
       if(numNeighbors.containsKey(p))
           if(p.getNumAliveNeighbors(alive) ==0)
               numNeighbors.remove(p);
       for(Square n: p.getNeighbors(board))
       {
           numNeighbors.remove(n);
           if(n.getNumAliveNeighbors(alive) != 0)
               numNeighbors.put(n,n.getNumAliveNeighbors(alive));
       }

        p.setNumNeighbors(p.getNumAliveNeighbors(alive));
    }
    public void printBoard(int size,Object[][] board)
    {
        char[][]arr = new char[size][size];
        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {
                arr[r][c] = '0';
            }
        }
        Iterator<Square> iter = alive.iterator();
        while(iter.hasNext())
        {
            Square current = iter.next();
            arr[current.getArrayY()][current.getArrayX()] = '1';
        }

        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {
                System.out.print(arr[r][c] + " ");
            }
            System.out.println();
        }


    }
    public void printStatArr(boolean[][] arr)
    {
        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {
                boolean s = arr[r][c];
                if(s == true)
                    System.out.print( "T ");
                else
                    System.out.print("f ");
            }

            System.out.println();
        }
    }
    public Square[][] getBoard()
    {
        return board;
    }
    public Set<Square> doCycle(int i)
    {
        //returns the Squares that changed
        double start = System.currentTimeMillis();
        HashMap<Square,Integer> results = new HashMap<>();
        Set<Square> changed = new HashSet<>();
        //-1 == kill  1 == live
        for(Square p: numNeighbors.keySet()) {
            int neighbors = p.getNumAliveNeighbors(alive);
            if (p.getArrayX() >= 0 && p.getArrayY() >= 0 && p.getArrayX() < board[0].length && p.getArrayY() < board.length) {
                if (statArr[p.getArrayY()][p.getArrayX()] && alive.contains(p)) {
                    if (neighbors == 3 || neighbors == 2) {
                        results.put(p, 0); //alive cell stays alive
                        System.out.println("keeping " + p + " alive");
                    } else {
                        results.put(p, -1);
                        changed.add(p);
                    }
                } else //is dead rn
                {
                    if (neighbors == 3) {
                        results.put(p, 1);
                        changed.add(p);
                    } else {
                        results.put(p, 0);
                    }
                }
            }
        }
        for(Square p:results.keySet())
        {
            if(p.getNumAliveNeighbors(alive) == 0)
            {
                numNeighbors.remove(p);
            }
            if(results.get(p) == 1)
            {
                animateCell(p);
                System.out.println("animating " + p);
            }
            else if(results.get(p) == -1)
            {
                killCell(p);
            }
            else
            {
            }
        }
        System.out.println("cycle time: " + (System.currentTimeMillis() - start));
        System.out.println(numNeighbors);
        System.out.println("alive: " + alive);
        System.out.println(board[2][2].getNumAliveNeighbors(alive));
        return changed;
    }


}
