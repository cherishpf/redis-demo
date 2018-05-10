package com.example.redisdemo.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * @author yangpengfei
 * @date 2018年5月8日
 */
@Data
public class User implements Serializable {
	private Integer userId;
	private Integer userAccountId;
	private String 	userName;
	private String 	userLoginPassword;
	private Integer userType;
	private String 	userMobileNumber;
	private String 	userEmail;
	private String 	userIdentificationCard;
	private LocalDateTime userCreateTime;
	private String 	userIdentificationName;
	private String 	userOrganization;
	private String userArea;
	private String checkCode;

}
