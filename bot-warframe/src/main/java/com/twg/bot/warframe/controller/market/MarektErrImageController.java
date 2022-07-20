package com.twg.bot.warframe.controller.market;


import com.alibaba.fastjson.JSONArray;
import com.twg.bot.warframe.dao.ErrorWM;
import com.twg.common.utils.ColorEnum;
import com.twg.common.utils.Seat;
import com.twg.common.utils.image.ImageUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/warframe/market")
public class MarektErrImageController {

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getMarektErrImage/{er}", produces = MediaType.IMAGE_PNG_VALUE)
    public void getImage(HttpServletResponse response, @PathVariable String er) throws IOException {
        response.setHeader("Content-Type", "image/png");
        List<ErrorWM> erWms = JSONArray.parseArray(URLDecoder.decode(er, "UTF-8"), ErrorWM.class);
        BufferedImage image = ImageUtils.getImage("/images/s.png");
        List<Seat> seats = new ArrayList<>();
        seats.add(ImageUtils.getSeat("---为您找到以下相似的物品---", 220, 82, ColorEnum.COLOR_TENGLUOZI));
        int x = 62, y = 120;
        for (ErrorWM erWm : erWms) {
            seats.add(ImageUtils.getSeat(erWm.getCh(), x, y, ColorEnum.COLOR_NODE));
            y += 30;
            if (y >= 300) {
                y = 120;
                x = 380;
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(ImageUtils.getBufferedImage(image, seats)).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        response.getOutputStream().write(out.toByteArray());
        //return ImageUtils.getBufferedImage(image, seats);

    }
}
