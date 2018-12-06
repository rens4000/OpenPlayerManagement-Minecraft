package nl.unusu4l.openplayermanagement.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserUtils {
	
	private static Map<UUID, Boolean> loggedin = new HashMap<>();
	private static List<UUID> notRegistered = new ArrayList<UUID>();
	
	public static boolean containsUser(UUID uuid) {
		return loggedin.containsKey(uuid);
	}
	
	public static boolean isLoggedin(UUID uuid) {
		if(!containsUser(uuid))
			return false;
		
		return loggedin.get(uuid);
	}
	
	public static Map<UUID, Boolean> getLoggedIn() {
		return loggedin;
	}

	public static List<UUID> getNotRegistered() {
		return notRegistered;
	}

}
