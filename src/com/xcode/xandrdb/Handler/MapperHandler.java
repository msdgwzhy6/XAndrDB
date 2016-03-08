package com.xcode.xandrdb.Handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import android.util.Log;

import com.xcode.xandrdb.Factory.ExecuterFactory;
import com.xcode.xandrdb.Session.SessionApplication;
import com.xcode.xandrdb.interfaces.HandlerExecuter;

public class MapperHandler implements InvocationHandler
{
	HandlerExecuter mExecuter = null;
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{

		if (Object.class.equals(method.getDeclaringClass()))
		{
			try
			{
				return method.invoke(this, args);
			} catch (Throwable t)
			{
				throw new RuntimeException(t);
			}
		}
		Log.i("XAndrDB","Execute Anotation……");
		mExecuter = ExecuterFactory.getHandler(method);
		if (mExecuter == null)
		{
			return null;
		} else
		{
			//循环去缓存库里面寻找已注册的注解，然后解析出注解内容
			for(Map.Entry<Class<? extends Annotation>, Class<? extends HandlerExecuter>> entry:ExecuterFactory.anotation_store.entrySet())
			{
				Class<? extends Annotation> class1 = entry.getKey();
				if(method.getAnnotation(class1) != null)//
				{
					Annotation a1 = method.getAnnotation(class1);
					Method methods[] = a1.annotationType().getDeclaredMethods();
					return mExecuter.execute(this, proxy, method, args,SessionApplication.getInstance(),(String)methods[0].invoke(a1));
				}
			}
		}
		return null;
	}

}
