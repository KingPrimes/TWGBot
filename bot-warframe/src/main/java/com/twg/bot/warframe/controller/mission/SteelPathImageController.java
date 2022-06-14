package com.twg.bot.warframe.controller.mission;

import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.dao.SocketGlobalStates;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.DateUtils;
import com.twg.common.utils.Seat;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.twg.bot.enums.WarframeTypeEnum.REDIS_MISSION_KEY;
import static com.twg.common.utils.ColorEnum.*;

@RestController
@RequestMapping("/warframe/mission")
public class SteelPathImageController {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IWarframeTranslationService trans;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getSteelPathImage")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/s.png");

        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        GlobalStates.SteelPath steelPath = sgs.getPacket().getData().getSteelPath();


        List<Seat> seats = new ArrayList<>();
        seats.add(ImageUtils.getSeat("--每周钢铁奖励轮回--", 260, 80, COLOR_G));
        seats.add(ImageUtils.getSeat("本次奖励:", 62, 120, COLOR_BACK));
        seats.add(ImageUtils.getSeat(trans.enToZh(steelPath.getCurrentReward().getName()), 62 + ImageUtils.getFortWidth("本次奖励:"), 120));
        seats.add(ImageUtils.getSeat(steelPath.getCurrentReward().getCost() + " 精华", 70 + ImageUtils.getFortWidth("本次奖励:" + steelPath.getCurrentReward().getName()), 120, COLOR_NODE));
        seats.add(ImageUtils.getSeat("剩余时间:", 62, 150, COLOR_BACK));
        seats.add(ImageUtils.getSeat(DateUtils.getDateWeek(steelPath.getActivation(), new Date(), Calendar.DAY_OF_MONTH, 7), ImageUtils.getFortWidth("剩余时间:") + 62, 150));
        seats.add(ImageUtils.getSeat("下次奖励:", 62, 180, COLOR_BACK));
        String key = "";
        for (int i = 0; i < steelPath.getRotation().size(); i++) {
            if (steelPath.getCurrentReward().getName().equals(steelPath.getRotation().get(i).getName())) {
                if (i + 1 < steelPath.getRotation().size()) {
                    key = steelPath.getRotation().get(i + 1).getName();
                } else {
                    key = steelPath.getRotation().get(0).getName();
                }
                break;
            }
        }
        key = trans.enToZh(key);
        seats.add(ImageUtils.getSeat(key, ImageUtils.getFortWidth("下次奖励:") + 62, 180, COLOR_NODE));
        assert image != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(ImageUtils.getBufferedImage(image, seats)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        response.getOutputStream().write(out.toByteArray());
    }
}
