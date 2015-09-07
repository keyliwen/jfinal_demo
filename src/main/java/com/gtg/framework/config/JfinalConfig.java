package com.gtg.framework.config;

import org.apache.log4j.Logger;

import com.gtg.app.controller.HelloController;
import com.gtg.app.model.Test;
import com.gtg.framework.common.DictKeys;
import com.gtg.framework.plugins.PropertiesPlugin;
import com.gtg.framework.plugins.TablePlugin;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

public class JfinalConfig extends JFinalConfig{

	private static Logger log = Logger.getLogger(JfinalConfig.class);
	
	@Override
	public void configConstant(Constants me) {
		log.info("jfinal 配置开始");
		log.info("configConstant缓存properties");
		new PropertiesPlugin(loadPropertyFile("init.properties")).start();
		log.info("configConstant设置是否开发模式");
		me.setDevMode(getPropertyToBoolean(DictKeys.config_devMode));
	}

	@Override
	public void configHandler(Handlers me) {
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		
	}

	@Override
	public void configPlugin(Plugins me) {
		log.info("configPlugin配置Druid数据库连接属性");
		DruidPlugin druidPlugin = new DruidPlugin(
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connnection_url),
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_username),
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_password),
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_driverClass));
		log.info("configPlugin配置Druid数据库连接池大小");
		druidPlugin.set((int)PropertiesPlugin.getParamMapValue(DictKeys.db_initialSize),
				(int)PropertiesPlugin.getParamMapValue(DictKeys.db_minIdle),
				(int)PropertiesPlugin.getParamMapValue(DictKeys.db_maxActive));
		log.info("configPlugin配置ActiveRecord插件");
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setDevMode(getPropertyToBoolean(DictKeys.config_devMode,false));
		arp.setShowSql(getPropertyToBoolean(DictKeys.config_showSql,false));
		arp.setDialect(new MysqlDialect());
		log.info("configPlugin添加Druid");
		me.add(druidPlugin);
		log.info("configPlugin注册表");
		new TablePlugin(arp).start();
		log.info("configPlugin添加ActiveRecord");
		me.add(arp);
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/hello", HelloController.class);
	}
	
}
