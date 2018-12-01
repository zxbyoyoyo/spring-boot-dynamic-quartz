package com.uec.common.dto.region;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 地域实体
 * @author: Ming.Lee/李明
 * @create: 2018-10-08 15:56
 **/
@Data
public class IknowsRegion implements Serializable {

    private int 	regionId;			//地区编码，身份证前6位
    private String 	regionName;			//地区名称
    private String 	province;			//省份
    private String 	provinceShort;		//省份简称
    private String 	city;				//市
    private String 	cityShort;			//市-简称
    private String 	allName;			//地区全称
    private String 	country;			//国家
    private int 	labelTypeId;		//标签类型ID
    private String 	labelTypeName;		//标签类型名
    private int 	cityCode;			//市编码
    private int     provinceCode;		//省编码
    private int 	level;				//级别1省、2市3县
    private int 	rankNum;			//排序号
    private Integer status;				//0/null不展示1展示

}