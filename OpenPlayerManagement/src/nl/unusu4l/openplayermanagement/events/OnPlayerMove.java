package nl.unusu4l.openplayermanagement.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import nl.unusu4l.openplayermanagement.utils.UserUtils;

public class OnPlayerMove implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		// Checks if the player hasn't logged in. If so: tell him to login/register.
		if(!UserUtils.isLoggedin(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
			if(!UserUtils.getNotRegistered().contains(event.getPlayer().getUniqueId())) {
				event.getPlayer().sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "Please login: /login <password>!");
				return;
			}
			event.getPlayer().sendMessage(OpenPlayerManagement.PREFIX + "You don't have an account. Please register one by doing /register <password> <password>.");
		}
	}

}
