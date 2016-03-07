package com.xcode.xandrdb.Executer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xcode.xandrdb.Executer.Util.ExecuterCore;
import com.xcode.xandrdb.Factory.SessionFactory;
import com.xcode.xandrdb.interfaces.HandlerExecuter;
/**
 * 执行插入操作的解析器
 * @author 肖蕾
 */
public class InsertExecuter implements HandlerExecuter
{
	@Override
	public Object execute(InvocationHandler handler, Object proxy, Method method, Object[] args,Application session,String annotationValue)
	{
		SQLiteDatabase sqLiteDatabase = null;
		try
		{
			String sql = ExecuterCore.getNativeSQl(annotationValue, method, args);
			sqLiteDatabase = session.openOrCreateDatabase(SessionFactory.getConfig().DB_Name, Context.MODE_PRIVATE, null);
			sqLiteDatabase.execSQL(sql);
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally 
		{
			if (sqLiteDatabase != null)
			{
				sqLiteDatabase.close();
			}
		}
		return null;
	}
}
