package com.twg.common.utils.image;


import com.twg.common.utils.ColorEnum;
import com.twg.common.utils.Fonts;
import com.twg.common.utils.Seat;
import com.twg.common.utils.image.combiner.ImageCombiner;
import com.twg.common.utils.image.combiner.enums.OutputFormat;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;


public class ImageUtils {

    private Font font;
    private Color backgroundColor;

    /**
     * 右下角平台
     *
     * @param form 平台
     * @return Seat
     * @see Seat
     */
    public static Seat getForm(String form) {
        Seat seat = new Seat(Fonts.FONT_SOURCE_CN_MEDIUM);
        seat.setMsg(form.toUpperCase(Locale.ROOT) + "平台");
        seat.setX(773);
        seat.setY(689);
        seat.setColor(ColorEnum.COLOR_FORM);
        return seat;
    }

    /**
     * 小图 平台
     *
     * @param form 平台
     * @return Seat
     */
    public static Seat getFormS(String form) {
        Seat seat = new Seat(Fonts.FONT_SOURCE_CN_MEDIUM);
        seat.setMsg(form.toUpperCase(Locale.ROOT) + "平台");
        seat.setX(773);
        seat.setY(140);
        seat.setColor(ColorEnum.COLOR_FORM);
        return seat;
    }

    public static Seat getFormBig(String form) {
        Seat seat = new Seat(Fonts.FONT_SOURCE_CN_MEDIUM);
        seat.setMsg(form.toUpperCase(Locale.ROOT) + "平台");
        seat.setX(1655);
        seat.setY(689);
        seat.setColor(ColorEnum.COLOR_FORM);
        return seat;
    }

    /**
     * 设置Seat
     *
     * @param msg   文本
     * @param x     x
     * @param y     y
     * @param color Color
     * @return Seat
     */
    public static Seat getSeat(String msg, int x, int y, ColorEnum color) {
        Seat seat = new Seat(Fonts.FONT_SOURCE_CN_MEDIUM);
        seat.setMsg(msg);
        seat.setX(x);
        seat.setY(y);
        seat.setColor(color);
        return seat;
    }

    public static Seat getSeat(String msg, int x, int y, ColorEnum color, Font font) {
        Seat seat = new Seat(font);
        seat.setMsg(msg);
        seat.setX(x);
        seat.setY(y);
        seat.setColor(color);
        return seat;
    }

    /**
     * 设置Seat
     *
     * @param msg 文本
     * @param x   X
     * @param y   Y
     * @return Seat
     */
    public static Seat getSeat(String msg, int x, int y) {
        Seat seat = new Seat(Fonts.FONT_SOURCE_CN_MEDIUM);
        seat.setMsg(msg);
        seat.setX(x);
        seat.setY(y);
        seat.setColor(ColorEnum.COLOR_MINOR);
        return seat;
    }

    /**
     * 文本居中于图片
     *
     * @param image 图片
     * @param font  字体
     * @param msg   文本
     * @return 文本位于图片的 X 轴 位置
     */
    public static int getFortW(BufferedImage image, Font font, String msg) {
        String[] stars = msg.split("\n");
        int n = 0;
        for (String s : stars) {
            if (n < s.length()) {
                n = s.length();
            }
        }
        n = n / 2 * font.getSize() + font.getSize() / 2;
        n = image.getWidth() - n;
        return n / 2;
    }

    /**
     * 取文本宽度
     *
     * @param msg 文本
     * @return 文本宽度
     */
    public static int getFortWidth(String msg) {
        FontMetrics fm = new JLabel().getFontMetrics(Fonts.FONT_SOURCE_CN_MEDIUM);
        return fm.stringWidth(msg) + 10;
    }

    /**
     * 取文本宽度
     *
     * @param msg  文本
     * @param font 字体
     * @return 宽度
     */
    public static int getFortWidth(String msg, Font font) {
        FontMetrics fm = new JLabel().getFontMetrics(font);
        return fm.stringWidth(msg);
    }

    /**
     * 取文本高度
     *
     * @param font 字体
     * @return 高度
     */
    public static int getFontHeight(Font font) {
        FontMetrics fm;
        if (font == null) {
            fm = new JLabel().getFontMetrics(Fonts.FONT_SOURCE_CN_MEDIUM);
        } else {
            fm = new JLabel().getFontMetrics(font);
        }
        return fm.getHeight();
    }

    public static int getFortWh(String msg) {
        int n = msg.length();
        return n / 2 * Fonts.FONT_SOURCE_CN_MEDIUM.getSize() + Fonts.FONT_SOURCE_CN_MEDIUM.getSize() * 2 - 20;
    }

    /**
     * 取文本高度
     *
     * @param font 字体
     * @param msg  文本
     * @return 文本高度
     */
    public static int getFortH(Font font, String msg) {
        return font.getSize() * msg.split("\n").length;
    }

    /**
     * 取文本高度
     *
     * @param msg 文本
     * @return 文本高度
     */
    public static int getFortH(String msg) {
        return Fonts.FONT_SOURCE_CN_MEDIUM.getSize() * msg.split("\n").length;
    }


    /**
     * 调整bufferedimage大小
     *
     * @param source  BufferedImage 原始image
     * @param targetW int  目标宽
     * @param targetH int  目标高
     * @param flag    boolean 是否同比例调整
     * @return BufferedImage  返回新image
     */
    public static BufferedImage resizeBufferedImage(BufferedImage source, int targetW, int targetH, boolean flag) {
        int type = source.getType();
        BufferedImage target;
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        if (flag && sx > sy) {
            sx = sy;
            targetW = (int) (sx * source.getWidth());
        } else if (flag && sx <= sy) {
            sy = sx;
            targetH = (int) (sy * source.getHeight());
        }
        // handmade
        if (type == BufferedImage.TYPE_CUSTOM) {
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(targetW, targetH, type);
        }
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    @NotNull
    public static BufferedImage getBufferedImage(BufferedImage image, List<Seat> seats) {
        assert image != null;
        Graphics2D g = image.createGraphics();
        /*g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);*/
        for (Seat seat : seats) {
            if (seat.getFont() != null) {
                g.setFont(seat.getFont());
            }
            if (seat.getColor() != null) {
                g.setColor(seat.getColor().getColor());
            }
            if (seat.getMsg() != null && !"".equals(seat.getMsg())) {
                g.drawString(seat.getMsg(), seat.getX(), seat.getY());
            }
            if (seat.getImagePaeh() != null && !"".equals(seat.getImagePaeh())) {
                g.drawImage(ImageUtils.getImage(seat.getImagePaeh()), seat.getX(), seat.getY(), null);
            }
            if (seat.getImage() != null) {
                g.drawImage(image, seat.getX(), seat.getY(), null);
            }
        }
        return image;
    }

    /**
     * 读取图片到内存
     *
     * @param fileName 图片路径
     * @return BufferedImage
     */
    public static BufferedImage getImage(String fileName) {
        try {
            InputStream in = ImageUtils.class.getResourceAsStream(fileName);
            assert in != null;
            return ImageIO.read(in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 根据文字生成图片
     *
     * @param text      文字内容 换行需带\n
     * @param font      文字得字体
     * @param color     背景颜色
     * @param fontColor 文字颜色
     * @return BufferedImage PNG图片
     */
    public BufferedImage followTextSizeToImage(String text, Font font, Color color, Color fontColor) throws Exception {
        int width = ImageUtils.getFortWidth(text, font);
        int height = ImageUtils.getFortH(font, text) + 5;
        String[] temp = text.split("\n");
        ImageCombiner combiner = new ImageCombiner(width, height, OutputFormat.PNG);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
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

    /**
     * 生成网格图片
     *
     * @param width       图片宽度
     * @param height      图片高度
     * @param color       图片背景色
     * @param reseauColor 网格颜色
     * @return BufferedImage
     */
    public BufferedImage reseauImage(int width, int height, Color color, Color reseauColor) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(image, 0, 0, color, null);
        int xx = width / 10, yx = height / 10, x = 0, y = 0;
        g.setColor(reseauColor);
        for (int i = 0; i <= xx; i++) {
            g.drawLine(x, 0, x, image.getHeight());
            x = x + 10;
        }
        for (int i = 0; i <= yx; i++) {
            g.drawLine(0, y, image.getWidth(), y);
            y = y + 10;
        }
        return image;
    }

    /**
     * 生成网格图片
     *
     * @param width  图片宽度
     * @param height 图片高度
     * @return BufferedImage
     */
    public BufferedImage reseauImage(int width, int height) {
        return reseauImage(width, height, new Color(35, 63, 85), new Color(47, 79, 79));
    }

    /**
     * 生成水印
     *
     * @param image 图片
     * @param text  水印内容
     */
    public void waterMarkImage(BufferedImage image, String text) throws Exception {
        BufferedImage water = followTextSizeToImage(text, Fonts.FONT_SOURCE_CN_MEDIUM_BOLD, new Color(0, 0, 0, 0), new Color(205, 201, 201, 40));
        Graphics2D g = image.createGraphics();
        //int x = (int) (Math.hypot(image.getWidth(),image.getHeight()) / water.getHeight());
        int x = 0, y = 0;
        int j = (image.getHeight() / water.getHeight()) + 8;
        boolean falg = true;
        for (int i = 0; i <= j; i++) {
            if (x >= image.getWidth()) {
                x = image.getWidth();
                y = 0;
                falg = false;
            }
            if (x < 0) {
                x = 0;
                falg = true;
            }
            g.drawImage(water, x, y, null);
            if (falg) {
                x = x + water.getWidth();
            } else {
                x = x - (water.getWidth() * 2);
            }
            y = y + (water.getHeight() * 2);

        }
    }


    public void setFont(Font font) {
        this.font = font;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    /**
     * 根据文字生成图片
     * 注意：使用此方法前必须SetFont
     *
     * @param text      文字内容 换行需带\n
     * @param color     背景颜色
     * @param fontColor 文字颜色
     * @return BufferedImage PNG图片
     */
    public BufferedImage followTextSizeToImage(String text, Color color, Color fontColor) throws Exception {
        return followTextSizeToImage(text, font, color, fontColor);
    }

    /**
     * 根据文字生成图片
     * 注意：使用此方法前必须SetFont SetBackColor
     *
     * @param text      文字内容 换行需带\n
     * @param fontColor 文字颜色
     * @return BufferedImage PNG图片
     */
    public BufferedImage followTextSizeToImage(String text, Color fontColor) throws Exception {
        return followTextSizeToImage(text, font, backgroundColor, fontColor);
    }
}
