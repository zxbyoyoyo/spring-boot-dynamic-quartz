package com.uec.task.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.AtomicLongMap;
import com.ronglian.datalake.dubboapi.bean.AggQueryBean;
import com.ronglian.datalake.dubboapi.bean.Words;
import com.ronglian.datalake.dubboapi.core.DubboRequest;
import com.ronglian.datalake.dubboapi.core.DubboResponse;
import com.ronglian.datalake.dubboapi.dto.request.IKnownsDetailParam;
import com.ronglian.datalake.dubboapi.dto.response.QueryResultBean;
import com.ronglian.datalake.dubboapi.service.IQueryForIknowsService;
import com.ronglian.datalake.dubboapi.util.BusinessIdGenerator;
import com.uec.common.dto.analysis.AnalysisConstant;
import com.uec.common.dto.analysis.AnalysisEntity;
import com.uec.common.dto.analysis.json.HotRegionJSON;
import com.uec.common.dto.analysis.json.MediaTypeEnum;
import com.uec.common.dto.analysis.json.MediaTypeJSON;
import com.uec.common.dto.analysis.json.SiteRankJSON;
import com.uec.common.dto.analysis.util.LocalDateUtils;
import com.uec.common.dto.plan.PlanDto;
import com.uec.common.dto.plan.Region;
import com.uec.common.dubboapi.dbservice.AnalysisDbService;
import com.uec.task.base.RegionLoading;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.uec.common.dto.analysis.AnalysisConstant.*;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  舆情分析--定时任务[日、周、月]</p>
 * <p>Author:朱晓兵</p>
 */
@Service
@Slf4j
public class AnalysisJobService {


    private static final int REPORT_TYPE_DAY = 0;
    private static final int REPORT_TYPE_WEEK = 1;
    private static final int REPORT_TYPE_MONTH = 2;
    ;

    /**
     * 舆情分析 dubbo 数据库操作接口
     */
    @Reference(version = "${dubbo.service.version}")
    AnalysisDbService analysisDbService;

    /**
     * 内部es查询接口
     */
    @Reference(version = "1.0.0")
    IQueryForIknowsService iQueryForIknowsService;


    /**
     * 每日定时任务
     * 监测方案 运行中||监测方案暂停&& 暂停时间为昨天
     */
    public void dayTask() {
        //1.查询所有方案，遍历方案，获取基本信息
        log.info(">>>>>>>>>>>>>>>>>>>>舆情分析 每日 定时任务 开始 " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        List<PlanDto> allPlanLst = analysisDbService.findAllPlanInfo();
        log.info("@@@ 每日查询所有方案 " + allPlanLst.size());
        //2.校验时间，是否需要查询入库（历史数据），监测方案 运行中||监测方案暂停&& 暂停时间为昨天
        boolean isRunning;
        boolean isStopInYesterday;
        // 公共es查询开始和结束时间
        long startTime = LocalDateUtils.localDateToDate(LocalDate.now()).getTime();
        long endTime = LocalDateUtils.localDateTimeToDate(LocalDateTime.now()).getTime();

        for (PlanDto planDto : allPlanLst) {
            isRunning = planDto.getStatus() == 1;
            isStopInYesterday = planDto.getStatus() == 0 &&
                    LocalDateUtils.dateToLocalDateTime(planDto.getUpdateTime()).compareTo(LocalDate.now().minusDays(1).atStartOfDay()) > 0;
            log.info("#### 每日具体方案 " + planDto.getId());
            log.info("#### 每日具体方案 是否保存 " + (isRunning || isStopInYesterday));
            if (isRunning || isStopInYesterday) {
                //3.查询es&&入库
                AnalysisEntity insertEntity = new AnalysisEntity();
                //#####
                //***1.媒体分布
                Map<String, Long> mediaTypeAggMap = doDubboAggGroup(planDto, MEDIA_TYPE_AGG_FIELD, 0, 0, startTime, endTime);
                Map<String, Long> todayAggMap = MediaTypeEnum.mergeAggMap(mediaTypeAggMap);
                MediaTypeJSON mediaTypeJSON;
                List<MediaTypeJSON> mediaTypeJSONList = new ArrayList<>();
                for (Map.Entry<String, Long> entry : todayAggMap.entrySet()) {
                    mediaTypeJSON = new MediaTypeJSON();
                    mediaTypeJSON.setMediaName(entry.getKey());
                    mediaTypeJSON.setArticleNum(entry.getValue());
                    mediaTypeJSONList.add(mediaTypeJSON);
                }
                insertEntity.setMediaType(JSON.toJSONString(mediaTypeJSONList));

                //***2.发布热区
                HotRegionJSON hotRegionJSON;
                List<HotRegionJSON> hotRegionJSONList = new ArrayList<>();
                Map<String, Long> hotRegionAggMap = doDubboAggGroup(planDto, REGION_AGG_FIELD, 0, 0, startTime, endTime);
                for (Map.Entry<String, Long> entry : hotRegionAggMap.entrySet()) {
                    hotRegionJSON = new HotRegionJSON();
                    hotRegionJSON.setAreaCode(entry.getKey());
                    if (RegionLoading.REGION_MAP.get(entry.getKey()) != null) {
                        hotRegionJSON.setAreaName(RegionLoading.REGION_MAP.get(entry.getKey()).getRegionName());
                    }
                    hotRegionJSON.setArticleNum(entry.getValue());
                    hotRegionJSONList.add(hotRegionJSON);
                }
                insertEntity.setRegion(JSON.toJSONString(hotRegionJSONList));
                //***3.网站排行
                Map<String, Long> siteRankAggMap = doDubboAggGroup(planDto, WEBSITE_AGG_FIELD, 0, 0, startTime, endTime);
                //排除sourceCrawl为空的情况
                siteRankAggMap.remove("");
                log.info("siteRankAggMap.size---" + siteRankAggMap.size());
                log.info("siteRankAggMap---" + siteRankAggMap.size());

                List<Map.Entry<String, Long>> siteRankNumList = new ArrayList<>(siteRankAggMap.entrySet());
                Collections.sort(siteRankNumList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
                // *** 取前 SITE_STORE_NUM 条 入库
                List<Map.Entry<String, Long>> sortedList = siteRankNumList.size() > AnalysisConstant.SITE_STORE_NUM ?
                        siteRankNumList.subList(0, AnalysisConstant.SITE_STORE_NUM) : siteRankNumList;

                List<SiteRankJSON> siteRankJSONList = new ArrayList<>();
                SiteRankJSON siteRank;
                for (Map.Entry<String, Long> entry : sortedList) {
                    siteRank = new SiteRankJSON();
                    siteRank.setSiteName(entry.getKey());
                    siteRank.setArticleNum(entry.getValue());
                    siteRankJSONList.add(siteRank);
                }
                insertEntity.setSiteRate(JSON.toJSONString(siteRankJSONList));
                //***4.情感指数
                double emoIndex = doDubboAggAvg(planDto, EMO_IDX_AGG_FIELD, 0, 0, startTime, endTime);
                insertEntity.setEmoIndex(emoIndex);
                //***5.基本信息
                //>>> 统计的日期
                Date yesterday = LocalDateUtils.localDateToDate(LocalDate.now().minusDays(1));
                insertEntity.setInsertTime(yesterday);

                //>>>数据状态（0.删除，1.正常）
                insertEntity.setDataState(1);
                //>>> 唯一id
                insertEntity.setUuid(UUID.randomUUID().toString().replace("-", ""));
                //>>> 用户id
                insertEntity.setUid(planDto.getCreateUser());
                //>>> 方案id
                insertEntity.setPid(planDto.getId());
                //>>> 方案名称
                insertEntity.setPlanName(planDto.getKpName());
                //>>> 方案名称
                insertEntity.setPlanType(String.valueOf(planDto.getKpType()));
                //>> 简报类型(0:日报,1：周报,2：月报)
                insertEntity.setReportType(REPORT_TYPE_DAY);
                //>>> 报告名称
                insertEntity.setReportName(planDto.getKpName() + "-" + DateFormatUtils.format(yesterday, "yyyy-MM-dd"));

                insertEntity.setCreateTime(new Date());
                insertEntity.setUpdateTime(new Date());
                analysisDbService.insert(insertEntity);

            }

        }
        log.info(">>>>>>>>>>>>>>>>>>>>舆情分析 每日 定时任务 结束 " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

    }

    /**
     * 每周定时任务：汇总前七天数据
     * 监测方案 运行中||监测方案暂停&& 暂停时间为昨天
     */
    public void weekTask() {
        log.info(">>>>>>>>>>>>>>>>>>>>舆情分析 每周 定时任务 开始 " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        //1.查询所有方案，遍历方案，获取基本信息
        List<PlanDto> allPlanLst = analysisDbService.findAllPlanInfo();
        log.info("@@@ 每周查询所有方案 " + allPlanLst.size());

        //方案运行状态 运行1|停止0
        boolean isRunning;
        //方案停止时间 是否在上周
        boolean isStopInLastWeek;


        LocalDateTime lastWeek = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime todayZero = LocalDate.now().atStartOfDay();

        for (PlanDto planDto : allPlanLst) {

            isRunning = planDto.getStatus() == 1;
            isStopInLastWeek = planDto.getStatus() == 0 && LocalDateUtils.dateToLocalDateTime(planDto.getUpdateTime()).compareTo(lastWeek) > 0;
            log.info("#### 每周具体方案 " + JSON.toJSONString(planDto));
            log.info("#### 每周具体方案 是否保存 " + (isRunning || isStopInLastWeek));
            if (isRunning || isStopInLastWeek) {
                //数据库中查询前七天
                List<AnalysisEntity> dbData = analysisDbService.qryByTimeRegion(planDto.getId(),
                        REPORT_TYPE_DAY,
                        getLaterTime(lastWeek, LocalDateUtils.dateToLocalDateTime(planDto.getKpStartTime())),
                        isRunning ? LocalDateUtils.localDateTimeToDate(todayZero) : planDto.getUpdateTime());
                log.info("#### 每周具体方案 前七天数据 " + dbData.size());
                //汇总&&入库
                if (CollectionUtils.isNotEmpty(dbData)) {
                    doInsertLogic(planDto, REPORT_TYPE_WEEK, dbData);
                }
            }


        }
        log.info(">>>>>>>>>>>>>>>>>>>>舆情分析 每周 定时任务 结束 " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));


    }


    /**
     * 每月定时任务：汇总前一个月
     */
    public void monthTask() {
        log.info(">>>>>>>>>>>>>>>>>>>>舆情分析 每月 定时任务 开始 " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        //1.查询所有方案，遍历方案，获取基本信息
        List<PlanDto> allPlanLst = analysisDbService.findAllPlanInfo();


        LocalDateTime lastMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime todayZero = LocalDate.now().atStartOfDay();
        boolean isRunning;
        boolean isStopInLastWeek;
        //2.校验时间，是否需要查询入库（历史数据），监测方案 运行中||监测方案暂停&& 暂停时间为昨天
        for (PlanDto planDto : allPlanLst) {
            isRunning = planDto.getStatus() == 1;
            isStopInLastWeek = planDto.getStatus() == 0
                    && LocalDateUtils.dateToLocalDateTime(planDto.getUpdateTime()).compareTo(lastMonth) > 0;

            log.info("#### 每月具体方案 " + JSON.toJSONString(planDto));
            log.info("#### 每月具体方案 是否保存 " + (isRunning || isStopInLastWeek));
            if (isRunning || isStopInLastWeek) {

                //数据库中查询前三十天
                List<AnalysisEntity> dbData = analysisDbService.qryByTimeRegion(planDto.getId(),
                        REPORT_TYPE_DAY,
                        getLaterTime(lastMonth, LocalDateUtils.dateToLocalDateTime(planDto.getKpStartTime())),
                        isRunning ? LocalDateUtils.localDateTimeToDate(todayZero) : planDto.getUpdateTime());
                log.info("#### 每周具体方案 前二十九天数据 " + dbData.size());
                //汇总&&入库
                if (CollectionUtils.isNotEmpty(dbData)) {
                    doInsertLogic(planDto, REPORT_TYPE_MONTH, dbData);
                }

            }

        }

        log.info(">>>>>>>>>>>>>>>>>>>>舆情分析 每月 定时任务 结束 " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

    }

    /**
     * 获取较新的时间
     *
     * @param a
     * @param b
     * @return
     */
    private Date getLaterTime(LocalDateTime a, LocalDateTime b) {
        return LocalDateUtils.localDateTimeToDate(a.compareTo(b) > 0 ? a : b);
    }


    /**
     * 入库逻辑[周、月]
     *
     * @param planDto
     * @param reportType
     * @param dbData
     */
    private void doInsertLogic(PlanDto planDto, int reportType, List<AnalysisEntity> dbData) {
        AtomicLongMap<String> acticleNumMap = AtomicLongMap.create();
        AtomicLongMap<String> positiveNumMap = AtomicLongMap.create();
        AtomicLongMap<String> negativeNumMap = AtomicLongMap.create();
        AtomicLongMap<String> hotRegionNumMap = AtomicLongMap.create();
        AtomicLongMap<String> siteRankNumMap = AtomicLongMap.create();
        Map<String, Double> emotionIndexMap = new HashMap<>();
        for (AnalysisEntity analysisEntity : dbData) {
            //获取 mediaType字段存储的json,[{"mediaName":"weixin","articleNum":11,"positive":23,"negative":15},{"mediaName":"weibo","articleSum":12,"positive":24,"negative":16}]
            String mediaTypeJson = analysisEntity.getMediaType();
            String hotRegion = analysisEntity.getRegion();
            String siteRate = analysisEntity.getSiteRate();
            List<MediaTypeJSON> dayMeidaType = JSON.parseArray(mediaTypeJson, MediaTypeJSON.class);
            List<HotRegionJSON> dayHotRegion = JSON.parseArray(hotRegion, HotRegionJSON.class);
            List<SiteRankJSON> daySiteRank = JSON.parseArray(siteRate, SiteRankJSON.class);
            double dayEmoIndexVal = analysisEntity.getEmoIndex();

            //#####汇总
            //***1.媒体分布
            for (MediaTypeJSON mediaTypeJSON : dayMeidaType) {
                acticleNumMap.addAndGet(mediaTypeJSON.getMediaName(), mediaTypeJSON.getArticleNum());
                positiveNumMap.addAndGet(mediaTypeJSON.getMediaName(), mediaTypeJSON.getPositive());
                negativeNumMap.addAndGet(mediaTypeJSON.getMediaName(), mediaTypeJSON.getNegative());
            }

            //***2.发布热区
            for (HotRegionJSON hotRegionJSON : dayHotRegion) {
                hotRegionNumMap.addAndGet(hotRegionJSON.getAreaCode(), hotRegionJSON.getArticleNum());
            }

            //***3.网站排行
            for (SiteRankJSON siteRank : daySiteRank) {
                siteRankNumMap.addAndGet(siteRank.getSiteName(), siteRank.getArticleNum());
            }

            //***4.情感指数
            emotionIndexMap.put("emotionIndexTotal", MapUtils.getDoubleValue(emotionIndexMap, "emotionIndexTotal", 0) + dayEmoIndexVal);
        }


        //封装入库 Entity
        AnalysisEntity insertEntity = new AnalysisEntity();

        //*** 1.媒体分布
        List<MediaTypeJSON> mediaTypeInsertList = new ArrayList<>();
        Map<String, Long> mediaTypeActicleMap = acticleNumMap.asMap();
        MediaTypeJSON mtype;
        for (Map.Entry<String, Long> entry : mediaTypeActicleMap.entrySet()) {
            mtype = new MediaTypeJSON();
            mtype.setMediaName(entry.getKey());
            mtype.setArticleNum(entry.getValue());
            mtype.setPositive(positiveNumMap.get(entry.getKey()));
            mtype.setNegative(negativeNumMap.get(entry.getKey()));
            mediaTypeInsertList.add(mtype);
        }
        insertEntity.setMediaType(JSON.toJSONString(mediaTypeInsertList));

        //*** 2.地区
        List<HotRegionJSON> hotRegionInsertList = new ArrayList<>();
        Map<String, Long> iterMap = hotRegionNumMap.asMap();
        HotRegionJSON hotRegion;
        for (Map.Entry<String, Long> entry : iterMap.entrySet()) {
            hotRegion = new HotRegionJSON();
            hotRegion.setAreaCode(entry.getKey());
            if (RegionLoading.REGION_MAP.get(entry.getKey()) != null) {
                hotRegion.setAreaName(RegionLoading.REGION_MAP.get(entry.getKey()).getRegionName());
            }
            hotRegion.setArticleNum(entry.getValue());
            hotRegionInsertList.add(hotRegion);
        }
        insertEntity.setRegion(JSON.toJSONString(hotRegionInsertList));

        //*** 3.网站来源排行
        List<SiteRankJSON> siteRankInsertList = new ArrayList<>();
        iterMap = siteRankNumMap.asMap();
        SiteRankJSON siteRank;
        for (Map.Entry<String, Long> entry : iterMap.entrySet()) {
            siteRank = new SiteRankJSON();
            siteRank.setSiteName(entry.getKey());
            siteRank.setArticleNum(entry.getValue());
            siteRankInsertList.add(siteRank);
        }
        insertEntity.setSiteRate(JSON.toJSONString(siteRankInsertList));
        //*** 4.情感指数(emotionIndexTotal\30天的平均值)
        double emotionIndexTotal = MapUtils.getDoubleValue(emotionIndexMap, "emotionIndexTotal", 0);
        insertEntity.setEmoIndex(new BigDecimal(emotionIndexTotal / dbData.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //***5.基本信息
        //>>> 统计的日期
        Date today = LocalDateUtils.localDateToDate(LocalDate.now());
        insertEntity.setInsertTime(today);

        //>>>数据状态（0.删除，1.正常）
        insertEntity.setDataState(1);
        //>>> 唯一id
        insertEntity.setUuid(UUID.randomUUID().toString().replace("-", ""));
        //>>> 用户id
        insertEntity.setUid(planDto.getCreateUser());
        //>>> 方案id
        insertEntity.setPid(planDto.getId());
        //>>> 方案名称
        insertEntity.setPlanName(planDto.getKpName());
        //>>> 方案名称
        insertEntity.setPlanType(String.valueOf(planDto.getKpType()));
        //>> 简报类型(0:日报,1：周报,2：月报)
        insertEntity.setReportType(reportType);
        //>>> 报告名称
        insertEntity.setReportName(planDto.getKpName() + DateFormatUtils.format(today, "yyyy-MM-dd"));

        insertEntity.setCreateTime(new Date());
        insertEntity.setUpdateTime(new Date());

        analysisDbService.insert(insertEntity);
    }


    /**
     * dubbo汇聚 分组
     *
     * @param planDto
     * @param aggField
     * @return
     */
    private Map<String, Long> doDubboAggGroup(PlanDto planDto, String aggField,
                                              int pageStart, int pageSize, long startTime, long endTime) {

        Words words = JSONObject.parseObject(planDto.getKpMonitorKeys(), Words.class);
        IKnownsDetailParam iKnownsDetailParam = new IKnownsDetailParam();
        iKnownsDetailParam.setWords(words);
        if (planDto.getExcludeWords() != null) {
            iKnownsDetailParam.setExcludes(Arrays.asList(planDto.getExcludeWords().split(",")));
        }
        List<Region> regions = JSONArray.parseArray(planDto.getRegionWords(), Region.class);
        if (CollectionUtils.isNotEmpty(regions)) {
            List<String> list = new ArrayList<>();
            for (Region region : regions) {
                list.add(region.getRegionCode());
            }
            iKnownsDetailParam.setRegions(list);
        }

        //es查询
        iKnownsDetailParam.setStartTime(startTime);
        iKnownsDetailParam.setEndTime(endTime);
        iKnownsDetailParam.setPageStart(pageStart);
        iKnownsDetailParam.setPageSize(pageSize);
        iKnownsDetailParam.setAggQueryBean(new AggQueryBean(aggField, AggQueryBean.AggType.GROUP));
        DubboRequest<IKnownsDetailParam> dubboRequest = new DubboRequest<IKnownsDetailParam>(BusinessIdGenerator
                .generateBussinessId("iknow_task_group[" + aggField + "]"), iKnownsDetailParam);
        DubboResponse<QueryResultBean> dubboResponse = iQueryForIknowsService.iknowsQuery(dubboRequest);

        Map<String, Long> aggMap = new HashMap<>();
        if (dubboResponse.getCode() == DubboResponse.SUCCESS_CODE) {
            QueryResultBean queryResultBean = dubboResponse.getData();
            aggMap = queryResultBean.getAggMap();
        }
        return aggMap;
    }


    /**
     * dubbo汇聚 平均
     *
     * @param planDto
     * @param avgField
     * @param pageStart
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    private double doDubboAggAvg(PlanDto planDto, String avgField,
                                 int pageStart, int pageSize, long startTime, long endTime) {
        //从方案中获取基本信息
        Words words = JSONObject.parseObject(planDto.getKpMonitorKeys(), Words.class);
        IKnownsDetailParam iKnownsDetailParam = new IKnownsDetailParam();
        List<Region> regions = JSONArray.parseArray(planDto.getRegionWords(), Region.class);
        if (CollectionUtils.isNotEmpty(regions)) {
            List<String> list = new ArrayList<>();
            for (Region region : regions) {
                list.add(region.getRegionCode());
            }
            iKnownsDetailParam.setRegions(list);
        }

        //es查询
        iKnownsDetailParam.setStartTime(startTime);
        iKnownsDetailParam.setEndTime(endTime);
        iKnownsDetailParam.setPageStart(pageStart);
        iKnownsDetailParam.setPageSize(pageSize);

        iKnownsDetailParam.setAggQueryBean(new AggQueryBean(AggQueryBean.AggType.SUM, Arrays.asList(avgField)));
        DubboRequest<IKnownsDetailParam> dubboRequest = new DubboRequest<IKnownsDetailParam>(BusinessIdGenerator
                .generateBussinessId("iknow_task_avg[" + avgField + "]"), iKnownsDetailParam);
        DubboResponse<QueryResultBean> dubboResponse = iQueryForIknowsService.iknowsQuery(dubboRequest);
        double avg = 0;
        Map<String, Long> aggMap = null;
        if (dubboResponse.getCode() == DubboResponse.SUCCESS_CODE) {
            QueryResultBean queryResultBean = dubboResponse.getData();
            aggMap = queryResultBean.getAggMap();
            avg = queryResultBean.getTotalHits() == 0 ? 0 :
                    remainTwoDecimal(MapUtils.getDoubleValue(aggMap, avgField, 0L) / queryResultBean.getTotalHits());
        }
        return avg;
    }


    /**
     * 保留两位小数
     *
     * @param d
     * @return
     */
    private double remainTwoDecimal(Double d) {
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
