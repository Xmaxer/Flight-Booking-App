package skyscanner;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import connections.DBConnection;
import connections.Downloader;
import databases.DBTables;
import exceptions.NoDatabaseConnectedException;
import hidden.Constants;
import objects.FlightBooking;

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
		DBTables.createGeoLocationsTables();

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
		DBTables.createCurrenciesTable();

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
		DBTables.createLocalesTable();

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
	
	public static String getflights(FlightBooking booking)
	{
		String market = "IE";
		String currency = "EUR";
		String locale = "en-GB";
		String origin = booking.getAirportOutbound().getAirportID();
		String destination = booking.getAirportInbound().getAirportID();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String outDate = booking.getOutboundDate().format(format);
		String returnDate = (booking.getReturnDate() != null) ? booking.getReturnDate().format(format) : "";
		
		String url = quotesBaseURL + market + "/" + currency + "/" + locale + "/" + origin + "/" + destination + "/" + outDate + "/" + returnDate + "?apiKey=" + Constants.SKYSCANNER_API_KEY;
		String results = Downloader.getData(url);
		
		return results;
	}
}
