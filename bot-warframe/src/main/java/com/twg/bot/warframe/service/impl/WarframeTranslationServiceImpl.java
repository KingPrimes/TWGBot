package com.twg.bot.warframe.service.impl;


import com.twg.bot.warframe.domain.WarframeTranslation;
import com.twg.bot.warframe.mapper.WarframeTranslationMapper;
import com.twg.bot.warframe.service.IWarframeTranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WarframeTranslationService业务层处理
 * Warframe 中英文翻译
 *
 * @author KingPrimes
 * @date 2021-05-24
 */
@Service
public class WarframeTranslationServiceImpl implements IWarframeTranslationService {
    @Autowired
    private WarframeTranslationMapper warframeTranslationMapper;

    /**
     * 查询
     *
     * @param traId ID
     * @return 结果
     */
    @Override
    public WarframeTranslation selectWarframeTranslationById(Long traId) {
        return warframeTranslationMapper.selectWarframeTranslationById(traId);
    }

    /**
     * 英文 ->> 中文
     * 未找到返回原文
     *
     * @param us_en 英文
     */
    @Override
    public String enToZh(String us_en) {
        try {
            String traCh = warframeTranslationMapper.enToZh(us_en);
            if (traCh == null || traCh.equals("")) {
                return us_en;
            }
            return traCh;
        } catch (Exception e) {
            e.printStackTrace();
            return us_en;
        }

    }

    /**
     * 中文 ->> 英文
     * 未找到返回原文
     *
     * @param cn_zh 中文
     */
    @Override
    public String zhToEn(String cn_zh) {
        try {
            String tra = warframeTranslationMapper.zhToEn(cn_zh);
            if (tra != null || tra != "") {
                return tra;
            } else {
                return cn_zh;
            }
        } catch (Exception ignored) {
            return cn_zh;
        }
    }

    /**
     * 模糊查询英文->中 中->英
     * 双条件
     *
     * @param enOrzh 数据
     * @return 结果
     */
    @Override
    public String enOrzh(String enOrzh) {
        try {
            WarframeTranslation tra = warframeTranslationMapper.enOrzh(enOrzh);
            if (tra != null) {
                return "中文:" + tra.getTraCh() + "\n英文:" + tra.getTraEn();
            }
        } catch (Exception e) {
            return enOrzh;
        }
        return null;
    }

    /**
     * 中文 ->> 英文 列表 模糊匹配
     * 未找到返回原文
     *
     * @param cn_zh 中文
     */
    @Override
    public List<String> zhToEnList(String cn_zh) {
        try {
            List<String> trays = new ArrayList<>();
            List<WarframeTranslation> translations = warframeTranslationMapper.zhToEnList(cn_zh);
            for (WarframeTranslation warframeTranslation : translations) {
                trays.add(warframeTranslation.getTraEn());
            }
            return trays;
        } catch (Exception e) {
            return Collections.singletonList(cn_zh);
        }
    }

    /**
     * 英文 ->> 中文 列表 模糊匹配
     * 未找到返回原文
     *
     * @param us_en 英文
     */
    @Override
    public List<String> enToZhList(String us_en) {
        try {
            List<String> trays = new ArrayList<>();
            List<WarframeTranslation> translations = warframeTranslationMapper.enToZhList(us_en);
            for (WarframeTranslation warframeTranslation : translations) {
                trays.add(warframeTranslation.getTraCh());
            }
            return trays;
        } catch (Exception e) {
            return Collections.singletonList(us_en);
        }
    }

    /**
     * 模糊搜索中英文匹配值
     *
     * @param key 值
     */
    @Override
    public List<WarframeTranslation> enAndZhToList(String key) {
        return warframeTranslationMapper.enAndZhToList(key);
    }

    /**
     * 查询列表
     *
     * @param warframeTranslation 条件
     * @return 结果
     */
    @Override
    public List<WarframeTranslation> selectWarframeTranslationList(WarframeTranslation warframeTranslation) {
        return warframeTranslationMapper.selectWarframeTranslationList(warframeTranslation);
    }

    /**
     * 新增
     *
     * @param warframeTranslation 数据
     * @return 结果
     */
    @Override
    public int insertWarframeTranslation(WarframeTranslation warframeTranslation) {
        return warframeTranslationMapper.insertWarframeTranslation(warframeTranslation);
    }

    /**
     * 批量插入
     * 去除相同数据
     *
     * @param warframeTranslation 数据
     * @return 结果
     */
    @Override
    public int insertWarframeTranslationList(List<WarframeTranslation> warframeTranslation) {
        return warframeTranslationMapper.insertWarframeTranslationList(warframeTranslation);
    }

    /**
     * 修改
     *
     * @param warframeTranslation 数据
     * @return 结果
     */
    @Override
    public int updateWarframeTranslation(WarframeTranslation warframeTranslation) {
        return warframeTranslationMapper.updateWarframeTranslation(warframeTranslation);
    }

    /**
     * 批量删除
     *
     * @param traIds 需要删除的ID
     * @return 结果
     */
    @Override
    public int deleteWarframeTranslationByIds(Long[] traIds) {
        return warframeTranslationMapper.deleteWarframeTranslationByIds(traIds);
    }

    /**
     * 删除信息
     *
     * @param traId ID
     * @return 结果
     */
    @Override
    public int deleteWarframeTranslationById(Long traId) {
        return warframeTranslationMapper.deleteWarframeTranslationById(traId);
    }
}
