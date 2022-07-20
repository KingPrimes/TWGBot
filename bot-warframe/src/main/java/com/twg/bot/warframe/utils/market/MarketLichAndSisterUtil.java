package com.twg.bot.warframe.utils.market;

import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.twg.bot.enums.FunctionEnums;
import com.twg.bot.enums.WarframeTypeEnum;
import com.twg.bot.utils.ErroSendMessage;
import com.twg.bot.utils.Msg;
import com.twg.bot.utils.SelectGroupFunctionOnOff;
import com.twg.bot.warframe.dao.MarketLichOrSister;
import com.twg.bot.warframe.domain.market.WarframeMarketLichOrSister;
import com.twg.bot.warframe.service.IWarframeMarketLichOrSisterService;
import com.twg.bot.warframe.service.IWarframeMarketSisterService;
import com.twg.common.utils.http.HttpUtils;
import com.twg.common.utils.ip.GetServerPort;
import com.twg.common.utils.spring.SpringUtils;
import com.twg.common.utils.uuid.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Component
public class MarketLichAndSisterUtil {

    /**
     * 查询Market上赤毒武器与幻纹的售卖
     *
     * @param bot   bot
     * @param event event
     */
    public static int marketLich(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        //核查是否开启Warframe功能
        if (SelectGroupFunctionOnOff.getGroupFunctionOnOff(event.getGroupId(), FunctionEnums.FUNCTION_WARFRAME)) {
            String key = event.getRawMessage().toUpperCase(Locale.ROOT).replace(WarframeTypeEnum.TYPE_CD_PLUGIN.getType(), "").replace(WarframeTypeEnum.TYPE_C_PLUGIN.getType(), "").trim();
            try {
                //构造请求地址
                String url = "http://localhost:" +
                        GetServerPort.getPort() +
                        "/warframe/market/" + UUID.fastUUID() + "/getLichOrSisterImage/" +
                        URLEncoder.encode(key, "UTF-8") +
                        "/" + WarframeTypeEnum.TYPE_MARKET_CD;
                //发送消息
                bot.sendGroupMsg(event.getGroupId(), Msg
                                .builder()
                                .img(url)
                                .build(),
                        false);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            //提醒未开启此功能
            return ErroSendMessage.getFunctionOff(bot, event, FunctionEnums.FUNCTION_WARFRAME);
        }
        return 0;
    }


    /**
     * 查询Market上信条武器与幻纹的售卖
     *
     * @param bot   bot
     * @param event event
     */
    public static void marketSister(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        //核查是否开启Warframe功能
        if (SelectGroupFunctionOnOff.getGroupFunctionOnOff(event.getGroupId(), FunctionEnums.FUNCTION_WARFRAME)) {
            String key = event.getRawMessage().toUpperCase(Locale.ROOT).replace(WarframeTypeEnum.TYPE_XT_PLUGIN.getType(), "").replace(WarframeTypeEnum.TYPE_X_PLUGIN.getType(), "").trim();
            try {
                //构造请求地址
                String url = "http://localhost:" +
                        GetServerPort.getPort() +
                        "/warframe/market/" + UUID.fastUUID() + "/getLichOrSisterImage/" +
                        URLEncoder.encode(key, "UTF-8") +
                        "/" + WarframeTypeEnum.TYPE_MARKET_XT;
                //发送消息
                bot.sendGroupMsg(event.getGroupId(), Msg
                                .builder()
                                .img(url)
                                .build(),
                        false);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            //提醒未开启此功能
            ErroSendMessage.getFunctionOff(bot, event, FunctionEnums.FUNCTION_WARFRAME);
        }
    }

    /**
     * 查询Market赤毒武器与幻纹
     *
     * @param key 关键字
     * @return 实体类
     */
    public static MarketLichOrSister getMarketLich(String key) {
        try {
            //从数据库中查询相匹配的值
            WarframeMarketLichOrSister lich = SpringUtils.getBean(IWarframeMarketLichOrSisterService.class).selectWarframeMarketLichByItemName(key);
            MarketLichOrSister marketLich;
            String url;
            //判断是否查询幻纹
            if (lich.getElement() == null) {
                //不带幻纹
                url = "https://api.warframe.market/v1/auctions/search?" +
                        "type=lich&weapon_url_name=" + lich.getUrlName() + "&sort_by=price_asc";
            } else {
                //携带幻纹
                url = "https://api.warframe.market/v1/auctions/search?" +
                        "type=lich&element=" + lich.getElement() + "&having_ephemera=true&sort_by=price_asc";
            }
            marketLich = JSONObject.parseObject(
                    HttpUtils.sendGetOkHttp(url),
                    MarketLichOrSister.class);

            //创建新的变量用来接收排序完的数据
            MarketLichOrSister.Payload payload = new MarketLichOrSister.Payload();
            //排序数据
            payload.setAuctions(auctionsLichList(marketLich));
            //设置排序完成的数据
            marketLich.setPayload(payload);
            //设置该物品的中文名称
            marketLich.setItemName(lich.getItemName());
            //返回结果集
            return marketLich;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查询Market信条武器与幻纹
     *
     * @param key 关键字
     * @return 实体类
     */
    public static MarketLichOrSister getMarketSister(String key) {
        try {
            WarframeMarketLichOrSister sister = SpringUtils.getBean(IWarframeMarketSisterService.class).selectWarframeMarketSisterByItemName(key);
            MarketLichOrSister marketSister;
            String url;
            //判断是否查询幻纹
            if (sister.getElement() == null) {
                //不带幻纹
                url = "https://api.warframe.market/v1/auctions/search?" +
                        "type=sister&weapon_url_name=" + sister.getUrlName() + "&sort_by=price_asc";

            } else {
                //携带幻纹
                url = "https://api.warframe.market/v1/auctions/search?" +
                        "type=sister&element=" + sister.getElement() + "&having_ephemera=true&sort_by=price_asc";
            }
            marketSister = JSONObject.parseObject(
                    HttpUtils.sendGetOkHttp(url),
                    MarketLichOrSister.class);
            //创建新的变量用来接收排序完的数据
            MarketLichOrSister.Payload payload = new MarketLichOrSister.Payload();
            //排序数据
            payload.setAuctions(auctionsLichList(marketSister));
            //设置排序完成的数据
            marketSister.setPayload(payload);
            //设置该物品的中文名称
            marketSister.setItemName(sister.getItemName());
            //返回结果集
            return marketSister;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 处理MarketLich 由小到大排序
     *
     * @param lichOrSister 数据
     * @return 排序结果
     */
    private static List<MarketLichOrSister.Auctions> auctionsLichList(MarketLichOrSister lichOrSister) {
        List<MarketLichOrSister.Auctions> auctions = new ArrayList<>();
        for (MarketLichOrSister.Auctions auction : lichOrSister.getPayload().getAuctions()) {
            //判断是否是在线玩家
            if (auction.getOwner().getStatus().equals("ingame") && !auction.getClosed() && auction.getVisible()) {
                auctions.add(auction);
            }
        }
        //由小到大排序
        auctions.sort(Comparator.comparing(MarketLichOrSister.Auctions::getStartingPrice));
        if (auctions.size() > 4) {
            auctions = auctions.subList(0, 3);
        }
        return auctions;
    }


}
