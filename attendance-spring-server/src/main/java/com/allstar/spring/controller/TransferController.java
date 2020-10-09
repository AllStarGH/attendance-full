package com.allstar.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.allstar.spring.controller.kit.BaseActionUtil;
import com.allstar.spring.notation.Json;

@Controller
@RequestMapping("/TransferController")
public class TransferController extends BaseActionUtil {
	@Override
	protected String parameterMark(Object... args) {
		return super.parameterMark(args);
	}

	/*
	 * 下方为访问测试
	 */

	/**
	 * http://localhost:8080/attendance/TransferController/testNginxAction
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("testNginxAction")
	public Map<String, Object> testNginxAction() {
		System.err.println(this.getClass().getSimpleName());

		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("clz", TransferController.class);
		map.put("method", "testNginxAction");
		map.put("now_time", TransferController.now_time);

		return map;
	}

	/**
	 * http://localhost:8080/attendance/TransferController/indexAction
	 * 
	 * @return
	 */
	@RequestMapping("indexAction")
	public String indexAction() {
		String clzName = this.getClass().getName();
		System.err.println(clzName);
		System.out.println("indexAction");
		return "index";
	}

	/**
	 * http://localhost:8080/attendance/TransferController/testResultAction
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("testResultAction")
	public Json<String[]> testResultAction() {
		String[] strs = new String[] {};
		String parameterMark = parameterMark(TransferController.class);

		strs[0] = "11-a";
		strs[1] = "22-b";
		strs[2] = "33-c";
		strs[3] = "44-d";
		strs[4] = "55-e";
		strs[5] = "00-f";

		return new Json<String[]>(666, parameterMark, strs);
	}

}
