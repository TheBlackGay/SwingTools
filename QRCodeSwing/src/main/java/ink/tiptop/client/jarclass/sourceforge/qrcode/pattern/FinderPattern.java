//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.pattern;

import java.util.Vector;

import ink.tiptop.client.jarclass.sourceforge.qrcode.QRCodeDecoder;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.FinderPatternNotFoundException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.InvalidVersionException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.InvalidVersionInfoException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.VersionInformationException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Axis;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Line;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Point;
import ink.tiptop.client.jarclass.sourceforge.qrcode.reader.QRCodeImageReader;
import ink.tiptop.client.jarclass.sourceforge.qrcode.util.DebugCanvas;

public class FinderPattern {
    public static final int UL = 0;
    public static final int UR = 1;
    public static final int DL = 2;
    static final int[] VersionInfoBit = new int[]{31892, 34236, 39577, 42195, 48118, 51042, 55367, 58893, 63784, 68472, 70749, 76311, 79154, 84390, 87683, 92361, 96236, 102084, 102881, 110507, 110734, 117786, 119615, 126325, 127568, 133589, 136944, 141498, 145311, 150283, 152622, 158308, 161089, 167017};
    static DebugCanvas canvas = QRCodeDecoder.getCanvas();
    Point[] center;
    int version;
    int[] sincos;
    int[] width;
    int[] moduleSize;

    public static FinderPattern findFinderPattern(boolean[][] var0) throws FinderPatternNotFoundException, VersionInformationException {
        Line[] var1 = findLineAcross(var0);
        Line[] var2 = findLineCross(var1);
        Point[] var3 = null;

        try {
            var3 = getCenter(var2);
        } catch (FinderPatternNotFoundException var10) {
            throw var10;
        }

        int[] var4 = getAngle(var3);
        var3 = sort(var3, var4);
        int[] var5 = getWidth(var0, var3, var4);
        int[] var6 = new int[]{(var5[0] << QRCodeImageReader.DECIMAL_POINT) / 7, (var5[1] << QRCodeImageReader.DECIMAL_POINT) / 7, (var5[2] << QRCodeImageReader.DECIMAL_POINT) / 7};
        int var7 = calcRoughVersion(var3, var5);
        if (var7 > 6) {
            try {
                var7 = calcExactVersion(var3, var4, var6, var0);
            } catch (VersionInformationException var9) {
                ;
            }
        }

        return new FinderPattern(var3, var7, var4, var5, var6);
    }

    FinderPattern(Point[] var1, int var2, int[] var3, int[] var4, int[] var5) {
        this.center = var1;
        this.version = var2;
        this.sincos = var3;
        this.width = var4;
        this.moduleSize = var5;
    }

    public Point[] getCenter() {
        return this.center;
    }

    public Point getCenter(int var1) {
        return var1 >= 0 && var1 <= 2 ? this.center[var1] : null;
    }

    public int getWidth(int var1) {
        return this.width[var1];
    }

    public int[] getAngle() {
        return this.sincos;
    }

    public int getVersion() {
        return this.version;
    }

    public int getModuleSize() {
        return this.moduleSize[0];
    }

    public int getModuleSize(int var1) {
        return this.moduleSize[var1];
    }

    public int getSqrtNumModules() {
        return 17 + 4 * this.version;
    }

    static Line[] findLineAcross(boolean[][] var0) {
        int var3 = var0.length;
        int var4 = var0[0].length;
        Point var5 = new Point();
        Vector var6 = new Vector();
        int[] var7 = new int[5];
        int var8 = 0;
        boolean var9 = false;
        boolean var10 = false;

        while(true) {
            boolean var11 = var0[var5.getX()][var5.getY()];
            int var12;
            if (var11 == var10) {
                ++var7[var8];
            } else {
                if (!var11 && checkPattern(var7, var8)) {
                    int var13;
                    int var14;
                    int var15;
                    int var16;
                    if (!var9) {
                        var12 = var5.getX();

                        for(var16 = 0; var16 < 5; ++var16) {
                            var12 -= var7[var16];
                        }

                        var14 = var5.getX() - 1;
                        var13 = var15 = var5.getY();
                    } else {
                        var12 = var14 = var5.getX();
                        var13 = var5.getY();

                        for(var16 = 0; var16 < 5; ++var16) {
                            var13 -= var7[var16];
                        }

                        var15 = var5.getY() - 1;
                    }

                    var6.addElement(new Line(var12, var13, var14, var15));
                }

                var8 = (var8 + 1) % 5;
                var7[var8] = 1;
                var10 = !var10;
            }

            if (!var9) {
                if (var5.getX() < var3 - 1) {
                    var5.translate(1, 0);
                } else if (var5.getY() < var4 - 1) {
                    var5.set(0, var5.getY() + 1);
                    var7 = new int[5];
                } else {
                    var5.set(0, 0);
                    var7 = new int[5];
                    var9 = true;
                }
            } else if (var5.getY() < var4 - 1) {
                var5.translate(0, 1);
            } else {
                if (var5.getX() >= var3 - 1) {
                    Line[] var17 = new Line[var6.size()];

                    for(var12 = 0; var12 < var17.length; ++var12) {
                        var17[var12] = (Line)var6.elementAt(var12);
                    }

                    canvas.drawLines(var17, 12320699);
                    return var17;
                }

                var5.set(var5.getX() + 1, 0);
                var7 = new int[5];
            }
        }
    }

    static boolean checkPattern(int[] var0, int var1) {
        int[] var2 = new int[]{1, 1, 3, 1, 1};
        int var3 = 0;

        int var4;
        for(var4 = 0; var4 < 5; ++var4) {
            var3 += var0[var4];
        }

        var3 <<= QRCodeImageReader.DECIMAL_POINT;
        var3 /= 7;

        for(var4 = 0; var4 < 5; ++var4) {
            int var5 = var3 * var2[var4] - var3 / 2;
            int var6 = var3 * var2[var4] + var3 / 2;
            int var7 = var0[(var1 + var4 + 1) % 5] << QRCodeImageReader.DECIMAL_POINT;
            if (var7 < var5 || var7 > var6) {
                return false;
            }
        }

        return true;
    }

    static Line[] findLineCross(Line[] var0) {
        Vector var1 = new Vector();
        Vector var2 = new Vector();
        Vector var3 = new Vector();

        int var5;
        for(var5 = 0; var5 < var0.length; ++var5) {
            var3.addElement(var0[var5]);
        }

        int var6;
        for(var5 = 0; var5 < var3.size() - 1; ++var5) {
            var2.removeAllElements();
            var2.addElement(var3.elementAt(var5));

            for(var6 = var5 + 1; var6 < var3.size(); ++var6) {
                Line var4;
                int var7;
                if (Line.isNeighbor((Line)var2.lastElement(), (Line)var3.elementAt(var6))) {
                    var2.addElement(var3.elementAt(var6));
                    var4 = (Line)var2.lastElement();
                    if (var2.size() * 5 > var4.getLength() && var6 == var3.size() - 1) {
                        var1.addElement(var2.elementAt(var2.size() / 2));

                        for(var7 = 0; var7 < var2.size(); ++var7) {
                            var3.removeElement(var2.elementAt(var7));
                        }
                    }
                } else if (cantNeighbor((Line)var2.lastElement(), (Line)var3.elementAt(var6)) || var6 == var3.size() - 1) {
                    var4 = (Line)var2.lastElement();
                    if (var2.size() * 6 > var4.getLength()) {
                        var1.addElement(var2.elementAt(var2.size() / 2));

                        for(var7 = 0; var7 < var2.size(); ++var7) {
                            var3.removeElement(var2.elementAt(var7));
                        }
                    }
                    break;
                }
            }
        }

        Line[] var8 = new Line[var1.size()];

        for(var6 = 0; var6 < var8.length; ++var6) {
            var8[var6] = (Line)var1.elementAt(var6);
        }

        return var8;
    }

    static boolean cantNeighbor(Line var0, Line var1) {
        if (Line.isCross(var0, var1)) {
            return true;
        } else {
            return var0.isHorizontal() ? Math.abs(var0.getP1().getY() - var1.getP1().getY()) > 1 : Math.abs(var0.getP1().getX() - var1.getP1().getX()) > 1;
        }
    }

    static int[] getAngle(Point[] var0) {
        Line[] var1 = new Line[3];

        for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = new Line(var0[var2], var0[(var2 + 1) % var1.length]);
        }

        Line var7 = Line.getLongest(var1);
        Point var3 = new Point();

        for(int var4 = 0; var4 < var0.length; ++var4) {
            if (!var7.getP1().equals(var0[var4]) && !var7.getP2().equals(var0[var4])) {
                var3 = var0[var4];
                break;
            }
        }

        canvas.println("originPoint is: " + var3);
        new Point();
        Point var8;
        if (var3.getY() <= var7.getP1().getY() & var3.getY() <= var7.getP2().getY()) {
            if (var7.getP1().getX() < var7.getP2().getX()) {
                var8 = var7.getP2();
            } else {
                var8 = var7.getP1();
            }
        } else if (var3.getX() >= var7.getP1().getX() & var3.getX() >= var7.getP2().getX()) {
            if (var7.getP1().getY() < var7.getP2().getY()) {
                var8 = var7.getP2();
            } else {
                var8 = var7.getP1();
            }
        } else if (var3.getY() >= var7.getP1().getY() & var3.getY() >= var7.getP2().getY()) {
            if (var7.getP1().getX() < var7.getP2().getX()) {
                var8 = var7.getP1();
            } else {
                var8 = var7.getP2();
            }
        } else if (var7.getP1().getY() < var7.getP2().getY()) {
            var8 = var7.getP1();
        } else {
            var8 = var7.getP2();
        }

        int var5 = (new Line(var3, var8)).getLength();
        int[] var6 = new int[]{(var8.getY() - var3.getY() << QRCodeImageReader.DECIMAL_POINT) / var5, (var8.getX() - var3.getX() << QRCodeImageReader.DECIMAL_POINT) / var5};
        return var6;
    }

    static Point[] getCenter(Line[] var0) throws FinderPatternNotFoundException {
        Vector var1 = new Vector();

        for(int var2 = 0; var2 < var0.length - 1; ++var2) {
            Line var3 = var0[var2];

            for(int var4 = var2 + 1; var4 < var0.length; ++var4) {
                Line var5 = var0[var4];
                if (Line.isCross(var3, var5)) {
                    boolean var6 = false;
                    boolean var7 = false;
                    int var10;
                    int var11;
                    if (var3.isHorizontal()) {
                        var10 = var3.getCenter().getX();
                        var11 = var5.getCenter().getY();
                    } else {
                        var10 = var5.getCenter().getX();
                        var11 = var3.getCenter().getY();
                    }

                    var1.addElement(new Point(var10, var11));
                }
            }
        }

        Point[] var8 = new Point[var1.size()];

        for(int var9 = 0; var9 < var8.length; ++var9) {
            var8[var9] = (Point)var1.elementAt(var9);
        }

        if (var8.length == 3) {
            canvas.drawPolygon(var8, 267946120);
            return var8;
        } else {
            throw new FinderPatternNotFoundException("Invalid number of Finder Pattern detected");
        }
    }

    static Point[] sort(Point[] var0, int[] var1) {
        Point[] var2 = new Point[3];
        int var3 = getURQuadrant(var1);
        switch(var3) {
            case 1:
                var2[1] = getPointAtSide(var0, 1, 2);
                var2[2] = getPointAtSide(var0, 2, 4);
                break;
            case 2:
                var2[1] = getPointAtSide(var0, 2, 4);
                var2[2] = getPointAtSide(var0, 8, 4);
                break;
            case 3:
                var2[1] = getPointAtSide(var0, 4, 8);
                var2[2] = getPointAtSide(var0, 1, 8);
                break;
            case 4:
                var2[1] = getPointAtSide(var0, 8, 1);
                var2[2] = getPointAtSide(var0, 2, 1);
        }

        for(int var4 = 0; var4 < var0.length; ++var4) {
            if (!var0[var4].equals(var2[1]) && !var0[var4].equals(var2[2])) {
                var2[0] = var0[var4];
            }
        }

        return var2;
    }

    static int getURQuadrant(int[] var0) {
        int var1 = var0[0];
        int var2 = var0[1];
        if (var1 >= 0 && var2 > 0) {
            return 1;
        } else if (var1 > 0 && var2 <= 0) {
            return 2;
        } else if (var1 <= 0 && var2 < 0) {
            return 3;
        } else {
            return var1 < 0 && var2 >= 0 ? 4 : 0;
        }
    }

    static Point getPointAtSide(Point[] var0, int var1, int var2) {
        new Point();
        int var4 = var1 != 1 && var2 != 1 ? 2147483647 : 0;
        int var5 = var1 != 2 && var2 != 2 ? 2147483647 : 0;
        Point var3 = new Point(var4, var5);

        for(int var6 = 0; var6 < var0.length; ++var6) {
            switch(var1) {
                case 1:
                    if (var3.getX() < var0[var6].getX()) {
                        var3 = var0[var6];
                    } else if (var3.getX() == var0[var6].getX()) {
                        if (var2 == 2) {
                            if (var3.getY() < var0[var6].getY()) {
                                var3 = var0[var6];
                            }
                        } else if (var3.getY() > var0[var6].getY()) {
                            var3 = var0[var6];
                        }
                    }
                    break;
                case 2:
                    if (var3.getY() < var0[var6].getY()) {
                        var3 = var0[var6];
                    } else if (var3.getY() == var0[var6].getY()) {
                        if (var2 == 1) {
                            if (var3.getX() < var0[var6].getX()) {
                                var3 = var0[var6];
                            }
                        } else if (var3.getX() > var0[var6].getX()) {
                            var3 = var0[var6];
                        }
                    }
                case 3:
                case 5:
                case 6:
                case 7:
                default:
                    break;
                case 4:
                    if (var3.getX() > var0[var6].getX()) {
                        var3 = var0[var6];
                    } else if (var3.getX() == var0[var6].getX()) {
                        if (var2 == 2) {
                            if (var3.getY() < var0[var6].getY()) {
                                var3 = var0[var6];
                            }
                        } else if (var3.getY() > var0[var6].getY()) {
                            var3 = var0[var6];
                        }
                    }
                    break;
                case 8:
                    if (var3.getY() > var0[var6].getY()) {
                        var3 = var0[var6];
                    } else if (var3.getY() == var0[var6].getY()) {
                        if (var2 == 1) {
                            if (var3.getX() < var0[var6].getX()) {
                                var3 = var0[var6];
                            }
                        } else if (var3.getX() > var0[var6].getX()) {
                            var3 = var0[var6];
                        }
                    }
            }
        }

        return var3;
    }

    static int[] getWidth(boolean[][] var0, Point[] var1, int[] var2) throws ArrayIndexOutOfBoundsException {
        int[] var3 = new int[3];

        for(int var4 = 0; var4 < 3; ++var4) {
            boolean var5 = false;
            int var8 = var1[var4].getY();

            int var6;
            for(var6 = var1[var4].getX(); var6 >= 0; --var6) {
                if (var0[var6][var8] && !var0[var6 - 1][var8]) {
                    if (var5) {
                        break;
                    }

                    var5 = true;
                }
            }

            var5 = false;

            int var7;
            for(var7 = var1[var4].getX(); var7 < var0.length; ++var7) {
                if (var0[var7][var8] && !var0[var7 + 1][var8]) {
                    if (var5) {
                        break;
                    }

                    var5 = true;
                }
            }

            var3[var4] = var7 - var6 + 1;
        }

        return var3;
    }

    static int calcRoughVersion(Point[] var0, int[] var1) {
        int var2 = QRCodeImageReader.DECIMAL_POINT;
        int var3 = (new Line(var0[0], var0[1])).getLength() << var2;
        int var4 = (var1[0] + var1[1] << var2) / 14;
        int var5 = (var3 / var4 - 10) / 4;
        if ((var3 / var4 - 10) % 4 >= 2) {
            ++var5;
        }

        return var5;
    }

    static int calcExactVersion(Point[] var0, int[] var1, int[] var2, boolean[][] var3) throws InvalidVersionInfoException, InvalidVersionException {
        boolean[] var4 = new boolean[18];
        Point[] var5 = new Point[18];
        Axis var7 = new Axis(var1, var2[1]);
        var7.setOrigin(var0[1]);

        Point var6;
        int var8;
        for(var8 = 0; var8 < 6; ++var8) {
            for(int var9 = 0; var9 < 3; ++var9) {
                var6 = var7.translate(var9 - 7, var8 - 3);
                var4[var9 + var8 * 3] = var3[var6.getX()][var6.getY()];
                var5[var9 + var8 * 3] = var6;
            }
        }

        canvas.drawPoints(var5, 267946120);
        boolean var14 = false;

        try {
            var8 = checkVersionInfo(var4);
        } catch (InvalidVersionInfoException var13) {
            canvas.println("Version info error. now retry with other place one.");
            var7.setOrigin(var0[2]);
            var7.setModulePitch(var2[2]);

            for(int var10 = 0; var10 < 6; ++var10) {
                for(int var11 = 0; var11 < 3; ++var11) {
                    var6 = var7.translate(var10 - 3, var11 - 7);
                    var4[var11 + var10 * 3] = var3[var6.getX()][var6.getY()];
                    var5[var10 + var11 * 3] = var6;
                }
            }

            canvas.drawPoints(var5, 267946120);

            try {
                var8 = checkVersionInfo(var4);
            } catch (VersionInformationException var12) {
                throw var12;
            }
        }

        return var8;
    }

    static int checkVersionInfo(boolean[] var0) throws InvalidVersionInfoException {
        int var1 = 0;

        int var2;
        for(var2 = 0; var2 < VersionInfoBit.length; ++var2) {
            var1 = 0;

            for(int var3 = 0; var3 < 18; ++var3) {
                if (var0[var3] ^ (VersionInfoBit[var2] >> var3) % 2 == 1) {
                    ++var1;
                }
            }

            if (var1 <= 3) {
                break;
            }
        }

        if (var1 <= 3) {
            return 7 + var2;
        } else {
            throw new InvalidVersionInfoException("Too many errors in version information");
        }
    }
}
