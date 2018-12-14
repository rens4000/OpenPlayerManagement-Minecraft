package nl.unusu4l.openplayermanagement.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.unusu4l.openplayermanagement.utils.OPMLogger;

public class SettingsManager {
	
	static FileConfiguration config;
    static File configFile, configFolder;

    /*
     * Sets up the configFile.
     */
    public static void setup(OPMLogger logger) {
    	// Creates the folder object to prevent errors.
    	configFolder = new File("plugins/OpenPlayerManagement/");
    	
    	// Creates the file object.
    	configFile = new File("plugins/OpenPlayerManagement/", "config.yml");
    	
    	// Creates the folder to prevent errors.
    	configFolder.mkdirs();

    	// Doesn't the config file exist?
        if (!configFile.exists()) {
            try {
            	// Create the config file.
            	configFile.createNewFile();
            } catch (IOException e) {
            	// Errors? Log it and print the stacktrace.
            	logger.severe("The config could not be generated!");
            	e.printStackTrace();
            }
        }
        
        // Sets the config variable to the YAMLConfiguration.
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /*
     * Getter of the config.
     */
    public static FileConfiguration get() {
        return config;
    }

    /*
     * Saves the config to the config file.
     */
    public static void save() {
        try {
        	config.save(configFile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save yourfile.yml!");
        }
    }

    /*
     * Reloads the config.
     */
    public static void reload() {
    	config = YamlConfiguration.loadConfiguration(configFile);
    }
}
