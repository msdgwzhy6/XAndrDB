package com.xcode.xandrdb.Factory;
import java.lang.annotation.Annotation;

import com.xcode.xandrdb.Session.SessionApplication;
import com.xcode.xandrdb.interfaces.HandlerExecuter;
import com.xcode.xandrdb.interfaces.Session;
/**
 * SessionFactory 工厂类  专门用于对Session进行管理  避免Session过多滥用的情况
 * @author 肖蕾
 */
public class SessionFactory
{
	private static SessionConfig config = null;
	/**
	 * 选择默认的解析器
	 * @return
	 */
	public static Session getSession()
	{
		return SessionApplication.getInstance();
	}
	public static Session getSession(SessionConfig config)
	{
		setConfig(config);
		return SessionApplication.getInstance();
	}
	public static void setConfig(SessionConfig config)
	{
		SessionFactory.config = config;
	}
	public static SessionConfig getConfig()
	{
		if(config == null || config.DB_Name == null || "".equals(config.DB_Name))
		{
			throw new RuntimeException("You Have No SessionConfig Or Worng SessionConfig……");
		}
		return config;
	}
	
	/**
	 * 注册自己定义的注解，以及解析注解的解析器。
	 * @param annotaClass
	 * @param executer
	 */
	public static void regist(Class<? extends Annotation> annotaClass, Class<? extends HandlerExecuter> executer)
	{
		ExecuterFactory.regist(annotaClass, executer);
	}
}