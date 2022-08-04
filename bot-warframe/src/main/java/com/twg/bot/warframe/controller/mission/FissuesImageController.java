package com.twg.bot.warframe.controller.mission;


import com.twg.bot.warframe.dao.FissureList;
import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.utils.HtmlToImage;
import com.twg.bot.warframe.utils.WarframeUtils;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.Seat;
import com.twg.common.utils.image.ImageUtils;
import com.twg.common.utils.spring.SpringUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.twg.common.utils.ColorEnum.*;

@RestController
@RequestMapping("/warframe/mission")
public class FissuesImageController {

    @Autowired
    RedisCache redisCache;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getFissuesImage")
    public void getImage(HttpServletResponse response) throws InterruptedException, IOException {
        response.setHeader("Content-Type", "image/png");
        FissureList fissureList = SpringUtils.getBean(WarframeUtils.class).getFissureList();
        ByteArrayOutputStream out = SpringUtils.getBean(HtmlToImage.class).fissuesImage(fissureList);
        response.getOutputStream().write(out.toByteArray());
        /*BufferedImage image = ImageUtils.getImage("/images/backimg.png");
        int x = 62, y = 80;
        List<Seat> seats = new ArrayList<>();
        seats.add(ImageUtils.getSeat("古纪(T1)", x, y, COLOR_MEIDIELV));
        seats.add(new Seat("/images/fissues/T1.png", x + 110, y - 26));
        y += 40;
        for (GlobalStates.Fissures fissure : fissureList.getT1()) {
            y = getAnInt(x, y, seats, fissure);
        }
        seats.add(new Seat("/images/p.png", 47, y - 25));
        y += 10;
        seats.add(ImageUtils.getSeat("前纪(T2)", x, y, COLOR_MEIDIELV));
        seats.add(new Seat("/images/fissues/T2.png", x + 110, y - 26));
        y += 40;
        for (GlobalStates.Fissures fissure : fissureList.getT2()) {
            y = getAnInt(x, y, seats, fissure);
        }

        seats.add(new Seat("/images/p.png", 47, y - 25));
        y += 10;
        seats.add(ImageUtils.getSeat("中纪(T3)", x, y, COLOR_MEIDIELV));
        seats.add(new Seat("/images/fissues/T3.png", x + 110, y - 26));
        y += 40;
        for (GlobalStates.Fissures fissure : fissureList.getT3()) {
            y = getAnInt(x, y, seats, fissure);
        }
        seats.add(new Seat("/images/p.png", 47, y - 25));
        y += 10;
        seats.add(ImageUtils.getSeat("后纪(T4)", x, y, COLOR_MEIDIELV));
        seats.add(new Seat("/images/fissues/T4.png", x + 110, y - 26));
        y += 40;
        for (GlobalStates.Fissures fissure : fissureList.getT4()) {
            y = getAnInt(x, y, seats, fissure);
        }

        seats.add(ImageUtils.getSeat("安魂(T5)", x, y, COLOR_MEIDIELV));
        seats.add(new Seat("/images/fissues/T5.png", x + 110, y - 26));
        y += 40;
        for (GlobalStates.Fissures fissure : fissureList.getT5()) {
            y = getAnInt(x, y, seats, fissure);
        }
        assert image != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        Thumbnails.of(ImageUtils.getBufferedImage(image, seats)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        response.getOutputStream().write(out.toByteArray());*/

    }

    private int getAnInt(int x, int y, List<Seat> seats, GlobalStates.Fissures fissure) {
        seats.add(ImageUtils.getSeat(fissure.getNode(), x, y, COLOR_TENGLUOZI));
        seats.add(ImageUtils.getSeat(fissure.getMissionType(), x + 320, y, COLOR_NODE));
        seats.add(ImageUtils.getSeat(fissure.getEta(), x + 450, y, COLOR_MINOR));
        y += 30;
        return y;
    }
}
