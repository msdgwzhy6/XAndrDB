package com.xcode.xandrdb.Executer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xcode.xandrdb.Executer.Util.ExecuterCore;
import com.xcode.xandrdb.Factory.SessionFactory;
import com.xcode.xandrdb.annotation.Create;
import com.xcode.xandrdb.interfaces.HandlerExecuter;
/**
 * 执行新建操作的解析器
 * @author 肖蕾
 */
public class CreateExecuter implements HandlerExecuter
{
	/**
	 * 在这里，将写出来的sql语句，并解析特定的字符，重新组合成正确的sql
	 */
	@Override
	public Object execute(InvocationHandler handler, Object proxy, Method method, Object[] args,Application session,String annotationValue)
	{
		SQLiteDatabase sqLiteDatabase = null;
		try
		{
			String sql = ExecuterCore.getNativeSQl(annotationValue, method, args);
			sqLiteDatabase = session.openOrCreateDatabase(SessionFactory.getConfig().DB_Name, Context.MODE_PRIVATE, null);
			sqLiteDatabase.execSQL(sql);
			Log.i(Create.class.getName(), sql+"\nCreate Success!!!");
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.i(Create.class.getName(), annotationValue+"\nCreate Field!!!");
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
