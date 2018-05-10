import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI {
    static JFrame frame;
    static Timer timer;
    static boolean timerActive = false;
    static Canvas canvas;
    static Life l;
    static int numXSq;
    static int numYSq;
    public GUI(int numXSq,int numYSq, int w, int h, int fps) {
        this.numXSq = numXSq;
        this.numYSq = numYSq;
        JFrame frame = new JFrame("Game of Life");
        this.frame = frame;
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(w + 50, h + 50);
        Canvas c = new Canvas(w, h, numXSq,numYSq);
        canvas = c;
        l =c.getLife();

        c.setFocusable(true);
        c.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP)
                {
                    System.out.println("Ye");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        c.setPreferredSize(frame.getSize());
        c.setBackground(Color.WHITE);
        c.setVisible(true);
        c.addListeners(c);
        frame.setResizable(false);
        frame.setVisible(true);
        timer = new Timer(1000/fps, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.calcNewPositions(c.getLife());
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
                c.setFocusable(true);

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

        JMenuBar bar = buildJMenuBar(timer,timerActive);
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
    public static void parseRLEFile(File f)
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
        int[] offsets = calcOffSet(x,y);
        int xOffSet = offsets[0];
        int yOffSet = offsets[1];
        currentLine = input.nextLine(); //this is where the pattern starts
        while(input.hasNextLine())
            currentLine+=input.nextLine();
        char previousChar = ' ';
        String recentNum = "";
        int currentCol = 0;
        int currentRow = 0;
        for(int i = 0;i<currentLine.length();i++) {
            char currentChar = currentLine.charAt(i);
            if (currentChar == '!')
                return;
            else {
                if (currentChar == '$') {

                    if(Character.isDigit(previousChar))
                        for (int counter = 0; counter < Integer.parseInt(recentNum); counter++) {
                            currentRow++;
                            for (int newCol = 0; newCol < x; newCol++) {
                                canvas.getSquares()[currentRow + yOffSet][newCol + xOffSet].setFilled(false);
                                canvas.getLife().getStatArr()[currentRow + yOffSet][newCol + xOffSet] = false;
                                canvas.getChanged().add(canvas.getSquares()[currentRow + yOffSet][newCol + xOffSet]);
                                canvas.updatePixelsOfSquare(canvas.getSquares()[currentRow + yOffSet][newCol + xOffSet]);
                            }

                        }
                    else
                    {
                        currentRow++;
                    }
                    currentCol = 0;
                }
                if(Character.isDigit(currentChar))
                {
                    if(Character.isDigit(previousChar))
                    {
                        recentNum += currentChar;
                    }
                    else
                        recentNum = "" + currentChar;
                }
                else if (Character.isDigit(previousChar) && Character.isAlphabetic(currentChar)) {
                    boolean status = false;
                    if (currentChar == 'o')
                        status = true;
                    for (int counter = 0; counter < Integer.parseInt(recentNum); counter++) {
                        canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet].setFilled(status);
                        canvas.getLife().getStatArr()[currentRow+yOffSet][currentCol+xOffSet] = status;
                        canvas.getChanged().add(canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet]);
                        canvas.updatePixelsOfSquare(canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet]);
                        currentCol++;
                    }
                } else if (((Character.isAlphabetic(previousChar) || previousChar == '$') && Character.isAlphabetic(currentChar)) || previousChar == ' ') {
                    if (currentChar == 'o') {
                        canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet].setFilled(true);
                        canvas.getLife().getStatArr()[currentRow+yOffSet][currentCol+xOffSet] = true;
                        canvas.getChanged().add(canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet]);
                        canvas.updatePixelsOfSquare(canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet]);

                    } else {
                        canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet].setFilled(false);
                        canvas.getLife().getStatArr()[currentRow+yOffSet][currentCol+xOffSet] = false;
                        canvas.getChanged().add(canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet]);
                        canvas.updatePixelsOfSquare(canvas.getSquares()[currentRow+yOffSet][currentCol+xOffSet]);
                    }

                    currentCol++;
                } else {
                    System.out.println("you did something wrong when current char is " + currentChar + " previousChar: " + previousChar);
                }
            }
            previousChar = currentChar;
        }

    }

    public static void saveFile(String name)
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
            int lineLength = 0;
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
                    if(lineLength>=70)
                        writer.print("\n");
                    else
                    {
                    if(runningCount == 1)
                        writer.print(runningChar);
                    else {
                        writer.print(runningCount + runningChar);

                    }
                    lineLength++;
                    }
                    runningChar = currentChar;
                    runningCount = 1;
                }
            }
            writer.print("$");
        }
        writer.print("!");
        writer.close();


    }
    public static int[] calcOffSet(int x, int y)
    {
        int[] arr = new int[2];
        if(numXSq-x<numXSq*.35 || numYSq-y<numYSq*.35) {
            arr[0] = 0;
            arr[1] = 1;
        }
        else {
            arr[0] = (int) (((numXSq-x) / 4) + ((numXSq-x)* .15));
            arr[1] =  (int) (((numYSq-y) / 4) + ((numYSq-y) * .15));
        }
        return arr;

    }
    public static JMenuBar buildJMenuBar(Timer timer, boolean timerActive)
    {
        final boolean isActive = timerActive;
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem reset = new JMenuItem("Reset");
        JMenuItem start = new JMenuItem("Start");
        JMenuItem stop = new JMenuItem("Stop");
        JMenuItem step = new JMenuItem("Single Step");
        JMenuItem save = new JMenuItem("Save");
       open.addMouseListener(new MouseListener() {
           @Override
           public void mouseClicked(MouseEvent e) {

           }

           @Override
           public void mousePressed(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser(new File("/Users/tmoyer18/Dropbox/Data structures/Game of Life/Patterns"));
                int result = fileChooser.showOpenDialog(frame);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    File selected = fileChooser.getSelectedFile();
                    l.reset();
                    canvas.reset();
                    timer.stop();
                    canvas.repaint();
                    canvas.setFocusable(true);
                    parseRLEFile(selected);
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
        save.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                saveFile("newFile");
                canvas.setFocusable(true);
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
                canvas.calcNewPositions(l);
                canvas.repaint();
                canvas.setFocusable(true);
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
                l.reset();
                canvas.reset();
                timer.stop();
                canvas.repaint();
                canvas.setFocusable(true);
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
        stop.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                timer.stop();
                setTimerActive(false);
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

                    timer.start();
                    setTimerActive(false);

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
        menu.add(open);
        menu.add(reset);
        menu.add(save);
        menu.add(step);
        menu.add(start);
        menu.add(stop);
        bar.add(menu);
        return bar;
    }
    public static void setTimerActive(boolean x)
    {
        timerActive = x;
    }
    public static void main(String[] args) {
        GUI gui = new GUI(10   ,10,1200,1200,40);
        //TODO:implement sparse matrix (start by making numNeighbors (in life class) only have nodes with neighbors)
    }

}
