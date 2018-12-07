package nl.unusu4l.openplayermanagement.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.unusu4l.openplayermanagement.managers.SettingsManager;

public class OPMMysql {
	
	private static  String username = SettingsManager.get().getString("mysql.username");
	private static String password = SettingsManager.get().getString("mysql.password");
	private static String database = SettingsManager.get().getString("mysql.database");
	private static String host = SettingsManager.get().getString("mysql.host");
	private static int port = SettingsManager.get().getInt("mysql.port");
	
	/*
	 * Checks if connection to the database can be made.
	 */
	@SuppressWarnings("unused")
	public static boolean canConnect() {
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
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			DatabaseMetaData databaseMeta = connection.getMetaData();
			ResultSet tables = databaseMeta.getTables(null, null, "users", null);
			if (!tables.next()) {
				logger.info("[OpenPlayerManagement] `users` table doesn't exist. Creating it.");
				Statement stmt = connection.createStatement();
			      
			      String sql = "CREATE TABLE users " +
			                   "(id INTEGER(11) NOT NULL AUTO_INCREMENT, " +
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
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			DatabaseMetaData databaseMeta = connection.getMetaData();
			ResultSet tables = databaseMeta.getTables(null, null, "loginattempts", null);
			if (!tables.next()) {
				logger.info("[OpenPlayerManagement] `loginattempts` tabel doesn't exist. Creating it.");
				Statement stmt = connection.createStatement();
			      
			      String sql = "CREATE TABLE loginattempts " +
			                   "(id INTEGER(11) not NULL AUTO_INCREMENT, " +
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
	
	static void createSessionsTabel(Logger logger) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			DatabaseMetaData databaseMeta = connection.getMetaData();
			ResultSet tables = databaseMeta.getTables(null, null, "sessions", null);
			if (!tables.next()) {
				logger.info("[OpenPlayerManagement] `sessions` tabel doesn't exist. Creating it.");
				Statement stmt = connection.createStatement();
			      
			      String sql = "CREATE TABLE sessions " +
			                   "(id INTEGER not NULL AUTO_INCREMENT, " +
			                   " uuid VARCHAR(255), " + 
			                   " duration BIGINT(255), " + 
			                   " IP VARCHAR(255)," +
			                   " PRIMARY KEY ( id ))"; 
			      stmt.executeUpdate(sql);
			}
			logger.info("[OpenPlayerManagement] `sessions` table exists. Continuing.");
		} catch (SQLException e) {
			// Prints any errors.
			e.printStackTrace();
		}
	}
	
	public static void createTabels(Logger logger) {
		createUsersTabel(logger);
		createLoginAttemptsTabel(logger);
		createSessionsTabel(logger);
	}

	public static boolean userHasBeenRegistered(UUID uuid) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT id FROM users WHERE uuid='" + uuid + "'");
			if(!rs.next()) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean userHasSession(UUID uuid) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT id FROM sessions WHERE uuid='" + uuid + "'");
			if(!rs.next()) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean passwordMatches(UUID uuid, String userPassword) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT password FROM users WHERE uuid='" + uuid + "'");
			if(rs.next()) {
				if(rs.getString("password").equals(userPassword)) return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean sessionExpired(UUID uuid) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT duration FROM sessions WHERE uuid='" + uuid + "' AND id=(SELECT MAX(id) FROM sessions WHERE uuid='" + uuid + "')");
			if(rs.next()) {
				long duration = rs.getLong("duration");
				if(duration > System.currentTimeMillis()) {
					return false;
				}
				return true;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void makeUnsuccessfulLoginAttempt(UUID uuid, String input) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			
			Date date = new Date(System.currentTimeMillis());
			
			statement.executeUpdate("INSERT INTO loginattempts (uuid, input, date, successful) VALUES  ('" + uuid + 
					"', '" + input + "', '" + date + "', '0')");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void makeSuccessfulLoginAttempt(UUID uuid, String input) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			
			Date date = new Date(System.currentTimeMillis());
			
			statement.executeUpdate("INSERT INTO loginattempts (uuid, input, date, successful) VALUES  ('" + uuid + 
					"', '" + input + "', '" + date + "', '1')");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createUser(UUID uuid, String userPassword) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			
			statement.executeUpdate("INSERT INTO users (uuid, password) VALUES  ('" + uuid + 
					"', '" + userPassword + "')");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createSession(UUID uuid) {
		try {
			// Creates the connection.
			
			Player player = Bukkit.getPlayer(uuid);
			
			long duration = System.currentTimeMillis() + 60000;
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			
			statement.executeUpdate("INSERT INTO sessions (uuid, duration, ip) VALUES  ('" + uuid + 
					"', '" + duration + "', '" + player.getAddress().getAddress().toString() + "')");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean ipMatches(UUID uuid) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT ip, id FROM sessions WHERE uuid='" + uuid + "' AND id=(SELECT MAX(id) FROM sessions)");
			if(rs.next()) {
				Player player = Bukkit.getPlayer(uuid);
				player.sendMessage(rs.getInt("id") + " ");
				if(!rs.getString("ip").equals(player.getAddress().getAddress().toString())) return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
