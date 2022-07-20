package com.twg.bot.warframe.controller.subscribe;


import com.twg.bot.enums.WarframeSubscribeEnums;
import com.twg.common.utils.Fonts;
import com.twg.common.utils.Seat;
import com.twg.common.utils.image.ImageUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.twg.common.utils.ColorEnum.*;

@RestController
@RequestMapping("/warframe/subscriber")
public class SubscribeListController {

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getSubscriberHelp")
    public void getSubscriberHelp(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/s.png");
        int x = 80, y = 120;
        List<Seat> seats = new ArrayList<>();
        seats.add(ImageUtils.getSeat("---订阅指令表---", 280, 80, COLOR_NODE));
        for (WarframeSubscribeEnums enums : WarframeSubscribeEnums.values()) {
            if (enums.ordinal() != 0) {
                seats.add(ImageUtils.getSeat(enums.getName(), x, y));
                seats.add(ImageUtils.getSeat(":", x + 120, y, COLOR_BACK));
                seats.add(ImageUtils.getSeat(String.valueOf(enums.ordinal()), x + 140, y, COLOR_RIVEN_MOD));
                y += Fonts.FONT_SOURCE_CN_MEDIUM.getSize();
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        response.getOutputStream().write(out.toByteArray());
    }

}
