//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.reedsolomon;

import java.util.Arrays;

public class RsDecode {
    public static final int RS_PERM_ERROR = -1;
    public static final int RS_CORRECT_ERROR = -2;
    private static final Galois galois = Galois.getInstance();
    private int npar;

    public RsDecode(int var1) {
        this.npar = var1;
    }

    public int calcSigmaMBM(int[] var1, int[] var2, int[] var3) {
        int[] var4 = new int[this.npar];
        int[] var5 = new int[this.npar];
        var4[1] = 1;
        var5[0] = 1;
        int var6 = 1;
        int var7 = 0;
        int var8 = -1;

        for(int var9 = 0; var9 < this.npar; ++var9) {
            int var10 = var3[var9];

            int var11;
            for(var11 = 1; var11 <= var7; ++var11) {
                var10 ^= galois.mul(var5[var11], var3[var9 - var11]);
            }

            if (var10 != 0) {
                var11 = galois.toLog(var10);
                int[] var12 = new int[this.npar];

                int var13;
                for(var13 = 0; var13 <= var9; ++var13) {
                    var12[var13] = var5[var13] ^ galois.mulExp(var4[var13], var11);
                }

                var13 = var9 - var8;
                if (var13 > var7) {
                    var8 = var9 - var7;
                    var7 = var13;
                    if (var13 > this.npar / 2) {
                        return -1;
                    }

                    for(int var14 = 0; var14 <= var6; ++var14) {
                        var4[var14] = galois.divExp(var5[var14], var11);
                    }

                    var6 = var13;
                }

                var5 = var12;
            }

            System.arraycopy(var4, 0, var4, 1, Math.min(var4.length - 1, var6));
            var4[0] = 0;
            ++var6;
        }

        galois.mulPoly(var2, var5, var3);
        System.arraycopy(var5, 0, var1, 0, Math.min(var5.length, var1.length));
        return var7;
    }

    private int chienSearch(int[] var1, int var2, int var3, int[] var4) {
        int var5 = var4[1];
        if (var3 == 1) {
            if (galois.toLog(var5) >= var2) {
                return -2;
            } else {
                var1[0] = var5;
                return 0;
            }
        } else {
            int var6 = var3 - 1;

            for(int var7 = 0; var7 < var2; ++var7) {
                int var8 = 255 - var7;
                int var9 = 1;

                int var10;
                for(var10 = 1; var10 <= var3; ++var10) {
                    var9 ^= galois.mulExp(var4[var10], var8 * var10 % 255);
                }

                if (var9 == 0) {
                    var10 = galois.toExp(var7);
                    var5 ^= var10;
                    var1[var6--] = var10;
                    if (var6 == 0) {
                        if (galois.toLog(var5) >= var2) {
                            return -2;
                        }

                        var1[0] = var5;
                        return 0;
                    }
                }
            }

            return -2;
        }
    }

    private void doForney(int[] var1, int var2, int var3, int[] var4, int[] var5, int[] var6) {
        for(int var7 = 0; var7 < var3; ++var7) {
            int var8 = var4[var7];
            int var9 = 255 - galois.toLog(var8);
            int var10 = var6[0];

            int var11;
            for(var11 = 1; var11 < var3; ++var11) {
                var10 ^= galois.mulExp(var6[var11], var9 * var11 % 255);
            }

            var11 = var5[1];

            for(int var12 = 2; var12 < var3; var12 += 2) {
                var11 ^= galois.mulExp(var5[var12 + 1], var9 * var12 % 255);
            }

            int var10001 = galois.toPos(var2, var8);
            var1[var10001] ^= galois.mul(var8, galois.div(var10, var11));
        }

    }

    public int decode(int[] var1, int var2, boolean var3) {
        if (var2 >= this.npar && var2 <= 255) {
            int[] var4 = new int[this.npar];
            if (galois.calcSyndrome(var1, var2, var4)) {
                return 0;
            } else {
                int[] var5 = new int[this.npar / 2 + 2];
                int[] var6 = new int[this.npar / 2 + 1];
                int var7 = this.calcSigmaMBM(var5, var6, var4);
                if (var7 <= 0) {
                    return -2;
                } else {
                    int[] var8 = new int[var7];
                    int var9 = this.chienSearch(var8, var2, var7, var5);
                    if (var9 < 0) {
                        return var9;
                    } else {
                        if (!var3) {
                            this.doForney(var1, var2, var7, var8, var5, var6);
                        }

                        return var7;
                    }
                }
            }
        } else {
            return -1;
        }
    }

    public int decode(int[] var1, int var2) {
        return this.decode(var1, var2, false);
    }

    public int decode(int[] var1) {
        return this.decode(var1, var1.length, false);
    }

//    public static void main(String[] var0) {
//        int[] var1 = new int[]{32, 65, 205, 69, 41, 220, 46, 128, 236, 42, 159, 74, 221, 244, 169, 239, 150, 138, 70, 237, 85, 224, 96, 74, 219, 61};
//        RsDecode var2 = new RsDecode(17);
//        int var3 = var2.decode(var1);
//        System.out.println("r=" + var3);
//
//        for(int var4 = 0; var4 < 8; ++var4) {
//            var1[var4] ^= (int)(Math.random() * 256.0D);
//        }
//
//        System.out.println(Arrays.toString(var1));
//        var3 = var2.decode(var1);
//        System.out.println("r=" + var3);
//        System.out.println(Arrays.toString(var1));
//    }
}
