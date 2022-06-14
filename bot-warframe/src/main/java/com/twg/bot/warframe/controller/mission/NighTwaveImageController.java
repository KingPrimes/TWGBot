package com.twg.bot.warframe.controller.mission;

import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.dao.Nightwave;
import com.twg.bot.warframe.utils.WarframeUtils;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.Seat;
import com.twg.common.utils.image.ImageUtils;
import com.twg.common.utils.spring.SpringUtils;
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
import java.util.List;

import static com.twg.common.utils.ColorEnum.*;

@RestController
@RequestMapping("/warframe/mission")
public class NighTwaveImageController {

    @Autowired
    RedisCache redis;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getNighTwaveImage")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/invasions/invasion.png");
        List<Seat> seats = new ArrayList<>();

        Nightwave n = SpringUtils.getBean(WarframeUtils.class).getNighTwave();
        seats.add(ImageUtils.getSeat("---每日挑战---", 280, 80, COLOR_BACK));
        int x = 62, y = 110;
        for (GlobalStates.Nightwave.ActiveChallenges c : n.getDaily()) {
            seats.add(ImageUtils.getSeat((c.getDesc()) + "(" + c.getReputation() + ")", x, y, COLOR_TAN));
            y += 30;
        }
        y += 30;
        seats.add(ImageUtils.getSeat("---每周挑战---", 280, y, COLOR_BACK));
        y += 30;
        for (GlobalStates.Nightwave.ActiveChallenges c : n.getWeek()) {
            seats.add(ImageUtils.getSeat((c.getDesc()) + "(" + c.getReputation() + ")", x, y));
            y += 30;
        }
        y += 30;
        seats.add(ImageUtils.getSeat("---精英挑战---", 280, y, COLOR_BACK));
        y += 30;
        for (GlobalStates.Nightwave.ActiveChallenges c : n.getElite()) {
            seats.add(ImageUtils.getSeat(c.getDesc() + "(" + c.getReputation() + ")", x, y));
            y += 30;
        }
        y += 30;
        seats.add(ImageUtils.getSeat("到现在电波开启时长: " + n.getStartString(), x, y, COLOR_NODE));
        assert image != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(ImageUtils.getBufferedImage(image, seats)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        response.getOutputStream().write(out.toByteArray());

    }
}
