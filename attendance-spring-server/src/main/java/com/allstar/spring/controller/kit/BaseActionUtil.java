package com.allstar.spring.controller.kit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.allstar.spring.notation.Json;
import com.allstar.spring.service.exception.ServiceExEnum;
import com.allstar.spring.service.exception.ServiceException;
import com.allstar.spring.util.JavaWebTokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 控制器工具基类
 * 
 * @author admin @ControllerAdvice：包含@Component。可以被扫描到。统一处理异常。
 *
 */
@Slf4j
@ControllerAdvice
public class BaseActionUtil {
	/**
	 * "成功"
	 */
	public static final Integer SUCCESS = 200;

	/**
	 * 日志目录实际路径: "/home/admin/workspace/temporary-logs/Attendance"
	 */
	public final static String ENGINE_DAILY_PATH = "/home/admin/workspace/temporary-logs/Attendance";

	/**
	 * 当前时间
	 */
	public static String now_time = null;

	static {
		now_time = getNowTime();
	}

	/**
	 * 换行分隔符+p闭合标签,后缀
	 */
	protected static final String P_TAG_SUFFIX = System.getProperty("line.separator") + "</p>";

	/**
	 * p标签,前缀
	 */
	public static String p_tag_prefix = "<p>";

	/**
	 * the base builder of P_TAG_PREFIX
	 */
	public static StringBuilder base_builder = new StringBuilder();

	/**
	 * 文件输出流
	 */
	protected static FileOutputStream fos = null;

	/**
	 * 二进制数组,充作缓冲对象
	 */
	protected static byte[] buff = null;

	/**
	 * 切口,验证前端发来的令牌
	 * 
	 * @param jwt token
	 * @return 解析后的令牌散列表
	 */
	public Map<String, Object> verifyFrontToken(String jwt) {
		JavaWebTokenUtil util = new JavaWebTokenUtil();

		Map<String, Object> map = util.parseJavaWebToken(jwt);
		if (map == null) {
			String description = ServiceExEnum.OFFLINE_LOGIN.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		return map;
	}

	/**
	 * 获取session.attribute(Object),充当切面
	 * 
	 * @param session
	 * @return
	 * @throws ServiceException
	 */
	@Deprecated
	public Integer getAccidFromSession(HttpSession session) throws ServiceException {
		Integer accid = null;
		int anchor = 0;

		try {
			accid = Integer.parseInt(session.getAttribute("accid").toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(this.getClass().getName());
			anchor = 1;
		}

		if (anchor == 1) {
			String description = ServiceExEnum.OFFLINE_LOGIN.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		return accid;
	}

	/**
	 * 获取session.attribute(Object),充当切面
	 * 
	 * @param session
	 * @return
	 * @throws ServiceException
	 */
	@Deprecated
	public Integer getEmpnoFromSession(HttpSession session) throws ServiceException {
		int anchor = 0;
		Integer empno = null;

		try {
			empno = Integer.parseInt(session.getAttribute("empno").toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(this.getClass().getName());
			anchor = 1;
		}

		if (anchor == 1) {
			String description = ServiceExEnum.OFFLINE_LOGIN.getDescription();
			log.error(description);
			throw new ServiceException(description);
		}

		return empno;
	}

	/**
	 * 业务异常处理器
	 * 
	 * @param e
	 * @ExceptionHandler（Exception.class）：用在方法上面表示遇到这个异常就执行以下方法
	 * 
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler({ ServiceException.class })
	public Json<Void> exceptionHandler(Throwable e) {
		Json<Void> rr = new Json<Void>();

		rr.setMessage(e.getLocalizedMessage());

		/* 根据异常信息设置异常码 */
		switch (e.getLocalizedMessage()) {
		case "您不是管理员,无法进行此项操作":
			rr.setStatus(ServiceExEnum.ROLE_IS_NOT_ADMIN.getCode());
			break;

		case "无网络连接,请检查您的互联网状态":
			rr.setStatus(ServiceExEnum.NETWORK_NO_CONNECTION.getCode());
			break;

		case "验证码已过期,请重新点击发送验证码":
			rr.setStatus(ServiceExEnum.CAPTCHA_HAS_EXPIRED.getCode());
			break;

		case "原密码错误":
			rr.setStatus(ServiceExEnum.OLD_PASSWORD_ERROR.getCode());
			break;

		case "自定义文件非法状态异常":
			rr.setStatus(ServiceExEnum.FILE_ILLEGAL_STATE_EXCEPTION.getCode());
			break;

		case "文件自定义输入输出异常":
			rr.setStatus(ServiceExEnum.FILE_IO_EXCEPTION.getCode());
			break;

		case "文件类型不符合,请重新改正":
			rr.setStatus(ServiceExEnum.FILE_CONTENT_TYPE_EXCEPTION.getCode());
			break;

		case "文件过大,请重新选择合适的文件":
			rr.setStatus(ServiceExEnum.FILE_SIZE_EXCEPTION.getCode());
			break;

		case "文件为空,请重新检查":
			rr.setStatus(ServiceExEnum.FILE_EMPTY_EXCEPTION.getCode());
			break;

		case "邮箱验证码不正确":
			rr.setStatus(ServiceExEnum.CAPTCHA_NOT_CORRECTLY.getCode());
			break;

		case "邮箱验证码为空":
			rr.setStatus(ServiceExEnum.CAPTCHA_IS_NULL.getCode());
			break;

		case "此邮箱地址不存在":
			rr.setStatus(ServiceExEnum.NO_SUCH_MAILBOX_ADDRESS.getCode());
			break;

		case "验证码未过期,请等有效期过后再重新发送验证码":
			rr.setStatus(ServiceExEnum.CAPTCHA_HAD_EXISTS.getCode());
			break;

		case "不支持此款邮箱,目前支持绑定的邮箱有:126/163/aliyun/21cn/sina/qq":
			rr.setStatus(ServiceExEnum.MAILBOX_UNSUPPORTED.getCode());
			break;

		case "员工工号冲突,此工号已经存在于系统中":
			rr.setStatus(ServiceExEnum.EMPLOYEE_NUM_DUPLICATE_CONFLICT.getCode());
			break;

		case "未寻获此工号,无此用户":
			rr.setStatus(ServiceExEnum.EMP_NUM_NOT_FOUND.getCode());
			break;

		case "您已下线,请重新登录":
			rr.setStatus(ServiceExEnum.OFFLINE_LOGIN.getCode());
			break;

		case "该邮箱地址已被其他用户注册":
			rr.setStatus(ServiceExEnum.MAILBOX_HAS_EXISTED.getCode());
			break;

		case "该电话号码已被其他用户使用":
			rr.setStatus(ServiceExEnum.PHONE_HAS_EXISTED.getCode());
			break;

		case "系统故障,技术人员正在紧急抢修":
			rr.setStatus(ServiceExEnum.SYSTEM_FATAL_BUG.getCode());
			break;

		case "密码错误,请检查密码无误后再登录":
			rr.setStatus(ServiceExEnum.KEYWORD_ERR.getCode());
			break;

		case "系统内无此手机号码":
			rr.setStatus(ServiceExEnum.PHONE_NOT_FOUND.getCode());
			break;
		}

		log.info(rr.toString());
		return rr;
	}

	/**
	 * 
	 * @return
	 */
	public static String getNowTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = format.format(new Date());

		return dateStr;
	}

	/**
	 * 创建文件夹和文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String createFolderAndFile(String fileName) throws IOException {
		File file = new File(ENGINE_DAILY_PATH);

		if (!file.exists()) {
			file.mkdir();
		}

		File fi = new File(file, fileName);
		fi.createNewFile();

		String path = fi.getAbsolutePath();

		return path;
	}

	/**
	 * 把记录写入日志文件(有单行判断条件)
	 * 
	 * @param affect   受影响行数
	 * @param fileName
	 * @param sentence
	 */
	public void writeToFile(Integer affect, String fileName, String sentence) {
		String filePath = null;

		try {
			filePath = createFolderAndFile(fileName);
			System.err.println("File path:" + filePath);

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (affect == 1) {
			executeWrite(sentence, filePath);
		}
	}

	/**
	 * 把记录写入日志文件 <br>
	 * <b>Overload</b>
	 * 
	 * @param fileName
	 * @param sentence
	 */
	protected void writeToFile(String fileName, String sentence) {
		String filePath = null;

		try {
			filePath = createFolderAndFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		executeWrite(sentence, filePath);
	}

	/**
	 * 执行写入具体方法
	 * 
	 * @param record
	 * @param filePath
	 */
	public void executeWrite(String record, String filePath) {
		try {
			fos = new FileOutputStream(filePath, true);
			buff = record.getBytes();
			fos.write(buff);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 参数标记,即<b>打桩</b>
	 * 
	 * @param args
	 */
	protected String parameterMark(Object... args) {
		StringBuffer buffer = new StringBuffer();
		LinkedList<Object> list = new LinkedList<Object>();
		String clzName = this.getClass().getName();

		for (int i = 0; i < args.length; i++) {
			list.add(args[i]);
		}

		buffer.append(clzName + "\n");
		buffer.append("Received parameters from front:");

		for (Object object : list) {
			buffer.append("\n");
			buffer.append(object);
		}

		log.info(buffer.toString());
		return buffer.toString();
	}

}
