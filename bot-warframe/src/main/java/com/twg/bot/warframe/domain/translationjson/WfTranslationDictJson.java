package com.twg.bot.warframe.domain.translationjson;


import com.twg.common.core.domain.BaseEntity;

public class WfTranslationDictJson extends BaseEntity {
    private Long id;
    private String zh;
    private String en;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
