package nl.unusu4l.openplayermanagement.commands.maincommand;

import java.util.LinkedHashSet;
import java.util.Set;

import nl.unusu4l.openplayermanagement.OpenPlayerManagement;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenPlayerCommandExecutor implements CommandExecutor {
	
	private Set<OPMCMD> commands = new LinkedHashSet<OPMCMD>();
	
	public OpenPlayerCommandExecutor() {
		commands.add(new HelpCommand(this));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

	    Player player = (Player) sender;
		
		if(args.length == 0) {
			
			return true;
		}

		for(OPMCMD command : commands) {
		    if(command.getName().equalsIgnoreCase(args[0])) {
                if(command.getPermission() == null) {
                    command.run(player);
                    return true;
                }
                if(!player.hasPermission(command.getPermission())) {
                    player.sendMessage(OpenPlayerManagement.PREFIX + ChatColor.RED + "You don't have permission to execute this command.");
                    return true;
                }
                command.run(player);
            }
        }

		return true;
	}

	public Set<OPMCMD> getCommands() {
		return commands;
	}

}
