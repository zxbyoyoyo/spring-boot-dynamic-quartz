package com.uec.common.dubboapi.dbservice;

import com.uec.common.dto.region.IknowsRegion;

import java.util.List;

public interface RegionDbService {
	List<IknowsRegion> listAll();
}
