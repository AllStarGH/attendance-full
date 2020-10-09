package com.allstar.spring.test.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.allstar.spring.entity.EditProfile2;
import com.allstar.spring.entity.LoginInfo;
import com.allstar.spring.entity.TAccount;
import com.allstar.spring.service.ITAccountService;
import com.allstar.spring.service.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest {
	@Autowired
	private ITAccountService ias;

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void editsTest() {
		EditProfile2 e2 = new EditProfile2();

		e2.setBornTime(null);
		e2.setMailbox("1903256250@163.com");
		e2.setEmployeeName("AIMAIMAIM");
		e2.setDepartment(3);
		e2.setPhone("13774157475");
		e2.setRegion(4);
		e2.setRole(1);
		e2.setEmployeeNum(2020041941);

		try {
			Integer affects = ias.editProfileById2(e2, 45);
			System.err.println("affects==" + affects);
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

	@Test
	public void paginTest() {
		IPage<TAccount> iPage = ias.pagingSearch(1, 5);
		List<TAccount> records = iPage.getRecords();
		for (TAccount tAccount : records) {
			System.err.println(tAccount.toString());
		}
	}

	@Test
	public void selectInfoTest() {
		try {
			Integer maxIDFromTAccount = ias.getMaxIDFromTAccount();
			System.err.println(maxIDFromTAccount);
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

	@Test
	public void updateBasciInfoTest() {
		try {
			LoginInfo info = ias.editProfileById("companies", "11556498888", LocalDateTime.now(), "6511516@yy.com", 16);
			System.err.println(info);
		} catch (ServiceException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

	@Test
	public void updateKeywordTest() {
		try {
			Integer effects = ias.alterPassword("147147", "999555", "13774157471");
		} catch (ServiceException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

	@Test
	public void updatePasswordTest() {
		try {
			Integer affect = ias.modifiedPasswordByPhone("13774157471", "123");
			System.err.println("affect=" + affect);
		} catch (ServiceException e) {
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void gainRedisValueTest() throws Exception {
		ValueOperations operations = redisTemplate.opsForValue();
		String key = "Redis-Set";
		Object valObject = operations.get(key);
		Boolean hasKey = redisTemplate.hasKey(key);
		if (hasKey) {
			System.err.println(key + "==" + valObject.toString());
		} else {
			System.err.println(key + " not exist==" + hasKey);
		}
	}

	@Test
	public void redisGainTest() throws Exception {
		ValueOperations<String, HashMap> operations = (ValueOperations<String, HashMap>) redisTemplate.opsForValue();
		// 根据key从redis中获取数据
		HashMap<Integer, String> hashMap = operations.get("Redis-Set");
		for (Map.Entry<Integer, String> ele : hashMap.entrySet()) {
			System.err.println(ele.getKey() + " , " + ele.getValue());
		}
	}

	@Test
	public void redisSetTest() throws Exception {
		String key = "Redis-Set";
		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
		hashMap.put(0, "value 00");
		hashMap.put(1, "value 01");
		hashMap.put(2, "value 02");

		ValueOperations<String, HashMap> operations = redisTemplate.opsForValue();

		operations.set(key, hashMap, 20 * 60, TimeUnit.SECONDS);

		boolean exists = redisTemplate.hasKey(key);

		if (exists) {
			System.out.println(key + " exists is true");
		} else {
			System.out.println(key + " exists is false");
		}
	}

	@Test
	public void sendLicenceCode() {
		try {
			String send = ias.sendCaptchaCode("力博得@qq.cn");
			System.err.println("SEND===" + send);
		} catch (EmailException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.getLocalizedMessage();
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
	}

	@Test
	public void regTest() {
		TAccount a = new TAccount();

		a.setBornTime(LocalDateTime.of(2010, 2, 12, 13, 51));
		a.setDepartment(3);
		a.setEmployeeName("eject");
		a.setMailbox("191i22@icqq.cn");
		a.setPassword("123");
		a.setPhone("077-8271002021");
		a.setPortrait("../../workspace/eclipse-workspace/map522.jpg");
		a.setRegion(11);
		a.setRole(0);

		try {
			Integer affect = ias.saveAccount(a);
			System.err.println("r=" + affect);
		} catch (ServiceException e) {
			e.getLocalizedMessage();
		}
	}
}
