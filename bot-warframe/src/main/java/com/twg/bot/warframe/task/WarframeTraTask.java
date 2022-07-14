package com.twg.bot.warframe.task;

import com.twg.bot.warframe.utils.WarframeTraUtils;
import com.twg.common.core.domain.AjaxResult;
import com.twg.common.utils.spring.SpringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WarframeTraTask {

    @Scheduled(cron = "0 0 0 1/7 * ? ")
    //@Log(title = "WarframeTra",businessType = BusinessType.INSERT)
    public AjaxResult TaskUserDict() {
        int i = SpringUtils.getBean(WarframeTraUtils.class).getUserDict();
        return AjaxResult.success("翻译更新完成，共更新：" + i + "条数据");
    }
}
