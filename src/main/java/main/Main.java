package main;

import connections.DBConnection;
import hidden.Constants;
import skyscanner.SkyScannerAPI;

public class Main {


	private static final String DB_NAME = "flights";
	private static final String USER = Constants.USER;
	private static final String PASS = Constants.PASS;

	public static void main(String[] args)
	{
		DBConnection.setConnectionDetails("localhost", 3306);
		DBConnection.createConnection(USER, PASS);
		DBConnection.update("CREATE DATABASE IF NOT EXISTS " + DB_NAME + " "
				+ "DEFAULT CHARACTER SET utf8 " + 
				"DEFAULT COLLATE utf8_general_ci");
		DBConnection.closeConnection();
		DBConnection.createConnection(USER, PASS, DB_NAME);
		SkyScannerAPI.createTables();

	}
}
