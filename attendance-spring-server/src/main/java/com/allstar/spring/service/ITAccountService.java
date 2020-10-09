package com.allstar.spring.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.springframework.web.multipart.MultipartFile;

import com.allstar.spring.entity.EditProfile;
import com.allstar.spring.entity.EditProfile2;
import com.allstar.spring.entity.LoginInfo;
import com.allstar.spring.entity.TAccount;
import com.allstar.spring.service.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 * 账号表 服务类
 * </p>
 *
 * @author gzh
 * @since 2019-12-07
 */
public interface ITAccountService extends IService<TAccount> {
	/**
	 * 从taccount获取最大ID
	 * 
	 * @return
	 */
	Integer getMaxIDFromTAccount();

	/**
	 * 分页查询
	 * 
	 * @param pageth 页码
	 * @param item   每页条目数数量
	 * @return
	 * @throws ServiceException
	 */
	IPage<TAccount> pagingSearch(Integer pageth, Integer item) throws ServiceException;

	/**
	 * 修改密码
	 * 
	 * @param newPassword
	 * @param oldPassword
	 * @param phone
	 * @return
	 * @throws ServiceException
	 */
	Integer alterPassword(String newPassword, String oldPassword, String phone) throws ServiceException;

	/**
	 * 直接修改密码,<b>基于手机号码</b>
	 * 
	 * @param phone
	 * @param newPassword
	 * @return
	 * @throws ServiceException
	 */
	Integer modifiedPasswordByPhone(String phone, String newPassword) throws ServiceException;

	/**
	 * 修改个人基本资料
	 * <ol>
	 * <li>用户名</li>
	 * <li>手机号码(唯一)</li>
	 * <li>出生日期</li>
	 * <li>邮箱地址(唯一)</li>
	 * </ol>
	 * <b>基于ID</b>
	 * 
	 * @param employeeName
	 * @param phone
	 * @param bornTime
	 * @param mailbox
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	LoginInfo editProfileById(String employeeName, String phone, LocalDateTime bornTime, String mailbox, Integer id)
			throws ServiceException;

	/**
	 * <b>由管理员administrator</b>修改员工资料
	 * 
	 * @param e2 前台传与
	 * @param id 取于令牌
	 * @return
	 * @throws ServiceException
	 */
	Integer editProfileById2(EditProfile2 e2, Integer id) throws ServiceException;

	/**
	 * 修改用户头像
	 * 
	 * @param imgDataStr base64字符串
	 * @param token      令牌字符串
	 * @return base64格式字符串
	 * @throws ServiceException
	 */
	String updatePortrait(String imgDataStr, String token) throws ServiceException;

	/**
	 * 用户登录,含token验证用户信息
	 * 
	 * @param phone   用户电话,也是登录时的账号
	 * @param keyword 密码
	 * @return 含令牌字符串
	 * @throws ServiceException
	 */
	abstract LoginInfo loginVerifyToken(String phone, String keyword) throws ServiceException;

	/**
	 * 上传头像图片
	 * 
	 * @param request  请求对象
	 * @param portrait 图片文件
	 * @param token    令牌字符串
	 * @return
	 * @throws ServiceException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ServletException
	 */
	@Deprecated
	String uploadAvatar(HttpServletRequest request, MultipartFile portrait, String token)
			throws ServiceException, IllegalStateException, IOException, ServletException;

	/**
	 * 上传头像 <br>
	 * <b>本接口闲置</b>
	 * 
	 * @param request
	 * @param portrait 头像文件
	 * @return
	 * @throws ServiceException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ServletException
	 */
	@Deprecated
	String uploadAvatar(HttpServletRequest request, MultipartFile portrait)
			throws ServiceException, IllegalStateException, IOException, ServletException;

	/**
	 * 
	 * <ul>
	 * <li>往邮箱发送验证码;</li>
	 * <li>parameter:后台自动产生之验证码;注册用户目标邮箱;</li>
	 * <li>由开发者邮箱发送验证码至目标邮箱,发送后将验证码存入会话或redis中(需设定时限)</li>
	 * </ul>
	 * 
	 * @param userMailbox
	 * @return
	 * @throws ServiceException
	 * @throws EmailException
	 */
	abstract String sendCaptchaCode(String userMailbox) throws ServiceException, EmailException;

	/**
	 * 据手机号码而登 <br>
	 * <b>本接口闲置</b>
	 * 
	 * @param pwd
	 * @param phone
	 * @param session
	 * @return
	 * @throws ServiceException
	 */
	@Deprecated
	abstract TAccount loginBaseOnPhone(String pwd, String phone, HttpSession session) throws ServiceException;

	/**
	 * <pre>
	 * 增一位预前准备
	 * </pre>
	 * 
	 * @param jsonStr
	 * @return
	 * @throws ServiceException
	 * @throws UnsupportedEncodingException
	 */
	@Deprecated
	TAccount saveOnePre(String jsonStr) throws ServiceException, UnsupportedEncodingException;

	/**
	 * 校验注册提交之验证码(用户取自邮箱内)是否与本地redis中的一致
	 * 
	 * @param mailbox
	 * @param verifyCode
	 * @return
	 * @throws ServiceException
	 * @throws UnsupportedEncodingException
	 */
	Boolean validatedCaptcha(String mailbox, String verifyCode) throws ServiceException, UnsupportedEncodingException;

	/**
	 * <h3>此接口闲置</h3>
	 * 
	 * @param pwd
	 * @param empNo
	 * @param session
	 * @return
	 * @throws ServiceException
	 */
	@Deprecated
	TAccount login(String pwd, Integer empNo, HttpSession session) throws ServiceException;

	/**
	 * 
	 * @param phone
	 * @return
	 * @throws ServiceException
	 */
	TAccount foundByPhone(String phone) throws ServiceException;

	/**
	 * 
	 * @param mailbox
	 * @return
	 * @throws ServiceException
	 */
	TAccount foundByMailbox(String mailbox) throws ServiceException;

	/**
	 * 新增
	 * 
	 * @param account
	 * @return
	 * @throws ServiceException
	 */
	Integer saveAccount(TAccount account) throws ServiceException;

	/**
	 * 据工号查一位
	 * 
	 * @param empNum 员工编号
	 * @return
	 * @throws ServiceException
	 */
	TAccount findOneByEmpNum(Integer empNum) throws ServiceException;
}
