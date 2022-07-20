package com.twg.bot.warframe.controller.market;


import com.twg.bot.warframe.dao.Market;
import com.twg.bot.warframe.dao.MarketStatistic;
import com.twg.bot.warframe.utils.market.MarketItemUtil;
import com.twg.common.utils.Seat;
import com.twg.common.utils.StringUtils;
import com.twg.common.utils.image.ImageUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import net.coobird.thumbnailator.Thumbnails;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.twg.common.utils.ColorEnum.*;


/**
 * @author KingPrimes
 */
@RestController
@RequestMapping("/warframe/market")
public class MarektImageController {

    private Market market;
    private boolean seBy;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getMarektImage/{key}/{seBy}/{isMax}/{form}", produces = MediaType.IMAGE_PNG_VALUE)
    public synchronized void getImage(@NotNull HttpServletResponse response, @PathVariable String key, @PathVariable Boolean seBy, @PathVariable Boolean isMax, @PathVariable String form) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/invasions/invasion.png");
        List<Seat> seats = new ArrayList<>();
        this.seBy = seBy;

        market = new MarketItemUtil().market(form, key, seBy, isMax);

        if (market.getCode().equals("timeout")) {
            image = ImageUtils.getImage("/images/s.png");
            seats.add(ImageUtils.getSeat("请求超时请稍后重试！!", 180, 82, COLOR_NODE));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
            response.getOutputStream().write(out.toByteArray());
            return;
        }
        if (market.getPayload().getOrders().size() != 0) {
            MarketStatistic statistic = new MarketItemUtil().marketStatistics(key);
            if (isMax) {
                seats.add(ImageUtils.getSeat("满级", 785, 130, COLOR_RIVEN_MOD));
            }
            if (seBy) {
                seats.add(ImageUtils.getSeat("--以下是购买", 150, 80, COLOR_BACK));
            } else {
                seats.add(ImageUtils.getSeat("--以下是出售", 150, 80, COLOR_BACK));
            }
            int k;
            k = ImageUtils.getFortWidth("--以下是出售") + 150;
            seats.add(ImageUtils.getSeat("[ " + market.getInclude().getItem().getItems_in_set().get(0).getZhHans().getItem_name() + " ]", k, 80, COLOR_NODE));
            k += ImageUtils.getFortWidth("--以下是出售[ " + market.getInclude().getItem().getItems_in_set().get(0).getZhHans().getItem_name() + " ]") - 140;
            seats.add(ImageUtils.getSeat("的玩家--", k, 80, COLOR_BACK));
            seats.add(ImageUtils.getSeat("玩家名", 62, 130, COLOR_BACK));
            seats.add(ImageUtils.getSeat("数量", 450, 130, COLOR_BACK));
            seats.add(ImageUtils.getSeat("白金", 580, 130, COLOR_BACK));
            int x = 62, y = 160, g = 610;
            for (Market.Orders orders : market.getPayload().getOrders()) {
                seats.add(ImageUtils.getSeat("[ " + orders.getUser().getIngameName() + " ]", x, y, COLOR_NODE));
                x = 460;
                seats.add(ImageUtils.getSeat(String.valueOf(orders.getQuantity()), x, y));
                x = 580;
                seats.add(ImageUtils.getSeat(String.valueOf(orders.getPlatinum()), x, y, COLOR_CHIEF));
                seats.add(new Seat("/images/market/PlatinumLarge.png", x + 80, y - 20));
                y += 30;
                x = 62;
            }
            seats.add(ImageUtils.getSeat("--物品介绍--", 200, g, COLOR_BACK));
            g += 30;
            for (String str : StringUtils.insteadOfrnStrings(market.getInclude().getItem().getItems_in_set().get(0).getZhHans().getDescription(), 28)) {
                seats.add(ImageUtils.getSeat(str, 62, g, COLOR_G));
                g += 30;
            }
            g += 10;
            if (market.getInclude().getItem().getItems_in_set().get(0).getZhHans().getDrop().size() != 0) {
                seats.add(ImageUtils.getSeat("--包含" + "[ " + market.getInclude().getItem().getItems_in_set().get(0).getZhHans().getItem_name() + " ]物品的遗物", 150, g, COLOR_BACK));
                g += 30;
                x = 65;
                int j = 0;
                for (Market.Drop drop : market.getInclude().getItem().getItems_in_set().get(0).getZhHans().getDrop()) {
                    if (j % 3 == 0 && j != 0) {
                        g += 30;
                        x = 65;
                    }
                    seats.add(ImageUtils.getSeat(drop.getName(), x, g, COLOR_NODE));
                    x += ImageUtils.getFortWh(drop.getName()) + 70;
                    j++;
                }
            }
            x = 62;
            seats.add(ImageUtils.getSeat("快捷回复指令：/w " + market.getPayload().getOrders().get(0).getUser().getIngameName() + " Hi I want to buy: ", x, g + 30));
            seats.add(ImageUtils.getSeat(StringUtils.convertToCamelCaseK(market.getInclude().getItem().getItems_in_set().get(0).getUrl_name()) + "\n for " + market.getPayload().getOrders().get(0).getPlatinum() + " Platinum (warframe.market)", x, g + 60));
            if (statistic != null) {
                y += 20;
                seats.add(ImageUtils.getSeat("--近48小时买/卖状况--", 200, y, COLOR_BACK));
                seats.add(ImageUtils.getSeat("买:", 62, y + 30, COLOR_BACK));
                seats.add(ImageUtils.getSeat("卖:", 62, y + 60, COLOR_BACK));
                y += 30;
                x = 100;
                for (MarketStatistic._$48hours hours : statistic.getPayload().getStatisticsLive().get$48hours()) {
                    if ("buy".equals(hours.getOrderType())) {
                        seats.add(ImageUtils.getSeat("最低价:", x, y, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("最低价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMinPrice()), x, y, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMinPrice()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y - 20));
                        x += 28;
                        seats.add(ImageUtils.getSeat("最高价:", x, y, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("最高价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMaxPrice()), x, y, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMaxPrice()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y - 20));
                        x += 28;
                        seats.add(ImageUtils.getSeat("均价:", x, y, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("均价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMedian()), x, y, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMedian()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y - 20));
                        x += 28;
                        seats.add(ImageUtils.getSeat("浮动:", x, y, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("浮动:") + 5;
                        seats.add(ImageUtils.getSeat(hours.getMovingAvg() + " ±", x, y, COLOR_NODE));
                        x += ImageUtils.getFortWh(hours.getMovingAvg() + " ±");
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y - 20));
                    } else {
                        seats.add(ImageUtils.getSeat("最低价:", x, y + 30, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("最低价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMinPrice()), x, y + 30, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMinPrice()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y + 10));
                        x += 28;
                        seats.add(ImageUtils.getSeat("最高价:", x, y + 30, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("最高价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMaxPrice()), x, y + 30, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMaxPrice()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y + 10));
                        x += 28;
                        seats.add(ImageUtils.getSeat("均价:", x, y + 30, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("均价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMedian()), x, y + 30, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMedian()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y + 10));
                        x += 28;
                        seats.add(ImageUtils.getSeat("浮动:", x, y + 30, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("浮动:") + 5;
                        seats.add(ImageUtils.getSeat(hours.getMovingAvg() + " ±", x, y + 30, COLOR_NODE));
                        x += ImageUtils.getFortWh(hours.getMovingAvg() + " ±");
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y + 10));
                    }
                    x = 100;
                }
                y += 80;
                seats.add(ImageUtils.getSeat("--近90天买/卖状况--", 200, y, COLOR_BACK));
                seats.add(ImageUtils.getSeat("买:", 62, y + 30, COLOR_BACK));
                seats.add(ImageUtils.getSeat("卖:", 62, y + 60, COLOR_BACK));
                y += 30;
                x = 100;
                for (MarketStatistic._$90days hours : statistic.getPayload().getStatisticsLive().get$90days()) {
                    if ("buy".equals(hours.getOrderType())) {
                        seats.add(ImageUtils.getSeat("最低价:", x, y, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("最低价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMinPrice()), x, y, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMinPrice()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y - 20));
                        x += 28;
                        seats.add(ImageUtils.getSeat("最高价:", x, y, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("最高价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMaxPrice()), x, y, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMaxPrice()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y - 20));
                        x += 28;
                        seats.add(ImageUtils.getSeat("均价:", x, y, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("均价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMedian()), x, y, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMedian()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y - 20));
                        x += 28;
                        seats.add(ImageUtils.getSeat("浮动:", x, y, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("浮动:") + 5;
                        seats.add(ImageUtils.getSeat(hours.getMovingAvg() + " ±", x, y, COLOR_NODE));
                        x += ImageUtils.getFortWh(hours.getMovingAvg() + " ±");
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y - 20));
                        x = 100;
                    } else {
                        seats.add(ImageUtils.getSeat("最低价:", x, y + 30, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("最低价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMinPrice()), x, y + 30, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMinPrice()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y + 10));
                        x += 28;
                        seats.add(ImageUtils.getSeat("最高价:", x, y + 30, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("最高价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMaxPrice()), x, y + 30, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMaxPrice()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y + 10));
                        x += 28;
                        seats.add(ImageUtils.getSeat("均价:", x, y + 30, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("均价:") + 5;
                        seats.add(ImageUtils.getSeat(String.valueOf(hours.getMedian()), x, y + 30, COLOR_NODE));
                        x += ImageUtils.getFortWh(String.valueOf(hours.getMedian()));
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y + 10));
                        x += 28;
                        seats.add(ImageUtils.getSeat("浮动:", x, y + 30, COLOR_DUCATS));
                        x += ImageUtils.getFortWh("浮动:") + 5;
                        seats.add(ImageUtils.getSeat(hours.getMovingAvg() + " ±", x, y + 30, COLOR_NODE));
                        x += ImageUtils.getFortWh(hours.getMovingAvg() + " ±");
                        seats.add(new Seat("/images/market/PlatinumLarge.png", x, y + 10));
                        x = 100;
                    }
                }
            }
        } else {
            image = ImageUtils.getImage("/images/s.png");
            seats.add(ImageUtils.getSeat("您查询的物品当前无在线玩家售卖!", 180, 82, COLOR_NODE));
            seats.add(ImageUtils.getSeat("您查询的物品或许是不可交易!", 180, 110, COLOR_NODE));
            seats.add(ImageUtils.getSeat("发送 wiki 物品名 查看物品是否可以交易!", 180, 138, COLOR_NODE));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assert image != null;
        Thumbnails.of(ImageUtils.getBufferedImage(image, seats)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        response.getOutputStream().write(out.toByteArray());
    }

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getMarketInfo")
    public String getMarketInfo() {
        StringBuilder str = new StringBuilder();
        if (market != null) {
            final Market.Orders orders = market.getPayload().getOrders().get(0);
            if (seBy) {
                str.append("/w ")
                        .append(orders.getUser().getIngameName())
                        .append(" Hi! I want to sell: ")
                        .append(StringUtils.convertToCamelCaseK(market.getInclude().getItem().getItems_in_set().get(0).getUrl_name()))
                        .append(" for ")
                        .append(orders.getPlatinum())
                        .append(" platinum. (warframe.market)");
            } else {
                str.append("/w ")
                        .append(orders.getUser().getIngameName())
                        .append(" Hi! I want to buy: ")
                        .append(StringUtils.convertToCamelCaseK(market.getInclude().getItem().getItems_in_set().get(0).getUrl_name()))
                        .append(" for ")
                        .append(orders.getPlatinum())
                        .append(" platinum. (warframe.market)");
            }

        }
        return str.toString();
    }

}
