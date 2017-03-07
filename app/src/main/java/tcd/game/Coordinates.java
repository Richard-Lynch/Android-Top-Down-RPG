package tcd.game;

/**
 * Created by richie on 06/03/17.
 */

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // getters

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // equals using x and y
//    @Override
    public boolean equals(Coordinates obj) {
        if (    this.x == obj.getX() &
                this.y == obj.getY()) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (this.x*100) + this.y;
    }
}