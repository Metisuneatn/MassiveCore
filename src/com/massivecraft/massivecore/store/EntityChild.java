package com.massivecraft.massivecore.store;

import com.massivecraft.massivecore.Identified;

public interface EntityChild<E> extends Identified
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	EntityContainer<E> getEntityContainer();
	
	// -------------------------------------------- //
	// ATTACH AND DETACH
	// -------------------------------------------- //
	
	boolean attached();
	boolean detached();
	
	void preAttach(String id);
	void postAttach(String id);
	void preDetach(String id);
	void postDetach(String id);
	
	// -------------------------------------------- //
	// SYNC AND IO ACTIONS
	// -------------------------------------------- //
	
	boolean isLive();
	void changed();
	
	// -------------------------------------------- //
	// DERPINGTON
	// -------------------------------------------- //
	
	E load(E that);
	
}
