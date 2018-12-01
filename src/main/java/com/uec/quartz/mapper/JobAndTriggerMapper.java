package com.uec.quartz.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.uec.quartz.dto.JobAndTriggerDto;

import java.util.List;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
public interface JobAndTriggerMapper {

    List<JobAndTriggerDto> getJobAndTriggerDetails(Pagination page);

    JobAndTriggerDto getJobAndTriggerDto();
}
