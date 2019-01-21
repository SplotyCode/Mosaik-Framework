package io.github.splotycode.mosaik.util.math;

public class Vector2D extends Vector {

    public Vector2D() {
        super(2);
    }

    public Vector2D(double x, double y) {
        super(x, y);
    }

    public double getX() {
        return values[0];
    }

    public double getY() {
        return values[1];
    }

    public void setX(double x) {
        values[0] = x;
    }

    public void setY(double y) {
        values[1] = y;
    }

}
