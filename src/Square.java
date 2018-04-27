public class Square {
    int x;
    int y;
    int w;
    int h;
    boolean filled;



    public Square(int x, int y, int w, int h, boolean filled)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.filled = filled;
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


}
