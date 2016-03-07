package com.xcode.xandrdb;

import java.util.List;

import com.xcode.xandrdb.Factory.SessionConfig;
import com.xcode.xandrdb.Factory.SessionFactory;
import com.xcode.xandrdb.interfaces.Session;
import com.xcode.xandrdb.mapper.test;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Session session = SessionFactory.getSession(new SessionConfig().setDBName("xiaolei"));
		test t1 = session.getMapper(test.class);
		//t1.create("xiaolei");
		t1.insert();
		List<User> users = t1.select("xiaolei");
		users.add(new User());
		int count = t1.getCount();
		System.out.println(users);
	}
}
