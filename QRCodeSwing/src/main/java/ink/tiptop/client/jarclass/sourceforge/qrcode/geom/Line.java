//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.geom;


import ink.tiptop.client.jarclass.sourceforge.qrcode.util.QRCodeUtility;

public class Line {
    int x1;
    int y1;
    int x2;
    int y2;

    public Line() {
        this.x1 = this.y1 = this.x2 = this.y2 = 0;
    }

    public Line(int var1, int var2, int var3, int var4) {
        this.x1 = var1;
        this.y1 = var2;
        this.x2 = var3;
        this.y2 = var4;
    }

    public Line(Point var1, Point var2) {
        this.x1 = var1.getX();
        this.y1 = var1.getY();
        this.x2 = var2.getX();
        this.y2 = var2.getY();
    }

    public static boolean isNeighbor(Line var0, Line var1) {
        return Math.abs(var0.getP1().getX() - var1.getP1().getX()) < 2 && Math.abs(var0.getP1().getY() - var1.getP1().getY()) < 2 && Math.abs(var0.getP2().getX() - var1.getP2().getX()) < 2 && Math.abs(var0.getP2().getY() - var1.getP2().getY()) < 2;
    }

    public static boolean isCross(Line var0, Line var1) {
        if (var0.isHorizontal() && var1.isVertical()) {
            if (var0.getP1().getY() > var1.getP1().getY() && var0.getP1().getY() < var1.getP2().getY() && var1.getP1().getX() > var0.getP1().getX() && var1.getP1().getX() < var0.getP2().getX()) {
                return true;
            }
        } else if (var0.isVertical() && var1.isHorizontal() && var0.getP1().getX() > var1.getP1().getX() && var0.getP1().getX() < var1.getP2().getX() && var1.getP1().getY() > var0.getP1().getY() && var1.getP1().getY() < var0.getP2().getY()) {
            return true;
        }

        return false;
    }

    public static Line getLongest(Line[] var0) {
        Line var1 = new Line();

        for (int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2].getLength() > var1.getLength()) {
                var1 = var0[var2];
            }
        }

        return var1;
    }

    public Point getP1() {
        return new Point(this.x1, this.y1);
    }

    public void setP1(Point var1) {
        this.x1 = var1.getX();
        this.y1 = var1.getY();
    }

    public Point getP2() {
        return new Point(this.x2, this.y2);
    }

    public void setP2(Point var1) {
        this.x2 = var1.getX();
        this.y2 = var1.getY();
    }

    public void setLine(int var1, int var2, int var3, int var4) {
        this.x1 = var1;
        this.y1 = var2;
        this.x2 = var3;
        this.y2 = var4;
    }

    public void setP1(int var1, int var2) {
        this.x1 = var1;
        this.y1 = var2;
    }

    public void setP2(int var1, int var2) {
        this.x2 = var1;
        this.y2 = var2;
    }

    public void translate(int var1, int var2) {
        this.x1 += var1;
        this.y1 += var2;
        this.x2 += var1;
        this.y2 += var2;
    }

    public boolean isHorizontal() {
        return this.y1 == this.y2;
    }

    public boolean isVertical() {
        return this.x1 == this.x2;
    }

    public Point getCenter() {
        int var1 = (this.x1 + this.x2) / 2;
        int var2 = (this.y1 + this.y2) / 2;
        return new Point(var1, var2);
    }

    public int getLength() {
        int var1 = Math.abs(this.x2 - this.x1);
        int var2 = Math.abs(this.y2 - this.y1);
        int var3 = QRCodeUtility.sqrt(var1 * var1 + var2 * var2);
        return var3;
    }

    public String toString() {
        return "(" + this.x1 + "," + this.y1 + ")-(" + this.x2 + "," + this.y2 + ")";
    }
}
