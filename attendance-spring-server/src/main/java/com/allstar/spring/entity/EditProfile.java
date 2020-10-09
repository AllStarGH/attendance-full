package com.allstar.spring.entity;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 待改资料
 * 
 * @author admin
 *
 */
@Data
public class EditProfile {
	@NotNull(message = "缺少邮箱")
	@ApiModelProperty(value = "待改资料之邮箱")
	private String mailbox;

	@NotNull(message = "缺少出生日期")
	@ApiModelProperty(value = "待改资料之出生日期")
	private String bornTime;

	@NotNull(message = "缺少手机号")
	@ApiModelProperty(value = "待改资料之手机号")
	private String phone;

	@NotNull(message = "缺少名字")
	@ApiModelProperty(value = "待改资料之名字")
	private String employeeName;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EditProfile [mailbox=").append(mailbox).append(", bornTime=").append(bornTime).append(", phone=")
				.append(phone).append(", employeeName=").append(employeeName).append("]");
		return builder.toString();
	}

}