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
import java.util.Date;
import java.util.List;

import static com.twg.bot.enums.WarframeTypeEnum.REDIS_MISSION_KEY;
import static com.twg.common.utils.ColorEnum.COLOR_BACK;
import static com.twg.common.utils.ColorEnum.COLOR_NODE;

@RestController
@RequestMapping("/warframe/mission")
public class DailyDealsImageController {

    @Autowired
    RedisCache redisCache;
    @Autowired
    IWarframeTranslationService tra;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getDailyDealsImage")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/s.png");
        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        GlobalStates state = sgs.getPacket().getData();
        GlobalStates.DailyDeals deals = state.getDailyDeals().get(0);
        List<Seat> seatList = new ArrayList<>();
        seatList.add(ImageUtils.getSeat("--今日特惠物品--", 240, 80, COLOR_BACK));
        seatList.add(ImageUtils.getSeat("物品名称:", 62, 110, COLOR_BACK));
        seatList.add(ImageUtils.getSeat(tra.enToZh(deals.getItem()), 62 + ImageUtils.getFortWidth("物品名称:"), 110));
        seatList.add(ImageUtils.getSeat("原价/现价:", 62, 140, COLOR_BACK));
        seatList.add(ImageUtils.getSeat(deals.getOriginalPrice() + " / " + deals.getSalePrice() + " -->> " + deals.getDiscount() + "%", 62 + ImageUtils.getFortWidth("原价/现价:"), 140));
        seatList.add(ImageUtils.getSeat("总/余:", 62, 170, COLOR_BACK));
        if (deals.getTotal() - deals.getSold() == 0) {
            deals.setSold(deals.getTotal());
        } else if (deals.getTotal() - deals.getSold() == deals.getTotal()) {
            deals.setSold(0);
        } else {
            deals.setSold(deals.getTotal() - deals.getSold());
        }
        seatList.add(ImageUtils.getSeat(deals.getTotal() + " / " + deals.getSold(), 62 + ImageUtils.getFortWidth("总/余:"), 170));
        seatList.add(ImageUtils.getSeat("剩余时间:", 62, 200, COLOR_BACK));
        seatList.add(ImageUtils.getSeat(DateUtils.getDate(deals.getExpiry(), new Date()), 62 + ImageUtils.getFortWidth("剩余时间:"), 200, COLOR_NODE));
        assert image != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(ImageUtils.getBufferedImage(image, seatList)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(ImageUtils.getBufferedImage(image, seatList), "png", out);
        response.getOutputStream().write(out.toByteArray());

    }
}
