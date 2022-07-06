package com.twg.bot.warframe.controller.mission;

import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.dao.InvasionsDAO;
import com.twg.bot.warframe.dao.SocketGlobalStates;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.Fonts;
import com.twg.common.utils.StringUtils;
import com.twg.common.utils.image.ImageUtils;
import com.twg.framework.interceptor.IgnoreAuth;
import lombok.val;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.twg.bot.enums.WarframeTypeEnum.REDIS_MISSION_KEY;
import static com.twg.common.utils.ColorEnum.*;

@RestController
@RequestMapping("/warframe/mission")
public class InvasionsImageController {

    @Autowired
    private IWarframeTranslationService traService;
    @Autowired
    private RedisCache redisCache;

    @IgnoreAuth
    @GetMapping(value = "/{uuid}/getInvasionsImage")
    public void getImage(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "image/png");
        BufferedImage image = ImageUtils.getImage("/images/invasions/invasion.png");

        SocketGlobalStates sgs = redisCache.getCacheObject(REDIS_MISSION_KEY.getType());
        GlobalStates states = sgs.getPacket().getData();

        List<InvasionsDAO> invasionsList = new ArrayList<>();
        val invasion = states.getInvasions();
        for (GlobalStates.Invasions invasions : invasion) {
            if (!invasions.getCompleted()) {
                InvasionsDAO idao = new InvasionsDAO();
                idao.setNode(invasions.getNode().replace(StringUtils.quStr(invasions.getNode()), traService.enToZh(StringUtils.quStr(invasions.getNode()))));
                idao.setAttackingFaction(invasions.getAttackingFaction());
                idao.setDefendingFaction(invasions.getDefendingFaction());
                for (GlobalStates.Invasions.AttackerReward.CountedItems items : invasions.getAttackerReward().getCountedItems()) {
                    idao.setAttackerRewardE(items.getType());
                    idao.setAttackerReward(traService.enToZh(items.getType()));
                    idao.setAttckerCount(items.getCount());
                }
                for (GlobalStates.Invasions.DefenderReward.CountedItems items : invasions.getDefenderReward().getCountedItems()) {
                    idao.setDefenderRewardE(items.getType());
                    idao.setDefenderReward(traService.enToZh(items.getType()));
                    idao.setDefenderCount((items.getCount()));
                }
                if (invasions.getAttackerReward().getCountedItems().size() <= 0) {
                    idao.setAttackerRewardE("-1");
                    idao.setAttackerReward("-1");
                }
                if (invasions.getDefenderReward().getCountedItems().size() <= 0) {
                    idao.setDefenderRewardE("-1");
                    idao.setDefenderReward("-1");
                }
                idao.setCount(invasions.getCount());
                idao.setRequiredRuns(invasions.getRequiredRuns());
                idao.setCompletion(Double.valueOf(invasions.getCompletion()));
                invasionsList.add(idao);
            }
        }
        int y = 72;
        int x = 62;
        boolean falg = false;
        if (invasionsList.size() >= 7) {
            image = ImageUtils.getImage("/images/big.png");
            assert image != null;
            Graphics2D g = image.createGraphics();
            g.setFont(Fonts.FONT_TEXT);
            for (InvasionsDAO inv : invasionsList) {
                g.setColor(COLOR_BACK.getColor());
                g.drawString("进攻方:", x, y);
                g.setColor(COLOR_CHIEF.getColor());
                if (!"-1".equals(inv.getAttackerRewardE())) {
                    g.drawString(inv.getAttckerCount() + "X ", x, y + 80);
                    g.drawString(inv.getAttackerReward(), x + 35, y + 80);
                }
                if ("Grineer".equals(inv.getAttackingFaction())) {
                    g.setColor(COLOR_G.getColor());
                } else {
                    g.setColor(COLOR_MINOR.getColor());
                }
                x += ImageUtils.getFortWidth("进攻方:", Fonts.FONT_TEXT);
                g.drawString(inv.getAttackingFaction(), x + 30, y);
                x += 225;
                if (!"-1".equals(inv.getAttackerRewardE())) {
                    g.drawImage(ImageUtils.getImage("/images/images/" + checkBker(inv.getAttackerRewardE()) + ".png"), 110, y + 5, null);
                }
                g.setColor(COLOR_BACK.getColor());
                g.drawString("防守方:", x, y);
                g.setColor(COLOR_CHIEF.getColor());
                g.drawString(inv.getDefenderCount() + "X ", x, y + 80);
                g.drawString(inv.getDefenderReward(), x + 35, y + 80);

                if ("Grineer".equals(inv.getDefendingFaction())) {
                    g.setColor(COLOR_G.getColor());
                } else {
                    g.setColor(COLOR_MINOR.getColor());
                }
                x += ImageUtils.getFortWidth("防守方:", Fonts.FONT_TEXT);
                g.drawString(inv.getDefendingFaction(), x + 38, y);

                g.drawImage(ImageUtils.getImage("/images/images/" + checkBker(inv.getDefenderRewardE()) + ".png"), x - 25, y + 5, null);
                g.setColor(COLOR_NODE.getColor());
                g.drawString(inv.getNode(), x + 146, y + 25);
                y += 90;
                g.drawImage(ImageUtils.getImage("/images/p.png"), x - 360, y, null);
                if (y >= 850) {
                    y = 72;
                    x = 860 + 62;
                    falg = !falg;
                } else {
                    y += 27;
                    if (!falg) {
                        x = 62;
                    } else {
                        x = 860 + 62;
                    }
                }
            }
            g.setColor(COLOR_G.getColor());
            BufferedImage k = new BufferedImage(140, 830, BufferedImage.TYPE_INT_RGB);
            Graphics2D kg = k.createGraphics();
            kg.setColor(COLOR_FORM.getColor());
            kg.fillRect(0, 0, k.getWidth(), k.getHeight());
            g.drawImage(k, 770, 50, null);
            g.setColor(COLOR_FORM.getColor());

        } else {
            assert image != null;
            Graphics2D g = image.createGraphics();
            g.setFont(Fonts.FONT_TEXT);
            for (InvasionsDAO inv : invasionsList) {
                g.setColor(COLOR_BACK.getColor());
                g.drawString("进攻方:", x, y);
                g.setColor(COLOR_CHIEF.getColor());
                if (!"-1".equals(inv.getAttackerRewardE())) {
                    g.drawString(inv.getAttckerCount() + "X ", x, y + 80);
                    g.drawString(inv.getAttackerReward(), x + 35, y + 80);
                }
                if ("Grineer".equals(inv.getAttackingFaction())) {
                    g.setColor(COLOR_G.getColor());
                } else {
                    g.setColor(COLOR_MINOR.getColor());
                }
                x += ImageUtils.getFortWidth("进攻方:", Fonts.FONT_TEXT);
                g.drawString(inv.getAttackingFaction(), x + 30, y);
                x += 225;
                if (!"-1".equals(inv.getAttackerRewardE())) {
                    g.drawImage(ImageUtils.getImage("/images/images/" + checkBker(inv.getAttackerRewardE()) + ".png"), 110, y + 5, null);
                }
                g.setColor(COLOR_BACK.getColor());
                g.drawString("防守方:", x, y);
                g.setColor(COLOR_CHIEF.getColor());
                g.drawString(inv.getDefenderCount() + "X ", x, y + 80);
                g.drawString(inv.getDefenderReward(), x + 35, y + 80);

                if ("Grineer".equals(inv.getDefendingFaction())) {
                    g.setColor(COLOR_G.getColor());
                } else {
                    g.setColor(COLOR_MINOR.getColor());
                }
                x += ImageUtils.getFortWidth("防守方:", Fonts.FONT_TEXT);
                g.drawString(inv.getDefendingFaction(), x + 38, y);
                g.drawImage(ImageUtils.getImage("/images/images/" + checkBker(inv.getDefenderRewardE()) + ".png"), x - 25, y + 5, null);
                g.setColor(COLOR_NODE.getColor());
                g.drawString(inv.getNode(), x + 146, y + 25);
                y += 90;
                g.drawImage(ImageUtils.getImage("/images/p.png"), 47, y, null);
                y += 27;
                x = 62;
            }
            g.setColor(COLOR_FORM.getColor());
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(image).scale(1).outputQuality(1).outputFormat("png").toOutputStream(out);
        //ImageIO.write(image, "png", out);
        response.getOutputStream().write(out.toByteArray());

    }

    private String checkBker(String key) {
        if (key.contains("Barrel")) {
            return "Barrel";
        } else if (key.contains("Receiver")) {
            return "Receiver";
        } else if (key.contains("Link")) {
            return "Link";
        } else if (key.contains("Stock")) {
            return "Stock";
        } else if (key.contains("Blade")) {
            return "Blade";
        } else if (key.contains("Heatsink")) {
            return "Heatsink";
        } else if (key.contains("Hilt")) {
            return "Hilt";
        } else {
            return key;
        }
    }


}
