package com.xcode.xandrdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 *<br/>方法参数的注解
 *<br/>此注解不建议使用，建议使用新注解 Param
 * @author xiaolei
 */
@Deprecated
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface name
{
	public String value();
}
