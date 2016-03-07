package com.xcode.xandrdb.Factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import com.xcode.xandrdb.Handler.MapperHandler;

public class HandlerFactory
{
	/**
	 * 通过传过来的type，来选择不同的executer执行器
	 * @param type
	 */
	public HandlerFactory(Class<?> type)
	{
		this.type = type;
		handler = new MapperHandler();
	}
	private InvocationHandler handler;
	private Class<?> type;
	@SuppressWarnings("unchecked")
	public <T> T newInstance()
	{
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
	}
}
