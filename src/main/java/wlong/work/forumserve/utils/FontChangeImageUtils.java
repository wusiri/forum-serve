package wlong.work.forumserve.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FontChangeImageUtils {


    public static byte[] inputStreamToBytes(InputStream ins) {
        byte[] data = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            int i = -1;
            byte[] buf = new byte[1024];
            while ((i = ins.read(buf)) != -1) {
                baos.write(buf, 0, i);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return data;
    }


    // 根据str,font的样式以及输出文件目录
    public static void createImage(String str, Font font, File outFile,
                                   Integer width, Integer height) throws Exception {
        // 创建图片
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
        g.setColor(Color.black);// 在换成黑色
        g.setFont(font);// 设置画笔字体
        /** 用于获得垂直居中y */
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        for (int i = 0; i < 6; i++) {// 256 340 0 680
            g.drawString(str, i * 680, y);// 画出字符串
        }
        g.dispose();
        ImageIO.write(image, "png", outFile);// 输出png图片
    }
}


