//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.ecc;

public class BCH15_5 {
    int[][] gf16 = this.createGF16();
    boolean[] receiveData;
    int numCorrectedError;
    static String[] bitName = new String[]{"c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "d0", "d1", "d2", "d3", "d4"};

    public BCH15_5(boolean[] var1) {
        this.receiveData = var1;
    }

    public boolean[] correct() {
        int[] var1 = this.calcSyndrome(this.receiveData);
        int[] var2 = this.detectErrorBitPosition(var1);
        boolean[] var3 = this.correctErrorBit(this.receiveData, var2);
        return var3;
    }

    int[][] createGF16() {
        this.gf16 = new int[16][4];
        int[] var1 = new int[]{1, 1, 0, 0};

        int var2;
        for(var2 = 0; var2 < 4; ++var2) {
            this.gf16[var2][var2] = 1;
        }

        for(var2 = 0; var2 < 4; ++var2) {
            this.gf16[4][var2] = var1[var2];
        }

        for(var2 = 5; var2 < 16; ++var2) {
            int var3;
            for(var3 = 1; var3 < 4; ++var3) {
                this.gf16[var2][var3] = this.gf16[var2 - 1][var3 - 1];
            }

            if (this.gf16[var2 - 1][3] == 1) {
                for(var3 = 0; var3 < 4; ++var3) {
                    this.gf16[var2][var3] = (this.gf16[var2][var3] + var1[var3]) % 2;
                }
            }
        }

        return this.gf16;
    }

    int searchElement(int[] var1) {
        int var2;
        for(var2 = 0; var2 < 15 && (var1[0] != this.gf16[var2][0] || var1[1] != this.gf16[var2][1] || var1[2] != this.gf16[var2][2] || var1[3] != this.gf16[var2][3]); ++var2) {
            ;
        }

        return var2;
    }

    int[] getCode(int var1) {
        int[] var2 = new int[15];
        int[] var3 = new int[8];

        for(int var4 = 0; var4 < 15; ++var4) {
            int var5 = var3[7];
            int var6;
            int var7;
            if (var4 < 7) {
                var7 = (var1 >> 6 - var4) % 2;
                var6 = (var7 + var5) % 2;
            } else {
                var7 = var5;
                var6 = 0;
            }

            var3[7] = (var3[6] + var6) % 2;
            var3[6] = (var3[5] + var6) % 2;
            var3[5] = var3[4];
            var3[4] = (var3[3] + var6) % 2;
            var3[3] = var3[2];
            var3[2] = var3[1];
            var3[1] = var3[0];
            var3[0] = var6;
            var2[14 - var4] = var7;
        }

        return var2;
    }

    int addGF(int var1, int var2) {
        int[] var3 = new int[4];

        for(int var4 = 0; var4 < 4; ++var4) {
            int var5 = var1 >= 0 && var1 < 15 ? this.gf16[var1][var4] : 0;
            int var6 = var2 >= 0 && var2 < 15 ? this.gf16[var2][var4] : 0;
            var3[var4] = (var5 + var6) % 2;
        }

        return this.searchElement(var3);
    }

    int[] calcSyndrome(boolean[] var1) {
        int[] var2 = new int[5];
        int[] var3 = new int[4];

        int var4;
        int var5;
        for(var4 = 0; var4 < 15; ++var4) {
            if (var1[var4]) {
                for(var5 = 0; var5 < 4; ++var5) {
                    var3[var5] = (var3[var5] + this.gf16[var4][var5]) % 2;
                }
            }
        }

        var4 = this.searchElement(var3);
        var2[0] = var4 >= 15 ? -1 : var4;
        var2[1] = var2[0] < 0 ? -1 : var2[0] * 2 % 15;
        var3 = new int[4];

        for(var4 = 0; var4 < 15; ++var4) {
            if (var1[var4]) {
                for(var5 = 0; var5 < 4; ++var5) {
                    var3[var5] = (var3[var5] + this.gf16[var4 * 3 % 15][var5]) % 2;
                }
            }
        }

        var4 = this.searchElement(var3);
        var2[2] = var4 >= 15 ? -1 : var4;
        var2[3] = var2[1] < 0 ? -1 : var2[1] * 2 % 15;
        var3 = new int[4];

        for(var4 = 0; var4 < 15; ++var4) {
            if (var1[var4]) {
                for(var5 = 0; var5 < 4; ++var5) {
                    var3[var5] = (var3[var5] + this.gf16[var4 * 5 % 15][var5]) % 2;
                }
            }
        }

        var4 = this.searchElement(var3);
        var2[4] = var4 >= 15 ? -1 : var4;
        return var2;
    }

    int[] calcErrorPositionVariable(int[] var1) {
        int[] var2 = new int[4];
        var2[0] = var1[0];
        int var3 = (var1[0] + var1[1]) % 15;
        int var4 = this.addGF(var1[2], var3);
        var4 = var4 >= 15 ? -1 : var4;
        var3 = (var1[2] + var1[1]) % 15;
        int var5 = this.addGF(var1[4], var3);
        var5 = var5 >= 15 ? -1 : var5;
        var2[1] = var5 < 0 && var4 < 0 ? -1 : (var5 - var4 + 15) % 15;
        var3 = (var1[1] + var2[0]) % 15;
        int var6 = this.addGF(var1[2], var3);
        var3 = (var1[0] + var2[1]) % 15;
        var2[2] = this.addGF(var6, var3);
        return var2;
    }

    int[] detectErrorBitPosition(int[] var1) {
        int[] var2 = this.calcErrorPositionVariable(var1);
        int[] var3 = new int[4];
        if (var2[0] == -1) {
            return var3;
        } else if (var2[1] == -1) {
            var3[0] = 1;
            var3[1] = var2[0];
            return var3;
        } else {
            for(int var11 = 0; var11 < 15; ++var11) {
                int var4 = var11 * 3 % 15;
                int var5 = var11 * 2 % 15;
                int var7 = (var2[0] + var5) % 15;
                int var8 = this.addGF(var4, var7);
                var7 = (var2[1] + var11) % 15;
                int var9 = this.addGF(var7, var2[2]);
                int var10 = this.addGF(var8, var9);
                if (var10 >= 15) {
                    ++var3[0];
                    var3[var3[0]] = var11;
                }
            }

            return var3;
        }
    }

    boolean[] correctErrorBit(boolean[] var1, int[] var2) {
        for(int var3 = 1; var3 <= var2[0]; ++var3) {
            var1[var2[var3]] = !var1[var2[var3]];
        }

        this.numCorrectedError = var2[0];
        return var1;
    }

    public int getNumCorrectedError() {
        return this.numCorrectedError;
    }
}
