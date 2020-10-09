package com.allstar.spring.util;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * SMTP服务器枚举
 * </pre>
 * 
 * @author admin
 *
 */
@Slf4j
public enum SMTPServersEnum {
	/** smtp.126.com */
	ONE_TWO_SIX("126", "smtp.126.com"),
	/** smtp.163.com */
	ONE_SIX_THREE("163", "smtp.163.com"),
	/** smtp.mail.aliyun.com */
	ALIYUN("aliyun", "smtp.mail.aliyun.com"),
	/** smtp.21cn.com */
	TWENTY_ONE("21cn", "smtp.21cn.com"),
	/** smtp.sina.com */
	SINA("sina", "smtp.sina.com"),
	/** smtp.qq.com */
	QQ("qq", "smtp.qq.com"),
	/**  */
	INSTANCE;

	private String name;
	private String serverAddress;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getServerAddressByName(String name) {
		log.info("param name==" + name);
		for (SMTPServersEnum smtp : SMTPServersEnum.values()) {
			if (!"".equals(smtp.getName()) && smtp.getName() != null && smtp.getName().equals(name)) {
				return smtp.serverAddress;
			}
		}
		return null;
	}

	private SMTPServersEnum() {
	}

	private SMTPServersEnum(String name, String serverAddress) {
		this.name = name;
		this.serverAddress = serverAddress;
	}
}