package nl.unusu4l.openplayermanagement.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import nl.unusu4l.openplayermanagement.utils.UserUtils;

public class OnPlayerMove implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(!UserUtils.isLoggedin(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
			
		}
	}

}
