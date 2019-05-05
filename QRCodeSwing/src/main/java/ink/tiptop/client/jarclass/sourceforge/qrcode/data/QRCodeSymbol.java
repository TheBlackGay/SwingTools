//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.data;

import ink.tiptop.client.jarclass.sourceforge.qrcode.ecc.BCH15_5;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Point;
import ink.tiptop.client.jarclass.sourceforge.qrcode.pattern.LogicalSeed;

import java.util.Vector;

public class QRCodeSymbol {
    final int[][] numErrorCollectionCode = new int[][]{{7, 10, 13, 17}, {10, 16, 22, 28}, {15, 26, 36, 44}, {20, 36, 52, 64}, {26, 48, 72, 88}, {36, 64, 96, 112}, {40, 72, 108, 130}, {48, 88, 132, 156}, {60, 110, 160, 192}, {72, 130, 192, 224}, {80, 150, 224, 264}, {96, 176, 260, 308}, {104, 198, 288, 352}, {120, 216, 320, 384}, {132, 240, 360, 432}, {144, 280, 408, 480}, {168, 308, 448, 532}, {180, 338, 504, 588}, {196, 364, 546, 650}, {224, 416, 600, 700}, {224, 442, 644, 750}, {252, 476, 690, 816}, {270, 504, 750, 900}, {300, 560, 810, 960}, {312, 588, 870, 1050}, {336, 644, 952, 1110}, {360, 700, 1020, 1200}, {390, 728, 1050, 1260}, {420, 784, 1140, 1350}, {450, 812, 1200, 1440}, {480, 868, 1290, 1530}, {510, 924, 1350, 1620}, {540, 980, 1440, 1710}, {570, 1036, 1530, 1800}, {570, 1064, 1590, 1890}, {600, 1120, 1680, 1980}, {630, 1204, 1770, 2100}, {660, 1260, 1860, 2220}, {720, 1316, 1950, 2310}, {750, 1372, 2040, 2430}};
    final int[][] numRSBlocks = new int[][]{{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 2, 2}, {1, 2, 2, 4}, {1, 2, 4, 4}, {2, 4, 4, 4}, {2, 4, 6, 5}, {2, 4, 6, 6}, {2, 5, 8, 8}, {4, 5, 8, 8}, {4, 5, 8, 11}, {4, 8, 10, 11}, {4, 9, 12, 16}, {4, 9, 16, 16}, {6, 10, 12, 18}, {6, 10, 17, 16}, {6, 11, 16, 19}, {6, 13, 18, 21}, {7, 14, 21, 25}, {8, 16, 20, 25}, {8, 17, 23, 25}, {9, 17, 23, 34}, {9, 18, 25, 30}, {10, 20, 27, 32}, {12, 21, 29, 35}, {12, 23, 34, 37}, {12, 25, 34, 40}, {13, 26, 35, 42}, {14, 28, 38, 45}, {15, 29, 40, 48}, {16, 31, 43, 51}, {17, 33, 45, 54}, {18, 35, 48, 57}, {19, 37, 51, 60}, {19, 38, 53, 63}, {20, 40, 56, 66}, {21, 43, 59, 70}, {22, 45, 62, 74}, {24, 47, 65, 77}, {25, 49, 68, 81}};
    int version;
    int errorCollectionLevel;
    int maskPattern;
    int dataCapacity;
    boolean[][] moduleMatrix;
    int width;
    int height;
    Point[][] alignmentPattern;

    public QRCodeSymbol(boolean[][] var1) {
        this.moduleMatrix = var1;
        this.width = var1.length;
        this.height = var1[0].length;
        this.initialize();
    }

    public boolean getElement(int var1, int var2) {
        return this.moduleMatrix[var1][var2];
    }

    public int getNumErrorCollectionCode() {
        return this.numErrorCollectionCode[this.version - 1][this.errorCollectionLevel];
    }

    public int getNumRSBlocks() {
        return this.numRSBlocks[this.version - 1][this.errorCollectionLevel];
    }

    void initialize() {
        this.version = (this.width - 17) / 4;
        Point[][] var1 = new Point[1][1];
        int[] var2 = new int[1];
        if (this.version >= 2 && this.version <= 40) {
            var2 = LogicalSeed.getSeed(this.version);
            var1 = new Point[var2.length][var2.length];
        }

        for (int var3 = 0; var3 < var2.length; ++var3) {
            for (int var4 = 0; var4 < var2.length; ++var4) {
                var1[var4][var3] = new Point(var2[var4], var2[var3]);
            }
        }

        this.alignmentPattern = var1;
        this.dataCapacity = this.calcDataCapacity();
        boolean[] var5 = this.readFormatInformation();
        this.decodeFormatInformation(var5);
        this.unmask();
    }

    public int getVersion() {
        return this.version;
    }

    public String getVersionReference() {
        char[] var1 = new char[]{'L', 'M', 'Q', 'H'};
        return Integer.toString(this.version) + "-" + var1[this.errorCollectionLevel];
    }

    public Point[][] getAlignmentPattern() {
        return this.alignmentPattern;
    }

    boolean[] readFormatInformation() {
        boolean[] var1 = new boolean[15];

        int var2;
        for (var2 = 0; var2 <= 5; ++var2) {
            var1[var2] = this.getElement(8, var2);
        }

        var1[6] = this.getElement(8, 7);
        var1[7] = this.getElement(8, 8);
        var1[8] = this.getElement(7, 8);

        for (var2 = 9; var2 <= 14; ++var2) {
            var1[var2] = this.getElement(14 - var2, 8);
        }

        short var7 = 21522;

        for (int var3 = 0; var3 <= 14; ++var3) {
            boolean var4 = false;
            if ((var7 >>> var3 & 1) == 1) {
                var4 = true;
            } else {
                var4 = false;
            }

            if (var1[var3] == var4) {
                var1[var3] = false;
            } else {
                var1[var3] = true;
            }
        }

        BCH15_5 var8 = new BCH15_5(var1);
        boolean[] var9 = var8.correct();
        boolean[] var5 = new boolean[5];

        for (int var6 = 0; var6 < 5; ++var6) {
            var5[var6] = var9[10 + var6];
        }

        return var5;
    }

    void unmask() {
        boolean[][] var1 = this.generateMaskPattern();
        int var2 = this.getWidth();

        for (int var3 = 0; var3 < var2; ++var3) {
            for (int var4 = 0; var4 < var2; ++var4) {
                if (var1[var4][var3]) {
                    this.reverseElement(var4, var3);
                }
            }
        }

    }

    boolean[][] generateMaskPattern() {
        int var1 = this.getMaskPatternReferer();
        int var2 = this.getWidth();
        int var3 = this.getHeight();
        boolean[][] var4 = new boolean[var2][var3];

        for (int var5 = 0; var5 < var3; ++var5) {
            for (int var6 = 0; var6 < var2; ++var6) {
                if (!this.isInFunctionPattern(var6, var5)) {
                    switch (var1) {
                        case 0:
                            if ((var5 + var6) % 2 == 0) {
                                var4[var6][var5] = true;
                            }
                            break;
                        case 1:
                            if (var5 % 2 == 0) {
                                var4[var6][var5] = true;
                            }
                            break;
                        case 2:
                            if (var6 % 3 == 0) {
                                var4[var6][var5] = true;
                            }
                            break;
                        case 3:
                            if ((var5 + var6) % 3 == 0) {
                                var4[var6][var5] = true;
                            }
                            break;
                        case 4:
                            if ((var5 / 2 + var6 / 3) % 2 == 0) {
                                var4[var6][var5] = true;
                            }
                            break;
                        case 5:
                            if (var5 * var6 % 2 + var5 * var6 % 3 == 0) {
                                var4[var6][var5] = true;
                            }
                            break;
                        case 6:
                            if ((var5 * var6 % 2 + var5 * var6 % 3) % 2 == 0) {
                                var4[var6][var5] = true;
                            }
                            break;
                        case 7:
                            if ((var5 * var6 % 3 + (var5 + var6) % 2) % 2 == 0) {
                                var4[var6][var5] = true;
                            }
                    }
                }
            }
        }

        return var4;
    }

    private int calcDataCapacity() {
        boolean var1 = false;
        boolean var2 = false;
        int var3 = this.getVersion();
        byte var8;
        if (var3 <= 6) {
            var8 = 31;
        } else {
            var8 = 67;
        }

        int var4 = var3 / 7 + 2;
        int var5 = var3 == 1 ? 192 : 192 + (var4 * var4 - 3) * 25;
        int var7 = var5 + 8 * var3 + 2 - (var4 - 2) * 10;
        int var6 = (this.width * this.width - var7 - var8) / 8;
        return var6;
    }

    public int getDataCapacity() {
        return this.dataCapacity;
    }

    void decodeFormatInformation(boolean[] var1) {
        if (!var1[4]) {
            if (var1[3]) {
                this.errorCollectionLevel = 0;
            } else {
                this.errorCollectionLevel = 1;
            }
        } else if (var1[3]) {
            this.errorCollectionLevel = 2;
        } else {
            this.errorCollectionLevel = 3;
        }

        for (int var2 = 2; var2 >= 0; --var2) {
            if (var1[var2]) {
                this.maskPattern += 1 << var2;
            }
        }

    }

    public int getErrorCollectionLevel() {
        return this.errorCollectionLevel;
    }

    public int getMaskPatternReferer() {
        return this.maskPattern;
    }

    public String getMaskPatternRefererAsString() {
        String var1 = Integer.toString(this.getMaskPatternReferer(), 2);
        int var2 = var1.length();

        for (int var3 = 0; var3 < 3 - var2; ++var3) {
            var1 = "0" + var1;
        }

        return var1;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[] getBlocks() {
        int var1 = this.getWidth();
        int var2 = this.getHeight();
        int var3 = var1 - 1;
        int var4 = var2 - 1;
        Vector var5 = new Vector();
        Vector var6 = new Vector();
        int var7 = 0;
        int var8 = 7;
        byte var9 = 0;
        boolean var12 = true;

        do {
            var5.addElement(new Boolean(this.getElement(var3, var4)));
            if (this.getElement(var3, var4)) {
                var7 += 1 << var8;
            }

            --var8;
            if (var8 == -1) {
                var6.addElement(new Integer(var7));
                var8 = 7;
                var7 = 0;
            }

            do {
                if (var12) {
                    if ((var3 + var9) % 2 == 0) {
                        --var3;
                    } else if (var4 > 0) {
                        ++var3;
                        --var4;
                    } else {
                        --var3;
                        if (var3 == 6) {
                            --var3;
                            var9 = 1;
                        }

                        var12 = false;
                    }
                } else if ((var3 + var9) % 2 == 0) {
                    --var3;
                } else if (var4 < var2 - 1) {
                    ++var3;
                    ++var4;
                } else {
                    --var3;
                    if (var3 == 6) {
                        --var3;
                        var9 = 1;
                    }

                    var12 = true;
                }
            } while (this.isInFunctionPattern(var3, var4));
        } while (var3 != -1);

        int[] var13 = new int[var6.size()];

        for (int var14 = 0; var14 < var6.size(); ++var14) {
            Integer var15 = (Integer) var6.elementAt(var14);
            var13[var14] = var15;
        }

        return var13;
    }

    public void reverseElement(int var1, int var2) {
        this.moduleMatrix[var1][var2] = !this.moduleMatrix[var1][var2];
    }

    public boolean isInFunctionPattern(int var1, int var2) {
        if (var1 < 9 && var2 < 9) {
            return true;
        } else if (var1 > this.getWidth() - 9 && var2 < 9) {
            return true;
        } else if (var1 < 9 && var2 > this.getHeight() - 9) {
            return true;
        } else {
            if (this.version >= 7) {
                if (var1 > this.getWidth() - 12 && var2 < 6) {
                    return true;
                }

                if (var1 < 6 && var2 > this.getHeight() - 12) {
                    return true;
                }
            }

            if (var1 != 6 && var2 != 6) {
                Point[][] var3 = this.getAlignmentPattern();
                int var4 = var3.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    for (int var6 = 0; var6 < var4; ++var6) {
                        if ((var6 != 0 || var5 != 0) && (var6 != var4 - 1 || var5 != 0) && (var6 != 0 || var5 != var4 - 1) && Math.abs(var3[var6][var5].getX() - var1) < 3 && Math.abs(var3[var6][var5].getY() - var2) < 3) {
                            return true;
                        }
                    }
                }

                return false;
            } else {
                return true;
            }
        }
    }
}
