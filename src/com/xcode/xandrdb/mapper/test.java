package com.xcode.xandrdb.mapper;

import java.util.List;

import com.xcode.xandrdb.User;
import com.xcode.xandrdb.annotation.Create;
import com.xcode.xandrdb.annotation.Delete;
import com.xcode.xandrdb.annotation.Insert;
import com.xcode.xandrdb.annotation.Param;
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
	public String delete(@name("id")int id);
	
	//更新操作
	@Update("UPDATE xiaolei SET sname='xiaolei2', snumber='xiaolei2' WHERE (_id=#{id})")
	public String update(@name("id")int id);
	
	//查询操作
	@Select("SELECT * FROM xiaolei where sname=#{name}")
	public List<User> select(@Param("name")String name);
	
	//新建操作
	@Create("create table #{tabname}(_id integer primary key autoincrement,sname text,snumber text)")
	public String create(@Param("tabname")String tabname);
	
	@Select("select count(*) as num from xiaolei")
	public int getCount();
}
