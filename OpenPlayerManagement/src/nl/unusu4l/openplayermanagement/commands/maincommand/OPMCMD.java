package nl.unusu4l.openplayermanagement.commands.maincommand;

import org.bukkit.ChatColor;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;

public abstract class OPMCMD {
	
	private String name;
	private String usage;
	private String description;
	private String permission;
	
	public OPMCMD(String name, String usage, String description, String permission) {
		this.name = name;
		this.usage = usage;
		this.description = description;
		this.permission = permission;
	}
	
	abstract void run();

	public String getName() {
		return name;
	}
	
	public String toString() {
		return OpenPlayerManagement.PREFIX + ChatColor.AQUA + usage + ChatColor.GOLD + " - " + ChatColor.GREEN + description + ChatColor.WHITE + ".";
	}
	
	public String getUsage() {
		return usage;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getPermission() {
		return permission;
	}
	 
}
