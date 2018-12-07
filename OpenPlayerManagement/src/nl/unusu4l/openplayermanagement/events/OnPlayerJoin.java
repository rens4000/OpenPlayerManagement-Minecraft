package nl.unusu4l.openplayermanagement.events;

import java.util.UUID;

import org.bukkit.Bukkit;
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
		if(!OPMMysql.userHasBeenRegistered(event.getPlayer().getUniqueId())) {
			sendRegisterRequest(event.getPlayer().getUniqueId());
			return;
		}
		if(!OPMMysql.userHasSession(event.getPlayer().getUniqueId())) {
			promptLogin(event.getPlayer());
			event.getPlayer().sendMessage("No session");
			return;
		} else {
			if(sessionExpired(event.getPlayer().getUniqueId())) {
				promptLogin(event.getPlayer());
				return;
			}
			
			if(!OPMMysql.ipMatches(event.getPlayer().getUniqueId())) {
				promptLogin(event.getPlayer());
				return;
			}
			
			UserUtils.getLoggedIn().put(event.getPlayer().getUniqueId(), true);
			event.getPlayer().sendMessage(OpenPlayerManagement.PREFIX + "You have automatically logged in because your session was still valid.");
			return;
		}
		
	}

	private boolean sessionExpired(UUID uuid) {
		return OPMMysql.sessionExpired(uuid);
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
				if(UserUtils.isLoggedin(player.getUniqueId())) {
					cancel();
					return;
				}
				
				if(count != times) {
					player.sendMessage(OpenPlayerManagement.PREFIX + "Please login: /login <password>!");
				} else {
					player.kickPlayer(OpenPlayerManagement.PREFIX + ChatColor.RED + "You didn't login in time! Try it again.");
					cancel();
				}
			}
			
		}.runTaskTimer(OpenPlayerManagement.getPlugin(OpenPlayerManagement.class), 0, 100);
	}
	
	private void sendRegisterRequest(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		UserUtils.getLoggedIn().put(uuid, false);
		UserUtils.getNotRegistered().add(uuid);
		player.sendMessage(OpenPlayerManagement.PREFIX + "You don't have an account. Please register one by doing /register <password> <password>.");
		new BukkitRunnable() {

			@Override
			public void run() {
				player.sendMessage(OpenPlayerManagement.PREFIX + "You don't have an account. Please register one by doing /register <password> <password>.");
			}
			
		}.runTaskLaterAsynchronously(OpenPlayerManagement.getPlugin(OpenPlayerManagement.class), 120);
	}

}
