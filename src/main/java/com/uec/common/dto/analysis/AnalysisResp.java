package com.uec.common.dto.analysis;

import java.util.List;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
public class AnalysisResp {


    //热点舆情表格
    /**
     * 右下角更多链接
     */

    private String moreViewUrl;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章来源
     */
    private String articleSource;

    /**
     * 文章id
     */
    private String articleId;
    /**
     * 发布时间
     */
    private String publishTime;


    //预警分析柱状图
    /**
     * 预警等级 重大
     */
    private int level1;
    /**
     * 预警等级 较大
     */
    private int level2;
    /**
     * 预警等级 轻微
     */
    private int level3;
    /**
     * 预警等级 一般
     */
    private int Level4;


    //媒体分布饼图
    private String percent;
    //发布热区地图
    /**
     * 市县名称
     */
    private String areaName;
    /**
     * 热度值
     */
    private int hotValue;
    //来源排行柱状图
    /**
     * 来源网站的名称
     */
    private String siteName;
    /**
     * 文章总数
     */
    private int siteSum;
    //情感指数折线图
    /**
     * 每日情感指数平均值
     */
    private int emoIndexAvg;
    //词云分布
    /**
     * 词云
     */
    private List<String> wordClouds;



    public String getMoreViewUrl() {
        return moreViewUrl;
    }

    public void setMoreViewUrl(String moreViewUrl) {
        this.moreViewUrl = moreViewUrl;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleSource() {
        return articleSource;
    }

    public void setArticleSource(String articleSource) {
        this.articleSource = articleSource;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getLevel1() {
        return level1;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }

    public int getLevel2() {
        return level2;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public int getLevel3() {
        return level3;
    }

    public void setLevel3(int level3) {
        this.level3 = level3;
    }

    public int getLevel4() {
        return Level4;
    }

    public void setLevel4(int level4) {
        Level4 = level4;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getHotValue() {
        return hotValue;
    }

    public void setHotValue(int hotValue) {
        this.hotValue = hotValue;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getSiteSum() {
        return siteSum;
    }

    public void setSiteSum(int siteSum) {
        this.siteSum = siteSum;
    }

    public int getEmoIndexAvg() {
        return emoIndexAvg;
    }

    public void setEmoIndexAvg(int emoIndexAvg) {
        this.emoIndexAvg = emoIndexAvg;
    }

    public List<String> getWordClouds() {
        return wordClouds;
    }

    public void setWordClouds(List<String> wordClouds) {
        this.wordClouds = wordClouds;
    }
}
