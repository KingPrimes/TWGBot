package com.twg.bot.warframe.utils;


import com.twg.bot.warframe.dao.GlobalStates;
import com.twg.bot.warframe.dao.SocketGlobalStates;
import com.twg.common.core.redis.RedisCache;
import com.twg.common.utils.DateUtils;
import com.twg.common.utils.spring.SpringUtils;

import java.util.Date;
import java.util.Locale;

import static com.twg.bot.enums.WarframeTypeEnum.REDIS_MISSION_KEY;

/**
 * @description: Warframe 全局状态查询
 */
public class WarframeMissionUtils {

    public static void isUpdated(SocketGlobalStates states) {
        SocketGlobalStates redisState = SpringUtils.getBean(RedisCache.class).getCacheObject(REDIS_MISSION_KEY.getType());
        if (redisState == null) {
            SpringUtils.getBean(RedisCache.class).setCacheObject(REDIS_MISSION_KEY.getType(), states);
            return;
        }

        GlobalStates form = states.getPacket().getData();
        GlobalStates redisForm = redisState.getPacket().getData();

        if (form.getArbitration().getNode() == null) {
            states.getPacket().getData().setArbitration(redisForm.getArbitration());
        }
        SpringUtils.getBean(RedisCache.class).setCacheObject(REDIS_MISSION_KEY.getType(), states);

        //判断警报是否相同
        if (form.getAlerts() != null && redisForm.getAlerts() != null) {
            if (!form.getAlerts().equals(redisForm.getAlerts())) {
                WarframeDataUpdateMission.updateAlerts();
            }
        }
        //判断仲裁是否相同
        if (form.getArbitration() != null && redisForm.getArbitration() != null) {
            if (form.getArbitration().getNode() != null) {
                if (!form.getArbitration().equals(redisForm.getArbitration())) {
                    new Thread(WarframeDataUpdateMission::updateArbitration).start();
                }
            }
        }

        //判断 希图斯 是否相同
        if (form.getCetusCycle() != null && redisForm.getCetusCycle() != null) {
            if (form.getCetusCycle().getState().toLowerCase(Locale.ROOT).equals("day")) {
                if (DateUtils.getDateHour(form.getCetusCycle().getExpiry(), new Date()) == 0) {
                    long date = DateUtils.getDateMin(form.getCetusCycle().getExpiry(), new Date());
                    if (date == 13) {
                        new Thread(() -> WarframeDataUpdateMission.updateCetusCycle(DateUtils.getDate(form.getCetusCycle().getExpiry(), new Date()))).start();
                    }
                }
            }
        }
        //判断每日特惠是否相同
        if (form.getDailyDeals() != null && redisForm.getDailyDeals() != null) {
            if (form.getDailyDeals().get(0).getItem() != null) {
                if (!form.getDailyDeals().equals(redisForm.getDailyDeals())) {
                    new Thread(WarframeDataUpdateMission::updateDailyDeals).start();
                }
            }
        }
        //判断活动是否相同
        if (form.getEvents() != null && redisForm.getEvents() != null) {
            if (form.getEvents().retainAll(redisForm.getEvents()) && redisForm.getEvents().retainAll(form.getEvents())) {
                new Thread(WarframeDataUpdateMission::updateEvents).start();
            }
        }
        //判断入侵是否相同
        if (form.getInvasions() != null && redisForm.getInvasions() != null) {
            if (form.getInvasions().retainAll(redisForm.getInvasions()) && redisForm.getInvasions().retainAll(form.getInvasions())) {
                new Thread(WarframeDataUpdateMission::updateInvasions).start();
            }
        }
        //判断钢铁轮换是否相同
        if (form.getSteelPath() != null && redisForm.getSteelPath() != null) {
            if (!form.getSteelPath().equals(redisForm.getSteelPath())) {
                new Thread(WarframeDataUpdateMission::updateSteelPath).start();
            }
        }
        //判断虚空商人是否相同
        if (form.getVoidTrader() != null && redisForm.getVoidTrader() != null) {
            if (!form.getVoidTrader().equals(redisForm.getVoidTrader())) {
                if (form.getVoidTrader().getInventory().size() != 0 && form.getVoidTrader().getActive()) {
                    new Thread(() -> WarframeDataUpdateMission.updateVoidTrader("奸商又来了！\n这次又带来了什么坏东西呢？\n奸商刺杀1等3")).start();
                } else {
                    new Thread(() -> WarframeDataUpdateMission.updateVoidTrader("奸商走了！\n下次会带来什么好东西呢？")).start();
                }
            }
        }
    }
}
