public class Simulate {
    public Point[][] board;
     Life life = new Life(25,25);
    public Simulate(int size)
    {
        board = new Point[size][size];
    }
    public  void addStartingPoint(int x, int y)
    {
        Point p = new Point(x,y);
        System.out.println("adding " + p);
        life.animateCell(p);
    }
    public  void play(int numCycles)
    {
        for(int i = 0;i<numCycles;i++)
        {
            life.doCycle(i);
        }
    }

    public static void main(String[] args)
    {
        int[][] test = {{1,2,3},{4,5,6},{7,8,9}};
        for(int c = 0;c<test.length;c++)
        {
            for(int r = 0;r<test[0].length;r++)
            {
                System.out.print(test[c][r] + " ");
            }
            System.out.println();
        }
        System.out.println(test[1][2]);
    }
}
