package skyscanner;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import connections.DBConnection;
import connections.Downloader;
import exceptions.NoDatabaseConnectedException;
import hidden.Constants;

public class SkyScannerAPI {

	private final static String API_KEY = Constants.SKYSCANNER_API_KEY;
	private final static String allValidLocalesURL = "http://partners.api.skyscanner.net/apiservices/reference/v1.0/locales?apiKey=" + API_KEY;
	private final static String allCurrenciesURL = "http://partners.api.skyscanner.net/apiservices/reference/v1.0/currencies?apiKey=" + API_KEY;
	private final static String allGeoLocationsURL = "http://partners.api.skyscanner.net/apiservices/geo/v1.0?apiKey=" + API_KEY;
	private final static String quotesBaseURL = "http://partners.api.skyscanner.net/apiservices/browsequotes/v1.0/";
	
	
	public static void createTables()
	{
		if(DBConnection.getConnection() != null)
		{
			localesTable();
			currenciesTable();
			geoLocationsTable();
		} else
			try {
				throw new NoDatabaseConnectedException("No database connected");
			} catch (NoDatabaseConnectedException e) {
				e.printStackTrace();
			}
	}

	private static void geoLocationsTable() {
		DatabaseMetaData meta;
		try {
			meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, "airport", 
				     new String[] {"TABLE"});
			if(res.next())
			{
				return;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		  
		DBConnection.update("CREATE TABLE IF NOT EXISTS continent ("
				+ "continentID varchar(10),"
				+ "name varchar(255),"
				+ "PRIMARY KEY (continentID))");
		DBConnection.update("CREATE TABLE IF NOT EXISTS country ("
				+ "countryID varchar(10),"
				+ "name varchar(255),"
				+ "currencyID varchar(10),"
				+ "continentID varchar(10),"
				+ "FOREIGN KEY (currencyID) REFERENCES currency(currencyID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "FOREIGN KEY (continentID) REFERENCES continent(continentID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "PRIMARY KEY (countryID))");
		DBConnection.update("CREATE TABLE IF NOT EXISTS city ("
				+ "cityID varchar(10),"
				+ "name varchar(255),"
				+ "latitude varchar(20),"
				+ "longtitude varchar(20),"
				+ "countryID varchar(10),"
				+ "FOREIGN KEY (countryID) REFERENCES country(countryID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "PRIMARY KEY (cityID))");
		DBConnection.update("CREATE TABLE IF NOT EXISTS airport ("
				+ "airportID varchar(10),"
				+ "name varchar(255),"
				+ "latitude varchar(20),"
				+ "longtitude varchar(20),"
				+ "cityID varchar(10),"
				+ "FOREIGN KEY (cityID) REFERENCES city(cityID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "PRIMARY KEY (airportID))");

		String data = Downloader.getData(allGeoLocationsURL);

		JSONParser parser = new JSONParser();
		try {
			Iterator<?> a = ((JSONArray) ((JSONObject) parser.parse(data)).get("Continents")).iterator();
			while(a.hasNext())
			{
				String val = String.valueOf(a.next());
				JSONObject parsed = (JSONObject) parser.parse(val);
				String continentID = String.valueOf(parsed.get("Id"));
				DBConnection.update("INSERT INTO continent VALUES ('"
						+ continentID + "','"
						+ String.valueOf(parsed.get("Name")).replaceAll("'", "") + "')");

				Iterator<?> b = ((JSONArray) parsed.get("Countries")).iterator();

				while(b.hasNext())
				{
					val = String.valueOf(b.next());
					parsed = (JSONObject) parser.parse(val);
					String countryID = String.valueOf(parsed.get("Id"));
					DBConnection.update("INSERT INTO country VALUES ('"
							+ countryID + "','"
							+ String.valueOf(parsed.get("Name")).replaceAll("'", "") + "','"
							+ String.valueOf(parsed.get("CurrencyId")) + "','"
							+ continentID + "')");

					Iterator<?> c = ((JSONArray) parsed.get("Cities")).iterator();

					while(c.hasNext())
					{
						val = String.valueOf(c.next());
						parsed = (JSONObject) parser.parse(val);
						String cityID = String.valueOf(parsed.get("Id"));
						String[] coords = String.valueOf(parsed.get("Location")).replaceAll(" ", "").split(",");
						DBConnection.update("INSERT INTO city VALUES ('"
								+ cityID + "','"
								+ String.valueOf(parsed.get("Name")).replaceAll("'", "") + "','"
								+ coords[1] + "','"
								+ coords[0] + "','"
								+ countryID + "')");

						Iterator<?> d = ((JSONArray) parsed.get("Airports")).iterator();

						while(d.hasNext())
						{
							val = String.valueOf(d.next());
							parsed = (JSONObject) parser.parse(val);
							coords = String.valueOf(parsed.get("Location")).replaceAll(" ", "").split(",");
							DBConnection.update("INSERT INTO airport VALUES('"
									+ String.valueOf(parsed.get("Id")) + "','"
									+ String.valueOf(parsed.get("Name")).replaceAll("'", "") + "','"
									+ coords[1] + "','"
									+ coords[0] + "','"
									+ cityID + "')");
						}
					}
				}
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void currenciesTable() {
		DatabaseMetaData meta;
		try {
			meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, "currency", 
				     new String[] {"TABLE"});
			if(res.next())
			{
				return;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		DBConnection.update("CREATE TABLE IF NOT EXISTS currency ("
				+ "currencyID varchar(10),"
				+ "symbol varchar(10),"
				+ "PRIMARY KEY(currencyID))");
		String data = Downloader.getData(allCurrenciesURL);

		JSONParser parser = new JSONParser();
		try {
			Iterator<?> i = ((JSONArray) ((JSONObject) parser.parse(data)).get("Currencies")).iterator();
			while(i.hasNext())
			{
				String val = String.valueOf(i.next());
				DBConnection.update("INSERT INTO currency VALUES ('"
						+ String.valueOf(((JSONObject) parser.parse(String.valueOf(val))).get("Code")) + "','"
						+ String.valueOf(((JSONObject) parser.parse(String.valueOf(val))).get("Symbol")) + "')");
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void localesTable() {
		DatabaseMetaData meta;
		try {
			meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, "locale", 
				     new String[] {"TABLE"});
			if(res.next())
			{
				return;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		DBConnection.update("CREATE TABLE IF NOT EXISTS locale ("
				+ "localeID varchar(20),"
				+ "name varchar(255),"
				+ "PRIMARY KEY(localeID))");

		String data = Downloader.getData(allValidLocalesURL);

		JSONParser parser = new JSONParser();

		try {
			Iterator<?> i = ((JSONArray) ((JSONObject) parser.parse(data)).get("Locales")).iterator();
			while(i.hasNext())
			{
				String val = String.valueOf(i.next());
				DBConnection.update("INSERT INTO locale VALUES ('"
						+ String.valueOf(((JSONObject) parser.parse(String.valueOf(val))).get("Code")) + "','"
						+ String.valueOf(((JSONObject) parser.parse(String.valueOf(val))).get("Name")) + "')");
			}
		}catch(ParseException e)
		{
			e.printStackTrace();
		}
	}
}
