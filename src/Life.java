import java.util.*;

public class Life {
    HashMap<Square,Integer> numNeighbors = new HashMap<>();
    HashSet<Square> alive = new HashSet<>();
    Square[][] board;
    boolean[][] statArr;

    public Life (int x, int y)
    {
        board = new Square[y][x];
        statArr = new boolean[y][x];
        for(int r = 0;r<board.length;r++)
        {
            for(int c = 0;c<board[0].length;c++)
            {
                board[r][c] = new Square(c,r,0,0,50,50,false);
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
            n.setNumNeighbors(n.getNumNeighbors()+1);
            numNeighbors.put(n,n.getNumNeighbors());
        }
        p.setNumNeighbors(p.getNumAliveNeighbors(statArr,p.getNeighbors(board)));
        numNeighbors.put(p,p.getNumNeighbors());
    }


    public void killCell(Square p)
    {
        statArr[p.getArrayY()][p.getArrayX()] = false;
       alive.remove(p);
       for(Square n: p.getNeighbors(board))
       {
           if(n.getNumNeighbors()-1>=0)
           {
               n.setNumNeighbors(n.getNumNeighbors()-1);
           }
       }
        p.setNumNeighbors(p.getNumAliveNeighbors(statArr,p.getNeighbors(board)));
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
        for(Square p: numNeighbors.keySet())
        {
            int neighbors = p.getNumAliveNeighbors(statArr,p.getNeighbors(board));
            if(statArr[p.getArrayY()][p.getArrayX()] && alive.contains(p)) {
                if (neighbors == 3 || neighbors ==2) {
                    results.put(p,0); //alive cell stays alive
                }
                else
                {
                    results.put(p,-1);
                    changed.add(p);
                }
            }
            else //is dead rn
            {
                if(neighbors == 3)
                {
                    results.put(p,1);
                    changed.add(p);
                }
                else
                {
                    results.put(p,0);
                }
            }
        }
        for(Square p:results.keySet())
        {
            if(results.get(p) == 1)
            {
                animateCell(p);
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
        return changed;
    }


}
