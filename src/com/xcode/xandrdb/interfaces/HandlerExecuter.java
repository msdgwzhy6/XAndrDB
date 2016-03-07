package com.xcode.xandrdb.interfaces;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.app.Application;

/**
 * 定义解析器的接口
 * @author 肖蕾
 */
public interface HandlerExecuter
{
	/**
	 * 
	 * @param handler 	
	 * @param proxy		
	 * @param method	正在代理的方法。作用……用于获取参数，以及返回值的类型。
	 * @param args		方法中传进来的参数
	 * @param application	上下文
	 * @param executeValue	需要解析的自定义sql
	 * @return
	 */
	public Object execute(InvocationHandler handler,Object proxy, Method method, Object[] args,Application application,String executeValue);
}
