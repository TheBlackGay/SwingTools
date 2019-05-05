//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.util;

import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Line;
import ink.tiptop.client.jarclass.sourceforge.qrcode.geom.Point;

public interface DebugCanvas {
    void println(String var1);

    void drawPoint(Point var1, int var2);

    void drawCross(Point var1, int var2);

    void drawPoints(Point[] var1, int var2);

    void drawLine(Line var1, int var2);

    void drawLines(Line[] var1, int var2);

    void drawPolygon(Point[] var1, int var2);

    void drawMatrix(boolean[][] var1);
}
