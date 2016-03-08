package com.xcode.xandrdb.Session;

import java.util.HashMap;
import java.util.Map;

import com.xcode.xandrdb.Factory.HandlerFactory;
import com.xcode.xandrdb.Factory.SessionFactory;
import com.xcode.xandrdb.interfaces.Session;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SessionApplication extends Application implements Session
{
	private static SessionApplication mSession = null;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mSession = this;
		Log.i("XAndrDB", "XAndrDB'S Inited...");
	}

	public static SessionApplication getInstance()
	{
		if (mSession != null)
		{
			SQLiteDatabase database = mSession.openOrCreateDatabase(SessionFactory.getConfig().DB_Name, Context.MODE_PRIVATE, null);
			database.close();
		}
		return mSession;
	}

	private Map<Class<?>, HandlerFactory> factorys = new HashMap<Class<?>, HandlerFactory>();

	/**
	 * 根据传入的类型，寻找对应的InvocationHandler
	 */
	@Override
	public <T> T getMapper(Class<T> type)
	{
		HandlerFactory factory = factorys.get(type);
		// 如果获取到的对象是null则给他新建一个 然后再保存
		if (factory == null)
		{
			factory = new HandlerFactory(type);
			factorys.put(type, factory);
		}
		return factory.newInstance();
	}
}
