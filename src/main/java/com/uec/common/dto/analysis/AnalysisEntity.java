package com.uec.common.dto.analysis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  舆情分析结果持久化类</p>
 * <p>Author:朱晓兵</p>
 */
@Data
public class AnalysisEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 唯一id
     */
    private String uuid;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 方案id
     */
    private String pid;
    /**
     * 媒体类型
     */
    private String mediaType;
    /**
     * 热点地区
     */
    private String region;
    /**
     * 网站排行
     */
    private String siteRate;
    /**
     * 情感指数
     */
    private Double emoIndex;

    /**
     * 统计数据的日期
     */
    private Date insertTime;
    /**
     * 简报类型(0:日报,1：周报,2：月报)
     */
    private int reportType;
    /**
     * 数据状态(0:已删除,1:正常)
     */
    private Integer dataState;
    /**
     * 创建时间
     */

    private Date createTime;
    /**
     * 创建人id
     */
    private String createUser;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人id
     */
    private String updateUser;

    /**
     * 方案名称
     */
    private String planName;
    /**
     * 方案类型
     */
    private String planType;


    /**
     * 报告名称
     */
    private String reportName;


}