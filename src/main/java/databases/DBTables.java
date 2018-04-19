package databases;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import connections.DBConnection;
import gui.GUI;
import gui.KTextField;
import objects.Customer;
import objects.FlightBooking;

public class DBTables {

	public final static String CONTINENT_TNAME = "continent";
	public final static String COUNTRY_TNAME = "country";
	public final static String CITY_TNAME = "city";
	public final static String AIRPORT_TNAME = "airport";
	public final static String CURRENCY_TNAME = "currency";
	public final static String CUSTOMER_TNAME = "customer";
	public final static String FLIGHT_TNAME = "flight";
	public final static String BOOKING_TNAME = "booking";
	
	public static void createTables()
	{
		createCustomerTable();
		createFlightTable();
		createBookingTable();
	}


	private static void createBookingTable() {
		DBConnection.update("CREATE TABLE IF NOT EXISTS " + DBTables.BOOKING_TNAME + " ("
				+ "flightnumber varchar(10),"
				+ "passportnumber varchar(30),"
				+ "seatnumber varchar(3),"
				+ "PRIMARY KEY (flightnumber, seatnumber),"
				+ "FOREIGN KEY (flightnumber) REFERENCES flight(flightnumber) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "FOREIGN KEY (passportnumber) REFERENCES customer(passportnumber) ON DELETE RESTRICT ON UPDATE CASCADE)");
	}

	private static void createFlightTable() {
		DBConnection.update("CREATE TABLE IF NOT EXISTS " + DBTables.FLIGHT_TNAME + " ("
				+ "airline varchar(255),"
				+ "flightnumber varchar(10),"
				+ "airportdeparture varchar(10),"
				+ "airportarrival varchar(10),"
				+ "departuredate datetime,"
				+ "PRIMARY KEY (flightnumber),"
				+ "FOREIGN KEY (airportdeparture) REFERENCES airport(airportID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "FOREIGN KEY (airportarrival) REFERENCES airport(airportID) ON DELETE RESTRICT ON UPDATE CASCADE)");
	}

	private static void createCustomerTable() {
		DBConnection.update("CREATE TABLE IF NOT EXISTS " + DBTables.CUSTOMER_TNAME + " ("
				+ "firstname varchar(255),"
				+ "lastname varchar(255),"
				+ "passportnumber varchar(30),"
				+ "dob date,"
				+ "email varchar(255),"
				+ "telnumber varchar(30),"
				+ "nationality varchar(50),"
				+ "PRIMARY KEY (passportnumber))");
	}

	public static void insertBooking(Customer customer)
	{
		DBConnection.update("INSERT INTO " + DBTables.CUSTOMER_TNAME + " values ("
				+ "'" + customer.getFname() + "',"
				+ "'" + customer.getLname() + "',"
				+ "'" + customer.getPassportNo() + "',"
				+ "'" + DateTimeFormatter.ofPattern("YYYY-MM-dd").format(customer.getDob()) + "',"
				+ "'" + customer.getEmail() + "',"
				+ "'" + customer.getPhoneNo() + "',"
				+ "'" + customer.getNationality() + "')");

		FlightBooking fb = customer.getBooking();
		String fn = "";
		ResultSet rs = DBConnection.query("SELECT * FROM " + DBTables.FLIGHT_TNAME + " "
				+ "WHERE airportdeparture = '" + GUI.customer.getBooking().getAirportOutbound().getAirportID() + "' "
				+ "AND airportarrival = '" + GUI.customer.getBooking().getAirportInbound().getAirportID() + "' "
				+ "AND departuredate = '" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(GUI.customer.getBooking().getOutboundDate()) + "'");

		try {
			if(rs.next())
			{
				fn = rs.getString("flightnumber");
			}
			else
			{
				fn = fb.generateFlightNumber(false);
				DBConnection.update("INSERT INTO " + DBTables.FLIGHT_TNAME + " values ("
						+ "'" + fb.getAirlineDeparture() + "',"
						+ "'" + fn + "',"
						+ "'" + fb.getAirportOutbound().getAirportID() + "',"
						+ "'" + fb.getAirportInbound().getAirportID() + "',"
						+ "'" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(fb.getOutboundDate()) + "'"
						+ ")");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		DBConnection.update("INSERT INTO " + DBTables.BOOKING_TNAME + " values ("
				+ "'" + fn + "',"
				+ "'" + customer.getPassportNo() + "',"
				+ "'" + fb.getSeatNumberDeparture() + "')");

		if(fb.isReturning())
		{
			rs = DBConnection.query("SELECT * FROM " + DBTables.FLIGHT_TNAME + " "
					+ "WHERE airportdeparture = '" + GUI.customer.getBooking().getAirportInbound().getAirportID() + "' "
					+ "AND airportarrival = '" + GUI.customer.getBooking().getAirportOutbound().getAirportID() + "' "
					+ "AND departuredate = '" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(GUI.customer.getBooking().getReturnDate()) + "'");
			try {
				if(rs.next())
				{
					fn = rs.getString("flightnumber");
				}
				else
				{
					fn = fb.generateFlightNumber(true);
					DBConnection.update("INSERT INTO " + DBTables.FLIGHT_TNAME + " values ("
							+ "'" + fb.getAirlineReturn() + "',"
							+ "'" + fn + "',"
							+ "'" + fb.getAirportInbound().getAirportID() + "',"
							+ "'" + fb.getAirportOutbound().getAirportID() + "',"
							+ "'" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(fb.getReturnDate()) + "'"
							+ ")");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			DBConnection.update("INSERT INTO " + DBTables.BOOKING_TNAME + " values ("
					+ "'" + fn + "',"
					+ "'" + customer.getPassportNo() + "',"
					+ "'" + fb.getSeatNumberReturn() + "')");
		}
	}


	public static boolean createGeoLocationsTables() {
		DatabaseMetaData meta;
		try {
			meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, DBTables.AIRPORT_TNAME, 
					new String[] {"TABLE"});
			if(res.next())
			{
				return false;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		DBConnection.update("CREATE TABLE IF NOT EXISTS " + DBTables.CONTINENT_TNAME + " ("
				+ "continentID varchar(10),"
				+ "name varchar(255),"
				+ "PRIMARY KEY (continentID))");
		DBConnection.update("CREATE TABLE IF NOT EXISTS " + DBTables.COUNTRY_TNAME + " ("
				+ "countryID varchar(10),"
				+ "name varchar(255),"
				+ "currencyID varchar(10),"
				+ "continentID varchar(10),"
				+ "FOREIGN KEY (currencyID) REFERENCES " + DBTables.CURRENCY_TNAME  + "(currencyID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "FOREIGN KEY (continentID) REFERENCES " + DBTables.CONTINENT_TNAME + "(continentID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "PRIMARY KEY (countryID))");
		DBConnection.update("CREATE TABLE IF NOT EXISTS " + DBTables.CITY_TNAME + " ("
				+ "cityID varchar(10),"
				+ "name varchar(255),"
				+ "latitude varchar(20),"
				+ "longtitude varchar(20),"
				+ "countryID varchar(10),"
				+ "FOREIGN KEY (countryID) REFERENCES " + DBTables.COUNTRY_TNAME +"(countryID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "PRIMARY KEY (cityID))");
		DBConnection.update("CREATE TABLE IF NOT EXISTS " + DBTables.AIRPORT_TNAME + " ("
				+ "airportID varchar(10),"
				+ "name varchar(255),"
				+ "latitude varchar(20),"
				+ "longtitude varchar(20),"
				+ "cityID varchar(10),"
				+ "FOREIGN KEY (cityID) REFERENCES " + DBTables.CITY_TNAME + "(cityID) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "PRIMARY KEY (airportID))");
		return true;
	}


	public static boolean createCurrenciesTable() {
		DatabaseMetaData meta;
		try {
			meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, DBTables.CURRENCY_TNAME, 
					new String[] {"TABLE"});
			if(res.next())
			{
				return false;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		DBConnection.update("CREATE TABLE IF NOT EXISTS " + DBTables.CURRENCY_TNAME +" ("
				+ "currencyID varchar(10),"
				+ "symbol varchar(10),"
				+ "PRIMARY KEY(currencyID))");
		return true;
	}


/*	public static boolean createLocalesTable() {
		DatabaseMetaData meta;
		try {
			meta = DBConnection.getConnection().getMetaData();
			ResultSet res = meta.getTables(null, null, "locale", 
					new String[] {"TABLE"});
			if(res.next())
			{
				return false;
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		DBConnection.update("CREATE TABLE IF NOT EXISTS locale ("
				+ "localeID varchar(20),"
				+ "name varchar(255),"
				+ "PRIMARY KEY(localeID))");
		return true;
	}*/


	public static void updateBooking(Customer customer) {

		DBConnection.update("UPDATE " + DBTables.CUSTOMER_TNAME + " "
				+ "SET firstname='" + customer.getFname() + "', "
				+ "lastname='" + customer.getLname() + "', "
				+ "dob='" + DateTimeFormatter.ofPattern("YYYY-MM-dd").format(customer.getDob()) + "', "
				+ "email='" + customer.getEmail() + "', "
				+ "telnumber='" + customer.getPhoneNo() + "', "
				+ "nationality='" + customer.getNationality() + "' "
				+ "WHERE passportnumber='" + customer.getPassportNo() + "'");

		ResultSet rs = DBConnection.query("SELECT flightnumber FROM " + DBTables.FLIGHT_TNAME + " "
				+ "WHERE airportdeparture='" + customer.getBooking().getAirportOutbound().getAirportID() + "' AND "
				+ "airportarrival='" + customer.getBooking().getAirportInbound().getAirportID() + "' AND "
				+ "departuredate='" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(customer.getBooking().getOutboundDate()) + "'");

		String fn = "";
		try {
			if(rs.next())
			{
				fn = rs.getString("flightnumber");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(!fn.isEmpty())
		{
			DBConnection.update("UPDATE " + DBTables.BOOKING_TNAME + " "
					+ "SET seatnumber='" + customer.getBooking().getSeatNumberDeparture() + "'"
							+ " WHERE flightnumber='" + fn + "' AND passportnumber='" + customer.getPassportNo() +"'");
		}
	}


	public static ResultSet getAllFlights() {
		return DBConnection.query("SELECT f.airline, f.flightnumber, f.airportdeparture, f.airportarrival, f.departuredate , COUNT(*) AS counter " + 
				"FROM " + DBTables.FLIGHT_TNAME + " f, " + DBTables.BOOKING_TNAME + " b " + 
				"WHERE b.flightnumber = f.flightnumber " + 
				"GROUP BY b.flightnumber");
	}


	public static ResultSet getFlightForCustomer() {
		return DBConnection.query("SELECT * FROM " + DBTables.FLIGHT_TNAME + " "
				+ "WHERE airportdeparture = '" + GUI.customer.getBooking().getAirportOutbound().getAirportID() + "' "
				+ "AND airportarrival = '" + GUI.customer.getBooking().getAirportInbound().getAirportID() + "' "
				+ "AND departuredate = '" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(GUI.customer.getBooking().getOutboundDate()) + "'");
	}


	public static ResultSet getAllFlightsByFlightNumber(ResultSet rs) {
		try {
			return DBConnection.query("SELECT * FROM " + DBTables.BOOKING_TNAME + " "
					+ "WHERE flightnumber = '" + rs.getString("flightnumber") +"'");
		} catch (SQLException e) {
			e.printStackTrace();
		};
		return null;
	}


	public static ResultSet getAllReturningFlightsForCustomer() {
		return DBConnection.query("SELECT * FROM " + DBTables.FLIGHT_TNAME + " "
				+ "WHERE airportdeparture = '" + GUI.customer.getBooking().getAirportInbound().getAirportID() + "' "
				+ "AND airportarrival = '" + GUI.customer.getBooking().getAirportOutbound().getAirportID() + "' "
				+ "AND departuredate = '" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(GUI.customer.getBooking().getReturnDate()) + "'");
	}


	public static ResultSet getInputLocation(String newVal) {
		return DBConnection.query("SELECT c.name AS city, ct.name AS country, a.name AS airport, a.airportID, con.name AS continent FROM " + DBTables.COUNTRY_TNAME + " ct JOIN " + DBTables.CITY_TNAME + " c ON c.countryID = ct.countryID " + 
				"JOIN " + DBTables.AIRPORT_TNAME + " a ON a.cityID = c.cityID "
				+ "JOIN " + DBTables.CONTINENT_TNAME + " con ON con.continentID = ct.continentID " + 
				"WHERE c.name LIKE '%" + newVal + "%' " + 
				"OR ct.name LIKE '%" + newVal + "%' "
				+ "OR a.name LIKE '%" + newVal + "%' " + 
				"ORDER BY c.name ASC LIMIT 5");
	}


	public static ResultSet getAllFlightsForSelectedCustomer(Customer c) {
		return DBConnection.query("SELECT * FROM " + DBTables.FLIGHT_TNAME + " "
				+ "WHERE airportdeparture = '" + c.getBooking().getAirportOutbound().getAirportID() + "' "
				+ "AND airportarrival = '" + c.getBooking().getAirportInbound().getAirportID() + "' "
				+ "AND departuredate = '" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(c.getBooking().getOutboundDate()) + "'");
	}


	@SuppressWarnings("rawtypes")
	public static ResultSet findCustomer(KTextField passportField) {
		return DBConnection.query("SELECT * FROM " + DBTables.BOOKING_TNAME + " b JOIN " + DBTables.FLIGHT_TNAME + " f ON f.flightnumber = b.flightnumber JOIN " + DBTables.CUSTOMER_TNAME + " c ON c.passportnumber = b.passportnumber "
				+ "WHERE c.passportnumber = '" + passportField.getText() + "'");
	}
}
