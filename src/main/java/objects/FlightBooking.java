package objects;

import java.time.LocalDate;

public class FlightBooking implements Cloneable{

	private Airport airportOutbound;
	private Airport airportInbound;
	private Double totalCost;
	private String airlineOutbound;
	private String airlineInbound;
	private LocalDate outboundDate;
	private LocalDate returnDate;
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
		this.airlineOutbound = airlineOutbound;
		this.airlineInbound = airlineInbound;
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
			
			this.airportInbound = airportInbound != null ? airportInbound.clone() : null;
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

	public String getAirlineOutbound() {
		return airlineOutbound;
	}

	public void setAirlineOutbound(String airlineOutbound) {
		this.airlineOutbound = airlineOutbound;
	}

	public String getAirlineInbound() {
		return airlineInbound;
	}

	public void setAirlineInbound(String airlineInbound) {
		this.airlineInbound = airlineInbound;
	}

	public LocalDate getOutboundDate() {
		return outboundDate;
	}

	public void setOutboundDate(LocalDate outboundDate) {
		this.outboundDate = outboundDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public boolean isReturning() {
		return returning;
	}

	public void setReturning(boolean returning) {
		this.returning = returning;
	}
}
