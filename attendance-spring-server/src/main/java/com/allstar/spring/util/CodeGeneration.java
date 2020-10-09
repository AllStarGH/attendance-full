package com.allstar.spring.util;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 演示例子，执行main方法自动生成对应项目目录
 * 
 * @author admin
 *
 */
public class CodeGeneration {
	/**
	 * String projectPath = "D:/project"; <em>windows下生成文件的位置之项目前缀;</em><br>
	 * <i>生成文件的位置之项目前缀</i>
	 */
	private static String project_path = "/home/admin/workspace/eclipse/eclipse-workspace/attendance";

	/**
	 * 作者名
	 */
	private static String author = "gzh";

	/**
	 * 模块名,可空
	 */
	private static String module_name = "";

	/**
	 * 外层包名
	 */
	private static String package_name = "com.allstargh.spring";

	/**
	 * 数据源地址
	 */
	private static String data_source_uri = "jdbc:mysql://localhost:3306/attendance?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";

	public static void main(String[] args) {
		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();

		gc.setOutputDir(project_path + "/src/main/java");
		gc.setFileOverride(false);// 文件是否覆盖
		gc.setAuthor(author); // 生成文件的作者名称
		gc.setOpen(false);
		gc.setActiveRecord(true);
		gc.setEnableCache(false);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(false);// XML columList
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl(data_source_uri);

		// 设置数据库驱动,因为我的数据库版本是mysql5.7,所以使用该驱动
		dsc.setDriverName("com.mysql.cj.jdbc.Driver");

		// mysql5.6以下的驱动
		// dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername("root"); // 数据库用户名
		dsc.setPassword("root"); // 数据库密码
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(module_name);
		pc.setParent(package_name);
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// DO NOTHING
			}
		};

		List<FileOutConfig> focList = new ArrayList<>();

		focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输入文件名称
				return project_path + "/src/main/resources/mapper/" + pc.getModuleName() + "/"
						+ tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
			}
		});
		cfg.setFileOutConfigList(focList);

		mpg.setCfg(cfg);
		mpg.setTemplate(new TemplateConfig().setXml(null));

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();

		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
		mpg.execute();

		System.err.println("GENERATION SUCCESS");
	}
}