package com.genesys.repository;

public class RepositoryObjectIterator
{
	private int _index = 0, _count = 0;
	private RepositoryObjects _objects = null;
	private RepositoryObject _object = null;
	public RepositoryObjectIterator(QueryResponse query)
	{
		_objects = query.getObjects();
		_count = _objects.count();
	}
	public boolean each()
	{
		if( _index == _count ) return false;
		_object = _objects.get(_index++);
		return true;
	}
	public boolean first()
	{
		if( _count == 0 ) return false;
		_object = _objects.get(0);
		return true;
	}
	public RepositoryObject getObj()
	{
		return _object;
	}
}