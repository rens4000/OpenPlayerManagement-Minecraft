package nl.unusu4l.openplayermanagement.commands.maincommand;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Set;

public class HelpCommand extends OPMCMD {

	private OpenPlayerCommandExecutor executor;

	public HelpCommand(OpenPlayerCommandExecutor executor) {
		super("Help", "/openplayermanagement help", "Gives you a list of all", "openplayermanagement.help");
		this.executor = executor;
	}

	@Override
	void run(Player player) {
		Set<OPMCMD> commands = executor.getCommands();
		Iterator<OPMCMD> commandsIterator = commands.iterator();

        player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.GREEN + "COMMANDS");

		while(commandsIterator.hasNext()) {
		    OPMCMD command = commandsIterator.next();
            player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.GOLD + command.getUsage() + ChatColor.AQUA + " - " + ChatColor.GREEN + command.getDescription());
        }

        player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.GREEN + "COMMANDS");
	}

}
