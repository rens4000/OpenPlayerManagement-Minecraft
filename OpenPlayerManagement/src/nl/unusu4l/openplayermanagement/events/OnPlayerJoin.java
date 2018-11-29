package nl.unusu4l.openplayermanagement.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import nl.unusu4l.openplayermanagement.mysql.OPMMysql;
import nl.unusu4l.openplayermanagement.utils.UserUtils;

public class OnPlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!OPMMysql.userHasBeenRegistered(event.getPlayer())) {
			sendRegisterRequest(event.getPlayer());
			return;
		}
		if(!OPMMysql.userHasSession(event.getPlayer())) {
			promptLogin(event.getPlayer());
			return;
		} else {
			if(sessionExpired(event.getPlayer())) {
				promptLogin(event.getPlayer());
				return;
			}
			UserUtils.getLoggedIn().put(event.getPlayer().getUniqueId(), true);
			event.getPlayer().sendMessage(OpenPlayerManagement.PREFIX + "You have automatically logged in because your session was still valid.");
			return;
		}
	}

	private boolean sessionExpired(Player player) {
		return OPMMysql.sessionExpired(player);
	}

	private void promptLogin(Player player) {
		UserUtils.getLoggedIn().put(player.getUniqueId(), false);
		player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "Please login: /login <password>!");
		
		int times = 5;
		
		new BukkitRunnable() {

			int count = 0;
			
			@Override
			public void run() {
				count += 1;
				if(count != times) {
					player.sendMessage(OpenPlayerManagement.PREFIX + "You don't have an account. Please register one by doing /register <password> <password>.");
				} else {
					player.kickPlayer(OpenPlayerManagement.PREFIX + ChatColor.RED + "You didn't login in time! Try it again.");
					cancel();
				}
			}
			
		}.runTaskTimer(OpenPlayerManagement.getPlugin(OpenPlayerManagement.class), 100, 0);
	}
	
	private void sendRegisterRequest(Player player) {
		UserUtils.getLoggedIn().put(player.getUniqueId(), false);
		player.sendMessage(OpenPlayerManagement.PREFIX + "You don't have an account. Please register one by doing /register <password> <password>.");
		new BukkitRunnable() {

			@Override
			public void run() {
				player.sendMessage(OpenPlayerManagement.PREFIX + "You don't have an account. Please register one by doing /register <password> <password>.");
			}
			
		}.runTaskLaterAsynchronously(OpenPlayerManagement.getPlugin(OpenPlayerManagement.class), 120);
	}

}
