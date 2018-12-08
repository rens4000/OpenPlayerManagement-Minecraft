package nl.unusu4l.openplayermanagement.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import nl.unusu4l.openplayermanagement.mysql.OPMMysql;
import nl.unusu4l.openplayermanagement.utils.UserUtils;

public class ChangePasswordCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Checks if the sender is a player. If it is not. Return.
		if(!(sender instanceof Player)) {
			sender.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You need to be a player to perform this command.");
			return false;
		}
		
		Player player = (Player) sender;
		
		// Checks if the player already has logged in.
		if(!UserUtils.isLoggedin(player.getUniqueId())) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You need to be logged in for that.");
			return true;
		}
		
		if(args.length != 3) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You are using the command in the wrong way! Do: /changepassword <old-password> <password> <password>.");
			return true;
		}
		
		String oldPassword = args[0];
		String password = args[1];
		String passwordConfirm = args[2];
		
		if(!OPMMysql.passwordMatches(player.getUniqueId(), oldPassword)) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "The old password was incorrect. Please try again.");
			return false;
		}
		
		if(!password.equals(passwordConfirm)) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "The password doesn't match the confirm password. Make sure that they are the same.");
			return false;
		}
		
		OPMMysql.changePassword(player.getUniqueId(), password);
		player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.GREEN + "Your password has been changed.");
		
		return true;
	}

}
