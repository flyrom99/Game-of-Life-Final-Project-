public class Square {
    int x;
    int y;
    int w;
    int h;
    boolean filled;
    boolean clickedOn;

    public Square(int x, int y, int w, int h, boolean filled)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.filled = filled;
    }
    public boolean isClickedOn() {
        return clickedOn;
    }

    public void setClickedOn(boolean repainted) {
        this.clickedOn = repainted;
    }
    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void toggleFilled()
    {
        if(filled)
            this.filled = false;
        else
            this.filled = true;
    }
    public static void printArr(Square[][] arr)
    {
        for(int r = 0;r<arr[0].length;r++)
        {
            for(int c = 0;c<arr.length;c++)
            {
                System.out.print(arr[r][c].isFilled() + " ");
            }
            System.out.println();
        }
    }

    public String toString()
    {
        return "(" + x + ", " + y + ") s: " + filled;
    }


}
