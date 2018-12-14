package nl.unusu4l.openplayermanagement.commands.maincommand;

import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OpenPlayerCommandExecutor implements CommandExecutor {
	
	private Set<OPMCMD> commands = new LinkedHashSet<OPMCMD>();
	
	public OpenPlayerCommandExecutor() {
		commands.add(new HelpCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 0) {
			
			return true;
		}
		return true;
	}

}
