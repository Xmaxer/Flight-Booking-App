package main;

import connections.DBConnection;
import hidden.Constants;

public class Main {


	private static final String CURRENCY = "EUR";
	private static final String LOCALE = "en-GB";
	private static final String DB_NAME = "flights";
	private static final String USER = Constants.USER;
	private static final String PASS = Constants.PASS;
	//private static final String allMarketCountries = "http://partners.api.skyscanner.net/apiservices/reference/v1.0/countries/" + LOCALE + "?apiKey=" + API_KEY;

	public static void main(String[] args)
	{
		
		DBConnection.createConnection(USER, PASS);
		DBConnection.update("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
		DBConnection.closeConnection();
		DBConnection.createConnection(USER, PASS, DB_NAME);

/*		try {
			String[] s = {"Continents", "Countries", "Cities", "Airports"};
			JSONParser parser = new JSONParser();
			String parsing = getData(allValidLocalesURL);
			JSONArray array = (JSONArray) ((JSONObject) parser.parse(parsing)).get("Continents");
			@SuppressWarnings("unchecked")
			Iterator<String> i = array.iterator();
			while(i.hasNext())
			{
				String obj = String.valueOf(((JSONObject) parser.parse(String.valueOf(i.next()))).get("Name"));
				System.out.println(obj);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
	}
}
