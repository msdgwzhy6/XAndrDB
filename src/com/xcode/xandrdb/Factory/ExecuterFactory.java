package com.xcode.xandrdb.Factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.xcode.xandrdb.Executer.CreateExecuter;
import com.xcode.xandrdb.Executer.DeleteExecuter;
import com.xcode.xandrdb.Executer.InsertExecuter;
import com.xcode.xandrdb.Executer.SelectExecuter;
import com.xcode.xandrdb.Executer.UpdateExecuter;
import com.xcode.xandrdb.annotation.Create;
import com.xcode.xandrdb.annotation.Delete;
import com.xcode.xandrdb.annotation.Insert;
import com.xcode.xandrdb.annotation.Select;
import com.xcode.xandrdb.annotation.Update;
import com.xcode.xandrdb.interfaces.HandlerExecuter;

/**
 * 由这个类来对所有的执行器进行统一的管理，也方便以后的扩展
 * 
 * @author xiaolei
 */
public class ExecuterFactory
{
	private static Map<Method, HandlerExecuter> executers = new HashMap<Method, HandlerExecuter>();
	/**
	 * 执行器的库，用这个主要是可以定义自己的解析器库。
	 */
	public static Map<Class<? extends Annotation>, Class<? extends HandlerExecuter>> anotation_store = new HashMap<Class<? extends Annotation>, Class<? extends HandlerExecuter>>();

	static//注册默认的注解，以及对应的解析器
	{
		anotation_store.put(Insert.class, InsertExecuter.class);
		anotation_store.put(Select.class, SelectExecuter.class);
		anotation_store.put(Update.class, UpdateExecuter.class);
		anotation_store.put(Delete.class, DeleteExecuter.class);
		anotation_store.put(Create.class, CreateExecuter.class);
	}
	/**
	 * 通过这个方法可以自己注册自己的注解，以及自己的注解解释器
	 */
	public static void regist(Class<? extends Annotation> annotaClass, Class<? extends HandlerExecuter> executer)
	{
		anotation_store.put(annotaClass, executer);
	}
	/**
	 * 从执行器库中直接拿取，如果没有，则根据一系列规则自己去建立
	 * @param method
	 * @return
	 */
	public static HandlerExecuter getHandler(Method method)
	{
		HandlerExecuter executer = executers.get(method);
		if(executer != null)
		{
			return executer;
		}
		Annotation annotations[] = method.getAnnotations();
		Class<?> value = null;
		for (int i = 0; i < annotations.length; i++)
		{
			Annotation annotation = annotations[i];
			Class<?> type = annotation.annotationType();
			value = anotation_store.get(type);
			if (value != null)
			{
				break;
			}
		}
		if (value != null)
		{
			try
			{
				executer = (HandlerExecuter) value.newInstance();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			executers.put(method, executer);
		}
		return executer;
	}
}
