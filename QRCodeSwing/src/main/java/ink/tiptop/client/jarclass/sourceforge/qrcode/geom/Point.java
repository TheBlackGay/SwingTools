//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.geom;

import ink.tiptop.client.jarclass.sourceforge.qrcode.util.QRCodeUtility;

public class Point {
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 4;
    public static final int TOP = 8;
    int x;
    int y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int var1, int var2) {
        this.x = var1;
        this.y = var2;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int var1) {
        this.x = var1;
    }

    public void setY(int var1) {
        this.y = var1;
    }

    public void translate(int var1, int var2) {
        this.x += var1;
        this.y += var2;
    }

    public void set(int var1, int var2) {
        this.x = var1;
        this.y = var2;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public static Point getCenter(Point var0, Point var1) {
        return new Point((var0.getX() + var1.getX()) / 2, (var0.getY() + var1.getY()) / 2);
    }

    public boolean equals(Point var1) {
        return this.x == var1.x && this.y == var1.y;
    }

    public int distanceOf(Point var1) {
        int var2 = var1.getX();
        int var3 = var1.getY();
        return QRCodeUtility.sqrt((this.x - var2) * (this.x - var2) + (this.y - var3) * (this.y - var3));
    }
}
