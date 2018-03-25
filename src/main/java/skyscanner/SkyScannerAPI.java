package skyscanner;

import connections.DBConnection;
import exceptions.NoDatabaseConnectedException;
import hidden.Constants;

public class SkyScannerAPI {

	private final String API_KEY = Constants.SKYSCANNER_API_KEY;
	private final String allValidLocalesURL = "http://partners.api.skyscanner.net/apiservices/reference/v1.0/locales?apiKey=" + API_KEY;
	private final String allCurrenciesURL = "http://partners.api.skyscanner.net/apiservices/reference/v1.0/currencies?apiKey=" + API_KEY;
	private final String allGeoLocationsURL = "http://partners.api.skyscanner.net/apiservices/geo/v1.0?apiKey=" + API_KEY;
	
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
	}

	private static void currenciesTable() {
	}

	private static void localesTable() {
		
	}
}
