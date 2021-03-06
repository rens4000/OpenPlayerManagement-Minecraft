package nl.unusu4l.openplayermanagement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import nl.unusu4l.openplayermanagement.commands.ChangePasswordCommand;
import nl.unusu4l.openplayermanagement.commands.LoginCommand;
import nl.unusu4l.openplayermanagement.commands.RegisterCommand;
import nl.unusu4l.openplayermanagement.events.OnBlockPlace;
import nl.unusu4l.openplayermanagement.events.OnChat;
import nl.unusu4l.openplayermanagement.events.OnPlayerJoin;
import nl.unusu4l.openplayermanagement.events.OnPlayerMove;
import nl.unusu4l.openplayermanagement.managers.SettingsManager;
import nl.unusu4l.openplayermanagement.mysql.OPMMysql;
import nl.unusu4l.openplayermanagement.utils.OPMLogger;

public class OpenPlayerManagement extends JavaPlugin {
	
	public static String PREFIX;
	
	private OPMLogger logger;
	
	private boolean startupcancelled = false;
	
	@Override
	public void onEnable() {
		// Sets the logger.
		logger = new OPMLogger(getServer().getConsoleSender());
		
		// Sets up the settingsmanager.
		setupSettingsManager();
		
		// Sets the prefix to the configurable value and formats it to the right format(colors and spaces).
		PREFIX = ChatColor.translateAlternateColorCodes('&', SettingsManager.get().getString("messages.prefix")) + ChatColor.RESET + " ";
		
		// Setup MYSQL.
		setupMYSQL();
		
		// Register Events.
		registerEvents();
		
		// Register Commands.
		registerCommands();
		
		// Tell the console that the plugin has been loaded.
		logger.info("[OpenPlayerManagement] OPM v" + getDescription().getVersion() + " has started.");
	}
	
	/*
	 * Registers all the commands.
	 */
	private void registerCommands() {
		getCommand("login").setExecutor(new LoginCommand());
		getCommand("register").setExecutor(new RegisterCommand(logger));
		getCommand("changepassword").setExecutor(new ChangePasswordCommand(logger));
		logger.info("[OpenPlayerManagement] Commands have been registered.");
	}

	/*
	 * Registers all the events.
	 */
	private void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
		Bukkit.getPluginManager().registerEvents(new OnPlayerMove(), this);
		Bukkit.getPluginManager().registerEvents(new OnBlockPlace(), this);
		Bukkit.getPluginManager().registerEvents(new OnChat(), this);
		logger.info("[OpenPlayerManagement] Events have been registered.");
	}

	/*
	 * Sets up the database.
	 */
	private void setupMYSQL() {
		canConnectToDatabase();
		if(startupcancelled) return;
		OPMMysql.createTabels(logger);
	}

	/*
	 * Check if we can establish a connection with the database.
	 */
	private void canConnectToDatabase() {
		if(!OPMMysql.canConnect()) {
			disablePlugin("Connection to the database could not be established.");
			return;
		}
		logger.info("[OpenPlayerManagement] Connection to the database could be established successfully.");
	}
	
	/*
	 * Disables the plugin and gives the reason why.
	 */
	private void disablePlugin(String reason) {
		logger.severe("[OpenPlayerManagement] //////////////////////////////////////////////////////////////");
		logger.severe("[OpenPlayerManagement] STOPPING THE SERVER!");
		logger.severe("[OpenPlayerManagement] //////////////////////////////////////////////////////////////");
		logger.severe("[OpenPlayerManagement] The plugin ran in some trouble and the plugin had");
		logger.severe("[OpenPlayerManagement] to be disabled.");
		logger.severe("[OpenPlayerManagement] Reason: " + reason);
		logger.severe("[OpenPlayerManagement] //////////////////////////////////////////////////////////////");
		logger.severe("[OpenPlayerManagement] END OF INFO.");
		logger.severe("[OpenPlayerManagement] //////////////////////////////////////////////////////////////");
		Bukkit.getPluginManager().disablePlugin(this);
		startupcancelled = true;
	}

	/*
	 * Sets up the settingsmanager(creates the config).
	 */
	private void setupSettingsManager() {
		// Sets up the file.
		SettingsManager.setup(logger);
		
		// Message defaults.
		SettingsManager.get().addDefault("messages.prefix", "&f&lOPEN&7&lPLAYER&c&lMANAGEMENT");
		
		// MYSQL defaults.
		
		String database = "openplayermanagement";
		
		SettingsManager.get().addDefault("mysql.host", "127.0.0.1");
		SettingsManager.get().addDefault("mysql.port", 3306);
		SettingsManager.get().addDefault("mysql.username", "root");
		SettingsManager.get().addDefault("mysql.password", "");
		SettingsManager.get().addDefault("mysql.database", database);
		
		// Logmode default.
		SettingsManager.get().addDefault("extralogmode", true);
		
		// If the defaults don't exist. Make them.
		SettingsManager.get().options().copyDefaults(true);
		
		// Saves the changes to the config file.
		SettingsManager.save();
		
		// Logs that the settingsmanager has been setup.
		logger.info("[OpenPlayerManagement] The settingsmanager has been setup");
	}

}
