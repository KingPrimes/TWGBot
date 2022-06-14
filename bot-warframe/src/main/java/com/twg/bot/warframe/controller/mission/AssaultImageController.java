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
import static com.twg.common.utils.ColorEnum.COLOR_BACK;
import static com.twg.common.utils.ColorEnum.COLOR_CHIEF;

@RestController
@RequestMapping("/warframe/mission")
public class AssaultImageController {

    @Autowired
    RedisCache redisCache;
    @Autowired
    IWarframeTranslationService trans;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getAssaultImage")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/assault/assault.png");
        List<Seat> seats = new ArrayList<>();
        Date date = new Date();
        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        GlobalStates.Sortie assault = sgs.getPacket().getData().getSortie();

        assault.setEta(DateUtils.getDate((assault.getExpiry()), date));
        seats.add(ImageUtils.getSeat("[ " + assault.getBoss() + " ]", 197, 80));
        seats.add(ImageUtils.getSeat("主导的突击任务", 197 + ImageUtils.getFortWidth("[ " + assault.getBoss() + " ]") + 20, 80, COLOR_BACK));
        seats.add(ImageUtils.getSeat("[ " + assault.getFaction() + " ]", 197, 121));
        seats.add(ImageUtils.getSeat(assault.getEta(), 197, 158, COLOR_CHIEF));
        int y = 242;
        for (GlobalStates.Sortie.Variants variants : assault.getVariants()) {
            String node = variants.getNode()
                    .replace(
                            StringUtils.quStr(
                                    variants.getNode()),
                            trans.enToZh(StringUtils.quStr(variants.getNode())));
            seats.add(ImageUtils.getSeat(node, 197, y));
            y += 40;
            seats.add(ImageUtils.getSeat(trans.enToZh(variants.getMissionType()), 197, y, COLOR_CHIEF));
            y += 40;
            seats.add(ImageUtils.getSeat(trans.enToZh(variants.getModifier()), 197, y));
            y += 60;
        }
        assert image != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(ImageUtils.getBufferedImage(image, seats)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        response.getOutputStream().write(out.toByteArray());
    }
}
