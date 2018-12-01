package com.uec.common.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String 		id;					//主键, 32位UUID
	private String 		userName;			//用户名
	private String 		password;			//密码
	private String 		realName;			//姓名
	private String 		mobile;				//联系电话
	private String 		email;				//邮箱
	private String 		region;				//地域code
	private String 		wechat;				//微信号
	private Integer 	state;				//用户状态(0:停用,1:正常,2:试用)
	private Date 		registerTime;		//注册时间
	private Date 		expirationTime;		//到期时间
	private String 		salt;				//盐
	private Integer 	dataState;			//数据状态(0:已删除,1:正常)
	private Date 		createTime;			//创建时间
	private String 		createUser;			//创建人id
	private Date 		updateTime;			//修改时间
	private String 		updateUser;			//修改人id
}
