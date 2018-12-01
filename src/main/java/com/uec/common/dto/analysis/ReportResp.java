package com.uec.common.dto.analysis;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  简报中心</p>
 * <p>Author:朱晓兵</p>
 */
@Data
public class ReportResp implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reportId;
    private String reportTime;
    private String reportName;
    private String planType;
    private String planName;
    private String viewAnalysisUrl;
}
