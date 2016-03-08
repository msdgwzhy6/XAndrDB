package com.xcode.xandrdb.Executer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xcode.xandrdb.Executer.Core.selectExecuterCore;
import com.xcode.xandrdb.Executer.Util.ExecuterCore;
import com.xcode.xandrdb.Factory.SessionFactory;
import com.xcode.xandrdb.interfaces.HandlerExecuter;

public class SelectExecuter implements HandlerExecuter
{
	@Override
	public Object execute(InvocationHandler handler, Object proxy, Method method, Object[] args,Application session,String annotationValue)
	{
		SQLiteDatabase sqLiteDatabase = null;
		Cursor cursor = null;
		Object object = null;
		try
		{
			String sql = ExecuterCore.getNativeSQl(annotationValue, method, args);
			sqLiteDatabase = session.openOrCreateDatabase(SessionFactory.getConfig().DB_Name, Context.MODE_PRIVATE, null);
			cursor = sqLiteDatabase.rawQuery(sql, null);
			Class<?> returnType = method.getReturnType();
			//在这里需要对返回类型进行判断
			//1.基本类型
			if(returnType.isPrimitive() || returnType.getName().startsWith("java.lang"))
			{
				object = selectExecuterCore.cusorToSimpleClass(cursor, returnType);
			}
			//2.List类型
			else if(returnType.equals(List.class))
			{
				object = selectExecuterCore.cusorToList(cursor, returnType,method);
			}
			//3.数组类型
			else if (returnType.isArray()) 
			{
				object = selectExecuterCore.cusorToArray(cursor, returnType,method);
			}//4.自定义JavaBean
			else 
			{
				object = selectExecuterCore.cusorToJavaBean(cursor, returnType);
			}
			//System.out.println(object);
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally
		{
			if(cursor != null)
			{
				cursor.close();
			}
			if(sqLiteDatabase != null)
			{
				sqLiteDatabase.close();
			}
		}
		return object;
	}
}
