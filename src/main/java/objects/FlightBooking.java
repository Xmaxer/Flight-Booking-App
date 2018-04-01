package objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import connections.DBConnection;

public class FlightBooking implements Cloneable{

	private Airport airportOutbound;
	private Airport airportInbound;
	private Double totalCost;
	private String airlineDeparture;
	private String airlineReturn;
	private LocalDateTime departureDate;
	private LocalDateTime returnDate;
	private String seatNumberDeparture;
	private String seatNumberReturn;
	private boolean returning;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		FlightBooking clone = (FlightBooking) super.clone();
		clone.setAirportInbound(((Airport) clone.getAirportInbound() != null) ? (Airport) clone.getAirportInbound().clone() : null);
		clone.setAirportOutbound(((Airport) clone.getAirportOutbound() != null) ? (Airport) clone.getAirportOutbound().clone() : null);
		return clone;
	}

	public FlightBooking(Airport airportOutbound, Airport airportInbound, double cost, String airlineOutbound, String airlineInbound)
	{
		this.airportOutbound = airportOutbound;
		this.airportInbound = airportInbound;
		this.totalCost = cost;
		this.airlineDeparture = airlineOutbound;
		this.airlineReturn = airlineInbound;
		this.returning = false;
	}

	public FlightBooking()
	{
		this.returning = false;
	}
	public Airport getAirportOutbound() {
		return airportOutbound;
	}

	public void setAirportOutbound(Airport airportOutbound) {
		try {

			this.airportOutbound = airportOutbound != null ? airportOutbound.clone() : null;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public Airport getAirportInbound() {
		return airportInbound;
	}

	public void setAirportInbound(Airport airportInbound) {
		try {
			System.out.println(airportInbound);
			this.airportInbound = airportInbound != null ? airportInbound.clone() : null;
			System.out.println(this.airportInbound);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getAirlineDeparture() {
		return airlineDeparture;
	}

	public void setAirlineDeparture(String airlineDeparture) {
		this.airlineDeparture = airlineDeparture;
	}

	public String getAirlineReturn() {
		return airlineReturn;
	}

	public void setAirlineReturn(String airlineReturn) {
		this.airlineReturn = airlineReturn;
	}

	public LocalDateTime getOutboundDate() {
		return departureDate;
	}

	public void setOutboundDate(LocalDateTime outboundDate) {
		this.departureDate = outboundDate;
	}

	public LocalDateTime getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDateTime returnDate) {
		if(returnDate != null)
			returning = true;
		this.returnDate = returnDate;
	}

	public boolean isReturning() {
		return returning;
	}

	public void setReturning(boolean returning) {
		this.returning = returning;
	}
	public String getSeatNumberDeparture() {
		return seatNumberDeparture;
	}

	public void setSeatNumberDeparture(String seatNumberDeparture) {
		this.seatNumberDeparture = seatNumberDeparture;
	}

	public String getSeatNumberReturn() {
		return seatNumberReturn;
	}

	public void setSeatNumberReturn(String seatNumberReturn) {
		if(seatNumberReturn != null)
			returning = true;
		this.seatNumberReturn = seatNumberReturn;
	}

	public String generateFlightNumber(boolean returning)
	{
		String fn = "";

		String[] split;
		if(returning)		
			split = airlineReturn.split(" ");
		else
			split = airlineDeparture.split(" ");
		for(String s : split)
			fn += s.toUpperCase().charAt(0);

		ResultSet results = DBConnection.query("SELECT flightnumber FROM flight "
				+ "WHERE flightnumber LIKE '%" + fn + "%'");
		int latest = 0;
		try {
			while(results.next())
			{
				String n = results.getString("flightnumber");
				String f = "";
				for(char c : n.toCharArray())
					if(Character.isDigit(c))
						f += String.valueOf(c);
				int ne = Integer.valueOf(f);
				if(ne > latest)
					latest = ne;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fn + String.format("%04d", ++latest);
	}
}
