//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.reader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ink.tiptop.client.jarclass.sourceforge.qrcode.QRCodeDecoder;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.InvalidDataBlockException;
import ink.tiptop.client.jarclass.sourceforge.qrcode.util.DebugCanvas;

public class QRCodeDataBlockReader {
    int[] blocks;
    int dataLengthMode;
    int blockPointer = 0;
    int bitPointer = 7;
    int dataLength = 0;
    int numErrorCorrectionCode;
    DebugCanvas canvas;
    static final int MODE_NUMBER = 1;
    static final int MODE_ROMAN_AND_NUMBER = 2;
    static final int MODE_8BIT_BYTE = 4;
    static final int MODE_KANJI = 8;
    final int[][] sizeOfDataLengthInfo = new int[][]{{10, 9, 8, 8}, {12, 11, 16, 10}, {14, 13, 16, 12}};

    public QRCodeDataBlockReader(int[] var1, int var2, int var3) {
        this.blocks = var1;
        this.numErrorCorrectionCode = var3;
        if (var2 <= 9) {
            this.dataLengthMode = 0;
        } else if (var2 >= 10 && var2 <= 26) {
            this.dataLengthMode = 1;
        } else if (var2 >= 27 && var2 <= 40) {
            this.dataLengthMode = 2;
        }

        this.canvas = QRCodeDecoder.getCanvas();
    }

    int getNextBits(int var1) throws ArrayIndexOutOfBoundsException {
        boolean var2 = false;
        int var3;
        int var4;
        int var8;
        if (var1 < this.bitPointer + 1) {
            var3 = 0;

            for(var4 = 0; var4 < var1; ++var4) {
                var3 += 1 << var4;
            }

            var3 <<= this.bitPointer - var1 + 1;
            var8 = (this.blocks[this.blockPointer] & var3) >> this.bitPointer - var1 + 1;
            this.bitPointer -= var1;
            return var8;
        } else if (var1 < this.bitPointer + 1 + 8) {
            var3 = 0;

            for(var4 = 0; var4 < this.bitPointer + 1; ++var4) {
                var3 += 1 << var4;
            }

            var8 = (this.blocks[this.blockPointer] & var3) << var1 - (this.bitPointer + 1);
            ++this.blockPointer;
            var8 += this.blocks[this.blockPointer] >> 8 - (var1 - (this.bitPointer + 1));
            this.bitPointer -= var1 % 8;
            if (this.bitPointer < 0) {
                this.bitPointer += 8;
            }

            return var8;
        } else if (var1 >= this.bitPointer + 1 + 16) {
            System.out.println("ERROR!");
            return 0;
        } else {
            var3 = 0;
            var4 = 0;

            int var5;
            for(var5 = 0; var5 < this.bitPointer + 1; ++var5) {
                var3 += 1 << var5;
            }

            var5 = (this.blocks[this.blockPointer] & var3) << var1 - (this.bitPointer + 1);
            ++this.blockPointer;
            int var6 = this.blocks[this.blockPointer] << var1 - (this.bitPointer + 1 + 8);
            ++this.blockPointer;

            int var7;
            for(var7 = 0; var7 < var1 - (this.bitPointer + 1 + 8); ++var7) {
                var4 += 1 << var7;
            }

            var4 <<= 8 - (var1 - (this.bitPointer + 1 + 8));
            var7 = (this.blocks[this.blockPointer] & var4) >> 8 - (var1 - (this.bitPointer + 1 + 8));
            var8 = var5 + var6 + var7;
            this.bitPointer -= (var1 - 8) % 8;
            if (this.bitPointer < 0) {
                this.bitPointer += 8;
            }

            return var8;
        }
    }

    int getNextMode() throws ArrayIndexOutOfBoundsException {
        return this.blockPointer > this.blocks.length - this.numErrorCorrectionCode - 2 ? 0 : this.getNextBits(4);
    }

    int guessMode(int var1) {
        switch(var1) {
            case 3:
                return 1;
            case 4:
            case 8:
            default:
                return 8;
            case 5:
                return 4;
            case 6:
                return 4;
            case 7:
                return 4;
            case 9:
                return 8;
            case 10:
                return 8;
            case 11:
                return 8;
            case 12:
                return 4;
            case 13:
                return 4;
            case 14:
                return 4;
            case 15:
                return 4;
        }
    }

    int getDataLength(int var1) throws ArrayIndexOutOfBoundsException {
        int var2;
        for(var2 = 0; var1 >> var2 != 1; ++var2) {
            ;
        }

        return this.getNextBits(this.sizeOfDataLengthInfo[this.dataLengthMode][var2]);
    }

    public byte[] getDataByte() throws InvalidDataBlockException {
        this.canvas.println("Reading data blocks.");
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();

        try {
            while(true) {
                int var2 = this.getNextMode();
                if (var2 == 0) {
                    if (var1.size() <= 0) {
                        throw new InvalidDataBlockException("Empty data block");
                    }

                    return var1.toByteArray();
                }

                if (var2 != 1 && var2 != 2 && var2 != 4 && var2 != 8) {
                    throw new InvalidDataBlockException("Invalid mode: " + var2 + " in (block:" + this.blockPointer + " bit:" + this.bitPointer + ")");
                }

                this.dataLength = this.getDataLength(var2);
                if (this.dataLength < 1) {
                    throw new InvalidDataBlockException("Invalid data length: " + this.dataLength);
                }

                switch(var2) {
                    case 1:
                        var1.write(this.getFigureString(this.dataLength).getBytes());
                        break;
                    case 2:
                        var1.write(this.getRomanAndFigureString(this.dataLength).getBytes());
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        break;
                    case 4:
                        var1.write(this.get8bitByteArray(this.dataLength));
                        break;
                    case 8:
                        var1.write(this.getKanjiString(this.dataLength).getBytes());
                }
            }
        } catch (ArrayIndexOutOfBoundsException var3) {
            throw new InvalidDataBlockException("Data Block Error in (block:" + this.blockPointer + " bit:" + this.bitPointer + ")");
        } catch (IOException var4) {
            throw new InvalidDataBlockException(var4.getMessage());
        }
    }

    public String getDataString() throws ArrayIndexOutOfBoundsException {
        this.canvas.println("Reading data blocks...");
        String var1 = "";

        while(true) {
            int var2 = this.getNextMode();
            this.canvas.println("mode: " + var2);
            if (var2 == 0) {
                System.out.println("");
                return var1;
            }

            if (var2 != 1 && var2 != 2 && var2 != 4 && var2 != 8) {
                ;
            }

            this.dataLength = this.getDataLength(var2);
            this.canvas.println(Integer.toString(this.blocks[this.blockPointer]));
            System.out.println("length: " + this.dataLength);
            switch(var2) {
                case 1:
                    var1 = var1 + this.getFigureString(this.dataLength);
                    break;
                case 2:
                    var1 = var1 + this.getRomanAndFigureString(this.dataLength);
                case 3:
                case 5:
                case 6:
                case 7:
                default:
                    break;
                case 4:
                    var1 = var1 + this.get8bitByteString(this.dataLength);
                    break;
                case 8:
                    var1 = var1 + this.getKanjiString(this.dataLength);
            }
        }
    }

    String getFigureString(int var1) throws ArrayIndexOutOfBoundsException {
        int var2 = var1;
        int var3 = 0;
        String var4 = "";

        do {
            if (var2 >= 3) {
                var3 = this.getNextBits(10);
                if (var3 < 100) {
                    var4 = var4 + "0";
                }

                if (var3 < 10) {
                    var4 = var4 + "0";
                }

                var2 -= 3;
            } else if (var2 == 2) {
                var3 = this.getNextBits(7);
                if (var3 < 10) {
                    var4 = var4 + "0";
                }

                var2 -= 2;
            } else if (var2 == 1) {
                var3 = this.getNextBits(4);
                --var2;
            }

            var4 = var4 + Integer.toString(var3);
        } while(var2 > 0);

        return var4;
    }

    String getRomanAndFigureString(int var1) throws ArrayIndexOutOfBoundsException {
        int var2 = var1;
        boolean var3 = false;
        String var4 = "";
        char[] var5 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '$', '%', '*', '+', '-', '.', '/', ':'};

        do {
            int var8;
            if (var2 > 1) {
                var8 = this.getNextBits(11);
                int var6 = var8 / 45;
                int var7 = var8 % 45;
                var4 = var4 + String.valueOf(var5[var6]);
                var4 = var4 + String.valueOf(var5[var7]);
                var2 -= 2;
            } else if (var2 == 1) {
                var8 = this.getNextBits(6);
                var4 = var4 + String.valueOf(var5[var8]);
                --var2;
            }
        } while(var2 > 0);

        return var4;
    }

    public byte[] get8bitByteArray(int var1) throws ArrayIndexOutOfBoundsException {
        int var2 = var1;
        boolean var3 = false;
        ByteArrayOutputStream var4 = new ByteArrayOutputStream();

        do {
            int var5 = this.getNextBits(8);
            var4.write((byte)var5);
            --var2;
        } while(var2 > 0);

        return var4.toByteArray();
    }

    String get8bitByteString(int var1) throws ArrayIndexOutOfBoundsException {
        int var2 = var1;
        boolean var3 = false;
        String var4 = "";

        do {
            int var5 = this.getNextBits(8);
            var4 = var4 + (char)var5;
            --var2;
        } while(var2 > 0);

        return var4;
    }

    String getKanjiString(int var1) throws ArrayIndexOutOfBoundsException {
        int var2 = var1;
        boolean var3 = false;
        String var4 = "";

        do {
            int var12 = this.getNextBits(13);
            int var5 = var12 % 192;
            int var6 = var12 / 192;
            int var7 = (var6 << 8) + var5;
            boolean var8 = false;
            int var13;
            if (var7 + '腀' <= 40956) {
                var13 = var7 + '腀';
            } else {
                var13 = var7 + '셀';
            }

            byte[] var9 = new byte[]{(byte)(var13 >> 8), (byte)(var13 & 255)};

            try {
                var4 = var4 + new String(var9, "Shift_JIS");
            } catch (UnsupportedEncodingException var11) {
                var11.printStackTrace();
            }

            --var2;
        } while(var2 > 0);

        return var4;
    }
}
