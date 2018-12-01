package com.dao;

import com.model.MonitorAnalysis;
import com.model.MonitorAnalysisWithBLOBs;

public interface MonitorAnalysisMapper {
    int deleteByPrimaryKey(String uuid);

    int insert(MonitorAnalysisWithBLOBs record);

    int insertSelective(MonitorAnalysisWithBLOBs record);

    MonitorAnalysisWithBLOBs selectByPrimaryKey(String uuid);

    int updateByPrimaryKeySelective(MonitorAnalysisWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(MonitorAnalysisWithBLOBs record);

    int updateByPrimaryKey(MonitorAnalysis record);
}