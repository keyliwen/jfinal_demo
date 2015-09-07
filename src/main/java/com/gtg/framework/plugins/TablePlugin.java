package com.gtg.framework.plugins;

import java.util.List;

import org.apache.log4j.Logger;

import com.gtg.framework.annotation.Table;
import com.gtg.framework.model.BaseModel;
import com.gtg.framework.utils.ClassSearchUtil;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
/**
 * ɨ��model�ϵ�ע�⣬��model��table
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
			//��ȡע�����
			Table tableBind = (Table)model.getAnnotation(Table.class);
			if (tableBind == null) {
				log.error(model.getName() + "�̳���BaseModel,��û��ע��󶨱���");
				continue;
			}
			
			//��ȡӳ���
			String tableName = tableBind.tableName().trim();
			String pkName = tableBind.pkName().trim();
			if (tableName.equals("") || pkName.equals("")) {
				log.error(model.getName() + "ע����󣬱�����������Ϊ��");
				continue;
			}
			
			//ע��ӳ��
			arp.addMapping(tableName, pkName, model);
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}
	
}
