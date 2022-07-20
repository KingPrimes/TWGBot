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
public class ArbitrationImageController {

    @Autowired
    RedisCache redisCache;
    @Autowired
    IWarframeTranslationService trans;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getArbitrationImage")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/s.png");
        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        GlobalStates.Arbitration arbitration = sgs.getPacket().getData().getArbitration();
        if (arbitration == null) {
            return;
        }
        String node = arbitration.getNode().
                replace(
                        StringUtils.quStr(arbitration.getNode()),
                        trans.enToZh(StringUtils.quStr(arbitration.getNode())
                        )
                );
        List<Seat> seats = new ArrayList<>();
        seats.add(ImageUtils.getSeat("仲裁任务如下:", 62, 80, COLOR_BACK));
        seats.add(ImageUtils.getSeat("任务地点:", 62, 110, COLOR_BACK));
        seats.add(ImageUtils.getSeat(node, 62 + ImageUtils.getFortWidth("任务地点:"), 110));
        seats.add(ImageUtils.getSeat("派系:", 62, 140, COLOR_BACK));
        seats.add(ImageUtils.getSeat(arbitration.getEnemy(), 62 + ImageUtils.getFortWidth("任务地点:"), 140));
        seats.add(ImageUtils.getSeat("任务类型:", 62, 170, COLOR_BACK));
        seats.add(ImageUtils.getSeat(trans.enToZh(arbitration.getType()), 62 + ImageUtils.getFortWidth("任务类型:"), 170, COLOR_CHIEF));
        seats.add(ImageUtils.getSeat("距离结束:", 62, 200, COLOR_BACK));
        seats.add(ImageUtils.getSeat(DateUtils.getDate((arbitration.getExpiry()), new Date()), 62 + ImageUtils.getFortWidth("距离结束:"), 200, COLOR_CHIEF));
        assert image != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(ImageUtils.getBufferedImage(image, seats)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        response.getOutputStream().write(out.toByteArray());
    }

}
