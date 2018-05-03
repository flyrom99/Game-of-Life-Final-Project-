import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

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
    public static void main(String[] args) {
        Scanner in = null;
        try
        {
            in = new Scanner(new File("queenbeeshuttle.rle"));
        }
        catch (FileNotFoundException ex)
        {

        }
        while(in.hasNextLine())
            System.out.println(in.nextLine());
    }

}
