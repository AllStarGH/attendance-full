package com.allstar.spring.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 时间方法工具类
 * </pre>
 * 
 * @author admin
 *
 */
@Slf4j
public class DateTimeUtils {
	private static DateTimeUtils single = new DateTimeUtils();

	private DateTimeUtils() {

	}

	/**
	 * 饿汉
	 * 
	 * @return
	 */
	public static DateTimeUtils getInstance() {
		return DateTimeUtils.single;
	}

	/**
	 * <pre>
	 * string转local date time
	 * </pre>
	 * 
	 * @param timeStr
	 * @return
	 */
	public LocalDateTime StrTransformLocalDateTime(String timeStr) {
		final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				.appendPattern("yyyy-MM-dd[['T'HH][:mm][:ss]]").parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
				.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
				.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0).toFormatter();

		final LocalDateTime localDateTime = LocalDateTime.parse(timeStr, formatter);
		log.info("local date time===" + localDateTime);

		return localDateTime;
	}

	/**
	 * 
	 * @param timeStr
	 * @return
	 */
	public String getSubTimeStr(String timeStr) {
		String subTimeStr = null;
		Integer i = null;
		
		try {
			i = timeStr.indexOf("T");
			subTimeStr = timeStr.substring(0, i);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error(e.getLocalizedMessage());
			subTimeStr = LocalDateTime.now().toString();
			
			i = subTimeStr.indexOf("T");
			subTimeStr = subTimeStr.substring(0, i);
		}

		log.info("substring local date time===" + subTimeStr);
		return subTimeStr;
	}

//	public static void main(String[] args) {
//		DateTimeUtils utils = new DateTimeUtils();
//		String subTimeStr = utils.getSubTimeStr(null);
//		String timeStr="2020-02-25T04:33:39.000Z";
//		int i = timeStr.indexOf("T");
//		String substring = timeStr.substring(0, i);
//		LocalDateTime localDateTime = utils.StrTransformLocalDateTime(substring);
//		System.out.println(localDateTime);
//	}
}