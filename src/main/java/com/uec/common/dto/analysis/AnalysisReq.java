package com.uec.common.dto.analysis;

import lombok.Data;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
@Data
public class AnalysisReq {
    /**
     * 图表类型标识
     * 媒体分组汇总趋势图:media_trend
     * 最新舆情表格:last_article
     * 预警分析柱状图:warning_analysis
     * 媒体分布饼图:media_distribution
     * 发布热区地图:hot_region
     * 来源排行柱状图:site_source
     * 情感指数折线图:emotion_index
     * 词云分布图:word_cloud
     */
    private String chartType;
    /**
     * 时间间隔标识 --今日:0,近7天:1, 近30天:3
     */
    private int timeType;

    /**
     * 方案id
     */
    private String pid;
    /**
     * 预警等级: 0 最新 1 重大 2 较大，默认为0
     */
    private int warnLevel;
}
