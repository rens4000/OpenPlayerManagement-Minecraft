package nl.unusu4l.openplayermanagement.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import nl.unusu4l.openplayermanagement.managers.SettingsManager;

public class OPMLogger {
	
	private ConsoleCommandSender consoleSender;
	
	public OPMLogger(ConsoleCommandSender consoleSender) {
		this.consoleSender = consoleSender;
	}
	
	public final void info(String log) {
		consoleSender.sendMessage("[INFO] [OpenPlayerManagement] " + log);
	}
	
	public final void severe(String log) {
		consoleSender.sendMessage(ChatColor.RED + "[ERROR] " + ChatColor.WHITE + "[OpenPlayerManagement] " + ChatColor.RED + log);
	}
	
	public final void highInfo(String log) {
		if(!SettingsManager.get().getBoolean("extralogmode")) return;
		consoleSender.sendMessage("[EXTRA-INFO] [OpenPlayerManagement] " + log);
	}

}
