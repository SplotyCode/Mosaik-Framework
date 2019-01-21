package io.github.splotycode.mosaik.util.math;

public class Vector3D extends Vector {

    public Vector3D() {
        super(3);
    }

    public Vector3D(double x, double y, double z) {
        super(x, y, z);
    }

    public double getX() {
        return values[0];
    }

    public double getY() {
        return values[1];
    }

    public double getZ() {
        return values[2];
    }

    public void setX(double x) {
        values[0] = x;
    }

    public void setY(double y) {
        values[1] = y;
    }

    public void setZ(double z) {
        values[2] = z;
    }

}
