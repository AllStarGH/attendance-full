package com.allstar.spring.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.allstar.spring.controller.kit.BaseActionUtil;
import com.allstar.spring.controller.kit.TAccountControllerUtil;
import com.allstar.spring.entity.EditProfile;
import com.allstar.spring.entity.EditProfile2;
import com.allstar.spring.entity.LoginInfo;
import com.allstar.spring.entity.RegistInfo;
import com.allstar.spring.entity.TAccount;
import com.allstar.spring.notation.Json;
import com.allstar.spring.service.ITAccountService;
import com.allstar.spring.service.exception.ServiceException;
import com.allstar.spring.util.DateTimeUtils;
import com.allstar.spring.util.JavaWebTokenUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 账号表 前端控制器
 * </p>
 *
 * @author gzh
 * @since 2019-12-07
 */
@Slf4j
@Controller
@Api(tags = "系统用户事务控制器")
@RequestMapping("/TAccountController")
public class TAccountController extends BaseActionUtil {
	@Autowired
	private ITAccountService ias;

	TAccountControllerUtil inst = TAccountControllerUtil.getInstance();

	DateTimeUtils dateUtil = DateTimeUtils.getInstance();

	@Override
	protected String parameterMark(Object... args) {
		return super.parameterMark(args);
	}

	/**
	 * http://localhost:8080/attendance/TAccountController/pagingSearchAction?pageth=0&items=5
	 * 
	 * @param items   每页条数
	 * @param pageth  页码
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "pagingSearchAction", method = RequestMethod.GET)
	public Json<IPage<TAccount>> pagingSearchAction(@RequestParam(value = "items", defaultValue = "4") Integer items,
			@RequestParam(value = "pageth", defaultValue = "0") Integer pageth, HttpServletRequest request) {
		parameterMark("\n页码=>" + pageth);
		parameterMark("\n每页条数=>" + items);
		String token = request.getHeader("token");
		Map<String, Object> map = verifyFrontToken(token);

		IPage<TAccount> page = ias.pagingSearch(pageth, items);

		return new Json<IPage<TAccount>>(SUCCESS, page);
	}

	/**
	 * /attendance/TAccountController/alterPasswordAction
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param request
	 * @return
	 */
	@ResponseBody
	@PostMapping("alterPasswordAction")
	@ApiOperation(value = "用户修改密码接口", notes = "修改密码", response = Json.class)
	@ApiImplicitParam(paramType = "query", name = "修改密码", value = "旧密码&新密码", required = true)
	public Json<Integer> alterPasswordAction(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, HttpServletRequest request) {
		JavaWebTokenUtil tokenUtil = new JavaWebTokenUtil();

		parameterMark("oldPassword:" + oldPassword + ",newPassword:" + newPassword);

		String token = request.getHeader("token");
		Map<String, Object> map02 = tokenUtil.parseJavaWebToken(token);
		String phone = map02.get("accPhone").toString();

		Integer affects = ias.alterPassword(newPassword, oldPassword, phone);

		inst.alterPasswordActionRecord(map02);

		return new Json<Integer>(SUCCESS, affects);
	}

	/**
	 * /attendance/TAccountController/editProfileAction
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@ResponseBody
	@Deprecated
	@PostMapping("editProfileAction")
	public Json<LoginInfo> editProfileAction(@RequestBody HashMap<String, String> map, HttpServletRequest request) {
		JavaWebTokenUtil tokenUtil = new JavaWebTokenUtil();

		for (Map.Entry<String, String> e : map.entrySet()) {
			parameterMark("key:" + e.getKey());
			parameterMark("value:" + e.getValue());
		}
		String tokenStr = request.getHeader("tokenStr");
		parameterMark("tokenStr== " + tokenStr);

		LocalDateTime dateTime = dateUtil.StrTransformLocalDateTime(map.get("bornTime"));
		Map<String, Object> map01 = tokenUtil.parseJavaWebToken(tokenStr);
		Integer id = Integer.parseInt(map01.get("accId").toString());

		LoginInfo info = ias.editProfileById(map.get("employeeName"), map.get("phone"), dateTime, map.get("mailbox"),
				id);

		inst.editProfileActionRecord(id, info);

		return new Json<LoginInfo>(SUCCESS, info);
	}

	/**
	 * /attendance/TAccountController/editProfileAction1
	 * 
	 * @param info
	 * @param request
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "修改帐号资料接口01")
	@PostMapping("editProfileAction1")
	@ApiImplicitParam(paramType = "body", name = "帐号待改资料实体", value = "待改资料", required = true)
	public Json<LoginInfo> editProfileAction1(@RequestBody EditProfile profile, HttpServletRequest request) {
		JavaWebTokenUtil tokenUtil = new JavaWebTokenUtil();
		parameterMark("EditProfile== " + profile.toString());

		String tokenStr = request.getHeader("tokenStr");
		parameterMark("tokenStr== " + tokenStr);

		String subTimeStr = dateUtil.getSubTimeStr(profile.getBornTime());
		LocalDateTime dateTime = dateUtil.StrTransformLocalDateTime(subTimeStr);

		Map<String, Object> map01 = tokenUtil.parseJavaWebToken(tokenStr);
		Integer id = Integer.parseInt(map01.get("accId").toString());

		LoginInfo info = ias.editProfileById(profile.getEmployeeName(), profile.getPhone(), dateTime,
				profile.getMailbox(), id);
		inst.editProfileActionRecord(id, info);

		return new Json<LoginInfo>(SUCCESS, info);
	}

	/**
	 * /attendance/TAccountController/editProfileByAdminAction
	 * 
	 * @param profile
	 * @param request
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "管理员修改帐号资料接口")
	@PostMapping("editProfileByAdminAction")
	@ApiImplicitParam(paramType = "body", name = "帐号待改资料实体2", value = "待改资料2", required = true)
	public Json<Integer> editProfileByAdminAction(@RequestBody EditProfile2 profile, HttpServletRequest request) {
		JavaWebTokenUtil tokenUtil = new JavaWebTokenUtil();
		parameterMark(profile.toString());

		String tokenStr = request.getHeader("tokenStr");
		parameterMark("tokenStr== " + tokenStr);
		Map<String, Object> map01 = tokenUtil.parseJavaWebToken(tokenStr);
		Integer id = Integer.parseInt(map01.get("accId").toString());

		Integer affects = ias.editProfileById2(profile, id);

		inst.editProfileByAdminActionRecord(profile, map01, affects);

		return new Json<Integer>(SUCCESS, affects);
	}

	/**
	 * /attendance/TAccountController/uploadAvatarAction<br>
	 * <b>此接口闲置</b>
	 * 
	 * @param req
	 * @param map
	 * @return
	 * @throws ServiceException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ServletException
	 */
	@PostMapping("uploadAvatarAction")
	@ResponseBody
	@Deprecated
	public Json<String[]> uploadAvatarAction(@RequestParam(value = "portrait", required = false) MultipartFile portrait,
			HttpServletRequest req) throws ServiceException, IllegalStateException, IOException, ServletException {
		String[] uploads = new String[0];

		log.info(portrait.getName());
		parameterMark(portrait.getOriginalFilename(), req.getServletContext().getContextPath());

		String uploadAvatar = ias.uploadAvatar(req, portrait);
		uploads[0] = uploadAvatar;

		return new Json<String[]>(SUCCESS, uploads);
	}

	/**
	 * 
	 * @param portrait
	 * @param token
	 * @return
	 * @throws ServiceException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ServletException
	 */
	@ResponseBody
	@PostMapping("uploadAvatarAction1")
	public Json<HashMap<String, String>> uploadAvatarAction1(
			@RequestParam(value = "portrait", required = false) String portrait, @RequestParam("token") String token)
			throws ServiceException, IllegalStateException, IOException, ServletException {
		HashMap<String, String> hashMap = new HashMap<String, String>();

		parameterMark(portrait);
		parameterMark(token);

		Map<String, Object> map = verifyFrontToken(token);
		String updatePortrait = ias.updatePortrait(portrait, token);

		hashMap.put("uploadAvatar", updatePortrait);

		inst.uploadAvatarActionRecord(map);
		return new Json<HashMap<String, String>>(SUCCESS, hashMap);
	}

	/**
	 * /attendance/TAccountController/uploadAvatarAction2
	 * 
	 * @param portrait
	 * @param req
	 * @param token
	 * @return
	 * @throws ServiceException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ServletException
	 */
	@Deprecated
	@ResponseBody
	@PostMapping("uploadAvatarAction2")
	public Json<HashMap<String, String>> uploadAvatarAction2(
			@RequestParam(value = "portrait", required = false) MultipartFile portrait, HttpServletRequest req,
			@RequestParam("token") String token)
			throws ServiceException, IllegalStateException, IOException, ServletException {
		HashMap<String, String> hashMap = new HashMap<String, String>();

		log.info(portrait.getName());
		parameterMark(portrait.getOriginalFilename(), req.getServletContext().getContextPath());
		parameterMark(token);

		String uploadAvatar = ias.uploadAvatar(req, portrait, token);
		hashMap.put("uploadAvatar", uploadAvatar);

		inst.uploadAvatarActionRecord(null);

		return new Json<HashMap<String, String>>(SUCCESS, hashMap);
	}

	/**
	 * /attendance/TAccountController/sendCaptchaCodeAction
	 * 
	 * @param userMailbox
	 * @return
	 * @throws ServiceException
	 * @throws EmailException
	 */
	@ResponseBody
	@ApiOperation(value = "向邮箱发送验证码接口", notes = "返回MimeMessage")
	@RequestMapping(value = "sendCaptchaCodeAction", method = RequestMethod.POST)
	@ApiImplicitParam(paramType = "body", name = "userMailbox", value = "邮箱", required = true)
	public Json<String[]> sendCaptchaCodeAction(@RequestBody String userMailbox)
			throws ServiceException, EmailException {
		parameterMark(userMailbox);

		String sends = ias.sendCaptchaCode(userMailbox);
		log.info(sends);

		inst.sendCaptchaCodeActionRecord(sends, userMailbox);

		return new Json<String[]>(SUCCESS, sends);
	}

	/**
	 * /attendance/TAccountController/loginAction
	 * 
	 * @param session
	 * @param phone
	 * @param password
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "loginAction", method = RequestMethod.POST)
	public Json<TAccount> loginAction(HttpSession session, @RequestBody HashMap<String, String> map) {
		parameterMark(map.get("phone"), map.get("password"));
		log.info(map.get("phone") + "于" + now_time + "准备登录");

		TAccount account = ias.loginBaseOnPhone(map.get("password"), map.get("phone"), session);

		inst.loginActionRecord(map.get("phone"), account);

		return new Json<TAccount>(SUCCESS, account);
	}

	/**
	 * /attendance/TAccountController/loginAction2
	 * 
	 * <pre>
	 * <h4>注解ApiOperation</h4>
	 * option的value的内容是这个method的描述，<br>notes是详细描述，response是最终返回的json<br>
	 * model(如Void.class=>返回null);其他参数忽略
	 * </pre>
	 * 
	 * @param phone
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loginAction2", method = RequestMethod.POST)
	@ApiOperation(value = "用户登录接口", notes = "用户登录接口", response = LoginInfo.class)
	@ApiImplicitParam(paramType = "query", name = "帐号登录", value = "手机号&密码", required = true)
	public Json<LoginInfo> loginAction2(@RequestParam("phone") String phone,
			@RequestParam("password") String password) {
		parameterMark(phone + "," + password);
		log.info("手机号(==帐号):" + phone + "于" + now_time + "准备登录");

		LoginInfo info = ias.loginVerifyToken(phone, password);

		inst.loginActionRecord(phone, info);

		return new Json<LoginInfo>(SUCCESS, info);
	}

	/**
	 * /attendance/TAccountController/regAction
	 * 
	 * @param account
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ServiceException
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "regAction", method = RequestMethod.POST)
	public Json<Integer> regAction(@RequestBody String account) throws ServiceException, UnsupportedEncodingException {
		parameterMark(account);

		TAccount account1 = ias.saveOnePre(account);
		Integer rows = ias.saveAccount(account1);

		inst.regActionRecord(account1, rows); // write into record log file

		return new Json<Integer>(SUCCESS, rows);
	}

	/**
	 * /attendance/TAccountController/regAction1
	 * 
	 * @param account
	 * @return
	 * @throws ServiceException
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@ApiOperation(value = "新帐号注册接口01")
	@RequestMapping(value = "regAction1", method = RequestMethod.POST)
	@ApiImplicitParam(paramType = "body", name = "新帐号注册实体", value = "帐号", required = true)
	public Json<Integer> regAction1(@RequestBody RegistInfo account)
			throws ServiceException, UnsupportedEncodingException {
		parameterMark(account.toString());
		TAccount registInfo = new RegistInfo();// 向上造型

		registInfo = account;
		log.info("registInfo=>" + registInfo);

		Boolean bool = ias.validatedCaptcha(account.getMailbox(), account.getVeriCode());
		log.info("validate captcha result=>" + bool);
		Integer rows = ias.saveAccount(registInfo);

		inst.regActionRecord(registInfo, rows);

		return new Json<Integer>(SUCCESS, rows);
	}
}
