package objects;

public class FlightBooking implements Cloneable{

	private Airport airportOutbound;
	private Airport airportInbound;
	private double totalCost;
	private String airlineOutbound;
	private String airlineInbound;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		FlightBooking clone = (FlightBooking) super.clone();
		clone.setAirportInbound((Airport) clone.getAirportInbound().clone());
		clone.setAirportOutbound((Airport) clone.getAirportOutbound().clone());
		return clone;
	}
	
	public FlightBooking(Airport airportOutbound, Airport airportInbound, double cost, String airlineOutbound, String airlineInbound)
	{
		this.airportOutbound = airportOutbound;
		this.airportInbound = airportInbound;
		this.totalCost = cost;
		this.airlineOutbound = airlineOutbound;
		this.airlineInbound = airlineInbound;
	}

	public Airport getAirportOutbound() {
		return airportOutbound;
	}

	public void setAirportOutbound(Airport airportOutbound) {
		this.airportOutbound = airportOutbound;
	}

	public Airport getAirportInbound() {
		return airportInbound;
	}

	public void setAirportInbound(Airport airportInbound) {
		this.airportInbound = airportInbound;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
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
}
