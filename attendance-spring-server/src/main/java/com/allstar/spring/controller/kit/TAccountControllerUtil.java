package com.allstar.spring.controller.kit;

import java.util.HashMap;
import java.util.Map;

import com.allstar.spring.entity.EditProfile2;
import com.allstar.spring.entity.LoginInfo;
import com.allstar.spring.entity.TAccount;

/**
 * 
 * @author admin
 *
 */
public class TAccountControllerUtil extends BaseActionUtil {
	private static TAccountControllerUtil instance;

	/**
	 * daily file name
	 */
	public static final String DAILY_FILE_NAME = "TAccount-Record.txt";

	/** 锁 */
	private static final Object LOCK = new Object();

	/**
	 * 懒汉式
	 */
	public static TAccountControllerUtil getInstance() {
		if (instance == null) {
			// 决定是否需要锁定
			synchronized (LOCK) {
				if (instance == null) {
					instance = new TAccountControllerUtil();
				}
			}
		}

		return instance;
	}

	/**
	 * [getRegionByOrder description:根据数字获取地区名称]
	 * 
	 * @param key [description]
	 * @return [description]
	 */
	public String getRegionByOrder(Integer key) {
		String area = "";

		switch (key) {
		case 0:
			area = "安淮";
			break;

		case 1:
			area = "南徽";
			break;

		case 2:
			area = "古丰";
			break;

		case 3:
			area = "方千";
			break;

		case 4:
			area = "播星";
			break;

		case 5:
			area = "角港";
			break;

		case 6:
			area = "米园";
			break;

		case 7:
			area = "湖地";
			break;

		case 8:
			area = "寿韫";
			break;

		case 9:
			area = "天玺台";
			break;

		case 10:
			area = "苏崧";
			break;

		case 11:
			area = "南威";
			break;

		case 12:
			area = "都张集";
			break;

		case 13:
			area = "企塘";
			break;

		case 14:
			area = "中缇";
			break;
		}

		return area;
	}

	/**
	 * [getDepartmentByOrder description:根据数字获得岗位名称]
	 * 
	 * @param key [description]
	 * @return [description]
	 */
	public String getDepartmentByOrder(Integer key) {
		String position = "";

		switch (key) {
		case 0:
			position = "研发";
			break;

		case 1:
			position = "财务";
			break;

		case 2:
			position = "人事";
			break;

		case 3:
			position = "销售";
			break;

		case 4:
			position = "公关";
			break;

		case 5:
			position = "法务";
			break;

		case 6:
			position = "安全";
			break;

		}

		return position;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getRoleByKey(Integer key) {
		String role = "";

		if (key == 0) {
			role = "系统管理员";
		} else {
			role = "普通用户";
		}

		return role;
	}

	/**
	 * 
	 * @param infomation
	 * @param mailbox
	 */
	public void sendCaptchaCodeActionRecord(String infomation, String mailbox) {
		if (infomation != null && !"".equals(infomation)) {
			base_builder.append(p_tag_prefix);
			base_builder.append("于");
			base_builder.append(now_time);
			base_builder.append("生成随机验证码:");
			base_builder.append(infomation);
			base_builder.append(",并发送至");
			base_builder.append(mailbox);
			base_builder.append(P_TAG_SUFFIX);

			writeToFile(DAILY_FILE_NAME, base_builder.toString());
		}
	}

	/**
	 * 
	 * @param ta
	 * @param affect
	 */
	public void regActionRecord(TAccount ta, Integer affect) {
		base_builder.append(p_tag_prefix);
		base_builder.append(affect);
		base_builder.append("位员工于");
		base_builder.append(now_time);
		base_builder.append("注册成功,工号:");
		base_builder.append(ta.getEmployeeNum());
		base_builder.append(",姓名:");
		base_builder.append(ta.getEmployeeName());
		base_builder.append(",所在地区:");
		base_builder.append(getRegionByOrder(ta.getRegion()));
		base_builder.append(",所在部门:");
		base_builder.append(getDepartmentByOrder(ta.getDepartment()));
		base_builder.append(",账号角色:");
		base_builder.append(getRoleByKey(ta.getRole()));
		base_builder.append(",邮箱:");
		base_builder.append(ta.getMailbox());
		base_builder.append(",手机号(登录账号)为:");
		base_builder.append(ta.getPhone());
		base_builder.append(P_TAG_SUFFIX);

		writeToFile(affect, DAILY_FILE_NAME, base_builder.toString());
	}

	/**
	 * 
	 * @param phone
	 * @param account
	 */
	public void loginActionRecord(String phone, TAccount account) {
		base_builder.append(p_tag_prefix);
		base_builder.append(phone);
		base_builder.append("于");
		base_builder.append(now_time);
		base_builder.append("正在准备登录... ");

		if (account != null) {
			base_builder.append(phone);
			base_builder.append(" 登录成功. 工号:");
			base_builder.append(account.getEmployeeNum());
			base_builder.append(",姓名:");
			base_builder.append(account.getEmployeeName());
			base_builder.append(",地区:");
			base_builder.append(getRegionByOrder(account.getRegion()));
			base_builder.append(",部门:");
			base_builder.append(getDepartmentByOrder(account.getDepartment()));
			base_builder.append(",账号角色:");
			base_builder.append(getRoleByKey(account.getRole()));
		} else {
			base_builder.append(" 登录失败");
		}
		base_builder.append(P_TAG_SUFFIX);

		writeToFile(DAILY_FILE_NAME, base_builder.toString());
	}

	/**
	 * 
	 * @param map
	 */
	public void uploadAvatarActionRecord(Map<String, Object> map) {
		base_builder.append(p_tag_prefix);
		base_builder.append("ID为");
		base_builder.append(map.get("accId").toString());
		base_builder.append("的用户");
		base_builder.append(map.get("accEmployeeName").toString());
		base_builder.append("于");
		base_builder.append(now_time);
		base_builder.append("成功上传了头像");
		base_builder.append(P_TAG_SUFFIX);
		writeToFile(DAILY_FILE_NAME, base_builder.toString());
	}

	/**
	 * 
	 * @param id
	 * @param info
	 */
	public void editProfileActionRecord(Integer id, LoginInfo info) {
		base_builder.append(p_tag_prefix);
		base_builder.append("id为");
		base_builder.append(id);
		base_builder.append("的用户于");
		base_builder.append(now_time);
		base_builder.append("修改了个人基本资料,新手机号码为:");
		base_builder.append(info.getPhone());
		base_builder.append(P_TAG_SUFFIX);
		writeToFile(DAILY_FILE_NAME, base_builder.toString());
	}

	/**
	 * 
	 * @param map02
	 */
	public void alterPasswordActionRecord(Map<String, Object> map02) {
		base_builder.append(p_tag_prefix);
		base_builder.append("id为");
		base_builder.append(map02.get("accId").toString());
		base_builder.append(",工号为");
		base_builder.append(map02.get("accEmployeeNum").toString());
		base_builder.append("的用户于");
		base_builder.append(now_time);
		base_builder.append("成功修改了密码");
		base_builder.append(P_TAG_SUFFIX);
		writeToFile(DAILY_FILE_NAME, base_builder.toString());
	}

	/**
	 * 
	 * @param randomCode
	 * @param userMailbox
	 * @param expires
	 */
	public void randomCodeRecord(Integer randomCode, String userMailbox, Integer expires) {
		base_builder.append(p_tag_prefix);
		base_builder.append(now_time);
		base_builder.append("生成验证码:");
		base_builder.append(randomCode);
		base_builder.append(",目标邮箱为");
		base_builder.append(userMailbox);
		base_builder.append(",有效期");
		base_builder.append(expires);
		base_builder.append("分钟");
		base_builder.append(P_TAG_SUFFIX);
		writeToFile(DAILY_FILE_NAME, base_builder.toString());
	}

	/**
	 * 
	 * @param profile
	 * @param map01
	 * @param affects
	 */
	public void editProfileByAdminActionRecord(EditProfile2 profile, Map<String, Object> map01, Integer affects) {
		base_builder.append(p_tag_prefix);
		base_builder.append("ID为");
		base_builder.append(map01.get("accId"));
		base_builder.append("的管理员于");
		base_builder.append(now_time);
		base_builder.append("成功修改了");
		base_builder.append(affects);
		base_builder.append("名员工的资料,该员工工号为:");
		base_builder.append(profile.getEmployeeNum());
		base_builder.append(",手机号为:");
		base_builder.append(profile.getPhone());
		base_builder.append(P_TAG_SUFFIX);
		writeToFile(affects, DAILY_FILE_NAME, base_builder.toString());
	}
}
