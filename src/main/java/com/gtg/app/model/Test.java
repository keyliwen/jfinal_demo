package com.gtg.app.model;

import com.gtg.framework.annotation.Table;
import com.gtg.framework.model.BaseModel;
import com.jfinal.plugin.activerecord.Model;

@Table(tableName="test",pkName="id")
public class Test extends BaseModel<Test>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final Test dao = new Test();
}
