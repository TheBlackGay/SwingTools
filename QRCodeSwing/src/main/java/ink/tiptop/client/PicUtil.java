package ink.tiptop.client;

import ink.tiptop.client.jarclass.sourceforge.qrcode.QRCodeDecoder;
import ink.tiptop.client.jarclass.sourceforge.qrcode.exception.DecodingFailedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Copyright (C), 2019 HKRT
 * Author:   哦豁,Zhongjf
 * Date:     2019/4/30 10:09
 * Description:
 */
public class PicUtil {
//    public static void main(String[] args) throws Exception {
//        String s = decoderQRCode("D:\\pic\\2019043009534211.png");
//        System.out.println(s);
//        // https://qr.95516.com/00010048/0fbc7f32d3b4a41fdc4d28a39e6845130438834e5c
//    }

    public static String decoderQRCode(String imgPath) throws IOException {
        // QRCode 二维码图片的文件
        File imageFile = new File(imgPath);
        if (imageFile.isDirectory()) {
            return "";
        }
        String lowerCase = imgPath.toLowerCase();
        if (!(lowerCase.endsWith(".jpg") || lowerCase.endsWith(".png") )) {
            return "";
        }

        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(imageFile);
            QRCodeDecoder decoder = new QRCodeDecoder();

            content = new String(decoder.decode(new CodeImage(bufImg)), StandardCharsets.UTF_8);
        } catch (IOException | DecodingFailedException e) {
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
        return content;
    }
}
