#XAndrDB
安卓端开源数据库ORM操作框架。

##使用说明：
在使用本框架之前首先
将本项目根目录下的XAndrDB.jar引入到你的项目中，然后。
在你的app的AndroidManifest.xml中配置：

	<application
	android:name="com.xcode.xandrdb.Session.SessionApplication"
	……
	>
####这时候就该考虑使用问题了。
不多说，直接贴代码：

	package com.xcode.xandrdb.mapper;
	
	import com.xcode.xandrdb.User;
	import com.xcode.xandrdb.annotation.Create;
	import com.xcode.xandrdb.annotation.Delete;
	import com.xcode.xandrdb.annotation.Insert;
	import com.xcode.xandrdb.annotation.Select;
	import com.xcode.xandrdb.annotation.Update;
	import com.xcode.xandrdb.annotation.name;
	
	public interface test
	{
		//插入的操作
		@Insert("INSERT INTO xiaolei ('sname', 'snumber') VALUES ('xiaolei', 'xiaolei')")
		public String insert();
		
		//删除操作
		@Delete("delete from xiaolei where id = #{id}")
		public String delete(@Param("id")int id);
		
		//更新操作
		@Update("UPDATE xiaolei SET sname='xiaolei2', snumber='xiaolei2' WHERE (_id=#{id})")
		public String update(@Param("id")int id);
		
		//查询操作
		@Select("SELECT * FROM xiaolei WHERE _id = #{id}")
		public User[] select(@Param("id")int id);
		
		//新建操作
		@Create("create table #{user.name}(_id integer primary key autoincrement,sname text,snumber text)")
		public String create(@Param("user")User user);
	}
这里只是定义了一系列的数据库操作action的Mapper。那我们看看怎么使用这些Mapper：

		Session session = SessionFactory.getSession(new SessionConfig().setDBName("xiaolei"));
		test t1 = session.getMapper(test.class);
		User users[] = t1.select(1);
		System.out.println(users);

是的，所有的数据库操作都必须使用Session对象，去操作。然后使用Session对象拿到mapper，内部通过动态代理操作返回你一个mapper对象，然后你操作你的mapper里面定义的方法就是在操作数据库了。

###这是一个完全面向切面，使用 自定义注解+反射+动态代理 结合在一起发挥强大功能的数据库框架。


##曾经..
	曾经有一份至真嘅爱情摆喺我面前，
	但我冇去珍惜，
	到冇咗嘅时候先至后悔莫及，
	尘世间最痛苦莫过于此。
	如果个天可以畀个机会我返转头嘅话，我会同个女仔讲我爱佢！
	如果 系都要喺呢份爱加上一个期限，
	我希望系一万年！
