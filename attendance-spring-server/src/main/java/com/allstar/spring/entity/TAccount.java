package com.allstar.spring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * <p>
 * 账号表
 * </p>
 *
 * @author gzh
 * @since 2019-12-07
 */
@Data
@TableName("t_account")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TAccount extends Model<TAccount> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "帐号ID")
	private Integer id;

	/**
	 * 雇员的姓名
	 */
	@NotNull(message = "缺少员工姓名")
	@ApiModelProperty(value = "名字")
	private String employeeName;

	/**
	 * 出生日期(年月日)
	 */
	@NotNull(message = "缺少出生日期")
	@ApiModelProperty(value = "出生日期")
	private LocalDateTime bornTime;

	/**
	 * 账号注册时间
	 */
	@ApiModelProperty(value = "注册时间")
	private LocalDateTime regTime;

	/**
	 * 手机号码
	 */
	@NotNull(message = "缺少手机号码")
	@ApiModelProperty(value = "手机号码")
	private String phone;

	/**
	 * 邮箱地址
	 */
	@NotNull(message = "缺少邮箱地址")
	@ApiModelProperty(value = "邮箱")
	private String mailbox;

	/**
	 * 头像(地址)
	 */
	@ApiModelProperty(value = "头像")
	private String portrait;

	/**
	 * 员工所在地区,0-安淮,1-南徽,2-古丰,3-方千,4-播星,5-角港,6-米园,7-湖地,8-寿韫,
	 * 9-天玺台,10-苏崧,11-南威,12-都张集,13-企塘,14-中缇
	 */
	@ApiModelProperty(value = "地区")
	@NotNull(message = "缺少所在地区")
	private Integer region;

	/**
	 * 所属部门,0-研发,1-财务,2-人事,3-销售,4-公关,5-法务,6-安全
	 */
	@NotNull(message = "缺少所属部门")
	@ApiModelProperty(value = "部门")
	private Integer department;

	/**
	 * 系统内角色:0-管理员,1-系统普通用户
	 */
	@NotNull(message = "缺少权限角色")
	@ApiModelProperty(value = "角色")
	private Integer role;

	/**
	 * 员工工号
	 */
	@ApiModelProperty(value = "工号")
	private Integer employeeNum;

	/**
	 * 密码密文
	 */
	@NotNull(message = "缺少密码")
	@ApiModelProperty(value = "密码")
	private String password;

	/**
	 * 盐密钥
	 */
	@ApiModelProperty(value = "盐值")
	private String salt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public LocalDateTime getBornTime() {
		return bornTime;
	}

	public void setBornTime(LocalDateTime bornTime) {
		this.bornTime = bornTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Integer getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(Integer employeeNum) {
		this.employeeNum = employeeNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TAccount{" + "id=" + id + ", employeeName=" + employeeName + ", bornTime=" + bornTime + ", phone="
				+ phone + ", mailbox=" + mailbox + ", portrait=" + portrait + ", region=" + region + ", department="
				+ department + ", role=" + role + ", employeeNum=" + employeeNum + ", password=" + password + ", salt="
				+ salt + "}";
	}
}
