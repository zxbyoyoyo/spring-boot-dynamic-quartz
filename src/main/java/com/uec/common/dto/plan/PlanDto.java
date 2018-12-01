package com.uec.common.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 方案传输实体
 */
@Data
public class PlanDto implements Serializable {

    //主键UUID
    private String id;
    //方案名称
    private String kpName;
    //预警词
    private String kpMonitorKeys;
    //方案类型【1、模糊2、精准3、人物4、文章】
    private Integer kpType;
    //是否公开
    private boolean kpIsPublic;
    //开始方案时间，时间戳
    private Date kpStartTime;
    //结束方案时间，时间戳
    private Date kpEndTime;
    //时间范围【】
    private Integer kpTimeRange;
    //监控范围
    private Integer kpSiteRange;
    //周报类型
    private Integer kpRpType;
    //周报发送类型
    private Integer kpRpSendType;

    //开始时间，0-24
    private Integer kpWarnStartTime;
    //结束时间,0-24
    private Integer kpWarnEndTime;
    //预警发送类型
    private Integer kpWarnSendType;
    //是否周末预警
    private boolean kpHolidayWarn;

    //排除词。，隔开
    private String excludeWords;
    //地域词。，隔开
    private String regionWords;

    //是否预警
    private boolean kpIsWarn;
    //预警模型IDS
    private String  kpWranModels;

    //符合预警模型check
    private boolean kpWranCheck;
    //预警时间check
    private boolean kpWranTimeCheck;
    //自定义预警词开关
    private boolean kpWranSelfcheck;
    //自定义预警词
    private String kpWranSelf;

    //字典字段
    private Date createTime;

    private Date updateTime;

    private String createUser;

    private String updateUser;

    private Integer status;
    //1-删除
    private boolean delFlag;


    /**
     *   "createTime":121121212,
     *   "updateTime":121212121212,
     *   "createUser":"UUID",
     *   "updateUser":"UUID",
     *   "kpStatus":1
     * }
     */

}
