package com.twg.bot.warframe.controller.mission;


import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.dao.SocketGlobalStates;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.DateUtils;
import com.twg.common.utils.Fonts;
import com.twg.common.utils.image.ImageUtils;
import com.twg.common.utils.image.combiner.ImageCombiner;
import com.twg.common.utils.image.combiner.enums.OutputFormat;
import com.twg.framework.interceptor.IgnoreAuth;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Locale;

import static com.twg.bot.enums.WarframeTypeEnum.REDIS_MISSION_KEY;
import static com.twg.common.utils.ColorEnum.*;

@RestController
@RequestMapping("/warframe/mission")
public class AllCycleImageController {

    @Autowired
    RedisCache redisCache;
    @Autowired
    IWarframeTranslationService trans;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getAllCycleImage")
    public void getImage(HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "image/png");
        ImageUtils imageUtils = new ImageUtils();
        BufferedImage image = new ImageCombiner(imageUtils.reseauImage(400, 400), OutputFormat.PNG)
                .setCanvasRoundCorner(40)
                .combine();
        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        GlobalStates globalState = sgs.getPacket().getData();
        //地球
        GlobalStates.EarthCycle earthCycle = globalState.getEarthCycle();
        //夜灵平野
        GlobalStates.CetusCycle cetusCycle = globalState.getCetusCycle();
        //福尔图娜
        GlobalStates.VallisCycle vallisCycle = globalState.getVallisCycle();
        //魔胎之境
        GlobalStates.CambionCycle cambionCycle = globalState.getCambionCycle();
        //创建图片
        ImageCombiner combiner = new ImageCombiner(image, OutputFormat.PNG);
        //设置字体
        combiner.setFont(Fonts.FONT_SOURCE_CN_MEDIUM_BOLD);
        //设置字体
        imageUtils.setFont(Fonts.FONT_SOURCE_CN_MEDIUM_BOLD);
        //设置背景颜色
        imageUtils.setBackgroundColor(COLOR_CHIEF.getColor());


        int xd, xx, tx = 110, x = 10;

        //分割线
        BufferedImage p = ImageUtils.getImage("/images/p.png");
        BufferedImage now = imageUtils.followTextSizeToImage("状态:", new Color(0, 0, 0, 80), new Color(186, 163, 191));
        BufferedImage next = imageUtils.followTextSizeToImage("时间:", new Color(0, 0, 0, 80), new Color(186, 163, 191));
        xd = now.getWidth() + x;
        xx = next.getWidth() + x + 40;
        //地球
        {

            combiner.addTextElement("-----地球-----", tx, 0).setColor(COLOR_TEST.getColor());
            combiner.addImageElement(now, x, 30);

            BufferedImage earth;
            if (earthCycle.getIsDay()) {
                earth = ImageUtils.getImage("/images/cycle/sun.png");

            } else {
                earth = ImageUtils.getImage("/images/cycle/noon.png");

            }
            earth = Thumbnails.of(earth).size(30, 30).asBufferedImage();
            combiner.addImageElement(earth, xd, 30);
            if (earthCycle.getIsDay()) {
                combiner.addTextElement(trans.enToZh(earthCycle.getState()), xd + 40, 30).setColor(COLOR_TIME_FONT.getColor());
                combiner.addTextElement(DateUtils.getDate((earthCycle.getExpiry()), new Date()), xx, 60).setColor(COLOR_TIME_FONT.getColor());
            } else {
                combiner.addTextElement(trans.enToZh(earthCycle.getState()), xd + 40, 30).setColor(COLOR_RIVEN_MOD.getColor());
                combiner.addTextElement(DateUtils.getDate((earthCycle.getExpiry()), new Date()), xx, 60).setColor(COLOR_RIVEN_MOD.getColor());
            }
            combiner.addImageElement(next, x, 60);

            combiner.addImageElement(p, 0, 90);
        }
        //夜灵平野
        {
            combiner.addTextElement("----夜灵平野----", tx, 100).setColor(COLOR_TEST.getColor());
            combiner.addImageElement(now, x, 130);

            BufferedImage cetus;
            if (cetusCycle.getIsDay()) {
                cetus = ImageUtils.getImage("/images/cycle/sun.png");
            } else {
                cetus = ImageUtils.getImage("/images/cycle/noon.png");
            }
            cetus = Thumbnails.of(cetus).size(30, 30).asBufferedImage();
            combiner.addImageElement(cetus, xd, 130);
            if (cetusCycle.getIsDay()) {
                combiner.addTextElement(trans.enToZh(cetusCycle.getState()), xd + 40, 130).setColor(COLOR_TIME_FONT.getColor());
                combiner.addTextElement(DateUtils.getDate((cetusCycle.getExpiry()), new Date()), xx, 160).setColor(COLOR_TIME_FONT.getColor());
            } else {
                combiner.addTextElement(trans.enToZh(cetusCycle.getState()), xd + 40, 130).setColor(COLOR_RIVEN_MOD.getColor());
                combiner.addTextElement(DateUtils.getDate((cetusCycle.getExpiry()), new Date()), xx, 160).setColor(COLOR_RIVEN_MOD.getColor());
            }
            combiner.addImageElement(next, x, 160);

            combiner.addImageElement(p, 0, 190);
        }
        //福尔图娜
        {
            combiner.addTextElement("----福尔图娜----", tx, 200).setColor(COLOR_TEST.getColor());
            combiner.addImageElement(now, x, 230);

            BufferedImage vallis;
            if (!vallisCycle.getIsWarm()) {
                vallis = ImageUtils.getImage("/images/cycle/cold.png");
            } else {
                vallis = ImageUtils.getImage("/images/cycle/warm.png");
            }

            vallis = Thumbnails.of(vallis).size(30, 30).asBufferedImage();
            combiner.addImageElement(vallis, xd, 230);
            if (!vallisCycle.getIsWarm()) {
                combiner.addTextElement(trans.enToZh(vallisCycle.getState()), xd + 40, 230).setColor(COLOR_RIVEN_MOD.getColor());
                combiner.addTextElement(DateUtils.getDate((vallisCycle.getExpiry()), new Date()), xx, 260).setColor(COLOR_RIVEN_MOD.getColor());
            } else {
                combiner.addTextElement(trans.enToZh(vallisCycle.getState()), xd + 40, 230).setColor(COLOR_TIME_FONT.getColor());
                combiner.addTextElement(DateUtils.getDate((vallisCycle.getExpiry()), new Date()), xx, 260).setColor(COLOR_TIME_FONT.getColor());
            }
            combiner.addImageElement(next, x, 260);

            combiner.addImageElement(p, 0, 290);
        }
        //魔胎之境
        {
            combiner.addTextElement("----魔胎之境----", tx, 300).setColor(COLOR_TEST.getColor());
            combiner.addImageElement(now, x, 330);
            BufferedImage cambion = ImageUtils.getImage("/images/cycle/" + cambionCycle.getActive().toUpperCase(Locale.ROOT) + ".png");
            cambion = Thumbnails.of(cambion).size(30, 30).asBufferedImage();
            combiner.addImageElement(cambion, xd, 330);
            if (cambionCycle.getActive().trim().toLowerCase(Locale.ROOT).equals("vome")) {
                combiner.addTextElement(cambionCycle.getActive().toUpperCase(Locale.ROOT), xd + 40, 330).setColor(COLOR_RIVEN_MOD.getColor());
                combiner.addTextElement(DateUtils.getDate((cambionCycle.getExpiry()), new Date()), xx, 360).setColor(COLOR_RIVEN_MOD.getColor());
            } else {
                combiner.addTextElement(cambionCycle.getActive().toUpperCase(Locale.ROOT), xd + 40, 330).setColor(new Color(234, 105, 125));
                combiner.addTextElement(DateUtils.getDate((cambionCycle.getExpiry()), new Date()), xx, 360).setColor(new Color(234, 105, 125));
            }
            combiner.addImageElement(next, x, 360);

            combiner.addImageElement(p, 0, 390);
        }
        combiner.combine();
        ByteArrayOutputStream out = (ByteArrayOutputStream) combiner.getCombinedImageOutStream();
        response.getOutputStream().write(out.toByteArray());

    }
}
