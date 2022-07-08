package com.twg.bot.warframe.task;

import com.twg.bot.enums.FunctionEnums;
import com.twg.bot.utils.Msg;
import com.twg.bot.utils.SendAllGroup;
import com.twg.bot.warframe.domain.WarframeRivenTrend;
import com.twg.bot.warframe.service.IWarframeRivenTrendService;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import com.twg.bot.warframe.utils.forums.GetForumsRivenDispositionUpdates;
import com.twg.bot.warframe.utils.forums.RivenDispositionUpdatesImage;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.ListUtils;
import com.twg.common.utils.ip.GetServerPort;
import com.twg.common.utils.spring.SpringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("RivenDispositionUpdatesTask")
public class RivenDispositionUpdatesTask {

    /**
     * 更新紫卡倾向
     *
     * @param url "Url论坛地址"
     * @return 更新的条数
     * @throws Exception 可能存在空异常
     */
    public int renewRivenDisposition(String url) throws Exception {
        List<WarframeRivenTrend> trends = GetForumsRivenDispositionUpdates.getRivenDispositionUpdates(url);
        List<WarframeRivenTrend> redis_trends = new ArrayList<>();
        try {
            //获取之前的缓存
            redis_trends = SpringUtils.getBean(RedisCache.class).getCacheList("renew-riven-disposition");
        } catch (Exception e) {
            System.out.println("更新紫卡倾向失败：" + e.getMessage());
        }

        List<WarframeRivenTrend> image = new ArrayList<>();

        //对比之前的缓存是否相等
        if (!ListUtils.equals(trends, redis_trends)) {
            int i = 0;
            //遍历集合写入到数据库
            for (WarframeRivenTrend trend : trends) {
                trend.setRivenTrendName(trend.getRivenTrendName().trim());
                trend.setRivenTrendOldNum(trend.getRivenTrendOldNum().trim());
                trend.setRivenTrendNewNum(trend.getRivenTrendNewNum().trim());
                i += SpringUtils.getBean(IWarframeRivenTrendService.class).insertAndUpDateWarframeRivenTrend(trend);
                trend.setTraCh(SpringUtils.getBean(IWarframeTranslationService.class).enToZh(trend.getRivenTrendName()));
                image.add(new WarframeRivenTrend(trend));
            }

            RivenDispositionUpdatesImage.getImage(image);
            try {
                //删除之前的缓存
                SpringUtils.getBean(RedisCache.class).deleteObject("renew-riven-disposition");
            } catch (Exception ignored) {
            }
            //添加新的缓存
            SpringUtils.getBean(RedisCache.class).setCacheList("renew-riven-disposition", trends);
            SendAllGroup.sendAllGroup(Msg.builder().img("http://localhost:" + GetServerPort.getPort() + "/warframe/forums/riven/getNewsImage"), FunctionEnums.FUNCTION_WARFRAME);
            return i;
        }
        return 0;
    }
}
