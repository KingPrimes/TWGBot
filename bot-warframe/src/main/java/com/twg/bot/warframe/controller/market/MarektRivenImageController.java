package com.twg.bot.warframe.controller.market;

import com.twg.bot.warframe.dao.MarketRiven;
import com.twg.bot.warframe.dao.MarketRivenParameter;
import com.twg.bot.warframe.service.IWarframeMarketRivenTionService;
import com.twg.bot.warframe.utils.market.MarketRivenUtil;
import com.twg.common.utils.Fonts;
import com.twg.common.utils.Seat;
import com.twg.common.utils.image.ImageUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.twg.common.utils.ColorEnum.*;

@RestController
@RequestMapping("/warframe/market")
public class MarektRivenImageController {

    @Autowired
    IWarframeMarketRivenTionService rivenTionService;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getMarektRivenImage/{key}", produces = MediaType.IMAGE_PNG_VALUE)
    private void getImage(HttpServletResponse response, @PathVariable String key) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/big.png");
        try {
            MarketRivenParameter parameter = MarketRivenUtil.toMarketRiven(URLDecoder.decode(key, "UTF-8"));
            MarketRiven riven = MarketRivenUtil.marketRiven(parameter);
            if (riven != null) {
                List<MarketRiven.Auctions> rivenList = riven.getPayload().getAuctions();
                int x = 62, y = 160, i = 0;
                assert image != null;
                Graphics2D g = image.createGraphics();
                g.setFont(Fonts.FONT_SOURCE_CN_MEDIUM_BOLD);
                g.setColor(COLOR_NODE.getColor());
                g.drawString("????????????Warframe.Market??????????????????????????????????????????", 424, 80);
                for (MarketRiven.Auctions auctions : rivenList) {
                    if (i >= 4) {
                        break;
                    }
                    //????????????????????????????????????
                    g.drawImage(getRivenImage(auctions, parameter.getItemName()), x, y, null);
                    g.setFont(Fonts.FONT_TEXT);
                    g.setColor(COLOR_BACK.getColor());
                    g.drawString("??????ID:", x, y + 450);
                    g.setColor(COLOR_NODE.getColor());
                    g.drawString(auctions.getOwner().getIngameName(), x + ImageUtils.getFortWidth("??????ID:"), y + 450);
                    g.setColor(COLOR_BACK.getColor());
                    g.drawString("??????:", x, y + 480);
                    g.setColor(COLOR_NODE.getColor());
                    g.drawString(auctions.getBuyoutPrice().toString(), x + ImageUtils.getFortWidth("??????:"), y + 480);
                    x += 400;
                    i += 1;
                }
            } else {
                image = ImageUtils.getImage("/images/s.png");
                Graphics2D g = image.createGraphics();
                g.setFont(Fonts.FONT_SOURCE_CN_MEDIUM_BOLD);
                g.setColor(COLOR_NODE.getColor());
                g.drawString("?????????????????????????????????????????????", 80, 80);
                g.drawString("??????????????????????????????", 80, 80 + Fonts.FONT_SOURCE_CN_MEDIUM_BOLD.getSize());
            }
        } catch (Exception e) {
            image = ImageUtils.getImage("/images/s.png");
            Graphics2D g = image.createGraphics();
            g.setFont(Fonts.FONT_SOURCE_CN_MEDIUM_BOLD);
            g.setColor(COLOR_NODE.getColor());
            g.drawString("???????????????????????????????????????", 80, 80);
        }
        //return image;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //ImageIO.write(image, "png", out);
        Thumbnails.of(image).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        response.getOutputStream().write(out.toByteArray());
    }

    //??????
    private BufferedImage getRivenImage(MarketRiven.Auctions auctions, String name) {
        BufferedImage rivenImage = ImageUtils.getImage("/images/riven/Riven_Backdrop.png");
        List<Seat> rivenSeat = new ArrayList<>();
        assert rivenImage != null;
        int foutWidth, y = 222;
        //??????????????????
        foutWidth = rivenImage.getWidth() - ImageUtils.getFortWidth(name, Fonts.FONT_RIVEN);
        rivenSeat.add(ImageUtils.getSeat(name, foutWidth / 2, y, COLOR_RIVEN_TION, Fonts.FONT_RIVEN));
        //????????????
        foutWidth = rivenImage.getWidth() - ImageUtils.getFortWidth(auctions.getItem().getName(), Fonts.FONT_RIVEN);
        y += Fonts.FONT_RIVEN.getSize();
        rivenSeat.add(ImageUtils.getSeat(auctions.getItem().getName(), foutWidth / 2, y, COLOR_RIVEN_TION, Fonts.FONT_RIVEN));
        //????????????
        String tion;
        for (MarketRiven.Attributes attributes : auctions.getItem().getAttributes()) {
            tion = attributes.getValue() + "% " + rivenTionService.selectWarframeMarketRivenTionByUrlName(attributes.getUrlName()).getEffect();
            y += Fonts.FONT_RIVEN.getSize();
            foutWidth = rivenImage.getWidth() - ImageUtils.getFortWidth(tion, Fonts.FONT_RIVEN);
            rivenSeat.add(ImageUtils.getSeat(tion, foutWidth / 2, y, COLOR_RIVEN_TION, Fonts.FONT_RIVEN));
        }
        //????????????
        rivenSeat.add(ImageUtils.getSeat(auctions.getItem().getMasteryLevel().toString(), 120, 345, COLOR_RIVEN_TION, Fonts.FONT_RIVEN));
        //????????????
        rivenSeat.add(ImageUtils.getSeat(auctions.getItem().getReRolls().toString(), 238, 345, COLOR_RIVEN_TION, Fonts.FONT_RIVEN));
        //????????????
        rivenSeat.add(new Seat(getRivenPolarity(auctions.getItem().getPolarity()), 255, 40));
        return ImageUtils.getBufferedImage(rivenImage, rivenSeat);
    }

    //??????????????????
    private String getRivenPolarity(String str) {
        if (str.equals("madurai")) {
            return ("/images/riven/Riven_Madurai.png");
        }
        if (str.equals("naramon")) {
            return ("/images/riven/Riven_Naramon.png");
        }
        if (str.equals("vazarin")) {
            return ("/images/riven/Riven_Vazarin.png");
        }
        return ("/images/riven/Riven_Madurai.png");
    }

}
