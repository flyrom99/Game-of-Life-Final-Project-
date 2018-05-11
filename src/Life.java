import java.util.*;

public class Life {
    HashMap<Square,Integer> numNeighbors = new HashMap<>();
    HashSet<Square> alive = new HashSet<>();
    HashSet<Square> extraneousAlive = new HashSet<>();
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
    public HashSet<Square> getExtraneousAlive()
    {
        return extraneousAlive;
    }
    public Life(Square[][] board)
    {
        this.board = board;
        statArr = new boolean[board.length][board[0].length];
        //System.out.println("bW: " + board[0].length + " bH " + board.length + "  sW: " + statArr[0].length + " sH: " + statArr.length);
    }

    public void printImportantStuff(HashMap<Square, Boolean> changed)
    {
        System.out.println("alive: " + alive);
        System.out.println("numNeighbors: " + numNeighbors);
        System.out.println("changed: " + changed);
        printBoard(board);
        System.out.println(getPoint(8,4).getNumAliveNeighbors(alive));
    }
    public void printImportantStuff()
    {
        System.out.println("alive: " + alive);
        System.out.println("numNeighbors: " + numNeighbors);
        printBoard(board);
        System.out.println(getPoint(8,4).getNumAliveNeighbors(alive));
    }
    public void reset()
    {
        statArr = new boolean[board.length][board[0].length];
        alive.clear();
        numNeighbors.clear();
        for(int r = 0;r<board.length;r++)
        {
            for(int col = 0;col<board[0].length;col++)
            {
                Square s = board[r][col];
                board[r][col] = new Square(s.getX(),s.getY(),s.getArrayX(),s.getArrayY(),s.getW(),s.getH(),false,board);
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
        //System.out.println("neighbors of " + p + " are " + p.getNeighbors(board));
        for (Square n : p.getNeighbors(board)) {

                if (numNeighbors.keySet().contains(n)) {
                    numNeighbors.remove(n);
                }
                numNeighbors.put(n, n.getNumAliveNeighbors(alive));
        }
        if (numNeighbors.keySet().contains(p))
            numNeighbors.remove(p);
        numNeighbors.put(p, p.getNumAliveNeighbors(alive));
    }

    public HashSet<Square> getAlive()
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
                    arr[r][c] = board[r][c].getNumAliveNeighbors(alive) + ")";
                else
                    arr[r][c] = "" + board[r][c].getNumAliveNeighbors(alive);
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
        int n = 4-s.length();
        for(int i = 0;i<n;i++)
        {
            result+=" ";
        }
        return result;
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
    public Square[][] translateBoard(int xShift, int yShift)
    {
        Square[][] newBoard = new Square[board.length][board[0].length];
        //positive xShift means go right, negative xShift goes left
        // positive yShift means go up, negative yShift means go down

    }
    public Square[][] getBoard()
    {
        return board;
    }
    public HashMap<Square,Boolean> doCycle(int i)
    {
        //returns the Squares that changed
        double start = System.currentTimeMillis();
        HashMap<Square,Integer> results = new HashMap<>();
        HashMap<Square,Boolean> changed = new HashMap<>();
        //-1 == kill  1 == live
        //System.out.println("numNeighbors before cycle: " + numNeighbors);
        for(Square p: numNeighbors.keySet()) {
            int neighbors = p.getNumAliveNeighbors(alive);
            if (p.getArrayX() >= 0 && p.getArrayY() >= 0 && p.getArrayX() < board[0].length && p.getArrayY() < board.length) {
                if (p.isFilled() && alive.contains(p)) {
                    if (neighbors == 3 || neighbors == 2) {
                        results.put(p, 0); //alive cell stays alive
                       // System.out.println("keeping " + p + " alive");
                    } else {
                        results.put(p, -1);
                        //System.out.println("killing " + p);
                        changed.put(p,false);
                    }
                } else //is dead rn
                {
                    if (neighbors == 3) {
                        results.put(p, 1);
                        //System.out.println("animating " + p);
                        changed.put(p,true);
                    } else {
                        results.put(p, 0);
                        //System.out.println("keeping " + p + " dead");
                    }
                }
            }

        }
        for(Square p:results.keySet())
        {
            if(p.getNumAliveNeighbors(alive) == 0)
            {

            }
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
        System.out.println("cycle time: " + (System.currentTimeMillis() - start));
        //System.out.println("after");
        //printImportantStuff(changed);
        return changed;
    }


}
