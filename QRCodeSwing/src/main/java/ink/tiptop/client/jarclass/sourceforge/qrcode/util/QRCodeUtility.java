//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.util;

public class QRCodeUtility {
    public QRCodeUtility() {
    }

    public static int sqrt(int var0) {
        int var2 = 0;
        int var3 = 32768;
        int var4 = 15;

        do {
            int var1;
            if (var0 >= (var1 = (var2 << 1) + var3 << var4--)) {
                var2 += var3;
                var0 -= var1;
            }
        } while((var3 >>= 1) > 0);

        return var2;
    }
}
