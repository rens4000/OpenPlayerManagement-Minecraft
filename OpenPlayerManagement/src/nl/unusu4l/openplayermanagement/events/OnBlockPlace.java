package nl.unusu4l.openplayermanagement.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import nl.unusu4l.openplayermanagement.utils.UserUtils;

public class OnBlockPlace implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
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
