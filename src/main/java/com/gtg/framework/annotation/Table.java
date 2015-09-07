package com.gtg.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ���ݿ��ע��
 * @author keyliwen
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {
	/**
	 * ������
	 * @return
	 */
	String tableName();
	/**
	 * ������
	 * @return
	 */
	String pkName();
}
