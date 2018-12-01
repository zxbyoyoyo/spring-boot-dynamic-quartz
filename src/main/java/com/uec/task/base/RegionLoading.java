package com.uec.task.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.uec.common.dto.region.IknowsRegion;
import com.uec.common.dubboapi.dbservice.RegionDbService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegionLoading implements CommandLineRunner {


    /**
     * 舆情分析 dubbo 地域操作接口
     */
    @Reference(version = "${dubbo.service.version}")
    RegionDbService regionDbService;

    public static Map<String, IknowsRegion> REGION_MAP= new HashMap<>();
    public static Map<String, Integer> PROVICE_NAME_MAP= new HashMap<>();
    public static Map<String, String> PROVICE_ID_MAP= new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        List<IknowsRegion> regions = regionDbService.listAll();
        for (IknowsRegion region : regions) {

            if(region.getLevel()==1){
                PROVICE_ID_MAP.put(region.getRegionId()+"",region.getProvince());
                PROVICE_NAME_MAP.put(region.getProvince(),region.getRegionId());
            }
            REGION_MAP.put(region.getRegionId()+"", region);
        }
    }
}