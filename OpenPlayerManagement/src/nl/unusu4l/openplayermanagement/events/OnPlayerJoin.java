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
		// Checks if the user has not been registered yet.
		if(!OPMMysql.userHasBeenRegistered(event.getPlayer().getUniqueId())) {
			sendRegisterRequest(event.getPlayer().getUniqueId());
			return;
		}
		
		// Checks if the user has no session.
		if(!OPMMysql.userHasSession(event.getPlayer().getUniqueId())) {
			promptLogin(event.getPlayer());
			return;
		} else { // If the user does.
			// Check if the session has been expired.
			if(sessionExpired(event.getPlayer().getUniqueId())) {
				promptLogin(event.getPlayer());
				return;
			}
			
			// Check if the ip-addresses don't match.
			if(!OPMMysql.ipMatches(event.getPlayer().getUniqueId())) {
				promptLogin(event.getPlayer());
				return;
			}
			
			// If the session exists and is good to go: Log the player in and tell them why they could log in.
			UserUtils.getLoggedIn().put(event.getPlayer().getUniqueId(), true);
			event.getPlayer().sendMessage(OpenPlayerManagement.PREFIX + ChatColor.GOLD + "You have automatically logged in because your session was still valid.");
			return;
		}
		
	}

	/*
	 * Returns if the session has expired.
	 */
	private boolean sessionExpired(UUID uuid) {
		return OPMMysql.sessionExpired(uuid);
	}

	/*
	 * Prompts the player to login and kicks the player if he/she hasn't logged in in time.
	 */
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
					player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "Please login: /login <password>!");
				} else {
					player.kickPlayer(OpenPlayerManagement.PREFIX + ChatColor.RED + "You didn't login in time! Try it again.");
					cancel();
				}
			}
			
		}.runTaskTimer(OpenPlayerManagement.getPlugin(OpenPlayerManagement.class), 0, 100);
	}
	
	/*
	 * Sends the player a message that he/she needs to register.
	 */
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
