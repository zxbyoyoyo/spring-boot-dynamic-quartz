package com.uec.common.dto.analysis.json;

import lombok.Data;

import java.util.List;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
@Data
public class PageAnalysisJSON {
    private String moreViewUrl;
    private List<ArticleJSON> content;
}
