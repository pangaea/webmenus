package com.genesys.repository.sql;

public class QueryProperty
{
	private String name;
	private StatementBuilder.PropType type;
	
	public QueryProperty(String name, StatementBuilder.PropType type)
	{
		this.name = name;
		this.type = type;
	}
	public String getName()
	{
		return this.name;
	}
	public StatementBuilder.PropType getType()
	{
		return this.type;
	}
}