package com.uec.common.dto.analysis.json;

import lombok.Data;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
@Data
public class WarnLevelJSON {

    /**
     * 预警等级： 重大
     */
    private int level1;
    /**
     * 预警等级： 较大
     */
    private int level2;
    /**
     * 预警等级： 轻微
     */
    private int level3;
    /**
     * 预警等级： 一般
     */
    private int level4;
    /**
     * 纵轴时间
     */
    private String create_time;
}
