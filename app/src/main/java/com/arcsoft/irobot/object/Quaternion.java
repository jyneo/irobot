package com.arcsoft.irobot.object;

/**
 *
 * Created by yj2595 on 2018/3/9.
 */

public class Quaternion {

    private final double x, y, z, w;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }

    // Create a new object with the given components.
    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    // Create a new object with the given components.
    public Quaternion(final double[] v) {
        this.x = v[0];
        this.y = v[1];
        this.z = v[2];
        this.w = v[3];
    }
    public Quaternion(final float[] v) {
        this.x = v[0];
        this.y = v[1];
        this.z = v[2];
        this.w = v[3];
    }

    // Return a string representation of the invoking object.
    @Override
    public String toString() {
        return w + " + " + x + "i + " + y + "j + " + z + "k";
    }

    // Return the quaternion norm.
    public double norm() {
        return Math.sqrt(w * w + x * x + y * y + z * z);
    }

    // Return the quaternion conjugate.
    public Quaternion conjugate() {
        return new Quaternion(w, -x, -y, -z);
    }

    // Return a new Quaternion whose value is (this + b).
    public Quaternion plus(final Quaternion b) {
        Quaternion a = this;
        return new Quaternion(a.w +b.w, a.x +b.x, a.y +b.y, a.z +b.z);
    }

    // Return dot product (this dot b).
    public double dot(final Quaternion b) {
        Quaternion a = this;
        return a.w*b.x + a.x*b.x + a.y*b.y + a.z*b.z;
    }

    // Return a new Quaternion whose value is (this * b).
    public Quaternion times(final Quaternion b) {
        Quaternion a = this;
        double w = a.w *b.w - a.x *b.x - a.y *b.y - a.z *b.z;
        double x = a.w *b.x + a.x *b.w + a.y *b.z - a.z *b.y;
        double y = a.w *b.y - a.x *b.z + a.y *b.w + a.z *b.x;
        double z = a.w *b.z + a.x *b.y - a.y *b.x + a.z *b.w;
        return new Quaternion(w, x, y, z);
    }

    // Return a new Quaternion whose value is (this * b).
    public Quaternion times(final double b) {
        Quaternion a = this;
        return new Quaternion(a.w * b, a.x * b, a.y * b, a.z * b);
    }

    // Return a new Quaternion whose value is the inverse of this.
    public Quaternion inverse() {
        double d = w * w + x * x + y * y + z * z;
        return new Quaternion(w /d, -x /d, -y /d, -z /d);
    }

    // Return a / b
    // we use the definition a * b^-1 (as opposed to b^-1 a)
    public Quaternion divide(Quaternion b) {
        Quaternion a = this;
        return a.times(b.inverse());
    }

    // Return a new normalized Quaternion (this / this.norm()).
    public Quaternion normalized() {
        return this.times(1.0 / norm());
    }

    // Return Euler angles in radian..
    public double[] eulerAngles() {
        double ysqr = y * y;

        // roll (x-axis rotation)
        double t0 = +2.0 * (w * x + y * z);
        double t1 = +1.0 - 2.0 * (x * x + ysqr);
        double roll = Math.atan2(t0, t1);

        // pitch (y-axis rotation)
        double t2 = +2.0 * (w * y - z * x);
        t2 = t2 > 1.0 ? 1.0 : t2;
        t2 = t2 < -1.0 ? -1.0 : t2;
        double pitch = Math.asin(t2);

        // yaw (z-axis rotation)
        double t3 = +2.0 * (w * z + x * y);
        double t4 = +1.0 - 2.0 * (ysqr + z * z);
        double yaw = Math.atan2(t3, t4);

        return new double[] { roll, pitch, yaw };
    }

    // Return rotation axis in unit vector.format.
    public double[] rotationAxis() {
        double invN = 1.0 / Math.sqrt(x*x + y*y + z*z);
        return new double[] { x*invN, y*invN, z*invN};
    }

    // Create a new quaternion from angle and axis.
    public static Quaternion fromAngleAxis(double angle, double x, double y, double z) {
        angle *= 0.5 * Math.PI / 180;
        double w = Math.cos(angle);
        double s = Math.sin(angle) / Math.sqrt(x*x + y*y + z*z);
        return new Quaternion(w, x*s, y*s, z*s);
    }
}
