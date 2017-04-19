package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveSet;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class InternalEntityMap<E extends EntityInner> implements EntityContainer<E>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected transient WeakReference<EntityChild<?>> entity = new WeakReference<>(null);
	protected void setEntity(EntityChild<?> entity) { this.entity = new WeakReference<EntityChild<?>>(entity); }
	public EntityChild<?> getEntity() { return this.entity.get(); }
	
	public boolean isLive()
	{
		EntityChild<?> entity = this.getEntity();
		if (entity == null) return false;
		
		if ( ! entity.isLive()) return false;
		
		return true;
	}
	
	private MassiveMap<String, E> contents = new MassiveMap<>();
	public MassiveMap<String, E> getContents() { return contents; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private InternalEntityMap()
	{
		this(null);
	}
	
	public InternalEntityMap(EntityChild<?> entity)
	{
		this.setEntity(entity);
	}
	
	// -------------------------------------------- //
	// REFERENCE
	// -------------------------------------------- //
	
	@Override
	public void changed(String id)
	{
		if ( ! this.isLive()) return;
		
		//System.out.println(this.getColl().getName() + ": " +this.getId() + " was modified locally");
		
		// UNKNOWN is very unimportant really.
		// LOCAL_ATTACH is for example much more important and should not be replaced.
		this.getEntity().changed();
	}
	
	@Override
	public Coll<?> getColl()
	{
		return this.getEntity().getEntityContainer().getColl();
	}
	
	// -------------------------------------------- //
	// LOAD
	// -------------------------------------------- //
	
	public InternalEntityMap<E> load(InternalEntityMap<E> that)
	{
		if (that == null) throw new NullPointerException("that");
		
		// Loop over all the entities in that
		for (Entry<String, E> entry : that.contents.entrySet())
		{
			String id = entry.getKey();
			E entity = entry.getValue();
			
			E current = this.contents.get(id);
			if (current != null)
			{
				// Load if present
				current.load(entity);
			}
			else
			{
				// attach if not present
				this.attach(entity, id);
			}
		}
		
		// Clean entities of those that are not in that
		if (this.contents.size() != that.contents.size())
		{
			// Avoid CME
			Set<Entry<String, E>> removals = new MassiveSet<>();
			
			// Loop over all current entries ...
			for (Iterator<Entry<String, E>> it = this.contents.entrySet().iterator(); it.hasNext(); )
			{
				Entry<String, E> entry = it.next();
				String id = entry.getKey();
				
				// ... if it is not present in those ...
				if (that.contents.containsKey(id)) continue;
				
				// ... remove.
				removals.add(entry);
			}
			
			// Remove
			for (Entry<String, E> removal : removals)
			{
				this.detachFixed(removal.getValue(), removal.getKey());
			}
		}
		
		return this;
	}
	
	// -------------------------------------------- //
	// INNER ENTITIES
	// -------------------------------------------- //
	
	protected String fixId(Object oid)
	{
		if (oid == null) return null;
		
		if (oid instanceof String) return (String) oid;
		
		return null;
	}
	
	public String attach(E entity)
	{
		return this.attach(entity, null);
	}
	
	@Override
	public String attach(E entity, Object oid)
	{
		// Check entity
		if (entity == null) throw new NullPointerException("entity");
		
		if (entity.attached()) return entity.getId();
		
		String id;
		// Check/Fix id
		if (oid == null)
		{
			id = MStore.createId();
		}
		else
		{
			id = this.fixId(oid);
			if (id == null) return null;
			if (this.contents.containsKey(id)) return null;
		}
		
		entity.preAttach(id);
		
		// Add entity reference info
		entity.setEntityContainer(this);
		entity.setId(id);
		
		// Attach
		this.contents.put(id, entity);
		
		// POST
		entity.postAttach(id);
		
		return id;
	}
	
	@Override
	public E detachEntity(E entity)
	{
		if (entity == null) throw new NullPointerException("entity");
		
		String id = entity.getId();
		this.detachFixed(entity, id);
		return entity;
	}
	
	@Override
	public E detachId(Object oid)
	{
		return this.detachIdFixed(this.fixId(oid));
	}
	
	@Override
	public E detachIdFixed(String id)
	{
		if (id == null) throw new NullPointerException("id");
		
		E e = this.contents.get(id);
		if (e == null) return null;
		
		this.detachFixed(e, id);
		return e;
	}
	
	private void detachFixed(EntityInner entity, String id)
	{
		if (entity == null) throw new NullPointerException("entity");
		if (id == null) throw new NullPointerException("id");
		
		// PRE
		entity.preDetach(id);
		
		// Remove @ local
		this.contents.remove(id);
		
		// Identify Modification
		entity.changed();
		
		// POST
		entity.postDetach(id);
	}
	
}
