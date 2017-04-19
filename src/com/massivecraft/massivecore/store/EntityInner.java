package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.accessor.Accessor;
import com.massivecraft.massivecore.xlib.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Usage of this class is highly optional. You may persist anything. If you are
 * creating the class to be persisted yourself, it might be handy to extend this
 * Entity class. It just contains a set of shortcut methods.
 */

// Self referencing generic.
// http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ206
public class EntityInner<E extends EntityInner<E>> implements EntityChild<E>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected transient WeakReference<EntityContainer<E>> parent = new WeakReference<>(null);
	protected void setEntityContainer(EntityContainer<E> parent)
	{
		this.parent = new WeakReference<>(parent);
	}
	@Override public EntityContainer<E> getEntityContainer()
	{
		return this.parent.get();
	}
	
	public Coll<?> getColl()
	{
		return this.getEntityContainer().getColl();
	}
	
	protected transient String id;
	protected void setId(String id)
	{
		this.id = id;
	}
	@Override public String getId()
	{
		return this.id;
	}
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	@Override
	public boolean attached()
	{
		return this.getEntityContainer() != null && this.getId() != null;
	}
	
	@Override
	public boolean detached()
	{
		return !this.attached();
	}
	
	
	@Override
	public void preAttach(String id)
	{
		
	}
	
	@Override
	public void postAttach(String id)
	{
		
	}
	
	@Override
	public void preDetach(String id)
	{
		
	}
	
	@Override
	public void postDetach(String id)
	{
		
	}
	
	// -------------------------------------------- //
	// SYNC AND IO ACTIONS
	// -------------------------------------------- //
	
	@Override
	public boolean isLive()
	{
		String id = this.getId();
		if (id == null) return false;
		
		EntityContainer<E> parent = this.getEntityContainer();
		if (parent == null) return false;
		
		if (!parent.isLive()) return false;
		
		return true;
	}
	
	@Override
	public void changed()
	{
		if (!this.isLive()) return;
		
		//System.out.println(this.getColl().getName() + ": " +this.getId() + " was modified locally");
		
		// UNKNOWN is very unimportant really.
		// LOCAL_ATTACH is for example much more important and should not be replaced.
		this.getEntityContainer().changed(this.getId());
	}
	
	// -------------------------------------------- //
	// DERPINGTON
	// -------------------------------------------- //
	
	@Override
	@SuppressWarnings("unchecked")
	public E load(E that)
	{
		Accessor.get(this.getClass()).copy(that, this);
		return (E) this;
	}
	
	// -------------------------------------------- //
	// CONVENIENCE: DATABASE
	// -------------------------------------------- //
	
	// GENERIC
	public <T> T convertGet(T value, T standard)
	{
		return value != null ? value : standard;
	}
	
	public <T> T convertSet(T value, T standard)
	{
		this.changed();
		return Objects.equals(value, standard) ? null : value;
	}
	
	// BOOLEAN
	public boolean convertGet(Boolean value)
	{
		return convertGet(value, false);
	}
	
	public Boolean convertSet(Boolean value)
	{
		return convertSet(value, false);
	}
	
	// -------------------------------------------- //
	// STANDARDS
	// -------------------------------------------- //
	
	@Override
	public String toString()
	{
		Gson gson = MassiveCore.gson;
		Coll<?> coll = this.getColl();
		if (coll != null) gson = coll.getGson();
		
		return this.getClass().getSimpleName() + gson.toJson(this, this.getClass());
	}
	
}
