package com.gtg.framework.model;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Model;
/**
 * Model�����࣬��������Model��ļ̳У�����Model���з���д�ڴ�����
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
