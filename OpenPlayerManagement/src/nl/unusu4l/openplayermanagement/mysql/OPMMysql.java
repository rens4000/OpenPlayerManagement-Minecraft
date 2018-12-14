package nl.unusu4l.openplayermanagement.mysql;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.unusu4l.openplayermanagement.managers.SettingsManager;
import nl.unusu4l.openplayermanagement.utils.OPMLogger;

/*
 * TODO: COMMENT THE CODE FOR READABILITY.
 */

public class OPMMysql {
	
	private static  String username = SettingsManager.get().getString("mysql.username");
	private static String password = SettingsManager.get().getString("mysql.password");
	private static String database = SettingsManager.get().getString("mysql.database");
	private static String host = SettingsManager.get().getString("mysql.host");
	private static int port = SettingsManager.get().getInt("mysql.port");
	
	/*
	 * 
	 * GLOBAL MYSQL FUNCTIONS
	 * 
	 */
	
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
	
	/*
	 * 
	 * TABEL CREATION
	 * 
	 */
	
	private static void createSessionsTabel(OPMLogger logger) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			DatabaseMetaData databaseMeta = connection.getMetaData();
			ResultSet tables = databaseMeta.getTables(null, null, "sessions", null);
			if (!tables.next()) {
				logger.highInfo("[OpenPlayerManagement] `sessions` tabel doesn't exist. Creating it.");
				Statement stmt = connection.createStatement();
			      
			      String sql = "CREATE TABLE sessions " +
			                   "(id INTEGER not NULL AUTO_INCREMENT, " +
			                   " uuid VARCHAR(255), " + 
			                   " duration BIGINT(255), " + 
			                   " IP VARCHAR(255)," +
			                   " PRIMARY KEY ( id ))"; 
			      stmt.executeUpdate(sql);
			}
			logger.highInfo("[OpenPlayerManagement] `sessions` table exists. Continuing.");
		} catch (SQLException e) {
			// Prints any errors.
			e.printStackTrace();
		}
	}
	
	private static void createUsersTabel(OPMLogger logger) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			DatabaseMetaData databaseMeta = connection.getMetaData();
			ResultSet tables = databaseMeta.getTables(null, null, "users", null);
			if (!tables.next()) {
				logger.highInfo("[OpenPlayerManagement] `users` table doesn't exist. Creating it.");
				Statement stmt = connection.createStatement();
			      
			      String sql = "CREATE TABLE users " +
			                   "(id INTEGER(11) NOT NULL AUTO_INCREMENT, " +
			                   " uuid VARCHAR(255), " + 
			                   " email VARCHAR(84), " + 
			                   " password VARCHAR(54), " +
			                   " confirmed TINYINT(1), " +
			                   " PRIMARY KEY ( id ))"; 
			      stmt.executeUpdate(sql);
			}
			logger.highInfo("[OpenPlayerManagement] `users` table exists. Continuing.");
		} catch (SQLException e) {
			// Prints any errors.
			e.printStackTrace();
		}
	}

	private static void createLoginAttemptsTabel(OPMLogger logger) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			DatabaseMetaData databaseMeta = connection.getMetaData();
			ResultSet tables = databaseMeta.getTables(null, null, "loginattempts", null);
			if (!tables.next()) {
				logger.highInfo("[OpenPlayerManagement] `loginattempts` tabel doesn't exist. Creating it.");
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
			logger.highInfo("[OpenPlayerManagement] `loginattempts` table exists. Continuing.");
		} catch (SQLException e) {
			// Prints any errors.
			e.printStackTrace();
		}
	}
	
	public static void createTabels(OPMLogger logger) {
		createUsersTabel(logger);
		createLoginAttemptsTabel(logger);
		createSessionsTabel(logger);
	}
	
	
	/*
	 * 
	 * MYSQL CHECKS
	 * 
	 */
	
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
	
	public static boolean ipMatches(UUID uuid) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT ip, id FROM sessions WHERE uuid='" + uuid + "' AND id=(SELECT MAX(id) FROM sessions)");
			if(rs.next()) {
				Player player = Bukkit.getPlayer(uuid);
				if(!rs.getString("ip").equals(player.getAddress().getAddress().toString())) return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
			String encryptedPassword = md5(userPassword);
			if(rs.next()) {
				if(rs.getString("password").equals(encryptedPassword)) return true;
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
	
	/*
	 * 
	 * MYSQL INPUTS
	 * 
	 */

	public static void makeUnsuccessfulLoginAttempt(UUID uuid, String input) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			
			Date date = new Date(System.currentTimeMillis());
			
			statement.executeUpdate("INSERT INTO loginattempts (uuid, input, date, successful) VALUES  ('" + uuid + 
					"', '" + input + "', '" + date + "', '0')");
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createUser(UUID uuid, String userPassword, OPMLogger logger) {
		try {
			// Creates the connection.
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			
			String encryptedPassword = md5(userPassword);
			
			statement.executeUpdate("INSERT INTO users (uuid, password, confirmed) VALUES  ('" + uuid + 
					"', '" + encryptedPassword + "', '0')");
			
			logger.highInfo("'" + uuid + "' has been registered as an user.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void changePassword(UUID uuid, String userPassword, OPMLogger logger) {
		try {
			// Creates the connection.
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			
			String encryptedPassword = md5(userPassword);
			
			statement.executeUpdate("UPDATE users SET password='" + encryptedPassword + "' WHERE uuid='" + uuid + "'");
			
			logger.highInfo("'" + uuid + "' changed it's password.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createSession(UUID uuid) {
		try {
			// Creates the connection.
			
			Player player = Bukkit.getPlayer(uuid);
			
			long duration = System.currentTimeMillis() + 600000;
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			Statement statement = connection.createStatement();
			
			statement.executeUpdate("INSERT INTO sessions (uuid, duration, ip) VALUES  ('" + uuid + 
					"', '" + duration + "', '" + player.getAddress().getAddress().toString() + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * ENCRYPTION
	 * 
	 */
	
	private static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }
	
}
