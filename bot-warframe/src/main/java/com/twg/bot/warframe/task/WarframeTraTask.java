package com.twg.bot.warframe.task;

import com.twg.bot.warframe.utils.WarframeTraUtils;
import com.twg.common.utils.spring.SpringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WarframeTraTask {

    @Scheduled(cron = "0 0 0 1/7 * ? ")
    //@Log(title = "WarframeTra",businessType = BusinessType.INSERT)
    public void TaskUserDict() {
        SpringUtils.getBean(WarframeTraUtils.class).getUserDict();
    }
}
