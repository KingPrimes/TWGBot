package com.twg.bot.warframe.controller.mission;


import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.dao.SocketGlobalStates;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.DateUtils;
import com.twg.common.utils.Seat;
import com.twg.common.utils.StringUtils;
import com.twg.common.utils.image.ImageUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.twg.bot.enums.WarframeTypeEnum.REDIS_MISSION_KEY;
import static com.twg.common.utils.ColorEnum.*;

@RestController
@RequestMapping("/warframe/mission")
public class VoidImageController {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IWarframeTranslationService trans;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getVoidImage")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage bufferedImage;

        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        GlobalStates.VoidTrader v = sgs.getPacket().getData().getVoidTrader();

        String location = v.getLocation().replace(
                StringUtils.quStr(v.getLocation()),
                trans.enToZh(StringUtils.quStr(v.getLocation()))
        );
        List<Seat> seatList = new ArrayList<>();
        if (!v.getActive()) {
            bufferedImage = ImageUtils.getImage("/images/s.png");
            seatList.add(ImageUtils.getSeat("虚空商人所在地:", 62, 80));
            seatList.add(ImageUtils.getSeat(location, 62 + ImageUtils.getFortWidth("虚空商人所在地:"), 80, COLOR_CHIEF));
            seatList.add(ImageUtils.getSeat("距离虚空商人到来:", 62, 110));
            seatList.add(ImageUtils.getSeat(DateUtils.getDate(v.getActivation(), new Date()), 62 + ImageUtils.getFortWidth("距离虚空商人到来:"), 110, COLOR_NODE));
        } else {
            bufferedImage = ImageUtils.getImage("/images/backimg.png");
            int x = 62;
            int y = 70;
            for (GlobalStates.VoidTrader.Inventory i : v.getInventory()) {
                seatList.add(ImageUtils.getSeat("[ " + trans.enToZh(i.getItem()) + " ]", x, y, COLOR_NODE));
                x += 460;
                seatList.add(ImageUtils.getSeat(i.getCredits() / 1000 + "k", x, y));
                x += 68;
                seatList.add(new Seat("/images/void/Credits.png", x, y));
                x += 90;
                seatList.add(ImageUtils.getSeat(String.valueOf(i.getDucats()), x, y, COLOR_DUCATS));
                x += 50;
                seatList.add(new Seat("/images/void/Ducats.png", x, y));
                x = 62;
                y += 30;
            }
            seatList.add(ImageUtils.getSeat("虚空商人所在地:", 62, y, COLOR_BACK));
            seatList.add(ImageUtils.getSeat(location, 62 + ImageUtils.getFortWidth("虚空商人所在地:"), y, COLOR_CHIEF));
            seatList.add(ImageUtils.getSeat("距离虚空商人离去:", 62, y + 25, COLOR_BACK));
            seatList.add(ImageUtils.getSeat(DateUtils.getDate(v.getExpiry(), new Date()), 62 + ImageUtils.getFortWidth("距离虚空商人到来:"), y + 25, COLOR_CHIEF));
        }
        assert bufferedImage != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(ImageUtils.getBufferedImage(bufferedImage, seatList)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(ImageUtils.getBufferedImage(bufferedImage, seatList), "png", out);
        response.getOutputStream().write(out.toByteArray());

    }
}
