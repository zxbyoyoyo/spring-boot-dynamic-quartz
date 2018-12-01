package com.uec.common.dto.analysis.json;


import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 媒体类型对照表
 */
public enum MediaTypeEnum {

    WEIXIN("8", "微信", "weixin"),
    WEIBO("7", "微博", "weibo"),
    WEBSITE("6", "网页", "website"),
    APP("9", "APP", "app"),
    PAPER("9", "数字报", "paper");


    private String code;
    private String name;
    private String field;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }

    MediaTypeEnum(String code, String name, String field) {
        this.code = code;
        this.name = name;
        this.field = field;
    }

    public static Map<String, Long> mergeAggMap(Map<String, Long> aggMap) {
        Map<String, Long> mergedMap = new HashMap<>();
        for (MediaTypeEnum typeEnum : MediaTypeEnum.values()) {
            mergedMap.put(typeEnum.field, MapUtils.getLongValue(aggMap, typeEnum.code, 0L));
        }
        return mergedMap;
    }


}
