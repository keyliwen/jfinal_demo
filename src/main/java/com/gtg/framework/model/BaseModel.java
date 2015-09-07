package com.gtg.framework.model;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Model;
/**
 * Model基础类，用于其他Model类的继承，所有Model共有方法写在此类中
 * @author keyliwen
 *
 * @param <M>
 */
public abstract class BaseModel<M extends Model<M>> extends Model<M>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(BaseModel.class);
}
