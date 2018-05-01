import java.util.Arrays;

/**
 * Created by tmoyer18 on 4/26/18.
 */
public class Test {

    public static void test(Point[] f)
    {
        for(int i = 0;i<f.length;i++)
        {
            f[i].setStatus(true);
        }
    }
    public static void main(String[] args)
    {
        Point[] f = {new Point(3,4), new Point(4,5), new Point(5,6)};
        System.out.println(Arrays.toString(f));
        test(f);
        System.out.println(Arrays.toString(f));
    }

}
