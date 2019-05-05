//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.pattern;

import ink.tiptop.client.jarclass.sourceforge.qrcode.QRCodeDecoder;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.AlignmentPatternNotFoundException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.InvalidVersionException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Axis;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Line;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Point;
import ink.tiptop.client.jarclass.sourceforge.qrcode.util.DebugCanvas;

public class AlignmentPattern {
    static final int RIGHT = 1;
    static final int BOTTOM = 2;
    static final int LEFT = 3;
    static final int TOP = 4;
    static DebugCanvas canvas = QRCodeDecoder.getCanvas();
    Point[][] center;
    int patternDistance;

    AlignmentPattern(Point[][] var1, int var2) {
        this.center = var1;
        this.patternDistance = var2;
    }

    public static AlignmentPattern findAlignmentPattern(boolean[][] var0, FinderPattern var1) throws AlignmentPatternNotFoundException, InvalidVersionException {
        Point[][] var2 = getLogicalCenter(var1);
        int var3 = var2[1][0].getX() - var2[0][0].getX();
        Point[][] var4 = (Point[][])null;
        var4 = getCenter(var0, var1, var2);
        return new AlignmentPattern(var4, var3);
    }

    public Point[][] getCenter() {
        return this.center;
    }

    public void setCenter(Point[][] var1) {
        this.center = var1;
    }

    public int getLogicalDistance() {
        return this.patternDistance;
    }

    static Point[][] getCenter(boolean[][] var0, FinderPattern var1, Point[][] var2) throws AlignmentPatternNotFoundException {
        int var3 = var1.getModuleSize();
        Axis var4 = new Axis(var1.getAngle(), var3);
        int var5 = var2.length;
        Point[][] var6 = new Point[var5][var5];
        var4.setOrigin(var1.getCenter(0));
        var6[0][0] = var4.translate(3, 3);
        canvas.drawCross(var6[0][0], 8947967);
        var4.setOrigin(var1.getCenter(1));
        var6[var5 - 1][0] = var4.translate(-3, 3);
        canvas.drawCross(var6[var5 - 1][0], 8947967);
        var4.setOrigin(var1.getCenter(2));
        var6[0][var5 - 1] = var4.translate(3, -3);
        canvas.drawCross(var6[0][var5 - 1], 8947967);
        Point var7 = var6[0][0];

        for(int var8 = 0; var8 < var5; ++var8) {
            for(int var9 = 0; var9 < var5; ++var9) {
                if ((var9 != 0 || var8 != 0) && (var9 != 0 || var8 != var5 - 1) && (var9 != var5 - 1 || var8 != 0)) {
                    Point var10 = null;
                    Point var11;
                    if (var8 == 0) {
                        if (var9 > 0 && var9 < var5 - 1) {
                            var10 = var4.translate(var6[var9 - 1][var8], var2[var9][var8].getX() - var2[var9 - 1][var8].getX(), 0);
                            var6[var9][var8] = new Point(var10.getX(), var10.getY());
                            canvas.drawCross(var6[var9][var8], 267946120);
                        }
                    } else if (var9 == 0) {
                        if (var8 > 0 && var8 < var5 - 1) {
                            var10 = var4.translate(var6[var9][var8 - 1], 0, var2[var9][var8].getY() - var2[var9][var8 - 1].getY());
                            var6[var9][var8] = new Point(var10.getX(), var10.getY());
                            canvas.drawCross(var6[var9][var8], 267946120);
                        }
                    } else {
                        var11 = var4.translate(var6[var9 - 1][var8], var2[var9][var8].getX() - var2[var9 - 1][var8].getX(), 0);
                        Point var12 = var4.translate(var6[var9][var8 - 1], 0, var2[var9][var8].getY() - var2[var9][var8 - 1].getY());
                        var6[var9][var8] = new Point((var11.getX() + var12.getX()) / 2, (var11.getY() + var12.getY()) / 2 + 1);
                    }

                    if (var1.getVersion() > 1) {
                        var11 = getPrecisionCenter(var0, var6[var9][var8]);
                        canvas.drawCross(var6[var9][var8], 267946120);
                        int var14 = var11.getX() - var6[var9][var8].getX();
                        int var13 = var11.getY() - var6[var9][var8].getY();
                        canvas.println("Adjust AP(" + var9 + "," + var8 + ") to d(" + var14 + "," + var13 + ")");
                        var6[var9][var8] = var11;
                    }

                    canvas.drawCross(var6[var9][var8], 8947967);
                    canvas.drawLine(new Line(var7, var6[var9][var8]), 12303359);
                    var7 = var6[var9][var8];
                }
            }
        }

        return var6;
    }

    static Point getPrecisionCenter(boolean[][] var0, Point var1) throws AlignmentPatternNotFoundException {
        int var2 = var1.getX();
        int var3 = var1.getY();
        if (var2 >= 0 && var3 >= 0 && var2 <= var0.length - 1 && var3 <= var0[0].length - 1) {
            int var4;
            int var6;
            int var7;
            int var8;
            int var9;
            if (!var0[var1.getX()][var1.getY()]) {
                var4 = 0;
                boolean var5 = false;

                while(!var5) {
                    ++var4;

                    for(var6 = var4; var6 > -var4; --var6) {
                        for(var7 = var4; var7 > -var4; --var7) {
                            var8 = var1.getX() + var7;
                            var9 = var1.getY() + var6;
                            if (var8 < 0 || var9 < 0 || var8 > var0.length - 1 || var9 > var0[0].length - 1) {
                                throw new AlignmentPatternNotFoundException("Alignment Pattern finder exceeded out of image");
                            }

                            if (var0[var8][var9]) {
                                var1 = new Point(var1.getX() + var7, var1.getY() + var6);
                                canvas.drawPoint(var1, 267946120);
                                var5 = true;
                            }
                        }
                    }
                }
            }

            int var10;
            var4 = var10 = var6 = var1.getX();

            for(var7 = var8 = var9 = var1.getY(); var10 >= 1 && !targetPointOnTheCorner(var0, var10, var7, var10 - 1, var7); --var10) {
                ;
            }

            while(var6 < var0.length - 1 && !targetPointOnTheCorner(var0, var6, var7, var6 + 1, var7)) {
                ++var6;
            }

            while(var8 >= 1 && !targetPointOnTheCorner(var0, var4, var8, var4, var8 - 1)) {
                --var8;
            }

            while(var9 < var0[0].length - 1 && !targetPointOnTheCorner(var0, var4, var9, var4, var9 + 1)) {
                ++var9;
            }

            return new Point((var10 + var6 + 1) / 2, (var8 + var9 + 1) / 2);
        } else {
            throw new AlignmentPatternNotFoundException("Alignment Pattern finder exceeded out of image");
        }
    }

    static boolean targetPointOnTheCorner(boolean[][] var0, int var1, int var2, int var3, int var4) {
        if (var1 >= 0 && var2 >= 0 && var3 >= 0 && var4 >= 0 && var1 <= var0.length && var2 <= var0[0].length && var3 <= var0.length && var4 <= var0[0].length) {
            return !var0[var1][var2] && var0[var3][var4];
        } else {
            throw new AlignmentPatternNotFoundException("Alignment Pattern Finder exceeded image edge");
        }
    }

    public static Point[][] getLogicalCenter(FinderPattern var0) {
        int var1 = var0.getVersion();
        Point[][] var2 = new Point[1][1];
        int[] var3 = new int[1];
        var3 = LogicalSeed.getSeed(var1);
        var2 = new Point[var3.length][var3.length];

        for(int var4 = 0; var4 < var2.length; ++var4) {
            for(int var5 = 0; var5 < var2.length; ++var5) {
                var2[var5][var4] = new Point(var3[var5], var3[var4]);
            }
        }

        return var2;
    }
}
