package com.gtg.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 数据库表注解
 * @author keyliwen
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {
	/**
	 * 表名称
	 * @return
	 */
	String tableName();
	/**
	 * 表主键
	 * @return
	 */
	String pkName();
}
