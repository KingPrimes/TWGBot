package com.twg.bot.warframe.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/**
 * 紫卡倾向
 *
 * @author KingPrimes
 * @date 2021-06-02
 */
public class WarframeRivenTrend {
    private static final long serialVersionUID = 1L;

    private Long rivenTrendId;

    /**
     * 武器名字
     */
    private String rivenTrendName;

    /**
     * 旧的倾向
     */
    private String rivenTrendOldNum;

    /**
     * 新的倾向
     */
    private String rivenTrendNewNum;

    /**
     * 旧的倾向点数
     */
    private String rivenTrendOldDot;

    /**
     * 新的倾向点数
     */
    private String rivenTrendNewDot;

    private String traCh;

    /**
     * 此次更新得时间
     */
    private String isDate;


    public WarframeRivenTrend(WarframeRivenTrend warframeRivenTrend) {
        this.rivenTrendId = warframeRivenTrend.getRivenTrendId();
        this.rivenTrendName = warframeRivenTrend.getRivenTrendName();
        this.rivenTrendNewDot = warframeRivenTrend.getRivenTrendNewDot();
        this.rivenTrendNewNum = warframeRivenTrend.getRivenTrendNewNum();
        this.rivenTrendOldDot = warframeRivenTrend.getRivenTrendOldDot();
        this.rivenTrendOldNum = warframeRivenTrend.getRivenTrendOldNum();
        this.traCh = warframeRivenTrend.getTraCh();
        this.isDate = warframeRivenTrend.getIsDate();
    }

    public WarframeRivenTrend() {

    }

    public String getIsDate() {
        return isDate;
    }

    public void setIsDate(String isDate) {
        this.isDate = isDate;
    }

    public String getTraCh() {
        return traCh;
    }

    public void setTraCh(String traCh) {
        this.traCh = traCh;
    }

    public Long getRivenTrendId() {
        return rivenTrendId;
    }

    public void setRivenTrendId(Long rivenTrendId) {
        this.rivenTrendId = rivenTrendId;
    }

    public String getRivenTrendName() {
        return rivenTrendName;
    }

    public void setRivenTrendName(String rivenTrendName) {
        this.rivenTrendName = rivenTrendName;
    }

    public String getRivenTrendOldNum() {
        return rivenTrendOldNum;
    }

    public void setRivenTrendOldNum(String rivenTrendOldNum) {
        this.rivenTrendOldNum = rivenTrendOldNum;
    }

    public String getRivenTrendNewNum() {
        return rivenTrendNewNum;
    }

    public void setRivenTrendNewNum(String rivenTrendNewNum) {
        this.rivenTrendNewNum = rivenTrendNewNum;
    }

    public String getRivenTrendOldDot() {
        return rivenTrendOldDot;
    }

    public void setRivenTrendOldDot(String rivenTrendOldDot) {
        this.rivenTrendOldDot = rivenTrendOldDot;
    }

    public String getRivenTrendNewDot() {
        return rivenTrendNewDot;
    }

    public void setRivenTrendNewDot(String rivenTrendNewDot) {
        this.rivenTrendNewDot = rivenTrendNewDot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarframeRivenTrend)) return false;
        WarframeRivenTrend trend = (WarframeRivenTrend) o;
        return Objects.equals(getRivenTrendId(),
                trend.getRivenTrendId())

                && Objects.equals(getRivenTrendName(),
                trend.getRivenTrendName())

                && Objects.equals(getRivenTrendOldNum(),
                trend.getRivenTrendOldNum())

                && Objects.equals(getRivenTrendNewNum(),
                trend.getRivenTrendNewNum())

                && Objects.equals(getRivenTrendOldDot(),
                trend.getRivenTrendOldDot())

                && Objects.equals(getRivenTrendNewDot(),
                trend.getRivenTrendNewDot())

                && Objects.equals(getTraCh(),
                trend.getTraCh());
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("rivenTrendId", getRivenTrendId())
                .append("rivenTrendName", getRivenTrendName())
                .append("rivenTrendOldNum", getRivenTrendOldNum())
                .append("rivenTrendNewNum", getRivenTrendNewNum())
                .append("rivenTrendOldDot", getRivenTrendOldDot())
                .append("rivenTrendNewDot", getRivenTrendNewDot())
                .toString();
    }
}
