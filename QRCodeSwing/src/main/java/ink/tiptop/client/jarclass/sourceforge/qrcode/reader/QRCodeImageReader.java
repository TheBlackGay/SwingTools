//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.reader;

import java.util.Vector;

import ink.tiptop.client.jarclass.sourceforge.qrcode.QRCodeDecoder;
import ink.tiptop.client.jarclass.sourceforge.qrcode.data.QRCodeSymbol;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.*;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Axis;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Line;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Point;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.SamplingGrid;
import ink.tiptop.client.jarclass.sourceforge.qrcode.pattern.AlignmentPattern;
import ink.tiptop.client.jarclass.sourceforge.qrcode.pattern.FinderPattern;
import ink.tiptop.client.jarclass.sourceforge.qrcode.util.DebugCanvas;
import ink.tiptop.client.jarclass.sourceforge.qrcode.util.QRCodeUtility;

public class QRCodeImageReader {
    DebugCanvas canvas = QRCodeDecoder.getCanvas();
    public static int DECIMAL_POINT = 21;
    public static final boolean POINT_DARK = true;
    public static final boolean POINT_LIGHT = false;
    SamplingGrid samplingGrid;
    boolean[][] bitmap;

    public QRCodeImageReader() {
    }

    boolean[][] applyMedianFilter(boolean[][] var1, int var2) {
        boolean[][] var3 = new boolean[var1.length][var1[0].length];

        for(int var5 = 1; var5 < var1[0].length - 1; ++var5) {
            for(int var6 = 1; var6 < var1.length - 1; ++var6) {
                int var4 = 0;

                for(int var7 = -1; var7 < 2; ++var7) {
                    for(int var8 = -1; var8 < 2; ++var8) {
                        if (var1[var6 + var8][var5 + var7]) {
                            ++var4;
                        }
                    }
                }

                if (var4 > var2) {
                    var3[var6][var5] = true;
                }
            }
        }

        return var3;
    }

    boolean[][] applyCrossMaskingMedianFilter(boolean[][] var1, int var2) {
        boolean[][] var3 = new boolean[var1.length][var1[0].length];

        for(int var5 = 2; var5 < var1[0].length - 2; ++var5) {
            for(int var6 = 2; var6 < var1.length - 2; ++var6) {
                int var4 = 0;

                for(int var7 = -2; var7 < 3; ++var7) {
                    if (var1[var6 + var7][var5]) {
                        ++var4;
                    }

                    if (var1[var6][var5 + var7]) {
                        ++var4;
                    }
                }

                if (var4 > var2) {
                    var3[var6][var5] = true;
                }
            }
        }

        return var3;
    }

    boolean[][] filterImage(int[][] var1) {
        this.imageToGrayScale(var1);
        boolean[][] var2 = this.grayScaleToBitmap(var1);
        return var2;
    }

    void imageToGrayScale(int[][] var1) {
        for(int var2 = 0; var2 < var1[0].length; ++var2) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
                int var4 = var1[var3][var2] >> 16 & 255;
                int var5 = var1[var3][var2] >> 8 & 255;
                int var6 = var1[var3][var2] & 255;
                int var7 = (var4 * 30 + var5 * 59 + var6 * 11) / 100;
                var1[var3][var2] = var7;
            }
        }

    }

    boolean[][] grayScaleToBitmap(int[][] var1) {
        int[][] var2 = this.getMiddleBrightnessPerArea(var1);
        int var3 = var2.length;
        int var4 = var1.length / var3;
        int var5 = var1[0].length / var3;
        boolean[][] var6 = new boolean[var1.length][var1[0].length];

        for(int var7 = 0; var7 < var3; ++var7) {
            for(int var8 = 0; var8 < var3; ++var8) {
                for(int var9 = 0; var9 < var5; ++var9) {
                    for(int var10 = 0; var10 < var4; ++var10) {
                        var6[var4 * var8 + var10][var5 * var7 + var9] = var1[var4 * var8 + var10][var5 * var7 + var9] < var2[var8][var7];
                    }
                }
            }
        }

        return var6;
    }

    int[][] getMiddleBrightnessPerArea(int[][] var1) {
        int var3 = var1.length / 4;
        int var4 = var1[0].length / 4;
        int[][][] var5 = new int[4][4][2];

        int var7;
        int var8;
        for(int var6 = 0; var6 < 4; ++var6) {
            for(var7 = 0; var7 < 4; ++var7) {
                var5[var7][var6][0] = 255;

                for(var8 = 0; var8 < var4; ++var8) {
                    for(int var9 = 0; var9 < var3; ++var9) {
                        int var10 = var1[var3 * var7 + var9][var4 * var6 + var8];
                        if (var10 < var5[var7][var6][0]) {
                            var5[var7][var6][0] = var10;
                        }

                        if (var10 > var5[var7][var6][1]) {
                            var5[var7][var6][1] = var10;
                        }
                    }
                }
            }
        }

        int[][] var11 = new int[4][4];

        for(var7 = 0; var7 < 4; ++var7) {
            for(var8 = 0; var8 < 4; ++var8) {
                var11[var8][var7] = (var5[var8][var7][0] + var5[var8][var7][1]) / 2;
            }
        }

        return var11;
    }

    public QRCodeSymbol getQRCodeSymbol(int[][] var1) throws SymbolNotFoundException {
        int var2 = var1.length < var1[0].length ? var1[0].length : var1.length;
        DECIMAL_POINT = 23 - QRCodeUtility.sqrt(var2 / 256);
        this.bitmap = this.filterImage(var1);
        this.canvas.println("Drawing matrix.");
        this.canvas.drawMatrix(this.bitmap);
        this.canvas.println("Scanning Finder Pattern.");
        FinderPattern var3 = null;

        try {
            var3 = FinderPattern.findFinderPattern(this.bitmap);
        } catch (FinderPatternNotFoundException var16) {
            this.canvas.println("Not found, now retrying...");
            this.bitmap = this.applyCrossMaskingMedianFilter(this.bitmap, 5);
            this.canvas.drawMatrix(this.bitmap);

            try {
                var3 = FinderPattern.findFinderPattern(this.bitmap);
            } catch (FinderPatternNotFoundException var14) {
                throw new SymbolNotFoundException(var14.getMessage());
            } catch (VersionInformationException var15) {
                throw new SymbolNotFoundException(var15.getMessage());
            }
        } catch (VersionInformationException var17) {
            throw new SymbolNotFoundException(var17.getMessage());
        }

        this.canvas.println("FinderPattern at");
        String var4 = var3.getCenter(0).toString() + var3.getCenter(1).toString() + var3.getCenter(2).toString();
        this.canvas.println(var4);
        int[] var5 = var3.getAngle();
        this.canvas.println("Angle*4098: Sin " + Integer.toString(var5[0]) + "  " + "Cos " + Integer.toString(var5[1]));
        int var6 = var3.getVersion();
        this.canvas.println("Version: " + Integer.toString(var6));
        if (var6 >= 1 && var6 <= 40) {
            AlignmentPattern var7 = null;

            try {
                var7 = AlignmentPattern.findAlignmentPattern(this.bitmap, var3);
            } catch (AlignmentPatternNotFoundException var13) {
                throw new SymbolNotFoundException(var13.getMessage());
            }

            int var8 = var7.getCenter().length;
            this.canvas.println("AlignmentPatterns at");

            for(int var9 = 0; var9 < var8; ++var9) {
                String var10 = "";

                for(int var11 = 0; var11 < var8; ++var11) {
                    var10 = var10 + var7.getCenter()[var11][var9].toString();
                }

                this.canvas.println(var10);
            }

            this.canvas.println("Creating sampling grid.");
            this.samplingGrid = this.getSamplingGrid(var3, var7);
            this.canvas.println("Reading grid.");
            boolean[][] var18 = (boolean[][])null;

            try {
                var18 = this.getQRCodeMatrix(this.bitmap, this.samplingGrid);
            } catch (ArrayIndexOutOfBoundsException var12) {
                throw new SymbolNotFoundException("Sampling grid exceeded image boundary");
            }

            return new QRCodeSymbol(var18);
        } else {
            throw new InvalidVersionException("Invalid version: " + var6);
        }
    }

    public QRCodeSymbol getQRCodeSymbolWithAdjustedGrid(Point var1) throws IllegalStateException, SymbolNotFoundException {
        if (this.bitmap != null && this.samplingGrid != null) {
            this.samplingGrid.adjust(var1);
            this.canvas.println("Sampling grid adjusted d(" + var1.getX() + "," + var1.getY() + ")");
            boolean[][] var2 = (boolean[][])null;

            try {
                var2 = this.getQRCodeMatrix(this.bitmap, this.samplingGrid);
            } catch (ArrayIndexOutOfBoundsException var4) {
                throw new SymbolNotFoundException("Sampling grid exceeded image boundary");
            }

            return new QRCodeSymbol(var2);
        } else {
            throw new IllegalStateException("This method must be called after QRCodeImageReader.getQRCodeSymbol() called");
        }
    }

    SamplingGrid getSamplingGrid(FinderPattern var1, AlignmentPattern var2) {
        Point[][] var3 = var2.getCenter();
        int var4 = var1.getVersion();
        int var5 = var4 / 7 + 2;
        var3[0][0] = var1.getCenter(0);
        var3[var5 - 1][0] = var1.getCenter(1);
        var3[0][var5 - 1] = var1.getCenter(2);
        int var6 = var5 - 1;
        SamplingGrid var7 = new SamplingGrid(var6);
        Axis var12 = new Axis(var1.getAngle(), var1.getModuleSize());

        for(int var14 = 0; var14 < var6; ++var14) {
            for(int var15 = 0; var15 < var6; ++var15) {
                QRCodeImageReader.ModulePitch var13 = new QRCodeImageReader.ModulePitch();
                Line var8 = new Line();
                Line var9 = new Line();
                var12.setModulePitch(var1.getModuleSize());
                Point[][] var16 = AlignmentPattern.getLogicalCenter(var1);
                Point var17 = var3[var15][var14];
                Point var18 = var3[var15 + 1][var14];
                Point var19 = var3[var15][var14 + 1];
                Point var20 = var3[var15 + 1][var14 + 1];
                Point var21 = var16[var15][var14];
                Point var22 = var16[var15 + 1][var14];
                Point var23 = var16[var15][var14 + 1];
                Point var24 = var16[var15 + 1][var14 + 1];
                if (var15 == 0 && var14 == 0) {
                    if (var6 == 1) {
                        var17 = var12.translate(var17, -3, -3);
                        var18 = var12.translate(var18, 3, -3);
                        var19 = var12.translate(var19, -3, 3);
                        var20 = var12.translate(var20, 6, 6);
                        var21.translate(-6, -6);
                        var22.translate(3, -3);
                        var23.translate(-3, 3);
                        var24.translate(6, 6);
                    } else {
                        var17 = var12.translate(var17, -3, -3);
                        var18 = var12.translate(var18, 0, -6);
                        var19 = var12.translate(var19, -6, 0);
                        var21.translate(-6, -6);
                        var22.translate(0, -6);
                        var23.translate(-6, 0);
                    }
                } else if (var15 == 0 && var14 == var6 - 1) {
                    var17 = var12.translate(var17, -6, 0);
                    var19 = var12.translate(var19, -3, 3);
                    var20 = var12.translate(var20, 0, 6);
                    var21.translate(-6, 0);
                    var23.translate(-6, 6);
                    var24.translate(0, 6);
                } else if (var15 == var6 - 1 && var14 == 0) {
                    var17 = var12.translate(var17, 0, -6);
                    var18 = var12.translate(var18, 3, -3);
                    var20 = var12.translate(var20, 6, 0);
                    var21.translate(0, -6);
                    var22.translate(6, -6);
                    var24.translate(6, 0);
                } else if (var15 == var6 - 1 && var14 == var6 - 1) {
                    var19 = var12.translate(var19, 0, 6);
                    var18 = var12.translate(var18, 6, 0);
                    var20 = var12.translate(var20, 6, 6);
                    var23.translate(0, 6);
                    var22.translate(6, 0);
                    var24.translate(6, 6);
                } else if (var15 == 0) {
                    var17 = var12.translate(var17, -6, 0);
                    var19 = var12.translate(var19, -6, 0);
                    var21.translate(-6, 0);
                    var23.translate(-6, 0);
                } else if (var15 == var6 - 1) {
                    var18 = var12.translate(var18, 6, 0);
                    var20 = var12.translate(var20, 6, 0);
                    var22.translate(6, 0);
                    var24.translate(6, 0);
                } else if (var14 == 0) {
                    var17 = var12.translate(var17, 0, -6);
                    var18 = var12.translate(var18, 0, -6);
                    var21.translate(0, -6);
                    var22.translate(0, -6);
                } else if (var14 == var6 - 1) {
                    var19 = var12.translate(var19, 0, 6);
                    var20 = var12.translate(var20, 0, 6);
                    var23.translate(0, 6);
                    var24.translate(0, 6);
                }

                if (var15 == 0) {
                    var22.translate(1, 0);
                    var24.translate(1, 0);
                } else {
                    var21.translate(-1, 0);
                    var23.translate(-1, 0);
                }

                if (var14 == 0) {
                    var23.translate(0, 1);
                    var24.translate(0, 1);
                } else {
                    var21.translate(0, -1);
                    var22.translate(0, -1);
                }

                int var25 = var22.getX() - var21.getX();
                int var26 = var23.getY() - var21.getY();
                if (var4 < 7) {
                    var25 += 3;
                    var26 += 3;
                }

                var13.top = this.getAreaModulePitch(var17, var18, var25 - 1);
                var13.left = this.getAreaModulePitch(var17, var19, var26 - 1);
                var13.bottom = this.getAreaModulePitch(var19, var20, var25 - 1);
                var13.right = this.getAreaModulePitch(var18, var20, var26 - 1);
                var8.setP1(var17);
                var9.setP1(var17);
                var8.setP2(var19);
                var9.setP2(var18);
                var7.initGrid(var15, var14, var25, var26);

                int var27;
                for(var27 = 0; var27 < var25; ++var27) {
                    Line var10 = new Line(var8.getP1(), var8.getP2());
                    var12.setOrigin(var10.getP1());
                    var12.setModulePitch(var13.top);
                    var10.setP1(var12.translate(var27, 0));
                    var12.setOrigin(var10.getP2());
                    var12.setModulePitch(var13.bottom);
                    var10.setP2(var12.translate(var27, 0));
                    var7.setXLine(var15, var14, var27, var10);
                }

                for(var27 = 0; var27 < var26; ++var27) {
                    Line var11 = new Line(var9.getP1(), var9.getP2());
                    var12.setOrigin(var11.getP1());
                    var12.setModulePitch(var13.left);
                    var11.setP1(var12.translate(0, var27));
                    var12.setOrigin(var11.getP2());
                    var12.setModulePitch(var13.right);
                    var11.setP2(var12.translate(0, var27));
                    var7.setYLine(var15, var14, var27, var11);
                }
            }
        }

        return var7;
    }

    int getAreaModulePitch(Point var1, Point var2, int var3) {
        Line var4 = new Line(var1, var2);
        int var5 = var4.getLength();
        int var6 = (var5 << DECIMAL_POINT) / var3;
        return var6;
    }

    boolean[][] getQRCodeMatrix(boolean[][] var1, SamplingGrid var2) throws ArrayIndexOutOfBoundsException {
        int var3 = var2.getTotalWidth();
        this.canvas.println("gridSize=" + var3);
        Point var4 = null;
        boolean[][] var5 = new boolean[var3][var3];

        for(int var6 = 0; var6 < var2.getHeight(); ++var6) {
            for(int var7 = 0; var7 < var2.getWidth(); ++var7) {
                new Vector();

                for(int var9 = 0; var9 < var2.getHeight(var7, var6); ++var9) {
                    for(int var10 = 0; var10 < var2.getWidth(var7, var6); ++var10) {
                        int var11 = var2.getXLine(var7, var6, var10).getP1().getX();
                        int var12 = var2.getXLine(var7, var6, var10).getP1().getY();
                        int var13 = var2.getXLine(var7, var6, var10).getP2().getX();
                        int var14 = var2.getXLine(var7, var6, var10).getP2().getY();
                        int var15 = var2.getYLine(var7, var6, var9).getP1().getX();
                        int var16 = var2.getYLine(var7, var6, var9).getP1().getY();
                        int var17 = var2.getYLine(var7, var6, var9).getP2().getX();
                        int var18 = var2.getYLine(var7, var6, var9).getP2().getY();
                        int var19 = (var14 - var12) * (var15 - var17) - (var18 - var16) * (var11 - var13);
                        int var20 = (var11 * var14 - var13 * var12) * (var15 - var17) - (var15 * var18 - var17 * var16) * (var11 - var13);
                        int var21 = (var15 * var18 - var17 * var16) * (var14 - var12) - (var11 * var14 - var13 * var12) * (var18 - var16);
                        var5[var2.getX(var7, var10)][var2.getY(var6, var9)] = var1[var20 / var19][var21 / var19];
                        if (var6 == var2.getHeight() - 1 && var7 == var2.getWidth() - 1 && var9 == var2.getHeight(var7, var6) - 1 && var10 == var2.getWidth(var7, var6) - 1) {
                            var4 = new Point(var20 / var19, var21 / var19);
                        }
                    }
                }
            }
        }

        if (var4 == null || var4.getX() <= var1.length - 1 && var4.getY() <= var1[0].length - 1) {
            this.canvas.drawPoint(var4, 8947967);
            return var5;
        } else {
            throw new ArrayIndexOutOfBoundsException("Sampling grid pointed out of image");
        }
    }

    protected class ModulePitch {
        public int top;
        public int left;
        public int bottom;
        public int right;

        protected ModulePitch() {
        }
    }
}
