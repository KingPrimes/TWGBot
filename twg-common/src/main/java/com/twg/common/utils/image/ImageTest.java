package com.twg.common.utils.image;


import com.twg.common.utils.image.combiner.ImageCombiner;
import com.twg.common.utils.image.combiner.enums.OutputFormat;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTest {

    /**
     * 根据文字生成图片
     *
     * @param text      文字内容 换行需带\n
     * @param font      文字得字体
     * @param color     背景颜色
     * @param fontColor 文字颜色
     * @return BufferedImage
     */
    public static BufferedImage followTextSizeToImage(String text, Font font, Color color, Color fontColor) throws Exception {
        int width = ImageUtils.getFortWidth(text, font);
        int height = ImageUtils.getFortH(font, text) + 5;
        String[] temp = text.split("\n");
        ImageCombiner combiner = new ImageCombiner(width, height, OutputFormat.PNG);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(color);
        g.drawImage(image, 0, 0, color, null);
        g.setFont(font);
        g.setColor(fontColor);
        int x = 0, y = font.getSize();
        for (String str : temp) {
            g.drawString(str, x, y);
            y += font.getSize();
        }
        combiner.addImageElement(image, 0, 0).setRoundCorner(20);
        return combiner.combine();
    }


  /*  public static void main(String[] args) throws Exception {
        String temp = "价格";

        Thumbnails.of(followTextSizeToImage(temp, Fonts.FONT_SOURCE_CN_MEDIUM_BOLD, ColorEnum.COLOR_PRICE_BACKGROUND.getColor(), ColorEnum.COLOR_PRICE_FONT.getColor()))
                .scale(1)
                .outputFormat("png")
                .toFile(new File("./temp.png"));
    }*/
}
