package com.uec.task.job;

import com.uec.task.base.BaseJob;
import com.uec.task.service.AnalysisJobService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  舆情分析--定时任务[日、周、月]</p>
 * <p>Author:朱晓兵</p>
 */

public class AnalysisDayReportJob implements BaseJob {

    private static Logger _log = LoggerFactory.getLogger(AnalysisDayReportJob.class);

    @Autowired
    AnalysisJobService analysisJobService;

    /**
     * 每日定时任务
     * 监测方案 运行中||监测方案暂停&& 暂停时间为昨天
     */
    @Override
    public void execute(JobExecutionContext context) {
        analysisJobService.dayTask();
    }


}
