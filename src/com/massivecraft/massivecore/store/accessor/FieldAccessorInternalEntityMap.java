package com.massivecraft.massivecore.store.accessor;

import com.massivecraft.massivecore.store.InternalEntityMap;

import java.lang.reflect.Field;

public class FieldAccessorInternalEntityMap extends FieldAccessorSimple
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FieldAccessorInternalEntityMap(Field field)
	{
		super(field);
	}
	
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public void set(Object entity, Object val)
	{
		InternalEntityMap entityMap = (InternalEntityMap) this.get(entity);
		InternalEntityMap that = (InternalEntityMap) val;
		
		entityMap.load(that);
	}

}
