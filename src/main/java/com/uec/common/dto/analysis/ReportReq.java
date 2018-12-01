package com.uec.common.dto.analysis;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  简报中心 查询参数</p>
 * <p>Author:朱晓兵</p>
 */
@Data
public class ReportReq implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 报告id
     */
    private String reportId;
    /**
     * 待删除 报告id集合
     */
    private List<String> reportIds;

    /**
     * 第x页
     */
    private int pageNo;

    /**
     * 每页个数
     */
    private int pageSize;


}
