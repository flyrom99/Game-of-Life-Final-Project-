import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Scanner;

public class GUI {
    static Timer timer;
    static boolean timerActive = false;
    Canvas canvas;
    Life l;
    int numXSq;
    int numYSq;
    public GUI(int numXSq,int numYSq, int w, int h) {
        this.numXSq = numXSq;
        this.numYSq = numYSq;
        JFrame frame = new JFrame("Game of Life");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(w + 50, h + 50);
        Life life = new Life(numXSq,numYSq);
        Canvas c = new Canvas(w, h, numXSq,numYSq,life);
        canvas = c;
        l = life;
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem reset = new JMenuItem("Reset");
        JMenuItem start = new JMenuItem("Start");
        JMenuItem step = new JMenuItem("Single Step");
        JMenuItem save = new JMenuItem("Save");
        save.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                saveFile("newFile");
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        step.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                c.calcNewPositions(life);
                c.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        reset.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                life.reset();
                c.reset();
                timer.stop();
                c.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        menu.add(reset);
        menu.add(step);
        menu.add(save);
        bar.add(menu);
        c.setPreferredSize(frame.getSize());
        c.setBackground(Color.WHITE);
        c.setVisible(true);
        c.addListeners(c);
        frame.setResizable(false);
        frame.setVisible(true);
        timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 1; i++) {
                    c.calcNewPositions(life);
                }

                c.repaint();
            }
        });
        JButton b = new JButton("Start/Stop");
        b.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (timerActive) {
                    timer.stop();
                    timerActive = false;
                } else {
                    timer.start();
                    timerActive = true;
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        start.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (timerActive) {
                    timer.stop();
                    timerActive = false;
                } else {
                    timer.start();
                    timerActive = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        menu.add(start);
        frame.setBounds(40, 80, w + 1, h + 1);
        frame.add(c, BorderLayout.CENTER);
        frame.add(b, BorderLayout.PAGE_END);
        frame.add(bar, BorderLayout.BEFORE_FIRST_LINE);
    }
    public void parseLife6File(File f) {
        canvas.reset();
        Scanner input = null;
        String currentLine = "";
        try {
            input = new Scanner(f);
            input.nextLine(); // who cares about comments in the file
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(3);
        }
        while (input.hasNextLine())
        {
            String[] split = input.nextLine().split(" ");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            canvas.squares[y][x].setFilled(true);
        }
        canvas.repaint();
    }
    public void parseRLEFile(File f, int offset)
    {
        //fix this shit
        canvas.reset();
        Scanner input = null;
        String currentLine = "";
        try
        {
            input = new Scanner(f);
            currentLine = input.nextLine();
        }
        catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(3);
        }
        while(currentLine.charAt(0)=='#')
        {
            currentLine = input.nextLine();
        }
        String[] splitAroundComma = currentLine.split(",");
        String[][] splitAroundEq = {splitAroundComma[0].split(" = "),splitAroundComma[1].split(" = "),splitAroundComma[2].split(" = ")};
        int x = Integer.parseInt(splitAroundEq[0][1]);
        int y = Integer.parseInt(splitAroundEq[1][1]);
        currentLine = input.nextLine(); //this is where the pattern starts
        while(input.hasNextLine())
            currentLine+=input.nextLine();
        char previousChar = ' ';
        String recentNum = "";
        int currentCol = 0;
        int currentRow = 0;
        System.out.println("current line: " + currentLine);
        for(int i = 0;i<currentLine.length();i++) {
            if (currentLine.charAt(i) == '!')
                return;
            else {
                if (currentLine.charAt(i) == '$') {
                    currentCol = 0;
                    currentRow++;
                }
                else if(Character.isDigit(currentLine.charAt(i)))
                {
                    if(Character.isDigit(previousChar))
                    {
                        recentNum += currentLine.charAt(i);
                    }
                    else
                        recentNum = "" + currentLine.charAt(i);
                }
                else if (Character.isDigit(previousChar) && Character.isAlphabetic(currentLine.charAt(i))) {
                    boolean status = false;
                    if (currentLine.charAt(i) == 'o')
                        status = true;
                    for (int counter = 0; counter < Integer.parseInt(recentNum); counter++) {
                        canvas.getSquares()[currentRow+offset][currentCol+offset].setFilled(status);
                        currentCol++;

                    }
                } else if ((Character.isAlphabetic(previousChar) || previousChar == '$') && Character.isAlphabetic(currentLine.charAt(i))) {
                    if (currentLine.charAt(i) == 'o') {
                        canvas.getSquares()[currentRow+offset][currentCol+offset].setFilled(true);
                    } else {
                        canvas.getSquares()[currentRow+offset][currentCol+offset].setFilled(false);
                    }
                    currentCol++;
                } else {
                    System.out.println("you did something wrong when current char is " + currentLine.charAt(i) + " previousChar: " + previousChar);
                }
            }
            previousChar = currentLine.charAt(i);
        }

    }

    public void saveFile(String name)
    {
        PrintWriter writer = null;
        File file = new File(name + ".rle");
        try{
            writer = new PrintWriter(file);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
        writer.println("x = " + numXSq + ", y = " + numYSq + ", rule = B3/S23");
        Square[][] squares = canvas.getSquares();
        for(int r = 0;r<squares.length;r++)
        {
            int runningCount = 0;
            char runningChar = ' ';
            char currentChar = ' ';
            String appendThis = "";
            for(int c = 0;c<squares[0].length;c++)
            {
                System.out.println("running count: " + runningCount);
                System.out.println("rChar: " + runningChar + " cChar: " + currentChar);
                if(l.getStatArr()[r][c])
                    currentChar = 'o';
                else
                    currentChar = 'b';
                if(runningCount == 0) {
                    runningChar = currentChar;
                }
                if(currentChar == runningChar)
                    runningCount++;
                else
                {
                    System.out.println("else");
                    if(runningCount == 1)
                        writer.print(runningChar);
                    else {
                        System.out.println("gets here");
                        writer.print(runningCount + runningChar);
                    }
                    runningChar = currentChar;
                    runningCount = 1;
                }
            }
            writer.print("$" + "\n");
        }
        writer.print("!");
        writer.close();


    }

    public static void main(String[] args) {
        GUI gui = new GUI(120   ,120,1200,1200);
        gui.parseRLEFile(new File("frothingpuffer.rle"),50);
        //TODO: fix parser and make it so life array is very large and canvas array only shows portion of that array
    }

}
