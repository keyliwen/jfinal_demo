package com.gtg.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.gtg.common.DictKeys;
import com.jfinal.plugin.IPlugin;
/**
 * 读取properties配置文件数据放入缓存
 * @author keyliwen
 *
 */
public class PropertiesPlugin implements IPlugin{
	
	private static Logger log = Logger.getLogger(PropertiesPlugin.class);
	/**
	 * 保存配置数据
	 */
	private static Map<String, Object> paramMap = new HashMap<String, Object>();
	
	private Properties properties;
	
	public PropertiesPlugin(Properties properties) {
		this.properties = properties;
	}
	
	public static Object getParamMapValue(String key) {
		return paramMap.get(key);
	}  
	@Override
	public boolean start() {
		//读取常用参数信息
		log.info("propertiesPlugin读取常用参数信息");
		paramMap.put(DictKeys.config_devMode, properties.getProperty(DictKeys.config_devMode));
		//读取数据库连接信息
		log.info("propertiesPlugin读取数据库连接信息");
		paramMap.put(DictKeys.db_connection_driverClass, properties.getProperty(DictKeys.db_connection_driverClass).trim());
		paramMap.put(DictKeys.db_connection_username, properties.getProperty(DictKeys.db_connection_username).trim());
		paramMap.put(DictKeys.db_connection_password, properties.getProperty(DictKeys.db_connection_password).trim());
		paramMap.put(DictKeys.db_connnection_url, properties.getProperty(DictKeys.db_connnection_url).trim());
		//读取数据库连接池信息
		log.info("propertiesPlugin读取连接池信息");
		paramMap.put(DictKeys.db_initialSize, Integer.parseInt(properties.getProperty(DictKeys.db_initialSize).trim()));
		paramMap.put(DictKeys.db_maxActive, Integer.parseInt(properties.getProperty(DictKeys.db_maxActive).trim()));
		paramMap.put(DictKeys.db_minIdle, Integer.parseInt(properties.getProperty(DictKeys.db_minIdle).trim()));
		return false;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
