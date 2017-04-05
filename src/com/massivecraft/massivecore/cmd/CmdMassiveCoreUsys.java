package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCoreMConf;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

public class CmdMassiveCoreUsys extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMassiveCoreUsys i = new CmdMassiveCoreUsys() { public List<String> getAliases() { return MassiveCoreMConf.get().aliasesUsys; } };
	public static CmdMassiveCoreUsys get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverse cmdMassiveCoreUsysMultiverse = new CmdMassiveCoreUsysMultiverse();
	public CmdMassiveCoreUsysUniverse cmdMassiveCoreUsysUniverse = new CmdMassiveCoreUsysUniverse();
	public CmdMassiveCoreUsysWorld cmdMassiveCoreUsysWorld = new CmdMassiveCoreUsysWorld();
	public CmdMassiveCoreUsysAspect cmdMassiveCoreUsysAspect = new CmdMassiveCoreUsysAspect();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsys()
	{
		// Children
		this.addChild(this.cmdMassiveCoreUsysMultiverse);
		this.addChild(this.cmdMassiveCoreUsysUniverse);
		this.addChild(this.cmdMassiveCoreUsysWorld);
		this.addChild(this.cmdMassiveCoreUsysAspect);
		
		// Aliases
		this.addAliases("usys");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.USYS));
	}

}
