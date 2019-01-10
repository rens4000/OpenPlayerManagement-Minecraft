package nl.unusu4l.openplayermanagement.commands.maincommand;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Set;

public class VersionCommand extends OPMCMD {


	public VersionCommand() {
		super("Version", "/openplayermanagement version", "Shows you the version of the plugin", "openplayermanagement.help");
	}

	@Override
	void run(Player player) {

	}

}
