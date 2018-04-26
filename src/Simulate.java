public class Simulate {
    public Point[][] board;
     Life life = new Life(25);
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
        Simulate sim = new Simulate(10);
        sim.addStartingPoint(0,2);
        sim.addStartingPoint(1,2);
        sim.addStartingPoint(2,2);
        sim.addStartingPoint(3,1);
        sim.addStartingPoint(3,3);
        sim.addStartingPoint(4,2);
        sim.play(5);
    }
}
