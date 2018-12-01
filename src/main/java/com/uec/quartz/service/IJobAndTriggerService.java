package com.uec.quartz.service;


import com.uec.quartz.dto.JobAndTriggerDto;
import com.uec.quartz.util.PageUtil;

import java.util.Map;


/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
public interface IJobAndTriggerService {
    /**
     * @param @param  search
     * @param @return 参数
     * @return Map<String   ,   Object>    返回类型
     * @throws
     * @Title: getPageJob
     * @Description: TODO(查询定时任务 ， 分页)
     */
    public Map<String, Object> getPageJob(PageUtil search);

    /**
     * @param @return 参数
     * @return JobAndTriggerDto    返回类型
     * @throws
     * @Title: getPageJobmod
     * @Description: TODO(查询定时任务)
     */
    public JobAndTriggerDto getPageJobmod();

    /**
     * @param @param  jobClassName 任务路径名称
     * @param @param  jobGroupName 任务分组
     * @param @param  cronExpression cron时间规则
     * @param @throws Exception    参数
     * @return void    返回类型
     * @throws
     * @Title: addJob
     * @Description: TODO(添加任务)
     */
    public void addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception;

    /**
     * @param @param  jobClassName 任务路径名称
     * @param @param  jobGroupName 任务分组
     * @param @param  cronExpression cron时间规则
     * @param @throws Exception    参数
     * @return void    返回类型
     * @throws
     * @Title: updateJob
     * @Description: TODO(更新定时任务)
     */
    public void updateJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception;

    /**
     * @param @param  jobClassName 任务路径名称
     * @param @param  jobGroupName 任务分组
     * @param @throws Exception    参数
     * @return void    返回类型
     * @throws
     * @Title: deleteJob
     * @Description: TODO(删除定时任务)
     */
    public void deleteJob(String jobClassName, String jobGroupName) throws Exception;

    /**
     * @param @param  jobClassName 任务路径名称
     * @param @param  jobGroupName 任务分组
     * @param @throws Exception    参数
     * @return void    返回类型
     * @throws
     * @Title: pauseJob
     * @Description: TODO(暂停定时任务)
     */
    public void pauseJob(String jobClassName, String jobGroupName) throws Exception;

    /**
     * @param @param  jobClassName 任务路径名称
     * @param @param  jobGroupName 任务分组
     * @param @throws Exception    参数
     * @return void    返回类型
     * @throws
     * @Title: resumejob
     * @Description: TODO(恢复任务)
     */
    public void resumejob(String jobClassName, String jobGroupName) throws Exception;
}
