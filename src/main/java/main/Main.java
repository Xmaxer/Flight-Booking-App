package main;

import connections.DBConnection;
import gui.GUI;
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
		
		GUI.launchGUI(args);
		/*String d = Downloader.getData("http://partners.api.skyscanner.net/apiservices/browsequotes/v1.0/IE/EUR/en-GB/DUBL/PARI/2018-05-10/2018-05-15/?apiKey=" + Constants.SKYSCANNER_API_KEY);
		System.out.println(d);*/
	}
}
