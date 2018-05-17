import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
        c.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();
                c.setXInc(c.getW() / c.getxNum());
                c.setYInc(c.getH() / c.getyNum());
                int xSquare = ((x) / c.getXInc());
                int ySquare = ((y) / c.getYInc());
                Square square = c.getSquares()[ySquare][xSquare];
                System.out.println("you clicked on " + square + ", out of bounds = " + canvas.getLife().outOfBounds.contains(square) + " neighbors: " + Arrays.toString(square.getNeighbors(canvas.getSquares())));
                //System.out.println("current square status: " + square.isFilled());
                square.toggleFilled();
                square.setClickedOn(true);
                if(square.isFilled())
                    c.getLife().animateCell(square);
                else
                    c.getLife().killCell(square);
                c.updatePixelsOfSquare(square);
                c.repaint();
                c.setFocusable(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {

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
        c.setFocusable(true);
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
        c.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 69) // e
                {
                    System.out.println("you pressed e");
                    canvas.calcNewPositions(l);
                    canvas.repaint();
                    canvas.getLife().printNeighborBoard(canvas.getSquares());
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == 37) //left arrow
                {
                    System.out.println("you pressed left arrow");
                    canvas.newTranslate(-canvas.getyNum()/8,0,canvas.getSquares());
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == 38) //up arrow
                {
                    System.out.println("you pressed up arrow");
                    canvas.newTranslate(0,canvas.getyNum()/8,canvas.getSquares());
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if (e.getKeyCode() == 39) //right arrow
                {
                    System.out.println("you pressed right arrow");
                    canvas.newTranslate(canvas.getxNum()/8,0,canvas.getSquares());
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == 40) //down arrow
                {
                    System.out.println("you pressed down arrow");
                    boolean wasActive = timerActive;
                    timer.stop();
                    timerActive = false;
                    canvas.newTranslate(0,-canvas.getyNum()/8,canvas.getSquares());
                    if(wasActive) {
                        timer.start();
                        timerActive = true;
                    }
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == 82) //r
                {
                    timer.stop();
                    reset();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == KeyEvent.VK_O) //O
                {
                    JFileChooser fileChooser = new JFileChooser(new File(startingDirectory));
                    int result = fileChooser.showOpenDialog(frame);
                    if(result == JFileChooser.APPROVE_OPTION)
                    {
                        startingDirectory = fileChooser.getCurrentDirectory().getAbsolutePath();
                        File selected = fileChooser.getSelectedFile();
                        l.reset(canvas.getXInc(),canvas.getYInc());
                        reset();
                        canvas.setFocusable(true);
                        canvas.requestFocusInWindow();
                        timer.stop();
                        canvas.repaint();
                        decompressRLE(selected);
                        canvas.setFocusable(true);
                        canvas.requestFocusInWindow();
                    }
                }
                else if (e.getKeyCode() == 32)
                {
                    System.out.println("stopped");
                    if (timerActive) {
                        timer.stop();
                        timerActive = false;
                    } else {
                        timer.start();
                        timerActive = true;
                    }
                    c.setFocusable(true);
                }
                canvas.setFocusable(true);
                canvas.requestFocusInWindow();
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
        bar.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 69) // e
                {
                    System.out.println("you pressed e");
                    canvas.calcNewPositions(l);
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == 37) //left arrow
                {
                    System.out.println("you pressed left arrow");
                    canvas.newTranslate(-4,0,canvas.getSquares());
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == 38) //up arrow
                {
                    canvas.newTranslate(0,4,canvas.getSquares());
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if (e.getKeyCode() == 39) //right arrow
                {
                    System.out.println("you pressed right arrow");
                    canvas.newTranslate(4,0,canvas.getSquares());
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == 40) //down arrow
                {
                    System.out.println("you pressed down arrow");
                    canvas.newTranslate(0,-4,canvas.getSquares());
                    canvas.repaint();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == 82) //r
                {
                    timer.stop();
                    reset();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                }
                else if(e.getKeyCode() == KeyEvent.VK_O) //O
                {
                    JFileChooser fileChooser = new JFileChooser(new File(startingDirectory));
                    int result = fileChooser.showOpenDialog(frame);
                    if(result == JFileChooser.APPROVE_OPTION)
                    {
                        startingDirectory = fileChooser.getCurrentDirectory().getAbsolutePath();
                        File selected = fileChooser.getSelectedFile();
                        l.reset(canvas.getXInc(),canvas.getYInc());
                        reset();
                        canvas.setFocusable(true);
                        canvas.requestFocusInWindow();
                        timer.stop();
                        canvas.repaint();
                        decompressRLE(selected);
                        //canvas.getLife().printImportantStuff();
                        canvas.setFocusable(true);
                        canvas.requestFocusInWindow();
                    }
                }
                else if (e.getKeyCode() == 32)
                {
                    if (timerActive) {
                        timer.stop();
                        timerActive = false;
                    } else {
                        timer.start();
                        timerActive = true;
                    }
                    c.setFocusable(true);
                }
                canvas.setFocusable(true);
                canvas.requestFocusInWindow();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
    public void setLife(Life l)
    {
        this.l = l;
    }
    public static void reset()
    {
        canvas.reset();
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
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
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
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
        if(x>numXSq || y>numYSq)
            return;
        int[] offsets = calcOffSet(x,y);
        int xOffSet = 0;
        int yOffSet = 0;
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

    public void toggleTimer()
    {
        if (timerActive) {
            timer.stop();
            timerActive = false;
        } else {
            timer.start();
            timerActive = true;
        }
        canvas.setFocusable(true);
    }
    public static void parseDecompressed(String rle,int[] offset)
    {
        int xOffset = offset[0];
        int yOffset = offset[1];
        int currentRow = yOffset;
        int currentCol = xOffset;
        //System.out.println("xOffset: " + xOffset + " yOffset: " + yOffset);
        //System.out.println("RLE: " + rle);
        for(int i = 0;i<rle.length();i++)
        {
            if(rle.charAt(i) == '$')
            {
                currentRow++;
                currentCol = xOffset ;
                //System.out.println("New line");
            }
            else if(rle.charAt(i) == '!')
                break;
            else
            {
                if(rle.charAt(i) == 'o')
                {

                    if((currentCol)>numXSq || (currentCol)<canvas.getSquares()[0][0].getArrayX() || (currentRow)>numYSq || (currentRow)<canvas.getSquares()[0][0].getArrayY())
                    {
                        Square s = new Square(((currentCol)*canvas.getXInc()),(currentRow)*canvas.getYInc(),(currentCol),(currentRow),canvas.getXInc(),canvas.getYInc(),true,canvas.getSquares(),canvas.getAllCells());
                        canvas.getLife().getAlive().add(s);
                        canvas.getLife().outOfBounds.add(s);
                    }
                    else
                    {
                        canvas.getLife().getBoard()[(currentRow)][(currentCol)].setFilled(true);
                        canvas.getSquares()[currentRow][currentCol].setFilled(true);
                        canvas.life.getAlive().add(canvas.getSquares()[currentRow][currentCol]);
                        canvas.getChanged().put( canvas.getLife().getBoard()[(currentRow)][(currentCol)], canvas.getLife().getBoard()[(currentRow)][(currentCol)].isFilled());

                    }
                    canvas.updatePixelsOfSquare(canvas.getSquares()[(currentRow)][(currentCol)]);
                }
                else if(rle.charAt(i) == 'b')
                {
                    if(!((currentCol)>numXSq || (currentCol)<canvas.getSquares()[0][0].getArrayX() || (currentRow)>numYSq || (currentRow)<canvas.getSquares()[0][0].getArrayY()))
                    {
                        canvas.getSquares()[(currentRow)][(currentCol)].setFilled(false);
                        canvas.getLife().getBoard()[currentRow][currentCol].setFilled(false);
                    }
                }
                else
                {
                    System.out.println("you blow at parsing");
                }
                canvas.getSquares()[currentRow][currentCol].setCalculatedNeighbors(false);
                currentCol++;
            }

        }
        for(int r = 0;r<canvas.getSquares().length;r++)
        {
            for(int c =0 ;c<canvas.getSquares()[0].length;c++)
            {
                Square s = canvas.getSquares()[r][c];
                s.setCalculatedNeighbors(false);
                int numN = canvas.getSquares()[r][c].getNumAliveNeighbors();
                if(canvas.getSquares()[r][c].isFilled() || numN>0)
                    canvas.life.numNeighbors.put(s,s.getNumAliveNeighbors());
                if(s.isFilled())
                    canvas.getLife().getAlive().add(s);
            }
        }
        //canvas.newTranslate(5,5,canvas.getSquares());
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
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
                    l.reset(canvas.getXInc(),canvas.getYInc());
                    reset();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
                    timer.stop();
                    canvas.repaint();
                    decompressRLE(selected);
                    //canvas.getLife().printImportantStuff();
                    canvas.setFocusable(true);
                    canvas.requestFocusInWindow();
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
                canvas.requestFocusInWindow();
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
                canvas.requestFocusInWindow();
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
                canvas.requestFocusInWindow();
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
                canvas.getLife().printNeighborBoard(canvas.getSquares());
                timer.stop();
                setTimerActive(false);
                canvas.requestFocusInWindow();
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
                canvas.requestFocusInWindow();

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
        GUI gui = new GUI(800   ,800,1600,1600,100);
        //TODO:
    }

}
