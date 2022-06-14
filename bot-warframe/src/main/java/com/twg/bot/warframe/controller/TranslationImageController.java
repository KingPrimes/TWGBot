package com.twg.bot.warframe.controller;

import com.twg.bot.warframe.domain.WarframeTranslation;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import com.twg.common.utils.Seat;
import com.twg.common.utils.image.ImageUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class TranslationImageController {

    @Autowired
    IWarframeTranslationService translationService;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getTraImage/{key}")
    public void getImage(HttpServletResponse response, @PathVariable String key) throws IOException {
        response.setHeader("Content-Type", "image/gif");
        BufferedImage image = ImageUtils.getImage("/images/invasions/invasion.png");
        List<WarframeTranslation> translations = translationService.enAndZhToList(URLDecoder.decode(key, "UTF-8"));
        int x = 62, y = 80;
        List<Seat> seats = new ArrayList<>();
        for (WarframeTranslation tra : translations) {
            seats.add(ImageUtils.getSeat("中文:" + tra.getTraCh(), x, y));
            seats.add(ImageUtils.getSeat("英文:" + tra.getTraEn(), x + 300, y));
            y += 30;
        }
        assert image != null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(ImageUtils.getBufferedImage(image, seats), "png", out);
        response.getOutputStream().write(out.toByteArray());
    }
}
