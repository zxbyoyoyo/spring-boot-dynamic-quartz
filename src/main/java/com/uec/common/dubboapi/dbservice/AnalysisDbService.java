package com.uec.common.dubboapi.dbservice;


import com.uec.common.PageResult;
import com.uec.common.dto.analysis.AnalysisEntity;
import com.uec.common.dto.analysis.ReportReq;
import com.uec.common.dto.analysis.ReportResp;
import com.uec.common.dto.plan.PlanDto;

import java.util.Date;
import java.util.List;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  dubbo</p>
 * <p>Author:朱晓兵</p>
 */
public interface AnalysisDbService {

    /**
     * 保存每个方案每日舆情分析数据
     *
     * @param analysisEntity
     * @return
     */
    AnalysisEntity insert(AnalysisEntity analysisEntity);

    /**
     * 查询某个方案在某个区间数据
     *
     * @param pid
     * @param reportType
     * @param startTime
     * @param endTime
     * @return
     */
    List<AnalysisEntity> qryByTimeRegion(String pid, int reportType, Date startTime, Date endTime);

    /**
     * 查询全部方案 from mongodb
     *
     * @return
     */
    List<PlanDto> findAllPlanInfo();


    /**
     * 查询全部方案 from mongodb by id
     *
     * @return
     */
    PlanDto findPlanById(String pid);


    /**
     * 查询指定方案 舆情分析简报数据
     *
     * @return
     */
    List<String> findMediaTypeByUserIDandDate(String userId, Date date);

    /**
     * 查询 简报分页数据
     * @param reportReq
     * @return
     */
    PageResult<ReportResp> qryPageReport(ReportReq reportReq);
    /**
     * 删除指定 简报数据
     * @param reportReq
     * @return
     */
    int deleteReports(ReportReq reportReq);
    /**
     * 查询指定 简报数据
     * @param reportReq
     * @return
     */
    ReportResp qryDayReportDetail(ReportReq reportReq);
}
