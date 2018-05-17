import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Created by tmoyer18 on 4/26/18.
 */
public class Test {
    public static void main(String[] args)
    {
        TreeSet<Square> test = new TreeSet<>();
        Square[][] random = new Square[50][50];
        test.add(new Square(0,0,5,7,50,50,true,random,test));
        Square s = new Square(0,0,5,7,50,50,true,random,test);
        Square o = new Square(0,0,7,9,50,50,true,random,test);
        System.out.println(test.subSet(o,true,o,true).floor(o));
    }
}
