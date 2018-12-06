package nl.unusu4l.openplayermanagement.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import nl.unusu4l.openplayermanagement.mysql.OPMMysql;
import nl.unusu4l.openplayermanagement.utils.UserUtils;

public class LoginCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You need to be a player to perform this command.");
			return false;
		}
		Player player = (Player) sender;
		if(UserUtils.isLoggedin(player.getUniqueId())) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.GOLD + "You are already logged in.");
			return true;
		}
		
		if(UserUtils.getNotRegistered().contains(player.getUniqueId())) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You are not registered yet! Do: /register <password> <password>.");
			return true;
		}
		
		if(args.length != 1) {
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You are using the command in the wrong way! Do: /login <password>");
			return true;
		}
		
		if(OPMMysql.passwordMatches(player, args[0].toString())) {
			UserUtils.getLoggedIn().put(player.getUniqueId(), true);
			player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.GREEN + "You have successfully logged in.");
			return true;
		}
		
		player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "Attempt insuccessful. Try again!");
		OPMMysql.makeUnsuccessfulLoginAttempt(player, args[0].toString());
		
		return true;
	}

}
