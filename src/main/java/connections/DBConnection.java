package connections;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

public class DBConnection{
	
	private static String username;
	private static String password;
	private static String IP;
	private static int port;
	private static String baseURL;
	public static synchronized String getIP() {
		return IP;
	}
	public static synchronized void setIP(String iP) {
		IP = iP;
	}
	public static synchronized int getPort() {
		return port;
	}
	public static synchronized void setPort(int port) {
		DBConnection.port = port;
	}

	private static String database;
	private static Connection connection;
	private static Statement statement;
	
	public static synchronized void setConnectionDetails(String IP, int port)
	{
		DBConnection.IP = IP;
		DBConnection.port = port;
	}
	public static synchronized void closeConnection()
	{
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static synchronized int update(String query)
	{
		try {
			System.out.println(query);
			return DBConnection.statement.executeUpdate(query);
		} catch(SQLIntegrityConstraintViolationException e)
		{
			
		}
		catch(SQLSyntaxErrorException e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String s = sw.toString();
			if(s.contains("already exists"))
				System.out.println("Table already exists...");
			else
				e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static synchronized ResultSet query(String query)
	{
		try {
			System.out.println(query);
			return DBConnection.statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static synchronized void createConnection(String username, String password, String database)
	{
		DBConnection.baseURL = "jdbc:mysql://" + IP + ":" + port + "/";
		DBConnection.username = username;
		DBConnection.password = password;
		DBConnection.database = database;
		try {
			DBConnection.connection = DriverManager.getConnection(baseURL + database + "?user=" + username + "&password=" + password + "&useLegacyDatetimeCode=false&serverTimezone=UTC");
			statement = DBConnection.connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void createConnection(String username, String password)
	{
		createConnection(username, password, "");
	}

	public static synchronized String getUsername() {
		return username;
	}

	public static synchronized void setUsername(String username) {
		DBConnection.username = username;
	}

	public static synchronized String getPassword() {
		return password;
	}

	public static synchronized void setPassword(String password) {
		DBConnection.password = password;
	}

	public static synchronized String getDatabase() {
		return database;
	}

	public static synchronized void setDatabase(String database) {
		DBConnection.database = database;
	}

	public static synchronized Connection getConnection() {
		return connection;
	}

	public static synchronized void setConnection(Connection connection) {
		DBConnection.connection = connection;
	}

	public static synchronized Statement getStatement() {
		return statement;
	}

	public static synchronized void setStatement(Statement statement) {
		DBConnection.statement = statement;
	}
	
}
