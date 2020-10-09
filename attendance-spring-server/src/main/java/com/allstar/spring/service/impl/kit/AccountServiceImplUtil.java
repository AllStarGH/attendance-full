package com.allstar.spring.service.impl.kit;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import com.allstar.spring.entity.TAccount;
import com.allstar.spring.service.exception.ServiceExEnum;
import com.allstar.spring.service.exception.ServiceException;
import com.allstar.spring.util.DateTimeUtils;
import com.allstar.spring.util.SMTPServersEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 账户业务实现工具类
 * 
 * @author admin
 *
 */
@Slf4j
public class AccountServiceImplUtil {
	DateTimeUtils dateTimeUtils = DateTimeUtils.getInstance();

	SMTPServersEnum instEnum = SMTPServersEnum.INSTANCE;

	/**
	 * 设静态实例为私有,防止被引用,并初始化为null
	 */
	private static AccountServiceImplUtil instance;

	private HashMap<String, String> map = new HashMap<String, String>();
	private HashMap<String, String> map1 = new HashMap<String, String>();

	/**
	 * 盐值长度限制
	 */
	private static int salt_limit_len = 16;

	/**
	 * 密文长度限制
	 */
	private static int keyword_text_limit_len = 48;

	/**
	 * 占位符
	 */
	private static final AccountServiceImplUtil PLACEHOLDER = new AccountServiceImplUtil();

	/**
	 * 私有构造方法,防止被引用
	 */
	private AccountServiceImplUtil() {
	}

	private AccountServiceImplUtil(String arg) {
		instance = this;
	}

	/**
	 * 创建实例
	 * 
	 * @return
	 */
	public static AccountServiceImplUtil getInstance() {
		if (instance == null) {
			return PLACEHOLDER;
		}

		return instance;
	}

	/**
	 * 实际逻辑操作的时候,可能占位符/默认值并不能执行,所以要在所有的对外方法中添加实例检查
	 * 
	 * @return
	 */
	public boolean isVaildInstance() {
		if (this != PLACEHOLDER) {
			log.info("占位符/默认值不能执行");
			return false;
		}

		log.info("占位符/默认值能够执行");
		return true;
	}

	/**
	 * 检查文件合法之类型
	 * 
	 * @param suff
	 * @return
	 */
	public Boolean checkFileType(String suff) {
		ArrayList<String> typeList = new ArrayList<String>();
		
		typeList.add("image/jpeg");
		typeList.add("image/jpg");
		typeList.add("image/png");
		typeList.add("image/gif");
		typeList.add("image/bmp");

		if (!typeList.contains(suff)) {
			log.error("上传文件类型有误");
			return false;
		}

		return true;
	}

	/**
	 * 将注册时填入之验证码与redis中的验证码进行对比,返还开关值
	 * 
	 * @param regInputCode 注册时所输入之验证码
	 * @param value
	 * @return
	 * @throws ServiceException
	 * @throws UnsupportedEncodingException
	 */
	public boolean veryifyRedisCode(String regInputCode, String value)
			throws ServiceException, UnsupportedEncodingException {
		log.info("regInputCode===" + regInputCode + ", redis value===" + value);

		/*
		 * 通过ResourceBundle读取资源文件时资源文件的编码和程序文件的编码不一样，结果出现了不可见字符
		 */
		regInputCode = new String(regInputCode.getBytes("ISO-8859-1"), "UTF-8");
		value = new String(value.getBytes("ISO-8859-1"), "UTF-8");

		if (regInputCode == null || "".equals(regInputCode)) {// 验证码为空异常
			String description = ServiceExEnum.CAPTCHA_IS_NULL.getDescription();
			log.error(description);
			throw new ServiceException(description);
		} else if (value == null || "".equals(value)) {// 验证码尚未过期异常
			String description = ServiceExEnum.CAPTCHA_HAD_EXISTS.getDescription();
			log.error(description);
			throw new ServiceException(description);
		} else if (value != regInputCode && !regInputCode.equals(value)) {// 验证码不匹配异常
			String description = ServiceExEnum.CAPTCHA_NOT_CORRECTLY.getDescription();
			log.error(description);
			throw new ServiceException(description);
		} else {
			log.info("验证码校验已通过,可放行");
			return true;
		}
	}

	/**
	 * 算差值,<em>并与设定期限相对比,返回开关值</em>
	 * 
	 * @param firstNow
	 * @param lastMilli
	 * @param expire
	 * @return
	 */
	public Boolean countDifferent(Long firstNow, Long lastMilli, int expire) {
		Boolean judge = null;
		Long diffent = lastMilli - firstNow;
		expire *= 60L * 1000L;

		log.info("LASTMILLI - FIRSTNOW 相差值===" + diffent);
		if (diffent > expire) {
			log.info("随机码已过期限");
			judge = true;
		} else {
			log.info("随机码尚未过期限");
			judge = false;
		}
		log.info("judge===" + judge);
		return judge;
	}

	/**
	 * 
	 * @param userMailbox 目标用户的邮箱
	 * @param randomCode  随机生成之码
	 * @return
	 */
	public String sendToUserMailbox(String userMailbox, Integer randomCode) {
		MultiPartEmail email = new MultiPartEmail();
		String developMailbox = "huxmen@21cn.com";
		Integer expires = 10;// 分钟
		String send = null;

		email.setCharset("utf-8");
		email.setHostName(instEnum.getServerAddressByName(cutsOutMailBox(developMailbox)));// STMP服务器
		email.setAuthentication(developMailbox, "798271hwpkHY");// authentication身份验证
		try {
			email.addTo(userMailbox);
			email.setFrom(developMailbox);
			email.setSubject("send captcha code to you");
			email.setMsg("注册验证码为: " + randomCode + ", " + expires + "分钟内有效");
			send = email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			String desc = ServiceExEnum.NO_SUCH_MAILBOX_ADDRESS.getDescription();
			log.error(desc);
			throw new ServiceException(desc);
		}
		log.info("the message id of the underlying MimeMessage:" + send);
		return randomCode + "";
	}

	/**
	 * 按 `=` 分割字符串,压入哈希表
	 * 
	 * @param str
	 * @return
	 */
	public HashMap<String, String> splitStringPutsHashMap(String str) {
		String[] strs = str.split("=");

		if (strs.length < 2) {
			map.put(strs[0], null);
			return map;
		}

		map.put(strs[0], strs[1]);
		return map;
	}

	/**
	 * 按 `&` 分割字符串,生成哈希表
	 * 
	 * @param string
	 * @return
	 */
	public HashMap<String, String> generateMap(String string) {
		String[] sp = string.split("&");

		int n = 0;
		while (n < sp.length) {
			map1 = splitStringPutsHashMap(sp[n]);
			n++;
		}

		for (Map.Entry<String, String> entry : map1.entrySet()) {
			log.info(entry.getKey() + ":" + entry.getValue());
		}

		return map1;
	}

	/**
	 * 生成注册表单之所需标准Bean
	 * 
	 * @param hashMap
	 * @return
	 */
	public TAccount generateAccount(HashMap<String, String> hashMap) {
		TAccount account = new TAccount();

		account.setBornTime(dateTimeUtils.StrTransformLocalDateTime(hashMap.get("bornTime")));
		account.setDepartment(Integer.valueOf(hashMap.get("department")));
		account.setEmployeeName(hashMap.get("employeeName"));
		account.setPassword(hashMap.get("password"));
		account.setRole(Integer.valueOf(hashMap.get("role")));
		account.setMailbox(hashMap.get("mailbox"));
		account.setPhone(hashMap.get("phone"));
		account.setRegion(Integer.valueOf(hashMap.get("region")));
		log.info(account.toString());

		return account;
	}

	/**
	 * 解码,矫正中文乱码
	 * 
	 * @param jsonStr
	 * @return
	 */
	public String decodeJsonStr(String jsonStr) {
		try {
			jsonStr = URLDecoder.decode(jsonStr, "UTF-8");
			log.info("json string: " + jsonStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	/* ************************************************************ */

	/**
	 * 根据主域名获取SMTP服务器地址
	 * 
	 * @param address
	 * @return
	 */
	public String getServerAddressByName(String address) {
		String domain = cutsOutMailBox(address);

		// 循环比对
		String serverAddressByName = instEnum.getServerAddressByName(domain);
		log.info("serverAddressByName:" + serverAddressByName);

		return serverAddressByName;
	}

	/**
	 * 截取邮箱主域名
	 * 
	 * @param postbox 例如:951753@aliyun.com
	 * @return
	 */
	public String cutsOutMailBox(String postbox) {
		StringBuilder builder = new StringBuilder(postbox);
		String domain = null;

		if (builder.toString().length() > 4) {
			int at = builder.toString().indexOf("@");
			int point = builder.toString().lastIndexOf(".");

			domain = builder.toString().substring(at + 1, point);
			log.info("domain name:" + domain);
		}
		return domain;
	}

	/**
	 * 提取盐
	 *
	 * @return
	 */
	public String extractSalt() {
		Random random = new Random();

		StringBuilder builder = new StringBuilder(salt_limit_len);

		builder.append(random.nextInt(99999999));

		int length = builder.length();

		if (length < salt_limit_len) {
			for (int i = 0; i < salt_limit_len - length; i++) {
				int n = random.nextInt(9);
				builder.append(n + "");
			}
		}

		return builder.toString();
	}

	/**
	 * 获取十六进制字符串形式的MD5摘要(digest)
	 *
	 * @param src
	 * @return
	 */
	private String getMd5Hex(String src) {
		MessageDigest md5 = null;

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		byte[] bs = md5.digest(src.getBytes());

		byte[] encode = new Hex().encode(bs);

		String digest = new String(encode);

		return digest;
	}

	/**
	 * MD5+盐,输入密码,生成并返回密文
	 *
	 * @param pwd
	 * @return
	 */
	public String generateText(String pwd, String salt) {
		// 撒盐,并在MD5hex方法内均匀搅拌
		String hex = getMd5Hex(salt + pwd);
		System.out.println("hex:" + hex);

		char[] cs = new char[keyword_text_limit_len];

		// 再加密
		for (int i = 0; i < keyword_text_limit_len; i += 3) {
			cs[i] = hex.charAt(i / 3 * 2);
			cs[i + 1] = salt.charAt(i / 3);
			cs[i + 2] = hex.charAt(i / 3 * 2 + 1);
		}

		return new String(cs);
	}

	/**
	 * 校验加盐后是否和原文一致,逆向解密
	 *
	 * @param password 提交之密码
	 * @param text     原文
	 * @return
	 */
	public boolean verify(String password, String text) {
		char[] digestStr = new char[keyword_text_limit_len - salt_limit_len];
		char[] saltStr = new char[salt_limit_len];

		for (int i = 0; i < keyword_text_limit_len; i += 3) {
			digestStr[i / 3 * 2] = text.charAt(i);
			digestStr[i / 3 * 2 + 1] = text.charAt(i + 2);

			saltStr[i / 3] = text.charAt(i + 1);
		}

		String salt = new String(saltStr);

		boolean equals = getMd5Hex(salt + password).equals(new String(digestStr));
		log.info("equals===" + equals);

		return getMd5Hex(salt + password).equals(new String(digestStr));
	}

	/**
	 * 整合生成随机数
	 * 
	 * @return
	 */
	public Long integrationGeneration() {
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();

		int range = 15;

		long i = getRanNumber0();
		long j = getRanNumber1();
		long k = getRanNumber2();

		buffer.append(i);
		buffer.append(j);
		buffer.append(k);

		String str = buffer.toString();

		int length = str.length();
		System.err.println(length);

		if (length > range) {
			str = str.substring(0, range - (random.nextInt(range - 1)));
		}

		Long val = Long.valueOf(str);
		log.info("生成之工号: " + val);

		return val;
	}

	/**
	 * 审验已存在的工号
	 * 
	 * @param existNum 已存在的员工工号
	 * @return
	 */
	public Long inspect(Long existNum) {
		Long result;

		do {
			result = integrationGeneration();
		} while (result == existNum);
		log.info("result:" + result);

		return result;
	}

	/**
	 * 随机获取一个随机数-0
	 * 
	 * @return
	 */
	public long getRanNumber0() {
		Random random = new Random();

		long ran = random.nextInt(1000000000);
		log.info("ran==" + ran);

		return ran;
	}

	/**
	 * 随机获取一个随机数-1
	 * 
	 * @return
	 */
	public long getRanNumber1() {
		Random random = new Random();

		long max = random.nextInt(100);
		long min = random.nextInt(100);

		long ran = (long) (Math.random() * (max + min) + min);

		return ran;
	}

	/**
	 * 随机获取一个随机数-2
	 * 
	 * @return
	 */
	public long getRanNumber2() {
		Random random = new Random();

		long max = random.nextInt(100);
		long min = random.nextInt(100);

		long randomNum = System.currentTimeMillis();

		long ran3 = (long) (randomNum * (max + min) + min);

		return ran3;
	}
}