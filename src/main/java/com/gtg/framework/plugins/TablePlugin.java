package com.gtg.framework.plugins;

import java.util.List;

import org.apache.log4j.Logger;

import com.gtg.framework.annotation.Table;
import com.gtg.framework.model.BaseModel;
import com.gtg.framework.utils.ClassSearchUtil;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
/**
 * 扫描model上的注解，绑定model和table
 * @author keyliwen
 *
 */
public class TablePlugin implements IPlugin{
	
	protected final Logger log = Logger.getLogger(TablePlugin.class);
	
	private ActiveRecordPlugin arp;
	
	public TablePlugin(ActiveRecordPlugin arp) {
		this.arp = arp;
	}
	
	@SuppressWarnings({"rawtypes","unchecked"})
	@Override
	public boolean start() {
		List<Class<? extends BaseModel>> modelClasses = ClassSearchUtil.of(BaseModel.class).search();
		for (Class model : modelClasses) {
			//获取注解对象
			Table tableBind = (Table)model.getAnnotation(Table.class);
			if (tableBind == null) {
				log.error(model.getName() + "继承了BaseModel,但没有注解绑定表名");
				continue;
			}
			
			//获取映射表
			String tableName = tableBind.tableName().trim();
			String pkName = tableBind.pkName().trim();
			if (tableName.equals("") || pkName.equals("")) {
				log.error(model.getName() + "注解错误，表名或主键名为空");
				continue;
			}
			
			//注册映射
			arp.addMapping(tableName, pkName, model);
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}
	
}
