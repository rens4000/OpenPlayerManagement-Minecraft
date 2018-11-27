package nl.unusu4l.openplayermanagement.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import nl.unusu4l.openplayermanagement.managers.SettingsManager;

public class OPMMysql {
	
	/*
	 * Checks if connection to the database can be made.
	 */
	@SuppressWarnings("unused")
	public static boolean canConnect() {
		// Sets the variables for readability.
		String username = SettingsManager.get().getString("mysql.username");
		String password = SettingsManager.get().getString("mysql.password");
		String database = SettingsManager.get().getString("mysql.database");
		String host = SettingsManager.get().getString("mysql.host");
		int port = SettingsManager.get().getInt("mysql.port");
		
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		} catch (SQLException e) {
			// Print any errors and return false if the connection can't be made.
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	static void createUsersTabel(Logger logger) {
		// Sets the variables for readability.
		String username = SettingsManager.get().getString("mysql.username");
		String password = SettingsManager.get().getString("mysql.password");
		String database = SettingsManager.get().getString("mysql.database");
		String host = SettingsManager.get().getString("mysql.host");
		int port = SettingsManager.get().getInt("mysql.port");
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			DatabaseMetaData databaseMeta = connection.getMetaData();
			ResultSet tables = databaseMeta.getTables(null, null, "users", null);
			if (!tables.next()) {
				logger.info("[OpenPlayerManagement] `users` table doesn't exist. Creating it.");
				Statement stmt = connection.createStatement();
			      
			      String sql = "CREATE TABLE users " +
			                   "(id INTEGER not NULL, " +
			                   " uuid VARCHAR(255), " + 
			                   " email VARCHAR(84), " + 
			                   " password VARCHAR(54), " + 
			                   " PRIMARY KEY ( id ))"; 
			      stmt.executeUpdate(sql);
			}
			logger.info("[OpenPlayerManagement] `users` table exists. Continuing.");
		} catch (SQLException e) {
			// Prints any errors.
			e.printStackTrace();
		}
	}

	static void createLoginAttemptsTabel(Logger logger) {
		// Sets the variables for readability.
		String username = SettingsManager.get().getString("mysql.username");
		String password = SettingsManager.get().getString("mysql.password");
		String database = SettingsManager.get().getString("mysql.database");
		String host = SettingsManager.get().getString("mysql.host");
		int port = SettingsManager.get().getInt("mysql.port");
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			DatabaseMetaData databaseMeta = connection.getMetaData();
			ResultSet tables = databaseMeta.getTables(null, null, "loginattempts", null);
			if (!tables.next()) {
				logger.info("[OpenPlayerManagement] `loginattempts` tabel doesn't exist. Creating it.");
				Statement stmt = connection.createStatement();
			      
			      String sql = "CREATE TABLE loginattempts " +
			                   "(id INTEGER not NULL, " +
			                   " uuid VARCHAR(255), " + 
			                   " input TEXT(255), " + 
			                   " date DATETIME, " + 
			                   " successful TINYINT(1)," +
			                   " PRIMARY KEY ( id ))"; 
			      stmt.executeUpdate(sql);
			}
			logger.info("[OpenPlayerManagement] `loginattempts` table exists. Continuing.");
		} catch (SQLException e) {
			// Prints any errors.
			e.printStackTrace();
		}
	}
	
	public static void createTabels(Logger logger) {
		createUsersTabel(logger);
		createLoginAttemptsTabel(logger);
	}
	
}
