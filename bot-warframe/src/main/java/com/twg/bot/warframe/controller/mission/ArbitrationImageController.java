package com.twg.bot.warframe.controller.mission;


import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.dao.SocketGlobalStates;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import com.twg.bot.warframe.utils.HtmlToImage;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.spring.SpringUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.twg.bot.enums.WarframeTypeEnum.REDIS_MISSION_KEY;

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
        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        GlobalStates.Arbitration arbitration = sgs.getPacket().getData().getArbitration();
        if (arbitration == null) {
            return;
        }
       /* String node = arbitration.getNode().
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
        response.getOutputStream().write(out.toByteArray());*/
        ByteArrayOutputStream out = SpringUtils.getBean(HtmlToImage.class).arbitrationImage(arbitration);
        response.getOutputStream().write(out.toByteArray());
    }

}
