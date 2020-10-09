package com.allstar.spring.entity;

import lombok.Data;

/**
 * 用户信息类(包含用户的ID，账号和密码和<b>生成的token字符串</b>信息)
 * 
 * @author admin
 *
 */
@Data
public class LoginInfo extends TAccount {
	private static final long serialVersionUID = 1L;

	private String tokenStr;

}