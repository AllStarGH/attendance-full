package com.allstar.spring.entity;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册信息,派生类
 * 
 * @author admin
 *
 */
@Data
public class RegistInfo extends TAccount {
	private static final long serialVersionUID = 1L;

	/**
	 * 邮箱验证码
	 */
	@NotNull(message = "缺少邮箱验证码")
	@ApiModelProperty(value = "邮箱验证码,注册新帐号时由用户提交")
	private String veriCode;

	/**
	 * 再次输入之密码
	 */
	@NotNull(message = "缺少(repeat)密码")
	@ApiModelProperty(value = "重复输入的密码")
	private String rePassword;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegistInfo [veriCode=").append(veriCode).append(", rePassword=").append(rePassword)
				.append(", toString()=").append(super.toString()).append("]");
		return builder.toString();
	}

}