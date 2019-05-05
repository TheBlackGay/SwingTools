//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.geom;


import ink.tiptop.client.jarclass.sourceforge.qrcode.reader.QRCodeImageReader;

public class Axis {
    int sin;
    int cos;
    int modulePitch;
    Point origin;

    public Axis(int[] var1, int var2) {
        this.sin = var1[0];
        this.cos = var1[1];
        this.modulePitch = var2;
        this.origin = new Point();
    }

    public void setOrigin(Point var1) {
        this.origin = var1;
    }

    public void setModulePitch(int var1) {
        this.modulePitch = var1;
    }

    public Point translate(Point var1) {
        int var2 = var1.getX();
        int var3 = var1.getY();
        return this.translate(var2, var3);
    }

    public Point translate(Point var1, Point var2) {
        this.setOrigin(var1);
        int var3 = var2.getX();
        int var4 = var2.getY();
        return this.translate(var3, var4);
    }

    public Point translate(Point var1, int var2, int var3) {
        this.setOrigin(var1);
        return this.translate(var2, var3);
    }

    public Point translate(Point var1, int var2, int var3, int var4) {
        this.setOrigin(var1);
        this.modulePitch = var2;
        return this.translate(var3, var4);
    }

    public Point translate(int var1, int var2) {
        long var3 = (long) QRCodeImageReader.DECIMAL_POINT;
        Point var5 = new Point();
        int var6 = var1 == 0 ? 0 : this.modulePitch * var1 >> (int)var3;
        int var7 = var2 == 0 ? 0 : this.modulePitch * var2 >> (int)var3;
        var5.translate(var6 * this.cos - var7 * this.sin >> (int)var3, var6 * this.sin + var7 * this.cos >> (int)var3);
        var5.translate(this.origin.getX(), this.origin.getY());
        return var5;
    }
}
