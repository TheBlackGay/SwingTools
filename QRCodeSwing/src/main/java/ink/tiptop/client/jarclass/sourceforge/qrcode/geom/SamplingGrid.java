//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ink.tiptop.client.jarclass.sourceforge.qrcode.geom;

public class SamplingGrid {
    private SamplingGrid.AreaGrid[][] grid;

    public SamplingGrid(int var1) {
        this.grid = new SamplingGrid.AreaGrid[var1][var1];
    }

    public void initGrid(int var1, int var2, int var3, int var4) {
        this.grid[var1][var2] = new SamplingGrid.AreaGrid(var3, var4);
    }

    public void setXLine(int var1, int var2, int var3, Line var4) {
        this.grid[var1][var2].setXLine(var3, var4);
    }

    public void setYLine(int var1, int var2, int var3, Line var4) {
        this.grid[var1][var2].setYLine(var3, var4);
    }

    public Line getXLine(int var1, int var2, int var3) throws ArrayIndexOutOfBoundsException {
        return this.grid[var1][var2].getXLine(var3);
    }

    public Line getYLine(int var1, int var2, int var3) throws ArrayIndexOutOfBoundsException {
        return this.grid[var1][var2].getYLine(var3);
    }

    public Line[] getXLines(int var1, int var2) {
        return this.grid[var1][var2].getXLines();
    }

    public Line[] getYLines(int var1, int var2) {
        return this.grid[var1][var2].getYLines();
    }

    public int getWidth() {
        return this.grid[0].length;
    }

    public int getHeight() {
        return this.grid.length;
    }

    public int getWidth(int var1, int var2) {
        return this.grid[var1][var2].getWidth();
    }

    public int getHeight(int var1, int var2) {
        return this.grid[var1][var2].getHeight();
    }

    public int getTotalWidth() {
        int var1 = 0;

        for(int var2 = 0; var2 < this.grid.length; ++var2) {
            var1 += this.grid[var2][0].getWidth();
            if (var2 > 0) {
                --var1;
            }
        }

        return var1;
    }

    public int getTotalHeight() {
        int var1 = 0;

        for(int var2 = 0; var2 < this.grid[0].length; ++var2) {
            var1 += this.grid[0][var2].getHeight();
            if (var2 > 0) {
                --var1;
            }
        }

        return var1;
    }

    public int getX(int var1, int var2) {
        int var3 = var2;

        for(int var4 = 0; var4 < var1; ++var4) {
            var3 += this.grid[var4][0].getWidth() - 1;
        }

        return var3;
    }

    public int getY(int var1, int var2) {
        int var3 = var2;

        for(int var4 = 0; var4 < var1; ++var4) {
            var3 += this.grid[0][var4].getHeight() - 1;
        }

        return var3;
    }

    public void adjust(Point var1) {
        int var2 = var1.getX();
        int var3 = var1.getY();

        for(int var4 = 0; var4 < this.grid[0].length; ++var4) {
            for(int var5 = 0; var5 < this.grid.length; ++var5) {
                int var6;
                for(var6 = 0; var6 < this.grid[var5][var4].xLine.length; ++var6) {
                    this.grid[var5][var4].xLine[var6].translate(var2, var3);
                }

                for(var6 = 0; var6 < this.grid[var5][var4].yLine.length; ++var6) {
                    this.grid[var5][var4].yLine[var6].translate(var2, var3);
                }
            }
        }

    }

    private class AreaGrid {
        protected Line[] xLine;
        protected Line[] yLine;

        public AreaGrid(int var2, int var3) {
            this.xLine = new Line[var2];
            this.yLine = new Line[var3];
        }

        public int getWidth() {
            return this.xLine.length;
        }

        public int getHeight() {
            return this.yLine.length;
        }

        public Line getXLine(int var1) throws ArrayIndexOutOfBoundsException {
            return this.xLine[var1];
        }

        public Line getYLine(int var1) throws ArrayIndexOutOfBoundsException {
            return this.yLine[var1];
        }

        public Line[] getXLines() {
            return this.xLine;
        }

        public Line[] getYLines() {
            return this.yLine;
        }

        public void setXLine(int var1, Line var2) {
            this.xLine[var1] = var2;
        }

        public void setYLine(int var1, Line var2) {
            this.yLine[var1] = var2;
        }
    }
}
