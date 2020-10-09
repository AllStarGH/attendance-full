package com.allstar.spring.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

/**
 * @Configuration 标注在类上，相当于把该类作为spring的xml配置文件中的"beans",<br>
 *                作用为：配置spring容器(应用上下文)
 * 
 * @author admin
 *
 */
@Configuration
public class MyBatisPlusConfiguration {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private MybatisProperties properties;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	@Autowired(required = false)
	private Interceptor[] interceptors;

	@Autowired(required = false)
	private DatabaseIdProvider databaseIdProvider;

	/**
	 * mybatis-plus分页插件<i>for MySQL</i>
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor page = new PaginationInterceptor();
		page.setDialectType("mysql");
		return page;
	}

	/**
	 * 这里全部使用mybatis-autoconfigure已经自动加载的资源。<br>
	 * 不手动指定,配置文件和mybatis-boot的配置文件同步
	 * 
	 * @return
	 */
	@Bean
	public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
		// 导入mybatis配置
		MybatisConfiguration configuration = new MybatisConfiguration();

		MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
		mybatisPlus.setDataSource(dataSource);
		mybatisPlus.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			mybatisPlus.setPlugins(this.interceptors);
		}
		MybatisConfiguration mc = new MybatisConfiguration();
		mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);

		// 数据库字段设计为驼峰命名，默认开启的驼峰转下划线会报错字段找不到
		mc.setMapUnderscoreToCamelCase(true);

		// 配置打印sql语句
		configuration.setLogImpl(StdOutImpl.class);
		mybatisPlus.setConfiguration(configuration);

		mybatisPlus.setConfiguration(mc);

		if (this.databaseIdProvider != null) {
			mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
		}
		return mybatisPlus;
	}

	/**
	 * 打印sql
	 */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
		// 格式化sql语句
		Properties properties = new Properties();
		properties.setProperty("format", "false");
		performanceInterceptor.setProperties(properties);
		return performanceInterceptor;
	}
}