//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.util;

public class ContentConverter {
    static char n = '\n';

    public ContentConverter() {
    }

    public static String convert(String var0) {
        if (var0 == null) {
            return var0;
        } else {
            if (var0.indexOf("MEBKM:") > -1) {
                var0 = convertDocomoBookmark(var0);
            }

            if (var0.indexOf("MECARD:") > -1) {
                var0 = convertDocomoAddressBook(var0);
            }

            if (var0.indexOf("MATMSG:") > -1) {
                var0 = convertDocomoMailto(var0);
            }

            if (var0.indexOf("http\\://") > -1) {
                var0 = replaceString(var0, "http\\://", "\nhttp://");
            }

            return var0;
        }
    }

    private static String convertDocomoBookmark(String var0) {
        var0 = removeString(var0, "MEBKM:");
        var0 = removeString(var0, "TITLE:");
        var0 = removeString(var0, ";");
        var0 = removeString(var0, "URL:");
        return var0;
    }

    private static String convertDocomoAddressBook(String var0) {
        var0 = removeString(var0, "MECARD:");
        var0 = removeString(var0, ";");
        var0 = replaceString(var0, "N:", "NAME1:");
        var0 = replaceString(var0, "SOUND:", n + "NAME2:");
        var0 = replaceString(var0, "TEL:", n + "TEL1:");
        var0 = replaceString(var0, "EMAIL:", n + "MAIL1:");
        var0 = var0 + n;
        return var0;
    }

    private static String convertDocomoMailto(String var0) {
        char var2 = '\n';
        String var1 = removeString(var0, "MATMSG:");
        var1 = removeString(var1, ";");
        var1 = replaceString(var1, "TO:", "MAILTO:");
        var1 = replaceString(var1, "SUB:", var2 + "SUBJECT:");
        var1 = replaceString(var1, "BODY:", var2 + "BODY:");
        var1 = var1 + var2;
        return var1;
    }

    private static String replaceString(String var0, String var1, String var2) {
        String var3 = var0;

        for(int var4 = var0.indexOf(var1, 0); var4 > -1; var4 = var3.indexOf(var1, var4 + var2.length())) {
            var3 = var3.substring(0, var4) + var2 + var3.substring(var4 + var1.length());
        }

        return var3;
    }

    private static String removeString(String var0, String var1) {
        return replaceString(var0, var1, "");
    }
}
