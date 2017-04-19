package com.massivecraft.massivecore.store;

public interface EntityContainer<E>
{
	String attach(E entity);
	String attach(E entity, Object oid);
	
	E detachEntity(E entity);
	E detachId(Object oid);
	E detachIdFixed(String id);
	
	boolean isLive();
	
	void changed(String id);
	
	Coll<?> getColl();
}
