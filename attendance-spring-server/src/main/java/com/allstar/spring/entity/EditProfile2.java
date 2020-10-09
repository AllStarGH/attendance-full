package com.allstar.spring.entity;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 管理员修改之资料
 * 
 * @author admin
 *
 */
@Data
public class EditProfile2 extends EditProfile {
	@NotNull(message = "缺少工号")
	@ApiModelProperty(value = "待改资料之员工工号")
	private Integer employeeNum;

	@NotNull(message = "缺少部门")
	@ApiModelProperty(value = "待改资料之部门")
	private Integer department;

	@NotNull(message = "缺少权限角色")
	@ApiModelProperty(value = "待改资料之权限角色")
	private Integer role;

	@NotNull(message = "缺少地区")
	@ApiModelProperty(value = "待改资料之地区")
	private Integer region;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EditProfile2 [employeeNum=").append(employeeNum).append(", department=").append(department)
				.append(", role=").append(role).append(", region=").append(region).append(", toString()=")
				.append(super.toString()).append("]");
		return builder.toString();
	}

}