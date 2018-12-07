package nl.unusu4l.openplayermanagement.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import nl.unusu4l.openplayermanagement.mysql.OPMMysql;
import nl.unusu4l.openplayermanagement.utils.UserUtils;

public class RegisterCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Checks if sender is player.
		if(!(sender instanceof Player)) {
			sender.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You need to be a player to perform this command.");
			return false;
		}
		
		Player player = (Player) sender;
		
		// Checks if player is logged in,
		if(UserUtils.isLoggedin(player.getUniqueId())) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.GOLD + "You are already logged in with your account. You can't register a new one!");
			return true;
		}
		
		// Checks if the player has been registered already.
		if(!UserUtils.getNotRegistered().contains(player.getUniqueId())) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "Your account has already been registered!");
			return true;
		}
		
		// Makes sure that the player uses the command in the right way.
		if(args.length != 2) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You are using the command in the wrong way! Do: /register <password> <password>");
			return true;
		}
		
		// Temporarily stores the passwords in a variable. 
		String password = args[0];
		String confirmPassword = args[1];
		
		// Makes sure that the confirm password is the same as password.
		if(!password.equals(confirmPassword)) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "The passwords did not match!");
			return true;
		}
		
		// Creates the user in the database.
		OPMMysql.createUser(player.getUniqueId(), password);
		
		// Removes the player from the not registered list.
		UserUtils.getNotRegistered().remove(player.getUniqueId());
		
		// Logs the player in.
		UserUtils.getLoggedIn().put(player.getUniqueId(), true);
		
		return false;
	}

}
