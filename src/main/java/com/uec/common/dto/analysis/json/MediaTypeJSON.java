package com.uec.common.dto.analysis.json;

import lombok.Data;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
@Data
public class MediaTypeJSON {

    private String mediaName;
    private long articleNum;
    private long positive;
    private long negative;


}
