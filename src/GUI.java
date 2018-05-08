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
    public static void parseRLEFile(File f, int offset)
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
            char currentChar = currentLine.charAt(i);
            System.out.println("currentChar: " + currentChar + " previousChar: " + previousChar);
            if (currentChar == '!')
                return;
            else {
                System.out.println("previousChar is digit: " + Character.isDigit(previousChar));
                System.out.println("currentChar is $ or alpha: " + (Character.isAlphabetic(currentChar) || currentChar=='$'));
                if (currentChar == '$') {

                    if(Character.isDigit(previousChar))
                        for (int counter = 0; counter < Integer.parseInt(recentNum); counter++) {
                            currentRow++;
                            for (int newCol = 0; newCol < x; newCol++) {
                                System.out.println("doing multiple new lines");
                                canvas.getSquares()[currentRow + offset][newCol + offset].setFilled(false);
                                canvas.getLife().getStatArr()[currentRow + offset][newCol + offset] = false;
                                canvas.getChanged().add(canvas.getSquares()[currentRow + offset][newCol + offset]);
                                canvas.updatePixelsOfSquare(canvas.getSquares()[currentRow + offset][newCol + offset]);
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
                        canvas.getSquares()[currentRow+offset][currentCol+offset].setFilled(status);
                        canvas.getLife().getStatArr()[currentRow+offset][currentCol+offset] = status;
                        canvas.getChanged().add(canvas.getSquares()[currentRow+offset][currentCol+offset]);
                        canvas.updatePixelsOfSquare(canvas.getSquares()[currentRow+offset][currentCol+offset]);
                        System.out.println("updating " + canvas.getSquares()[currentRow+offset][currentCol+offset] + " " + Integer.parseInt(recentNum) + " times when current char is " + currentChar);
                        currentCol++;
                    }
                } else if (((Character.isAlphabetic(previousChar) || previousChar == '$') && Character.isAlphabetic(currentChar)) || previousChar == ' ') {
                    if (currentChar == 'o') {
                        canvas.getSquares()[currentRow+offset][currentCol+offset].setFilled(true);
                        canvas.getLife().getStatArr()[currentRow+offset][currentCol+offset] = true;
                        canvas.getChanged().add(canvas.getSquares()[currentRow+offset][currentCol+offset]);
                        canvas.updatePixelsOfSquare(canvas.getSquares()[currentRow+offset][currentCol+offset]);

                    } else {
                        canvas.getSquares()[currentRow+offset][currentCol+offset].setFilled(false);
                        canvas.getLife().getStatArr()[currentRow+offset][currentCol+offset] = false;
                        canvas.getChanged().add(canvas.getSquares()[currentRow+offset][currentCol+offset]);
                        canvas.updatePixelsOfSquare(canvas.getSquares()[currentRow+offset][currentCol+offset]);
                    }
                    System.out.println("updating " + canvas.getSquares()[currentRow+offset][currentCol+offset] + " when current char is " + currentChar);

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
                    parseRLEFile(selected,0);
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
        GUI gui = new GUI(600   ,600,1200,1200,60);
        gui.parseRLEFile(new File("unique-high-period.rle"),100);
        //TODO:implement sparse matrix
    }

}
