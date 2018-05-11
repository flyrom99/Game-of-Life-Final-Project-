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
    static String startingDirectory = "/Users/tmoyer18/Dropbox/Data structures/Game of Life/Patterns";
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
                canvas.setFocusable(true);
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
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 69)
                {
                    System.out.println("goes in here dawg");
                    canvas.calcNewPositions(l);
                    canvas.repaint();
                    canvas.setFocusable(true);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        JMenuBar bar = buildJMenuBar(timer,timerActive);
        frame.setBounds(40, 80, w + 1, h + 1);
        frame.add(c, BorderLayout.CENTER);
        frame.add(b, BorderLayout.PAGE_END);
        frame.add(bar, BorderLayout.BEFORE_FIRST_LINE);
    }
    public void setLife(Life l)
    {
        this.l = l;
    }
    public static void reset()
    {
        canvas.reset();
        canvas.setFocusable(true);
        canvas.repaint();
    }
    public void parseLife6File(File f) {
        reset();
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

    public static void decompressRLE(File f)
    {
        reset();
                canvas.setFocusable(true);;
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
        String result = "";
        currentLine = input.nextLine(); //this is where the pattern starts
        while(input.hasNextLine())
            currentLine+=input.nextLine();
        System.out.println("currentLine: " + currentLine);
        String numbers = "";
        char previousChar = ' ';
        for(int i = 0;i<currentLine.length();i++) {
            char currentChar = currentLine.charAt(i);
            if (Character.isDigit(previousChar) && !Character.isDigit(currentChar) && !numbers.equals("")) {
                for (int k = 0; k < Integer.parseInt(numbers); k++) {
                    result += currentChar;
                }
                numbers = "";

            } else if (Character.isDigit(currentChar)) {
                numbers += currentChar;
            } else
            {
                result+=currentChar;
            }
            previousChar = currentChar;

        }
        parseDecompressed(result,offsets);

    }

    public static void parseDecompressed(String rle,int[] offset)
    {
        int xOffset = offset[0];
        int yOffset = offset[1];
        int currentRow = yOffset;
        int currentCol = xOffset;
        System.out.println("xOffset: " + xOffset + " yOffset: " + yOffset);
        System.out.println("RLE: " + rle);
        for(int i = 0;i<rle.length();i++)
        {
            if(rle.charAt(i) == '$')
            {
                currentRow++;
                currentCol = xOffset ;
                System.out.println("New line");
            }
            else if(rle.charAt(i) == '!')
                return;
            else
            {
                if(rle.charAt(i) == 'o')
                {
                    if((currentCol)>numXSq || (currentCol)<canvas.getSquares()[0][0].getArrayX() || (currentRow)>numYSq || (currentRow)<canvas.getSquares()[0][0].getArrayY())
                    {
                        canvas.getLife().getExtraneousAlive().add(new Square(((currentCol)*canvas.getXInc()),(currentRow)*canvas.getYInc(),(currentCol),(currentRow),canvas.getXInc(),canvas.getYInc(),true,canvas.getSquares()));
                    }
                    else
                    {
                        canvas.getLife().getBoard()[(currentRow)][(currentCol)].setFilled(true);
                        canvas.getChanged().put( canvas.getLife().getBoard()[(currentRow)][(currentCol)], canvas.getLife().getBoard()[(currentRow)][(currentCol)].isFilled());

                    }
                    canvas.updatePixelsOfSquare(canvas.getSquares()[(currentRow)][(currentCol)]);
                }
                else if(rle.charAt(i) == 'b')
                {
                    if(!((currentCol)>numXSq || (currentCol)<canvas.getSquares()[0][0].getArrayX() || (currentRow)>numYSq || (currentRow)<canvas.getSquares()[0][0].getArrayY()))
                    {
                        canvas.getSquares()[(currentRow)][(currentCol)].setFilled(false);
                        canvas.getChanged().put( canvas.getLife().getBoard()[(currentRow)][(currentCol)], canvas.getLife().getBoard()[(currentRow)][(currentCol)].isFilled());
                    }
                }
                else
                {
                    System.out.println("you blow at parsing");
                }
                System.out.println("put " + canvas.getLife().getBoard()[(currentRow)][(currentCol)] + " as " + canvas.getLife().getBoard()[(currentRow)][(currentCol)].isFilled());
                currentCol++;
            }


        }
        canvas.repaint();
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
                System.out.println("You clicked on open");
                JFileChooser fileChooser = new JFileChooser(new File(startingDirectory));
                int result = fileChooser.showOpenDialog(frame);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    startingDirectory = fileChooser.getCurrentDirectory().getAbsolutePath();
                    File selected = fileChooser.getSelectedFile();
                    l.reset();
                    reset();
                    canvas.setFocusable(true);;
                    timer.stop();
                    canvas.repaint();
                    decompressRLE(selected);
                    //canvas.getLife().printImportantStuff();
                    canvas.setFocusable(true);
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
                System.out.println("You clicked on save");
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
                System.out.println("You clicked on step");
                //canvas.getLife().printImportantStuff();
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
                System.out.println("You clicked on reset");
                timer.stop();
                reset();
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
                System.out.println("You clicked on stop");
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
                System.out.println("You clicked on start");
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
        GUI gui = new GUI(200   ,200,1200,1200,40);
        //TODO:fix  parser
    }

}
