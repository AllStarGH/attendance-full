package com.allstar.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @SpringBootApplication： 申明让spring boot自动给程序进行必要的配置，这个配置等同于：
 * 
 * <ul>
 * <li>@Configuration</li>
 * <li>@EnableAutoConfiguration</li>
 * <li>@ComponentScan</li>
 * </ul>
 * 
 * 三个配置。
 * 
 * @author admin
 *
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.allstar.spring.*" })
@MapperScan(basePackages = { "com.allstar.spring.mapper" })
@SpringBootApplication(scanBasePackages = { "com.allstargh.spring" })
public class EnterpriseApplication extends SpringBootServletInitializer {
	public EnterpriseApplication() {
		super();
		setRegisterErrorPageFilter(false);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(EnterpriseApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseApplication.class, args);
	}
}
