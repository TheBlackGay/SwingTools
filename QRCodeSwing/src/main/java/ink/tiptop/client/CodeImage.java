package ink.tiptop.client;

import ink.tiptop.client.jarclass.sourceforge.qrcode.data.QRCodeImage;

import java.awt.image.BufferedImage;

/**
 * Copyright (C), 2019 HKRT
 * Author:   哦豁,Zhongjf
 * Date:     2019/4/30 10:09
 * Description:
 */
class CodeImage implements QRCodeImage {

    BufferedImage bufImg;

    public CodeImage(BufferedImage bufImg) {
        this.bufImg = bufImg;
    }

    @Override
    public int getHeight() {
        return bufImg.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        return bufImg.getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return bufImg.getWidth();
    }
}
