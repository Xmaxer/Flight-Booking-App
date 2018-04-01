package databases;

import java.time.format.DateTimeFormatter;

import connections.DBConnection;
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
		String fn = fb.generateFlightNumber(false);
		DBConnection.update("INSERT INTO flight values ("
				+ "'" + fb.getAirlineDeparture() + "',"
				+ "'" + fn + "',"
				+ "'" + fb.getAirportOutbound().getAirportID() + "',"
				+ "'" + fb.getAirportInbound().getAirportID() + "',"
				+ "'" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(fb.getOutboundDate()) + "'"
				+ ")");

		DBConnection.update("INSERT INTO booking values ("
				+ "'" + fn + "',"
				+ "'" + customer.getPassportNo() + "',"
				+ "'" + fb.getSeatNumberDeparture() + "')");
		fn = fb.generateFlightNumber(true);
		if(fb.isReturning())
		{
			DBConnection.update("INSERT INTO flight values ("
					+ "'" + fb.getAirlineReturn() + "',"
					+ "'" + fn + "',"
					+ "'" + fb.getAirportInbound().getAirportID() + "',"
					+ "'" + fb.getAirportOutbound().getAirportID() + "',"
					+ "'" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(fb.getReturnDate()) + "'"
					+ ")");
			DBConnection.update("INSERT INTO booking values ("
					+ "'" + fn + "',"
					+ "'" + customer.getPassportNo() + "',"
					+ "'" + fb.getSeatNumberReturn() + "')");
		}
	}
}
