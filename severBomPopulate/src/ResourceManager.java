/**
 * @author Yara Medhat
 *
 *
 */
package serverbompopulate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Yara Medhat
 *
 */
public class ResourceManager {
	private static Properties properties = new Properties();
	private static Driver driver = null;

	public static synchronized Connection getConnection() throws SQLException {
		if (driver == null) {
			try {
				properties = new Properties();
				InputStream input = ResourceManager.class.getResourceAsStream("db_config.properties");
			    properties.load(input);
				input.close();
				@SuppressWarnings("rawtypes")
				Class jdbcDriverClass = Class.forName(properties
						.getProperty("JDBC_DRIVER"));
				driver = (Driver) jdbcDriverClass.newInstance();
				DriverManager.registerDriver(driver);
			} catch (Exception e) {
				System.out.println("Failed to initialise JDBC driver");
			}
		}
		return DriverManager.getConnection(properties.getProperty("JDBC_URL"),
				properties.getProperty("JDBC_USER"),
				properties.getProperty("JDBC_PASSWORD"));
	}
	
	public static synchronized Connection getFinacleConnection() throws SQLException {
		if (driver == null) {
			try {
				properties = new Properties();
				InputStream input = ResourceManager.class.getResourceAsStream("db_config.properties");
			    properties.load(input);
				input.close();
				@SuppressWarnings("rawtypes")
				Class jdbcDriverClass = Class.forName(properties
						.getProperty("FINACLE_JDBC_DRIVER"));
				driver = (Driver) jdbcDriverClass.newInstance();
				DriverManager.registerDriver(driver);
			} catch (Exception e) {
				System.out.println("Failed to initialise JDBC driver");
			}
		}
		return DriverManager.getConnection(properties.getProperty("FINACLE_JDBC_URL"),
				properties.getProperty("FINACLE_JDBC_USER"),
				properties.getProperty("FINACLE_JDBC_PASSWORD"));
	}

	public static void close(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static void close(PreparedStatement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

	}
}
