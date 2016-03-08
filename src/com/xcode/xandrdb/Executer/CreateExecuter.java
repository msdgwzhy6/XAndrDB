package com.xcode.xandrdb.Executer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xcode.xandrdb.Executer.Util.ExecuterCore;
import com.xcode.xandrdb.Factory.SessionFactory;
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
		Cursor cursor = null;
		String tablename = "";
		try
		{
			String sql = ExecuterCore.getNativeSQl(annotationValue, method, args);
			sqLiteDatabase = session.openOrCreateDatabase(SessionFactory.getConfig().DB_Name, Context.MODE_PRIVATE, null);
			String regEx = " *create +table +([a-z_A-Z]+[a-z_A-Z1-9]*|'[a-z_A-Z]+[a-z_A-Z1-9]*')";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(sql.toLowerCase(Locale.getDefault()));
			
			if (m.find())
			{
				String createtable = m.group(0);
				tablename = createtable.substring(createtable.lastIndexOf(" ")+1);
			}
			//在执行新建表之前  首先检查一下在数据库中是否存在这个表
			cursor = sqLiteDatabase.rawQuery("select count(*) from sqlite_master where type='table' and name=?",new String[]{(tablename.startsWith("'")?(tablename.substring(1, tablename.length() - 1)):tablename)});
			if(cursor.moveToNext())
			{
				//如果不存在这个表则新建表
				if(cursor.getInt(0) == 0)
				{
					sqLiteDatabase.execSQL(sql);
					Log.i("XAndrDB", "Create "+tablename+" Success!!!");
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.i("XAndrDB", "Create "+tablename+" Field!!!");
		}finally 
		{
			if(cursor != null)
			{
				cursor.close();
			}
			if (sqLiteDatabase != null)
			{
				sqLiteDatabase.close();
			}
		}
		return null;
	}
}
