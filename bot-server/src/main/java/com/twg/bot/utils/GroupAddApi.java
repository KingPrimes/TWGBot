package com.twg.bot.utils;


import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * QQ-SDK的扩展
 * 对于群
 */
public class GroupAddApi {
    /**
     * 获取群头像Url地址
     *
     * @param groupId 群号
     * @return Url地址
     */
    public static String getGroupHeadImage(Long groupId) {
        //返回拼接的群头像网址
        return "https://p.qlogo.cn/gh/" + groupId + "/" + groupId + "/100";
    }

    /**
     * 判断用户是否是管理员或者群主
     *
     * @param bot   bot
     * @param event event
     * @return true 是管理员或群主
     */
    public static boolean isAdmin(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        String role = bot.getGroupMemberInfo(event.getGroupId(), event.getUserId(), true).getData().getRole();
        if (role == null || role.length() == 0) {
            return false;
        }
        return role.equals("owner") || role.equals("admin");
    }

    /**
     * 判断机器人是否是管理员
     *
     * @param bot     bot
     * @param groupId groupId
     * @return true 是管理员或群主
     */
    public static boolean isAdmin(@NotNull Bot bot, long groupId) {
        String role = Objects.requireNonNull(bot.getGroupMemberInfo(groupId, bot.getSelfId(), true)).getData().getRole();
        if (role == null || role.length() == 0) {
            return false;
        }
        return role.equals("owner") || role.equals("admin");
    }
}
