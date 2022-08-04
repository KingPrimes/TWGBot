package com.twg.bot.warframe.utils;

import com.twg.bot.warframe.dao.FissureList;
import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.dao.HintList;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import com.twg.common.utils.DateUtils;
import com.twg.common.utils.StringUtils;
import com.twg.common.utils.file.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.simple.Graphics2DRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;

@Component
public class HtmlToImage {

    private static final String HTML_PATH = "./TWGBot-html/";
    @Autowired
    IWarframeTranslationService trans;

    /**
     * 平原图片
     *
     * @return 字节流
     */
    public ByteArrayOutputStream allCycleImage(GlobalStates g) {

        //地球
        GlobalStates.EarthCycle earth = g.getEarthCycle();

        //夜灵平野
        GlobalStates.CetusCycle cetus = g.getCetusCycle();

        //福尔图娜
        GlobalStates.VallisCycle vallis = g.getVallisCycle();

        //魔胎之境
        GlobalStates.CambionCycle cambion = g.getCambionCycle();

        //扎里曼
        GlobalStates.ZarimanCycle zariman = g.getZarimanCycle();

        //Html 文件
        String html = FileUtils.getFileString(HTML_PATH + "html/allCycle.html");

        int width = getWidth(html);
        html = outH(html);

        if (html.contains("#table")) {
            StringBuffer str = new StringBuffer();
            str
                    .append("<h3>---地球---</h3>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<td>")
                    .append("状态")
                    .append("</td>")
                    .append("<td>")
                    .append(trans.enToZh(earth.getState()))
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td>")
                    .append("时间")
                    .append("</td>")
                    .append("<td>")
                    .append(DateUtils.getDate((earth.getExpiry()), new Date()))
                    .append("</td>")
                    .append("</tr>")
                    .append("</table>")
                    .append("<br/>")

                    .append("<h3>---夜灵平野---</h3>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<td>")
                    .append("状态")
                    .append("</td>")
                    .append("<td>")
                    .append(trans.enToZh(cetus.getState()))
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td>")
                    .append("时间")
                    .append("</td>")
                    .append("<td>")
                    .append(DateUtils.getDate((cetus.getExpiry()), new Date()))
                    .append("</td>")
                    .append("</tr>")
                    .append("</table>")
                    .append("<br/>")

                    .append("<h3>---福尔图娜---</h3>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<td>")
                    .append("状态")
                    .append("</td>")
                    .append("<td>")
                    .append(trans.enToZh(vallis.getState()))
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td>")
                    .append("时间")
                    .append("</td>")
                    .append("<td>")
                    .append(DateUtils.getDate((vallis.getExpiry()), new Date()))
                    .append("</td>")
                    .append("</tr>")
                    .append("</table>")
                    .append("<br/>")

                    .append("<h3>---魔胎之境---</h3>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<td>")
                    .append("状态")
                    .append("</td>")
                    .append("<td>")
                    .append(cambion.getActive().toUpperCase(Locale.ROOT))
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td>")
                    .append("时间")
                    .append("</td>")
                    .append("<td>")
                    .append(DateUtils.getDate((cambion.getExpiry()), new Date()))
                    .append("</td>")
                    .append("</tr>")
                    .append("</table>")
                    .append("<br/>")

                    .append("<h3>---扎里曼---</h3>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<td>")
                    .append("状态")
                    .append("</td>")
                    .append("<td>")
                    .append(zariman.getState().toUpperCase(Locale.ROOT))
                    .append("</td>")
                    .append("</tr>")
                    .append("<tr>")
                    .append("<td>")
                    .append("时间")
                    .append("</td>")
                    .append("<td>")
                    .append(DateUtils.getDate((zariman.getExpiry()), new Date()))
                    .append("</td>")
                    .append("</tr>")
                    .append("</table>")

            ;

            html = html.replaceAll("#table", str.toString());
        }
        //地球
        {
            if (html.contains("#earthType")) {
                html = html.replaceAll("#earthType", trans.enToZh(earth.getState()));
            }
            if (html.contains("#earthEnd")) {
                html = html.replaceAll("#earthEnd", DateUtils.getDate((earth.getExpiry()), new Date()));
            }
        }
        //夜灵平野
        {
            if (html.contains("#cetusType")) {
                html = html.replaceAll("#cetusType", trans.enToZh(cetus.getState()));
            }
            if (html.contains("#cetusEnd")) {
                html = html.replaceAll("#cetusEnd", DateUtils.getDate((cetus.getExpiry()), new Date()));
            }
        }
        //福尔图娜
        {
            if (html.contains("#vallisType")) {
                html = html.replaceAll("#vallisType", trans.enToZh(vallis.getState()));
            }
            if (html.contains("#vallisEnd")) {
                html = html.replaceAll("#vallisEnd", DateUtils.getDate((vallis.getExpiry()), new Date()));
            }
        }
        //魔胎之境
        {
            if (html.contains("#cambionType")) {
                html = html.replaceAll("#cambionType", cambion.getActive().toUpperCase(Locale.ROOT));
            }
            if (html.contains("#cambionEnd")) {
                html = html.replaceAll("#cambionEnd", DateUtils.getDate((cambion.getExpiry()), new Date()));
            }
        }
        //扎里曼
        {
            if (html.contains("#zarimanType")) {
                html = html.replaceAll("#zarimanType", zariman.getState().toUpperCase(Locale.ROOT));
            }
            if (html.contains("#zarimanEnd")) {
                html = html.replaceAll("#zarimanEnd", DateUtils.getDate((zariman.getExpiry()), new Date()));
            }
        }


        return tmpHtmlToImageByteArray("allCycle", html, width);

    }

    /**
     * 仲裁 Html 转 图片
     *
     * @param arbitration 数据
     * @return 字节流
     */
    public ByteArrayOutputStream arbitrationImage(GlobalStates.Arbitration arbitration) {
        String html = FileUtils.getFileString(HTML_PATH + "html/arbitration.html");
        int width = getWidth(html);
        html = outH(html);
        String node = arbitration.getNode().
                replace(
                        StringUtils.quStr(arbitration.getNode()),
                        trans.enToZh(StringUtils.quStr(arbitration.getNode())
                        )
                );
        if (html.contains("#table")) {

            String str = "<table>" +
                    "<tr>" +
                    "<td>" +
                    "任务地点" +
                    "</td>" +
                    "<td>" +
                    node +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>" +
                    "派系" +
                    "</td>" +
                    "<td>" +
                    arbitration.getEnemy() +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>" +
                    "任务类型" +
                    "</td>" +
                    "<td>" +
                    trans.enToZh(arbitration.getType()) +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>" +
                    "距离结束" +
                    "</td>" +
                    "<td>" +
                    DateUtils.getDate((arbitration.getExpiry()), new Date()) +
                    "</td>" +
                    "</tr>" +
                    "</table>";
            html = html.replaceAll("#table", str);
        }
        if (html.contains("#Node")) {
            html = html.replaceAll("#Node", node);
        }
        if (html.contains("#Ene")) {
            html = html.replaceAll("#Ene", arbitration.getEnemy());
        }
        if (html.contains("#Type")) {
            html = html.replaceAll("#Type", trans.enToZh(arbitration.getType()));
        }
        if (html.contains("#End")) {
            html = html.replaceAll("#End", DateUtils.getDate((arbitration.getExpiry()), new Date()));
        }

        return tmpHtmlToImageByteArray("arbitration", html, width);
    }

    /**
     * 突击图片
     *
     * @param sortie 数据
     * @return 图片 字节流
     */
    public ByteArrayOutputStream assaultImage(GlobalStates.Sortie sortie) {
        String html = FileUtils.getFileString(HTML_PATH + "html/assault.html");
        int width = getWidth(html);
        html = outH(html);

        if (html.contains("#table")) {
            StringBuilder str = new StringBuilder();
            str

                    .append("<h3>今天是由：[")
                    .append(sortie.getBoss())
                    .append("]主导的突击任务</h3>")
                    .append("<h3>派系：[")
                    .append(sortie.getFaction())
                    .append("]</h3>")
                    .append("<h3>任务结束：")
                    .append(DateUtils.getDate((sortie.getExpiry()), new Date()))
                    .append("</h3>")
                    .append("<h3>---任务详情---</h3>")
                    .append("<div class=\"assault\">")

            ;
            for (GlobalStates.Sortie.Variants variant : sortie.getVariants()) {
                str.append("<table>")
                        .append("<tr><td>任务地址：")
                        .append(variant.getNode()
                                .replace(
                                        StringUtils.quStr(
                                                variant.getNode()),
                                        trans.enToZh(StringUtils.quStr(variant.getNode()))))
                        .append("</td></tr>")
                        .append("<tr><td>任务类型：")
                        .append(trans.enToZh(variant.getMissionType()))
                        .append("</td></tr>")
                        .append("<tr><td>状态：")
                        .append(trans.enToZh(variant.getModifier()))
                        .append("</td></tr>")
                        .append("</table>");
            }

            str
                    .append("</div>")
            ;
            html = html.replaceAll("#table", str.toString());
        }

        if (!html.contains("#table")) {
            if (html.contains("#Boss")) {
                html = html.replaceAll("#Boss", sortie.getBoss());
            }

            if (html.contains("#Ene")) {
                html = html.replaceAll("#Ene", sortie.getFaction());
            }
            if (html.contains("#End")) {
                html = html.replaceAll("#End", DateUtils.getDate((sortie.getExpiry()), new Date()));
            }
            if (html.contains("#Deta")) {
                StringBuilder str = new StringBuilder();
                for (GlobalStates.Sortie.Variants variant : sortie.getVariants()) {
                    str.append("<table>")
                            .append("<tr><td>任务地址：")
                            .append(variant.getNode()
                                    .replace(
                                            StringUtils.quStr(
                                                    variant.getNode()),
                                            trans.enToZh(StringUtils.quStr(variant.getNode()))))
                            .append("</td></tr>")
                            .append("<tr><td>任务类型：")
                            .append(trans.enToZh(variant.getMissionType()))
                            .append("</td></tr>")
                            .append("<tr><td>状态：")
                            .append(trans.enToZh(variant.getModifier()))
                            .append("</td></tr>")
                            .append("</table>");
                }
                html = html.replaceAll("#Deta", str.toString());
            } else {
                int i = 1;
                for (GlobalStates.Sortie.Variants variant : sortie.getVariants()) {
                    String node = variant.getNode()
                            .replace(
                                    StringUtils.quStr(
                                            variant.getNode()),
                                    trans.enToZh(StringUtils.quStr(variant.getNode())));

                    if (html.contains("#Node" + i)) {
                        html = html.replaceAll("#Node" + i, node);
                    }

                    if (html.contains("#Type" + i)) {
                        html = html.replaceAll("#Type" + i, trans.enToZh(variant.getMissionType()));
                    }
                    if (html.contains("#State" + i)) {
                        html = html.replaceAll("#State" + i, trans.enToZh(variant.getModifier()));
                    }
                    i++;
                }
            }
        }
        return tmpHtmlToImageByteArray("assault", html, width);
    }

    /**
     * 每日特惠图片
     *
     * @param deals 数据
     * @return 图片字节流
     */
    public ByteArrayOutputStream dailyDealsImage(GlobalStates.DailyDeals deals) {
        String html = FileUtils.getFileString(HTML_PATH + "html/daily.html");
        int width = getWidth(html);
        html = outH(html);
        String item = trans.enToZh(deals.getItem());
        Integer origin = deals.getOriginalPrice();
        Integer sale = deals.getSalePrice();
        String discount = deals.getDiscount() + "%";
        if (deals.getTotal() - deals.getSold() == 0) {
            deals.setSold(deals.getTotal());
        } else if (deals.getTotal() - deals.getSold() == deals.getTotal()) {
            deals.setSold(0);
        } else {
            deals.setSold(deals.getTotal() - deals.getSold());
        }
        Integer total = deals.getTotal();
        Integer sold = deals.getSold();
        String endTime = DateUtils.getDate(deals.getExpiry(), new Date());

        if (html.contains("#table")) {
            String str = "<table><tr><td>物品名称：" +
                    item +
                    "</td></tr>" +
                    "<tr><td>原价/现价：" +
                    origin +
                    "/" +
                    sale +
                    "</td></tr>" +
                    "<tr><td>折扣比：" +
                    discount +
                    "</td></tr>" +
                    "<tr><td>总/余：" +
                    total +
                    "/" +
                    sold +
                    "</td></tr><tr><td>剩余时间：" +
                    endTime +
                    "</td></tr>" +
                    "</table>";
            html = html.replaceAll("#table", str);
        }
        if (!html.contains("#table")) {
            if (html.contains("#Item")) {
                html = html.replaceAll("#Item", item);
            }
            if (html.contains("#Origin")) {
                html = html.replaceAll("#Origin", String.valueOf(origin));
            }
            if (html.contains("#Sale")) {
                html = html.replaceAll("#Sale", String.valueOf(sale));
            }
            if (html.contains("#Discount")) {
                html = html.replaceAll("#Discount", discount);
            }
            if (html.contains("#Total")) {
                html = html.replaceAll("#Total", String.valueOf(total));
            }
            if (html.contains("#Sold")) {
                html = html.replaceAll("#Sold", String.valueOf(sold));
            }
            if (html.contains("#End")) {
                html = html.replaceAll("#End", endTime);
            }
        }
        return tmpHtmlToImageByteArray("daily", html, width);
    }

    /**
     * 裂隙图片
     *
     * @param fiss 数据
     * @return 图片 字节流
     */
    public ByteArrayOutputStream fissuesImage(FissureList fiss) {
        String html = FileUtils.getFileString(HTML_PATH + "html/fissues.html");
        int width = getWidth(html);
        html = outH(html);

        if (html.contains("#table")) {
            StringBuilder str = new StringBuilder();
            str
                    .append("<h3>---古纪裂隙---</h3><table class=\"t1\"><tr><th>任务地点</th><th>任务类型</th><th>距离结束</th></tr>")
            ;
            for (GlobalStates.Fissures fissure : fiss.getT1()) {
                str
                        .append("<tr><td>")
                        .append(fissure.getNode())
                        .append("</td><td>")
                        .append(fissure.getMissionType())
                        .append("</td><td>")
                        .append(fissure.getEta())
                        .append("</td></tr>")
                ;

            }
            str.append("</table><h3>---前纪裂隙---</h3><table class=\"t2\"><tr><th>任务地点</th><th>任务类型</th><th>距离结束</th></tr>");

            for (GlobalStates.Fissures fissure : fiss.getT2()) {
                str
                        .append("<tr><td>")
                        .append(fissure.getNode())
                        .append("</td><td>")
                        .append(fissure.getMissionType())
                        .append("</td><td>")
                        .append(fissure.getEta())
                        .append("</td></tr>")
                ;

            }
            str.append("</table><h3>---中纪裂隙---</h3><table class=\"t3\"><tr><th>任务地点</th><th>任务类型</th><th>距离结束</th></tr>");
            for (GlobalStates.Fissures fissure : fiss.getT3()) {
                str
                        .append("<tr><td>")
                        .append(fissure.getNode())
                        .append("</td><td>")
                        .append(fissure.getMissionType())
                        .append("</td><td>")
                        .append(fissure.getEta())
                        .append("</td></tr>")
                ;

            }

            str.append("</table><h3>---后纪裂隙---</h3><table class=\"t4\"><tr><th>任务地点</th><th>任务类型</th><th>距离结束</th></tr>");
            for (GlobalStates.Fissures fissure : fiss.getT4()) {
                str
                        .append("<tr><td>")
                        .append(fissure.getNode())
                        .append("</td><td>")
                        .append(fissure.getMissionType())
                        .append("</td><td>")
                        .append(fissure.getEta())
                        .append("</td></tr>")
                ;

            }

            str.append("</table><h3>---安魂裂隙---</h3><table class=\"t5\"><tr><th>任务地点</th><th>任务类型</th><th>距离结束</th></tr>");
            for (GlobalStates.Fissures fissure : fiss.getT5()) {
                str
                        .append("<tr><td>")
                        .append(fissure.getNode())
                        .append("</td><td>")
                        .append(fissure.getMissionType())
                        .append("</td><td>")
                        .append(fissure.getEta())
                        .append("</td></tr>")
                ;

            }
            str.append("</table>");

            html = html.replaceAll("#table", str.toString());
        }
        return tmpHtmlToImageByteArray("fissues", html, width);
    }

    /**
     * 获取宽度
     *
     * @param html html文档
     * @return 宽度
     */
    private int getWidth(String html) {
        Document doc = Jsoup.parse(html);
        int width = 500;
        //判断是否添加宽度标签
        if (!doc.getElementsByTag("w").isEmpty()) {
            String num = doc.getElementsByTag("w").text();
            if (StringUtils.isNumber(num)) width = Integer.parseInt(num);

        }
        return width;
    }

    /**
     * 删除不相干的字段
     *
     * @param html html 文档
     * @return 格式化之后的 html文档
     */
    private String outH(String html) {
        html = html.replaceAll("<!--", "<xx>").replaceAll("-->", "</xx>");
        Document doc = Jsoup.parse(html);
        if (!doc.getElementsByTag("xx").isEmpty()) {
            int i = doc.getElementsByTag("xx").size();
            for (; i > 0; i--) {
                html = new StringBuilder(html).replace(html.indexOf("<xx>"), html.indexOf("</xx>") + 5, "").toString().trim();
            }
        }
        if (!doc.getElementsByTag("w").isEmpty()) {
            html = new StringBuilder(html).replace(html.indexOf("<w>"), html.indexOf("</w>") + 4, "").toString().trim();
        }
        StringBuilder str = new StringBuilder(html);
        str.insert(str.indexOf("</body>"), "<div style=\"text-align: center;\">\n" +
                "\tPosted by:KingPrimes<br/>\n" +
                "\t" +
                HintList.getHint() +
                "\n</div>\n");
        return str.toString();
    }

    /**
     * html 文档转成 字节流
     *
     * @param htmlFilePath html文件路径
     * @param width        生成图片的宽度
     * @return 字节流
     */
    private ByteArrayOutputStream convertHtmlToImage(String htmlFilePath, int width) {
        try {
            File htmlFile = new File(htmlFilePath);
            String url = htmlFile.toURI().toURL().toExternalForm();
            BufferedImage image = Graphics2DRenderer.renderToImageAutoSize(url, width, BufferedImage.TYPE_INT_ARGB);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            return os;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ByteArrayOutputStream tmpHtmlToImageByteArray(String name, String html, int width) {
        String path = HTML_PATH;
        FileUtils.isMkdirs(path + "tmp");
        path = path + "tmp/" + name + ".html";
        try {
            FileOutputStream fo = new FileOutputStream(path);
            OutputStreamWriter os = new OutputStreamWriter(fo, StandardCharsets.UTF_8);
            os.write(html);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertHtmlToImage(path, width);
    }

}
