package com.xcode.xandrdb.Factory;

public class SessionConfig
{
	public SessionConfig()
	{	
	}
	public SessionConfig(String dbname)
	{
		this.DB_Name = dbname;
	}
	public SessionConfig setDBName(String dbName)
	{
		this.DB_Name = dbName;
		return this;
	}
	public String DB_Name = null;
}
