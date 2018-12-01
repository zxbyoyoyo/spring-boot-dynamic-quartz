package com.uec.common.dto.analysis.json;

import lombok.Data;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
@Data
public class MediaTypeField {
    private int weixin;
    private int weibo;
    private int website;
    private int app;
    private int paper;
    private String create_time;
}
