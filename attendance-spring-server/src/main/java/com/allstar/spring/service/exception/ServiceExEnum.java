package com.allstar.spring.service.exception;

/**
 * 业务异常枚举
 * 
 * @author admin
 *
 */
public enum ServiceExEnum {
	/** 您不是管理员,无法进行此项操作 */
	ROLE_IS_NOT_ADMIN(427, "您不是管理员,无法进行此项操作"),
	/** 无网络连接,请检查您的互联网状态 */
	NETWORK_NO_CONNECTION(426, "无网络连接,请检查您的互联网状态"),
	/** 验证码已过期,请重新点击发送验证码 */
	CAPTCHA_HAS_EXPIRED(425, "验证码已过期,请重新点击发送验证码"),
	/** 原密码错误 */
	OLD_PASSWORD_ERROR(424, "原密码错误"),
	/** 自定义文件非法状态异常 */
	FILE_ILLEGAL_STATE_EXCEPTION(423, "自定义文件非法状态异常"),
	/** 文件自定义输入输出异常 */
	FILE_IO_EXCEPTION(422, "文件自定义输入输出异常"),
	/** 文件类型不符合,请重新改正 */
	FILE_CONTENT_TYPE_EXCEPTION(421, "文件类型不符合,请重新改正"),
	/** 文件过大,请重新选择合适的文件 */
	FILE_SIZE_EXCEPTION(420, "文件过大,请重新选择合适的文件"),
	/** 文件为空,请重新检查 */
	FILE_EMPTY_EXCEPTION(419, "文件为空,请重新检查"),
	/** 邮箱验证码为空 */
	CAPTCHA_IS_NULL(418, "邮箱验证码为空"),
	/** 邮箱验证码不正确 */
	CAPTCHA_NOT_CORRECTLY(417, "邮箱验证码不正确"),
	/** 此邮箱地址不存在 */
	NO_SUCH_MAILBOX_ADDRESS(416, "此邮箱地址不存在"),
	/** 验证码未过期,请等有效期过后再重新发送验证码 */
	CAPTCHA_HAD_EXISTS(415, "验证码未过期,请等有效期过后再重新发送验证码"),
	/** 不支持此款邮箱,目前支持绑定的邮箱有:126/163/aliyun/21cn/sina/qq */
	MAILBOX_UNSUPPORTED(414, "不支持此款邮箱,目前支持绑定的邮箱有:126/163/aliyun/21cn/sina/qq"),
	/** 系统内无此手机号码 */
	PHONE_NOT_FOUND(413, "系统内无此手机号码"),
	/** 员工工号冲突,此工号已经存在于系统中 */
	EMPLOYEE_NUM_DUPLICATE_CONFLICT(406, "员工工号冲突,此工号已经存在于系统中"),
	/** 系统故障,工作人员正在紧急抢修 */
	SYSTEM_FATAL_BUG(412, "系统故障,技术人员正在紧急抢修"),
	/** 该电话号码已被其他用户使用 */
	PHONE_HAS_EXISTED(411, "该电话号码已被其他用户使用"),
	/** 该邮箱地址已被其他用户注册 */
	MAILBOX_HAS_EXISTED(410, "该邮箱地址已被其他用户使用"),
	/** 您已下线,请重新登录 */
	OFFLINE_LOGIN(409, "您已下线,请重新登录"),
	/** "密码错误,请检查密码无误后再登录" */
	KEYWORD_ERR(408, "密码错误,请检查密码无误后再登录"),
	/** "未寻获此工号,无此用户" */
	EMP_NUM_NOT_FOUND(407, "未寻获此工号,无此用户"),;

	private Integer code;
	private String description;

	private ServiceExEnum() {
	}

	private ServiceExEnum(Integer code, String description) {
		this.code = code;
		this.description = description;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 根据code获取description
	 * 
	 * @param code
	 * @return
	 */
	public String getDescByCode(Integer code) {
		for (ServiceExEnum element : ServiceExEnum.values()) {
			if (code == element.code) {
				return element.description;
			}
		}

		return null;
	}

	/**
	 * 根据description获取code
	 * 
	 * @param description
	 * @return
	 */
	public Integer getCodeByDesc(String description) {
		for (ServiceExEnum element : ServiceExEnum.values()) {
			if (description.equals(element.description)) {
				return element.code;
			}
		}

		return null;
	}
}