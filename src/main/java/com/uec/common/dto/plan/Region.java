package com.uec.common.dto.plan;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 地域实体
 * @author: Ming.Lee/李明
 * @create: 2018-09-20 18:08
 **/
@Data
public class Region implements Serializable {

    private String  regionCode;

    private String  regionName;


}