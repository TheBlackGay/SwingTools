//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.reedsolomon;

import java.util.Arrays;

public final class Galois {
    public static final int POLYNOMIAL = 29;
    private static final Galois instance = new Galois();
    private int[] expTbl = new int[510];
    private int[] logTbl = new int[256];

    private Galois() {
        this.initGaloisTable();
    }

    public static Galois getInstance() {
        return instance;
    }

    private void initGaloisTable() {
        int var1 = 1;

        for(int var2 = 0; var2 < 255; ++var2) {
            this.expTbl[var2] = this.expTbl[255 + var2] = var1;
            this.logTbl[var1] = var2;
            var1 <<= 1;
            if ((var1 & 256) != 0) {
                var1 = (var1 ^ 29) & 255;
            }
        }

    }

    public int toExp(int var1) {
        return this.expTbl[var1];
    }

    public int toLog(int var1) {
        return this.logTbl[var1];
    }

    public int toPos(int var1, int var2) {
        return var1 - 1 - this.logTbl[var2];
    }

    public int mul(int var1, int var2) {
        return var1 != 0 && var2 != 0 ? this.expTbl[this.logTbl[var1] + this.logTbl[var2]] : 0;
    }

    public int mulExp(int var1, int var2) {
        return var1 == 0 ? 0 : this.expTbl[this.logTbl[var1] + var2];
    }

    public int div(int var1, int var2) {
        return var1 == 0 ? 0 : this.expTbl[this.logTbl[var1] - this.logTbl[var2] + 255];
    }

    public int divExp(int var1, int var2) {
        return var1 == 0 ? 0 : this.expTbl[this.logTbl[var1] - var2 + 255];
    }

    public int inv(int var1) {
        return this.expTbl[255 - this.logTbl[var1]];
    }

    public void mulPoly(int[] var1, int[] var2, int[] var3) {
        Arrays.fill(var1, 0);

        for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] != 0) {
                int var5 = this.logTbl[var2[var4]];
                int var6 = Math.min(var3.length, var1.length - var4);

                for(int var7 = 0; var7 < var6; ++var7) {
                    if (var3[var7] != 0) {
                        var1[var4 + var7] ^= this.expTbl[var5 + this.logTbl[var3[var7]]];
                    }
                }
            }
        }

    }

    public boolean calcSyndrome(int[] var1, int var2, int[] var3) {
        int var4 = 0;

        for(int var5 = 0; var5 < var3.length; ++var5) {
            int var6 = 0;

            for(int var7 = 0; var7 < var2; ++var7) {
                var6 = var1[var7] ^ (var6 == 0 ? 0 : this.expTbl[this.logTbl[var6] + var5]);
            }

            var3[var5] = var6;
            var4 |= var6;
        }

        return var4 == 0;
    }
}
