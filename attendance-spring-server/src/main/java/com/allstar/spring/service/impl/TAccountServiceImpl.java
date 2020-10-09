package com.allstar.spring.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.allstar.spring.controller.kit.TAccountControllerUtil;
import com.allstar.spring.entity.EditProfile2;
import com.allstar.spring.entity.LoginInfo;
import com.allstar.spring.entity.TAccount;
import com.allstar.spring.mapper.TAccountMapper;
import com.allstar.spring.service.ITAccountService;
import com.allstar.spring.service.exception.ServiceExEnum;
import com.allstar.spring.service.exception.ServiceException;
import com.allstar.spring.service.impl.kit.AccountServiceImplUtil;
import com.allstar.spring.util.DateTimeUtils;
import com.allstar.spring.util.JavaWebTokenUtil;
import com.allstar.spring.util.SMTPServersEnum;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 账号表 服务实现类
 * </pre>
 *
 * @author gzh
 * @since 2019-12-07
 */
@Slf4j
@Service
public class TAccountServiceImpl extends ServiceImpl<TAccountMapper, TAccount> implements ITAccountService {
	TAccountControllerUtil controllerUtil = TAccountControllerUtil.getInstance();

	/**
	 * 上传文件夹之名称
	 */
	private static final String UPLOAD_DIR = "uploadavatar";

	/**
	 * 上传文件之最大尺寸
	 */
	private static final Long UPLOAD_MAX_SIZE = (1L * 1024 * 1024) / 10;

	@Autowired
	private RedisTemplate redisTemplate;

	AccountServiceImplUtil inst = AccountServiceImplUtil.getInstance();

	SMTPServersEnum smtpInst = SMTPServersEnum.INSTANCE;

	DateTimeUtils dateUtil = DateTimeUtils.getInstance();

	@Override
	public Integer saveAccount(TAccount account) throws ServiceException {
		log.info("account=>" + account.toString());
		Wrapper<TAccount> queryWrapper = null;
		StringBuilder builder = new StringBuilder();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Integer affect = null;

		String format = sdf.format(date);
		log.info("format date=>" + format);

		// 查询最大数值ID
		Integer maxid = getMaxIDFromTAccount();
		log.info("tbl_account select maxid=\n" + maxid);

		StringBuilder empNumStr = builder.append(format + ++maxid);
		Integer empNum = Integer.valueOf(empNumStr.toString());// 生成员工工号:年月日+ 1+已存最大ID
		log.info("this account empNum===" + empNum);

		// 邮箱唯一
		TAccount emp1 = foundByMailbox(account.getMailbox());
		if (emp1 != null) {
			log.error(ServiceExEnum.MAILBOX_HAS_EXISTED.getDescription());
			throw new ServiceException(ServiceExEnum.MAILBOX_HAS_EXISTED.getDescription());
		}

		// 电话号码唯一
		TAccount emp2 = foundByPhone(account.getPhone());
		if (emp2 != null) {
			log.error(ServiceExEnum.PHONE_HAS_EXISTED.getDescription());
			throw new ServiceException(ServiceExEnum.PHONE_HAS_EXISTED.getDescription());
		}

		// 获取盐资
		String salt = inst.extractSalt();

		// 加工密码
		String text = inst.generateText(account.getPassword(), salt);

		account.setRegTime(LocalDateTime.now());
		account.setPassword(text);
		account.setSalt(salt);
		account.setEmployeeNum(empNum);

		if (account.getRole() == null) {
			account.setRole(1);
		}

		// 如无设置头像,设置默认头像
		String defaultAvatar = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2507930259,267672761&fm=26&gp=0.jpg";
		if (account.getPortrait() == null) {
			account.setPortrait(defaultAvatar);
		}

		try {
			affect = baseMapper.insert(account);
		} catch (Exception e) {
			e.printStackTrace();
			String description = ServiceExEnum.EMPLOYEE_NUM_DUPLICATE_CONFLICT.getDescription();
			log.info(description);
			throw new ServiceException(description);
		}

		return affect;
	}

	/**
	 * 
	 * @param empNum
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public TAccount findOneByEmpNum(Integer empNum) throws ServiceException {
		QueryWrapper<TAccount> queryWrapper = new QueryWrapper<>();

		queryWrapper.eq("employee_num", empNum);

		return baseMapper.selectOne(queryWrapper);
	}

	@Override
	public TAccount foundByPhone(String phone) throws ServiceException {
		QueryWrapper<TAccount> qw = new QueryWrapper<TAccount>();

		qw.eq("phone", phone);

		return baseMapper.selectOne(qw);
	}

	@Override
	public TAccount foundByMailbox(String mailbox) throws ServiceException {
		QueryWrapper<TAccount> qw = new QueryWrapper<TAccount>();

		qw.eq("mailbox", mailbox);

		return baseMapper.selectOne(qw);
	}

	@Deprecated
	@Override
	public TAccount login(String pwd, Integer empNo, HttpSession session) throws ServiceException {
		QueryWrapper<TAccount> qw = new QueryWrapper<TAccount>();

		String name = this.getClass().getName();
		log.info(name, "login", null);

		// 验证用户是否存在 by employee_num
		qw.eq("employee_num", empNo);
		TAccount acc = baseMapper.selectOne(qw);

		if (acc == null) {
			log.error(ServiceExEnum.EMP_NUM_NOT_FOUND.getDescription());
			throw new ServiceException(ServiceExEnum.EMP_NUM_NOT_FOUND.getDescription());
		}

		// 验证密码是否正确
		boolean verify = inst.verify(pwd, acc.getPassword());
		if (verify == false) {
			log.error(ServiceExEnum.KEYWORD_ERR.getDescription());
			throw new ServiceException(ServiceExEnum.KEYWORD_ERR.getDescription());
		}

		session.setAttribute("accid", acc.getId());
		session.setAttribute("empno", acc.getEmployeeNum());
		session.setAttribute("phone", acc.getPhone());

		acc.setPassword(null);
		acc.setSalt(null);

		return acc;
	}

	@Override
	@Deprecated
	public TAccount saveOnePre(String jsonStr) throws ServiceException, UnsupportedEncodingException {
		String str = inst.decodeJsonStr(jsonStr);
		HashMap<String, String> hashMap = inst.generateMap(str);

		String captchaKey = "RegisterCaptcha_" + hashMap.get("mailbox");
		String value = redisTemplate.opsForValue().get(captchaKey).toString();
		log.info("captchaKey===" + captchaKey + ", captchaKey value===" + value);

		boolean b = inst.veryifyRedisCode(hashMap.get("veriCode"), value);
		log.info("veryifyRedisCode之结果===" + b);

		TAccount account = inst.generateAccount(hashMap);

		return account;
	}

	@Deprecated
	@Override
	public TAccount loginBaseOnPhone(String pwd, String phone, HttpSession session) throws ServiceException {
		QueryWrapper<TAccount> qw = new QueryWrapper<TAccount>();

		// 验证用户是否存在 by phone
		qw.eq("phone", phone);
		TAccount acc = baseMapper.selectOne(qw);

		if (acc == null) {
			log.error(ServiceExEnum.PHONE_NOT_FOUND.getDescription());
			throw new ServiceException(ServiceExEnum.PHONE_NOT_FOUND.getDescription());
		}

		// 验证密码是否正确
		boolean verify = inst.verify(pwd, acc.getPassword());
		if (verify == false) {
			log.error(ServiceExEnum.KEYWORD_ERR.getDescription());
			throw new ServiceException(ServiceExEnum.KEYWORD_ERR.getDescription());
		}

		session.setAttribute("accid", acc.getId());
		log.info("ACCID== " + acc.getId());
		session.setAttribute("empno", acc.getEmployeeNum());
		session.setAttribute("phone", acc.getPhone());

		acc.setPassword(null);
		acc.setSalt(null);

		return acc;
	}

	@Override
	public String sendCaptchaCode(String userMailbox) throws ServiceException, EmailException {
		Integer expires = 10;// 分钟
		Integer days = 12;
		Long updateNow = null;

		// TODO 验证码之键 RegisterCaptcha_ + userMailbox
		String captchaKey = "RegisterCaptcha_" + userMailbox;
		String localTimeKey = "localTimeKey_" + userMailbox;
		String firstTimeKey = "firstTimeKey_" + userMailbox;// 首次生成随机验证码存在键
		String send = null;

		String address = inst.getServerAddressByName(userMailbox);
		if (address == null || "".equals(address)) {
			log.error(ServiceExEnum.MAILBOX_UNSUPPORTED.getDescription());
			throw new ServiceException(ServiceExEnum.MAILBOX_UNSUPPORTED.getDescription());
		}

		Integer randomCode = (int) (inst.getRanNumber0() / 10000);
		if (randomCode < 10) {
			randomCode *= 1000;
		} else if (randomCode < 100) {
			randomCode *= 100;
		} else if (randomCode < 1000) {
			randomCode *= 10;
		}
		log.info("生成之验证码:" + randomCode);

		redisTemplate.opsForValue().set(captchaKey, randomCode, expires, TimeUnit.MINUTES);

		Boolean exists = redisTemplate.hasKey(captchaKey);
		Boolean exists1 = redisTemplate.hasKey(firstTimeKey);

		if (exists) {
			log.info("存入redis成功,有限时间" + expires + "分钟");
			if (!exists1) {
				// 若为首次,则记下
				log.info("首次generates captcha,则记下存入redis");
				redisTemplate.opsForValue().set(firstTimeKey, LocalDateTime.now(), days, TimeUnit.DAYS);
			}
		} else {
			log.error(captchaKey + "存入redis失败,exists===" + exists);
			throw new ServiceException(ServiceExEnum.SYSTEM_FATAL_BUG.getDescription());
		}

		Long firstNow = Long.parseLong(redisTemplate.opsForValue().get(firstTimeKey).toString());
		log.info("firstNow===" + firstNow);

		// 得到迄今为止自1970以来的毫秒数
		Long lastMilli = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		log.info("lastMilli===" + lastMilli);

		if ((lastMilli - firstNow) < 2 * 100) {
			log.info("首次发送随机验证码");
			send = inst.sendToUserMailbox(userMailbox, randomCode);

			if (send == null && "".equals(send)) {
				log.error("发送captchaCode失败");
				throw new ServiceException(ServiceExEnum.NETWORK_NO_CONNECTION.getDescription());
			}

			redisTemplate.opsForValue().set(localTimeKey, firstNow, days, TimeUnit.DAYS);
		} else {// 如若非首次发送captcha
			log.info("非首次发送captcha");

			try {
				updateNow = Long.parseLong(redisTemplate.opsForValue().get(localTimeKey).toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("首次发送captchaCode失败,导致无法往redis中插入localTimeKey,进而报NPE");
				throw new ServiceException(ServiceExEnum.NETWORK_NO_CONNECTION.getDescription());
			}
			if (updateNow != null) {
				log.info("updateNow===" + updateNow);
			}

			// 校验差值时间
			Boolean different = inst.countDifferent(updateNow, lastMilli, expires);
			if (!different) {
				String description = ServiceExEnum.CAPTCHA_HAD_EXISTS.getDescription();
				log.error(description);
				throw new ServiceException(description);
			}

			send = inst.sendToUserMailbox(userMailbox, randomCode);

			// 覆盖更新常规存入时间
			redisTemplate.opsForValue().set(localTimeKey, LocalDateTime.now(), days, TimeUnit.DAYS);
		}

		controllerUtil.randomCodeRecord(randomCode, userMailbox, expires);
		return send;
	}

	@Deprecated
	@Override
	public String uploadAvatar(HttpServletRequest request, MultipartFile portrait)
			throws ServiceException, IllegalStateException, IOException, ServletException {
		TAccount account = new TAccount();
		String suffix = null;

		// 检查文件是否为空
		if (portrait.isEmpty()) {
			String description = ServiceExEnum.FILE_EMPTY_EXCEPTION.getDescription();
			// 为空：抛出异常：FileEmptyException
			log.error(description);
			throw new ServiceException(description);
		}

		// 检查文件大小
		if (portrait.getSize() > UPLOAD_MAX_SIZE) {
			String description = ServiceExEnum.FILE_SIZE_EXCEPTION.getDescription();
			log.error(description);
			// 超出范围(> UPLOAD_MAX_SIZE)：抛出异常：FileSizeException
			throw new ServiceException(description);
		}

		// 检查文件类型
		String contentType = portrait.getContentType();
		log.info("头像文件类型:" + contentType);

		Boolean checkFileType = inst.checkFileType(contentType);
		if (!checkFileType) {
			String description = ServiceExEnum.FILE_CONTENT_TYPE_EXCEPTION.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		// 确定文件夹路径：request.getServletContext().getRealPath(UPLOAD_DIR);
		String parentPath = request.getServletContext().getRealPath(UPLOAD_DIR);
		log.info("上传文件夹路径为:" + parentPath);

		// 创建上传文件夹的File对象parent
		File parent = new File(parentPath);

		// 检查文件夹是否存在，如果不存在，则创建
		if (!parent.exists()) {
			parent.mkdirs();
		}
		log.info("上传文件夹的File对象parent绝对路径:" + parent.getAbsolutePath());

		// 获取原文件名：file.getOriginalFilename()
		String originalFilename = portrait.getOriginalFilename();
		log.info("获取原文件名:" + originalFilename);

		// 从原文件名中得到扩展名
		int beginIndex = originalFilename.lastIndexOf(".");
		if (beginIndex > 0) {
			suffix = originalFilename.substring(beginIndex);
		}

		// 确定文件名：uuid/nanoTime/...
		String fileName = System.nanoTime() + suffix;

		// 创建dest对象：new File(parent, filename);
		File dest = new File(parent, fileName);
		log.info("dest对象路径: " + dest.getAbsolutePath());

		// 执行上传：portrait.transferTo(dest);
		try {
			portrait.transferTo(dest);
		} catch (IllegalStateException e) {
			// catch:IllegalStateException：抛出FileIllegalStateException
			e.printStackTrace();
			String description = ServiceExEnum.FILE_ILLEGAL_STATE_EXCEPTION.getDescription();
			log.error(description);
			throw new ServiceException(description);
		} catch (IOException e) {
			// catch:IOException：抛出FileIOException
			e.printStackTrace();
			String description = ServiceExEnum.FILE_IO_EXCEPTION.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		// 获取用户ID
		String accid = request.getSession().getAttribute("accid").toString();
		Integer uid = Integer.parseInt(accid);

		// 生成肖像全名：/UPLOAD_DIR/文件名.扩展名
		String avatar = "/" + UPLOAD_DIR + "/" + fileName;
		log.info("avatar全名:" + avatar);

		// 执行更新：据id而改化身名称
		account.setId(uid);
		account.setPortrait(avatar);

		int affect = baseMapper.updateById(account);
		log.info("更新头像affect:" + affect);

		return avatar;
	}

	@Override
	public LoginInfo loginVerifyToken(String phone, String keyword) throws ServiceException {
		QueryWrapper<TAccount> qw = new QueryWrapper<TAccount>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		LoginInfo info = new LoginInfo();
		JavaWebTokenUtil tokenUtil = new JavaWebTokenUtil();

		// 验证用户是否存在 by phone
		qw.eq("phone", phone);
		TAccount acc = baseMapper.selectOne(qw);

		if (acc == null) {
			log.error(ServiceExEnum.PHONE_NOT_FOUND.getDescription());
			throw new ServiceException(ServiceExEnum.PHONE_NOT_FOUND.getDescription());
		}

		// 验证密码是否正确
		boolean verify = inst.verify(keyword, acc.getPassword());
		if (verify == false) {
			log.error(ServiceExEnum.KEYWORD_ERR.getDescription());
			throw new ServiceException(ServiceExEnum.KEYWORD_ERR.getDescription());
		}

		// TODO 往Map中加入相关信息,再代入JWT方法内进行加密,植入用户ID等相关信息
		map.put("accEmployeeNum", acc.getEmployeeNum());
		map.put("accPhone", acc.getPhone());
		map.put("accId", acc.getId());
		map.put("accEmployeeName", acc.getEmployeeName());
		String token = tokenUtil.createJavaWebToken(map);
		log.info("Token== " + token);

		info.setTokenStr(token);
		info.setBornTime(acc.getBornTime());
		info.setDepartment(acc.getDepartment());
		info.setEmployeeName(acc.getEmployeeName());
		info.setEmployeeNum(acc.getEmployeeNum());
		info.setMailbox(acc.getMailbox());
		info.setPortrait(acc.getPortrait());
		info.setPhone(acc.getPhone());
		info.setRegion(acc.getRegion());
		info.setRegTime(acc.getRegTime());
		info.setRole(acc.getRole());

		return info;
	}

	@Deprecated
	@Override
	public String uploadAvatar(HttpServletRequest request, MultipartFile portrait, String token)
			throws ServiceException, IllegalStateException, IOException, ServletException {
		TAccount account = new TAccount();
		String suffix = null;
		JavaWebTokenUtil tokenUtil = new JavaWebTokenUtil();
		String avatar = null;

		// 检查文件是否为空
		if (portrait.isEmpty()) {
			String description = ServiceExEnum.FILE_EMPTY_EXCEPTION.getDescription();
			// 为空：抛出异常：FileEmptyException
			log.error(description);
			throw new ServiceException(description);
		}

		// 检查文件大小
		if (portrait.getSize() > UPLOAD_MAX_SIZE) {
			String description = ServiceExEnum.FILE_SIZE_EXCEPTION.getDescription();
			log.error(description);
			// 超出范围(> UPLOAD_MAX_SIZE)：抛出异常：FileSizeException
			throw new ServiceException(description);
		}

		// 检查文件类型
		String contentType = portrait.getContentType();
		log.info("头像文件类型:" + contentType);

		Boolean checkFileType = inst.checkFileType(contentType);
		if (!checkFileType) {
			String description = ServiceExEnum.FILE_CONTENT_TYPE_EXCEPTION.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		// 确定文件夹路径：request.getServletContext().getRealPath(UPLOAD_DIR);
		String parentPath = request.getServletContext().getRealPath(UPLOAD_DIR);
		log.info("上传文件夹路径为:" + parentPath);

		// 创建上传文件夹的File对象parent
		File parent = new File(parentPath);

		// 检查文件夹是否存在，如果不存在，则创建
		if (!parent.exists()) {
			parent.mkdirs();
		}
		log.info("上传文件夹的File对象parent绝对路径:" + parent.getAbsolutePath());

		// 获取原文件名：file.getOriginalFilename()
		String originalFilename = portrait.getOriginalFilename();
		log.info("获取原文件名:" + originalFilename);

		// 从原文件名中得到扩展名
		int beginIndex = originalFilename.lastIndexOf(".");
		if (beginIndex > 0) {
			suffix = originalFilename.substring(beginIndex);
		}

		// 确定文件名：uuid/nanoTime/...
		String fileName = System.nanoTime() + suffix;

		// 创建dest对象：new File(parent, filename);
		File dest = new File(parent, fileName);
		String destPath = dest.getAbsolutePath();
		log.info("dest对象于服务器的路径: " + destPath);

		// 执行上传：portrait.transferTo(dest);
		try {
			portrait.transferTo(dest);
		} catch (IllegalStateException e) {
			// catch:IllegalStateException：抛出FileIllegalStateException
			e.printStackTrace();
			String description = ServiceExEnum.FILE_ILLEGAL_STATE_EXCEPTION.getDescription();
			log.error(description);
			throw new ServiceException(description);
		} catch (IOException e) {
			// catch:IOException：抛出FileIOException
			e.printStackTrace();
			String description = ServiceExEnum.FILE_IO_EXCEPTION.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		// 获取用户ID
		Map<String, Object> map = tokenUtil.parseJavaWebToken(token);
		Integer uid = Integer.valueOf(map.get("accId").toString());
		log.info("解析令牌后其用户ID== " + uid);

		// 生成肖像全名：/UPLOAD_DIR/文件名.扩展名
		avatar = "/" + UPLOAD_DIR + "/" + fileName;
		log.info("avatar全名:" + avatar);

		// 执行更新：据id而改化身名称
		account.setId(uid);
		account.setPortrait(avatar);

		int affect = baseMapper.updateById(account);
		log.info("更新头像affect:" + affect);

		return avatar;
	}

	@Override
	public String updatePortrait(String imgDataStr, String token) throws ServiceException {
		JavaWebTokenUtil tokenUtil = new JavaWebTokenUtil();
		TAccount account = new TAccount();

		Map<String, Object> webToken = tokenUtil.parseJavaWebToken(token);
		Integer uid = Integer.valueOf(webToken.get("accId").toString());
		log.info("解析令牌后其用户ID== " + uid);

		// 执行更新：据id而改肖像
		account.setId(uid);
		account.setPortrait(imgDataStr);

		int rows = baseMapper.updateById(account);
		if (rows == 0) {
			String description = ServiceExEnum.SYSTEM_FATAL_BUG.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		return imgDataStr;
	}

	@Override
	public LoginInfo editProfileById(String employeeName, String phone, LocalDateTime bornTime, String mailbox,
			Integer id) throws ServiceException {
		QueryWrapper<TAccount> qw = new QueryWrapper<TAccount>();
		TAccount account = new TAccount();
		JavaWebTokenUtil tokenUtil = new JavaWebTokenUtil();
		LoginInfo info = new LoginInfo();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		int rows = 0;

		// 检查phone是否唯一
		qw.eq("phone", phone);
		Integer count = baseMapper.selectCount(qw);
		// TAccount selectOne = baseMapper.selectOne(qw);
		if (count > 1) {
			String description = ServiceExEnum.PHONE_HAS_EXISTED.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		// 检查mailbox是否唯一
		qw.eq("mailbox", mailbox);
		// TAccount selectOne1 = baseMapper.selectOne(qw);
		Integer count1 = baseMapper.selectCount(qw);
		if (count1 > 1) {
			String description = ServiceExEnum.MAILBOX_HAS_EXISTED.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		account.setEmployeeName(employeeName);
		account.setPhone(phone);
		account.setBornTime(bornTime);
		account.setMailbox(mailbox);
		account.setId(id);

		try {
			rows = baseMapper.updateById(account);
		} catch (Exception e) {
			e.printStackTrace();
			String description = ServiceExEnum.PHONE_HAS_EXISTED.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}
		log.info("update rows == " + rows);

		if (rows != 1) {
			String description = ServiceExEnum.SYSTEM_FATAL_BUG.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		info.setEmployeeName(employeeName);
		info.setPhone(phone);
		info.setBornTime(bornTime);
		info.setMailbox(mailbox);

		// TODO 代入散列表获取令牌
		hashMap.put("accEmployeeName", employeeName);
		hashMap.put("accPhone", phone);
		hashMap.put("accMailBox", mailbox);
		hashMap.put("accId", id);

		String token = tokenUtil.createJavaWebToken(hashMap);
		info.setTokenStr(token);

		return info;
	}

	@Override
	public Integer modifiedPasswordByPhone(String phone, String newPassword) throws ServiceException {
		// 根据手机号查出相应账号
		QueryWrapper<TAccount> queryWrapper = new QueryWrapper<TAccount>();
		TAccount account = new TAccount();

		queryWrapper.eq("phone", phone);
		TAccount tAccount = baseMapper.selectOne(queryWrapper);
		log.info(tAccount.toString());

		// 利用盐值生成新密文
		String text = inst.generateText(newPassword, tAccount.getSalt());

		// 根据ID和新密文改密码
		account.setId(tAccount.getId());
		account.setPassword(text);

		int effect = baseMapper.updateById(account);
		log.info("effect row:" + effect);
		return effect;
	}

	@Override
	public Integer alterPassword(String newPassword, String oldPassword, String phone) throws ServiceException {
		QueryWrapper<TAccount> queryWrapper = new QueryWrapper<TAccount>();

		queryWrapper.eq("phone", phone);
		TAccount tAccount = baseMapper.selectOne(queryWrapper);
		log.info(tAccount.toString());

		boolean verify = inst.verify(oldPassword, tAccount.getPassword());
		if (!verify) {
			String description = ServiceExEnum.OLD_PASSWORD_ERROR.getDescription();
			log.info(description);
			throw new ServiceException(description);
		}

		Integer rows = modifiedPasswordByPhone(phone, newPassword);
		log.info("rows==" + rows);
		return rows;
	}

	@Override
	public IPage<TAccount> pagingSearch(Integer pageth, Integer item) throws ServiceException {
		QueryWrapper<TAccount> queryWrapper = new QueryWrapper<>();

		queryWrapper.orderByAsc("id");

		Page<TAccount> page = new Page<>(pageth, item);
		IPage<TAccount> iPage = baseMapper.selectPage(page, queryWrapper);

		List<TAccount> list = iPage.getRecords();
		// 将密码和盐值置空
		for (TAccount tAccount : list) {
			tAccount.setPassword(null);
			tAccount.setSalt(null);
		}
		log.info(JSON.toJSONString(iPage));

		return iPage;
	}

	@Override
	public Integer getMaxIDFromTAccount() {
		QueryWrapper<TAccount> queryWrapper = new QueryWrapper<TAccount>();

		queryWrapper.select("MAX(id) max_id");
		List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);
		list.forEach(System.out::println);

		Map<String, Object> map = list.get(0);
		String maxIdStr = map.get("max_id").toString();
		log.info("max account id= \n" + maxIdStr);

		Integer maxid = Integer.parseInt(maxIdStr);
		return maxid;
	}

	@Override
	public Boolean validatedCaptcha(String mailbox, String verifyCode)
			throws ServiceException, UnsupportedEncodingException {
		String captchaKey = "RegisterCaptcha_" + mailbox;
		String value = null;

		try {
			value = redisTemplate.opsForValue().get(captchaKey).toString();
		} catch (Exception e) {
			e.printStackTrace();
			String description = ServiceExEnum.CAPTCHA_HAS_EXPIRED.getDescription();
			log.info(description);
			throw new ServiceException(description);
		}
		log.info("captchaKey===" + captchaKey + ", captchaKey value===" + value);

		boolean result = inst.veryifyRedisCode(verifyCode, value);
		log.info("veryifyRedisCode之结果===" + result);
		return result;
	}

	@Override
	public Integer editProfileById2(EditProfile2 e2, Integer id) throws ServiceException {
		QueryWrapper<TAccount> qw = new QueryWrapper<TAccount>();
//		QueryWrapper<TAccount> qw2 = new QueryWrapper<TAccount>();
		UpdateWrapper<TAccount> updateWrapper = new UpdateWrapper<>();
		TAccount account = new TAccount();
		Integer rows = null;

		// 验证修改者之权限role是否为0
		TAccount account2 = baseMapper.selectById(id);
		log.info("admin==>" + account2);
		if (account2.getRole() != 0) {
			String description = ServiceExEnum.ROLE_IS_NOT_ADMIN.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		// 检查phone是否唯一
		qw.eq("phone", e2.getPhone());
		Integer count = baseMapper.selectCount(qw);
		if (count > 1) {
			String description = ServiceExEnum.PHONE_HAS_EXISTED.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		// 检查mailbox是否唯一
		qw.eq("mailbox", e2.getMailbox());
		Integer count1 = baseMapper.selectCount(qw);
		if (count1 > 1) {
			String description = ServiceExEnum.MAILBOX_HAS_EXISTED.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		String subTimeStr = dateUtil.getSubTimeStr(e2.getBornTime());
		LocalDateTime dateTime = dateUtil.StrTransformLocalDateTime(subTimeStr);

		account.setBornTime(dateTime);
		account.setDepartment(e2.getDepartment());
		account.setEmployeeName(e2.getEmployeeName());
		account.setMailbox(e2.getMailbox());
		account.setPhone(e2.getPhone());
		account.setRole(e2.getRole());
		account.setRegion(e2.getRegion());

		updateWrapper.eq("employee_num", e2.getEmployeeNum());

		try {
			rows = baseMapper.update(account, updateWrapper);
		} catch (Exception e) {
			e.printStackTrace();
			String description = ServiceExEnum.PHONE_HAS_EXISTED.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}
		log.info("update rows == " + rows);

		if (rows != 1) {
			String description = ServiceExEnum.SYSTEM_FATAL_BUG.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		return rows;
	}

}
