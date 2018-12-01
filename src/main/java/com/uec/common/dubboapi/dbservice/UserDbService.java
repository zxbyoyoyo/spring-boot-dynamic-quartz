package com.uec.common.dubboapi.dbservice;

import com.uec.common.dto.user.User;

import java.util.List;

public interface UserDbService{
	User findByUserName(String userName);

	User findById(String id);

	boolean update(User user);

	boolean save(User user);

	List<User> listAll();
}
