package com.twg.bot.warframe.controller.market;

import com.twg.bot.enums.WarframeTypeEnum;
import com.twg.bot.warframe.dao.MarketLichOrSister;
import com.twg.bot.warframe.service.IWarframeMarketElementService;
import com.twg.bot.warframe.service.IWarframeMarketLichOrSisterService;
import com.twg.bot.warframe.service.IWarframeMarketSisterService;
import com.twg.bot.warframe.utils.market.MarketLichAndSisterUtil;
import com.twg.common.utils.DateUtils;
import com.twg.common.utils.Seat;
import com.twg.common.utils.StringUtils;
import com.twg.common.utils.image.ImageUtils;
import com.twg.common.utils.spring.SpringUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.twg.common.utils.ColorEnum.*;

/**
 * Market 赤毒武器与幻纹
 */
@RestController
@RequestMapping("/warframe/market")
public class MarektLichAndSisterImageController {

    @Autowired
    IWarframeMarketLichOrSisterService lichService;
    @Autowired
    IWarframeMarketSisterService sisterService;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getLichOrSisterImage/{key}/{type}", produces = MediaType.IMAGE_PNG_VALUE)
    public void getImage(HttpServletResponse response, @PathVariable String key, @PathVariable WarframeTypeEnum type) throws IOException {
        response.setHeader("Content-Type", "image/png");
        // 查询 赤毒武器/幻纹 拍卖详情
        MarketLichOrSister licksOrSister;
        if (WarframeTypeEnum.TYPE_MARKET_CD.equals(type)) {
            licksOrSister = MarketLichAndSisterUtil.getMarketLich(URLDecoder.decode(key, "UTF-8"));
        } else {
            licksOrSister = MarketLichAndSisterUtil.getMarketSister(URLDecoder.decode(key, "UTF-8"));
        }

        BufferedImage image = ImageUtils.getImage("/images/invasions/invasion.png");
        List<Seat> seats = new ArrayList<>();
        if (licksOrSister != null) {
            seats.add(ImageUtils.getSeat("--以下是出售", 150, 80, COLOR_BACK));
            seats.add(ImageUtils.getSeat("[ " + licksOrSister.getItemName() + " ]", ImageUtils.getFortWidth("--以下是出售") + 150, 80, COLOR_NODE));
            seats.add(ImageUtils.getSeat("的玩家--", ImageUtils.getFortWh("--以下是出售[ " + licksOrSister.getItemName() + " ]") + 230, 80, COLOR_BACK));
            int x = 80, y = 160;
            seats.add(ImageUtils.getSeat("玩家名:", x, y + 30, COLOR_BACK));
            seats.add(ImageUtils.getSeat("起拍价:", x, y + 80, COLOR_BACK));
            seats.add(ImageUtils.getSeat("买断价:", x, y + 130, COLOR_BACK));
            seats.add(ImageUtils.getSeat("一口价:", x, y + 180, COLOR_BACK));
            seats.add(ImageUtils.getSeat("最高价:", x, y + 230, COLOR_BACK));
            seats.add(ImageUtils.getSeat("元素:", x + 24, y + 280, COLOR_BACK));
            seats.add(ImageUtils.getSeat("加成:", x + 24, y + 330, COLOR_BACK));
            seats.add(ImageUtils.getSeat("幻纹:", x + 24, y + 380, COLOR_BACK));
            seats.add(ImageUtils.getSeat("上架日期:", x - 24, y + 430, COLOR_BACK));
            seats.add(ImageUtils.getSeat("武器名:", x, y + 480, COLOR_BACK));
            seats.add(ImageUtils.getSeat("武器名:", x, y + 530, COLOR_BACK));
            x = x + ImageUtils.getFortWidth("上架日期:");
            for (MarketLichOrSister.Auctions lick : licksOrSister.getPayload().getAuctions()) {
                //玩家名
                seats.add(ImageUtils.getSeat(lick.getOwner().getIngameName(), x, y + 30, COLOR_NODE));
                seats.add(new Seat("/images/p.png", 47, y + 35));
                //起拍价
                seats.add(ImageUtils.getSeat(lick.getStartingPrice().toString(), x, y + 80, COLOR_NODE));
                seats.add(new Seat("/images/p.png", 47, y + 85));
                //买断价
                seats.add(ImageUtils.getSeat(lick.getBuyoutPrice().toString(), x, y + 130, COLOR_NODE));
                seats.add(new Seat("/images/p.png", 47, y + 135));
                //一口价
                seats.add(ImageUtils.getSeat(lick.getBuyoutPrice().toString(), x, y + 180, COLOR_NODE));
                seats.add(new Seat("/images/p.png", 47, y + 185));
                //最高价
                if (lick.getTopBid() != null) {
                    seats.add(ImageUtils.getSeat(lick.getTopBid().toString(), x, y + 230, COLOR_NODE));
                    seats.add(new Seat("/images/p.png", 47, y + 235));
                } else {
                    seats.add(ImageUtils.getSeat("无", x, y + 230));
                    seats.add(new Seat("/images/p.png", 47, y + 235));
                }
                String element = SpringUtils.getBean(IWarframeMarketElementService.class).selectWarframeMarketElementByElementEn(lick.getItem().getElement()).getElementCh();
                //元素
                seats.add(ImageUtils.getSeat(element, x, y + 280, COLOR_NODE));
                seats.add(new Seat("/images/p.png", 47, y + 285));
                //加成数值
                seats.add(ImageUtils.getSeat(lick.getItem().getDamage().toString(), x, y + 330, COLOR_NODE));
                seats.add(new Seat("/images/p.png", 47, y + 335));
                //幻纹
                if (lick.getItem().getHavingEphemera()) {
                    if (WarframeTypeEnum.TYPE_MARKET_CD == type) {
                        seats.add(ImageUtils.getSeat(lichService.selectWarframeMarketLichByElement(lick.getItem().getElement()).getItemName(), x, y + 380, COLOR_NODE));
                    } else {
                        seats.add(ImageUtils.getSeat(sisterService.selectWarframeMarketSisterByElement(lick.getItem().getElement()).getItemName(), x, y + 380, COLOR_NODE));
                    }

                    seats.add(new Seat("/images/p.png", 47, y + 385));
                } else {
                    seats.add(ImageUtils.getSeat("无", x, y + 380));
                    seats.add(new Seat("/images/p.png", 47, y + 385));
                }
                //上架日期
                seats.add(ImageUtils.getSeat(DateUtils.getDateSize(new Date(), lick.getUpdated()), x, y + 430, COLOR_NODE));
                seats.add(new Seat("/images/p.png", 47, y + 435));
                //武器名
                seats.add(ImageUtils.getSeat(licksOrSister.getItemName(), x, y + 480, COLOR_G));
                seats.add(new Seat("/images/p.png", 47, y + 485));
                seats.add(ImageUtils.getSeat(StringUtils.convertToCamelCaseK(lick.getItem().getWeaponUrlName()), x, y + 530, COLOR_G));
                seats.add(new Seat("/images/p.png", 47, y + 535));
                x = x + ImageUtils.getFortWidth(lick.getOwner().getIngameName()) + 40;
            }
            seats.add(ImageUtils.getSeat("快捷回复:", 80, y + 600, COLOR_RIVEN_MOD));
            seats.add(ImageUtils.getSeat("/w " + licksOrSister.getPayload().getAuctions().get(0).getOwner().getIngameName() + " Hi! ", 80, y + 625, COLOR_RIVEN_MOD));
            if (WarframeTypeEnum.TYPE_MARKET_CD == type) {
                seats.add(ImageUtils.getSeat("I want to buy: " + StringUtils.convertToCamelCaseK(licksOrSister.getPayload().getAuctions().get(0).getItem().getWeaponUrlName()) + "(Kuva Lich),", 80, y + 650, COLOR_RIVEN_MOD));
            } else {
                seats.add(ImageUtils.getSeat("I want to buy: " + StringUtils.convertToCamelCaseK(licksOrSister.getPayload().getAuctions().get(0).getItem().getWeaponUrlName()) + "(Sisters of Parvos),", 80, y + 650, COLOR_RIVEN_MOD));
            }
            seats.add(ImageUtils.getSeat("what's your offer? (warframe.market)", 80, y + 675, COLOR_RIVEN_MOD));
        } else {
            image = ImageUtils.getImage("/images/s.png");
            seats.add(ImageUtils.getSeat("未查询到您要查询的物品！", 180, 82, COLOR_NODE));
        }
        //return ImageUtils.getBufferedImage(image, seats);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assert image != null;
        Thumbnails.of(ImageUtils.getBufferedImage(image, seats)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        response.getOutputStream().write(out.toByteArray());
    }
}
