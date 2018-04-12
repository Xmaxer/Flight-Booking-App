package databases;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import connections.DBConnection;
import gui.CustomerDetailsContent;
import gui.GUI;
import objects.Customer;
import objects.FlightBooking;

public class DBTables {

	public static void createTables()
	{
		createCustomerTable();
		createFlightTable();
		createBookingTable();
	}


	private static void createBookingTable() {
		DBConnection.update("CREATE TABLE IF NOT EXISTS booking ("
				+ "flightnumber varchar(10),"
				+ "passportnumber varchar(30),"
				+ "seatnumber varchar(3),"
				+ "PRIMARY KEY (flightnumber, seatnumber),"
				+ "FOREIGN KEY (flightnumber) REFERENCES flight(flightnumber) ON DELETE RESTRICT ON UPDATE CASCADE,"
				+ "FOREIGN KEY (passportnumber) REFERENCES customer(passportnumber) ON DELETE RESTRICT ON UPDATE CASCADE)");
	}

	private static void createFlightTable() {
		DBConnection.update("CREATE TABLE IF NOT EXISTS flight ("
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
		DBConnection.update("CREATE TABLE IF NOT EXISTS customer ("
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
		DBConnection.update("INSERT INTO customer values ("
				+ "'" + customer.getFname() + "',"
				+ "'" + customer.getLname() + "',"
				+ "'" + customer.getPassportNo() + "',"
				+ "'" + DateTimeFormatter.ofPattern("YYYY-MM-dd").format(customer.getDob()) + "',"
				+ "'" + customer.getEmail() + "',"
				+ "'" + customer.getPhoneNo() + "',"
				+ "'" + customer.getNationality() + "')");

		FlightBooking fb = customer.getBooking();
		String fn = "";
		ResultSet rs = DBConnection.query("SELECT * FROM flight "
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
				DBConnection.update("INSERT INTO flight values ("
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
		if(GUI.customer.getBooking().isReturning())
		{

		}

		DBConnection.update("INSERT INTO booking values ("
				+ "'" + fn + "',"
				+ "'" + customer.getPassportNo() + "',"
				+ "'" + fb.getSeatNumberDeparture() + "')");
		
		if(fb.isReturning())
		{
			rs = DBConnection.query("SELECT * FROM flight "
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
					DBConnection.update("INSERT INTO flight values ("
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

			DBConnection.update("INSERT INTO booking values ("
					+ "'" + fn + "',"
					+ "'" + customer.getPassportNo() + "',"
					+ "'" + fb.getSeatNumberReturn() + "')");
		}
	}


	public static void createGeoLocationsTables() {
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
	}


	public static void createCurrenciesTable() {
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
	}


	public static void createLocalesTable() {
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
	}
}
