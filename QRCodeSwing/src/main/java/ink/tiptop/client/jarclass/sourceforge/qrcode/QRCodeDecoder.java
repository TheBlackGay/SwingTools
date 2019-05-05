//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode;


import ink.tiptop.client.jarclass.sourceforge.qrcode.data.QRCodeImage;
import ink.tiptop.client.jarclass.sourceforge.qrcode.data.QRCodeSymbol;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.DecodingFailedException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.InvalidDataBlockException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.SymbolNotFoundException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Point;
import ink.tiptop.client.jarclass.sourceforge.qrcode.reader.QRCodeDataBlockReader;
import ink.tiptop.client.jarclass.sourceforge.qrcode.reader.QRCodeImageReader;
import ink.tiptop.client.jarclass.sourceforge.qrcode.util.DebugCanvas;
import ink.tiptop.client.jarclass.sourceforge.qrcode.util.DebugCanvasAdapter;
import ink.tiptop.client.jarclass.sourceforge.reedsolomon.RsDecode;

import java.util.Vector;

public class QRCodeDecoder {
    static DebugCanvas canvas;
    int numTryDecode = 0;
    QRCodeSymbol qrCodeSymbol;
    Vector results = new Vector();
    Vector lastResults = new Vector();
    QRCodeImageReader imageReader;
    int numLastCorrections;
    boolean correctionSucceeded = true;

    public QRCodeDecoder() {
        canvas = new DebugCanvasAdapter();
    }

    public static DebugCanvas getCanvas() {
        return canvas;
    }

    public static void setCanvas(DebugCanvas var0) {
        canvas = var0;
    }

    public byte[] decode(QRCodeImage var1) throws DecodingFailedException {
        Point[] var2 = this.getAdjustPoints();
        Vector var3 = new Vector();

        while (this.numTryDecode < var2.length) {
            try {
                QRCodeDecoder.DecodeResult var4 = this.decode(var1, var2[this.numTryDecode]);
                if (var4.isCorrectionSucceeded()) {
                    byte[] var5 = var4.getDecodedBytes();
                    return var5;
                }

                var3.addElement(var4);
                canvas.println("Decoding succeeded but could not correct");
                canvas.println("all errors. Retrying..");
            } catch (DecodingFailedException var10) {
                if (var10.getMessage().indexOf("Finder Pattern") >= 0) {
                    throw var10;
                }
            } finally {
                ++this.numTryDecode;
            }
        }

        if (var3.size() == 0) {
            throw new DecodingFailedException("Give up decoding");
        } else {
            int var12 = -1;
            int var13 = 2147483647;

            for (int var6 = 0; var6 < var3.size(); ++var6) {
                QRCodeDecoder.DecodeResult var7 = (QRCodeDecoder.DecodeResult) var3.elementAt(var6);
                if (var7.getNumErrors() < var13) {
                    var13 = var7.getNumErrors();
                    var12 = var6;
                }
            }

            canvas.println("All trials need for correct error");
            canvas.println("Reporting #" + var12 + " that,");
            canvas.println("corrected minimum errors (" + var13 + ")");
            canvas.println("Decoding finished.");
            return ((QRCodeDecoder.DecodeResult) var3.elementAt(var12)).getDecodedBytes();
        }
    }

    Point[] getAdjustPoints() {
        Vector var1 = new Vector();

        int var2;
        for (var2 = 0; var2 < 4; ++var2) {
            var1.addElement(new Point(1, 1));
        }

        var2 = 0;
        int var3 = 0;

        int var5;
        for (int var4 = 0; var4 > -4; --var4) {
            for (var5 = 0; var5 > -4; --var5) {
                if (var5 != var4 && (var5 + var4) % 2 == 0) {
                    var1.addElement(new Point(var5 - var2, var4 - var3));
                    var2 = var5;
                    var3 = var4;
                }
            }
        }

        Point[] var6 = new Point[var1.size()];

        for (var5 = 0; var5 < var6.length; ++var5) {
            var6[var5] = (Point) var1.elementAt(var5);
        }

        return var6;
    }

    QRCodeDecoder.DecodeResult decode(QRCodeImage var1, Point var2) throws DecodingFailedException {
        try {
            if (this.numTryDecode == 0) {
                canvas.println("Decoding started");
                int[][] var3 = this.imageToIntArray(var1);
                this.imageReader = new QRCodeImageReader();
                this.qrCodeSymbol = this.imageReader.getQRCodeSymbol(var3);
            } else {
                canvas.println("--");
                canvas.println("Decoding restarted #" + this.numTryDecode);
                this.qrCodeSymbol = this.imageReader.getQRCodeSymbolWithAdjustedGrid(var2);
            }
        } catch (SymbolNotFoundException var6) {
            throw new DecodingFailedException(var6.getMessage());
        }

        canvas.println("Created QRCode symbol.");
        canvas.println("Reading symbol.");
        canvas.println("Version: " + this.qrCodeSymbol.getVersionReference());
        canvas.println("Mask pattern: " + this.qrCodeSymbol.getMaskPatternRefererAsString());
        int[] var7 = this.qrCodeSymbol.getBlocks();
        canvas.println("Correcting data errors.");
        var7 = this.correctDataBlocks(var7);

        try {
            byte[] var4 = this.getDecodedByteArray(var7, this.qrCodeSymbol.getVersion(), this.qrCodeSymbol.getNumErrorCollectionCode());
            return new QRCodeDecoder.DecodeResult(var4, this.numLastCorrections, this.correctionSucceeded);
        } catch (InvalidDataBlockException var5) {
            canvas.println(var5.getMessage());
            throw new DecodingFailedException(var5.getMessage());
        }
    }

    int[][] imageToIntArray(QRCodeImage var1) {
        int var2 = var1.getWidth();
        int var3 = var1.getHeight();
        int[][] var4 = new int[var2][var3];

        for (int var5 = 0; var5 < var3; ++var5) {
            for (int var6 = 0; var6 < var2; ++var6) {
                var4[var6][var5] = var1.getPixel(var6, var5);
            }
        }

        return var4;
    }

    int[] correctDataBlocks(int[] var1) {
        byte var2 = 0;
        int var3 = this.qrCodeSymbol.getDataCapacity();
        int[] var4 = new int[var3];
        int var5 = this.qrCodeSymbol.getNumErrorCollectionCode();
        int var6 = this.qrCodeSymbol.getNumRSBlocks();
        int var7 = var5 / var6;
        if (var6 == 1) {
            RsDecode var17 = new RsDecode(var7 / 2);
            var17.decode(var1);
            return var1;
        } else {
            int var8 = var3 % var6;
            int var9;
            int var11;
            if (var8 == 0) {
                var9 = var3 / var6;
                int[][] var10 = new int[var6][var9];

                int var12;
                for (var11 = 0; var11 < var6; ++var11) {
                    for (var12 = 0; var12 < var9; ++var12) {
                        var10[var11][var12] = var1[var12 * var6 + var11];
                    }

                    canvas.println("eccPerRSBlock=" + var7);
                    RsDecode var19 = new RsDecode(var7 / 2);
                    var19.decode(var10[var11]);
                }

                var11 = 0;

                for (var12 = 0; var12 < var6; ++var12) {
                    for (int var13 = 0; var13 < var9 - var7; ++var13) {
                        var4[var11++] = var10[var12][var13];
                    }
                }
            } else {
                var9 = var3 / var6;
                int var18 = var3 / var6 + 1;
                var11 = var6 - var8;
                int[][] var20 = new int[var11][var9];
                int[][] var21 = new int[var8][var18];

                int var14;
                int var15;
                int var16;
                for (var14 = 0; var14 < var6; ++var14) {
                    RsDecode var22;
                    if (var14 < var11) {
                        var15 = 0;

                        for (var16 = 0; var16 < var9; ++var16) {
                            if (var16 == var9 - var7) {
                                var15 = var8;
                            }

                            var20[var14][var16] = var1[var16 * var6 + var14 + var15];
                        }

                        canvas.println("eccPerRSBlock(shorter)=" + var7);
                        var22 = new RsDecode(var7 / 2);
                        var22.decode(var20[var14]);
                    } else {
                        var15 = 0;

                        for (var16 = 0; var16 < var18; ++var16) {
                            if (var16 == var9 - var7) {
                                var15 = var11;
                            }

                            var21[var14 - var11][var16] = var1[var16 * var6 + var14 - var15];
                        }

                        canvas.println("eccPerRSBlock(longer)=" + var7);
                        var22 = new RsDecode(var7 / 2);
                        var22.decode(var21[var14 - var11]);
                    }
                }

                var14 = 0;

                for (var15 = 0; var15 < var6; ++var15) {
                    if (var15 < var11) {
                        for (var16 = 0; var16 < var9 - var7; ++var16) {
                            var4[var14++] = var20[var15][var16];
                        }
                    } else {
                        for (var16 = 0; var16 < var18 - var7; ++var16) {
                            var4[var14++] = var21[var15 - var11][var16];
                        }
                    }
                }
            }

            if (var2 > 0) {
                canvas.println(var2 + " data errors corrected.");
            } else {
                canvas.println("No errors found.");
            }

            this.numLastCorrections = var2;
            return var4;
        }
    }

    byte[] getDecodedByteArray(int[] var1, int var2, int var3) throws InvalidDataBlockException {
        QRCodeDataBlockReader var5 = new QRCodeDataBlockReader(var1, var2, var3);

        try {
            byte[] var4 = var5.getDataByte();
            return var4;
        } catch (InvalidDataBlockException var7) {
            throw var7;
        }
    }

    String getDecodedString(int[] var1, int var2, int var3) throws InvalidDataBlockException {
        String var4 = null;
        QRCodeDataBlockReader var5 = new QRCodeDataBlockReader(var1, var2, var3);

        try {
            var4 = var5.getDataString();
            return var4;
        } catch (ArrayIndexOutOfBoundsException var7) {
            throw new InvalidDataBlockException(var7.getMessage());
        }
    }

    class DecodeResult {
        int numCorrections;
        boolean correctionSucceeded;
        byte[] decodedBytes;

        public DecodeResult(byte[] var2, int var3, boolean var4) {
            this.decodedBytes = var2;
            this.numCorrections = var3;
            this.correctionSucceeded = var4;
        }

        public byte[] getDecodedBytes() {
            return this.decodedBytes;
        }

        public int getNumErrors() {
            return this.numCorrections;
        }

        public boolean isCorrectionSucceeded() {
            return this.correctionSucceeded;
        }
    }
}
