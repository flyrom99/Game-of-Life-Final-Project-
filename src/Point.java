import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Point {
    private int x;
    private int y;
    private boolean status;
    private int numNeighbors;
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
        status = false;
    }
    public void setNumNeighbors(int i)
    {
        this.numNeighbors = i;
    }
    public int getNumNeighbors()
    {
        return this.numNeighbors;
    }
    public int getX()
    {
        return this.x;
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
    public void setStatus(boolean status) {
        //System.out.println("status of " + this + " is now " + this.status);
        this.status = status;
    }
    public boolean getStatus()
    {
        return this.status;
    }
    public boolean equals(Object other)
    {
        Point o = (Point)other;
        if(o.getX() == this.x)
        {
            return o.getY() == this.y;
        }
        else
            return false;
    }
    public Point[] getNeighbors(Point[][] board)
    {
        //generate new points to use as for keys in hashmap
        ArrayList<Point> points = new ArrayList<>();
        if((y-1>=0 && x-1>=0) && (y-1<board[0].length && x-1<board.length))
            points.add(board[y-1][x-1]);
        if((y-1>=0 && x>=0) && (y-1<board[0].length && x<board.length))
            points.add(board[y-1][x]);
        if((y-1>=0 && x+1>=0)&& (y-1<board[0].length && x+1<board.length))
            points.add(board[y-1][x+1]);
        if((y>=0 && x-1>=0) && (y<board[0].length && x-1<board.length))
            points.add(board[y][x-1]);
        if((y>=0 && x+1>=0)&& (y<board[0].length && x+1<board.length))
            points.add(board[y][x+1]);
        if((y+1>=0 && x-1>=0)&& (y+1<board[0].length && x-1<board.length))
            points.add(board[y+1][x-1]);
        if((y+1>=0 && x>=0) && (y+1<board[0].length && x<board.length))
            points.add(board[y+1][x]);
        if((y+1>=0 && x+1>=0)&& (y+1<board[0].length && x+1<board.length))
            points.add(board[y+1][x+1]);
        Point[] result = new Point[points.size()];
        for(int i = 0;i<points.size();i++)
        {
            result[i] = points.get(i);
        }
        return result;
    }

    public int getNumAliveNeighbors(boolean[][] stat, Point[] neighbors)
    {
        ArrayList<Point> result = new ArrayList<>();
        for(Point p : neighbors)
        {
            if(stat[p.getY()][p.getX()])
                result.add(p);
        }
        return result.size();
    }

    public String toString() {
        String s = "(" + x + ", " + y + ") " + status ;
        return  s;
    }

    public int hashCode()
    {
        return 3*x + 5*y;
    }

    public static void main(String[] args)
    {
        Point p = new Point(3,1);
        System.out.println(p.hashCode());
        Point n = new Point(3,1);
        System.out.println(n.hashCode());
        HashMap<Point, Integer> test = new HashMap<>();
        test.put(p,1);
        System.out.println(test);
        test.put(n,2);
        System.out.println(test);
    }

}
