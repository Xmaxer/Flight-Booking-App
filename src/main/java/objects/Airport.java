package objects;

public class Airport implements Cloneable{
	
	@Override
	protected Airport clone() throws CloneNotSupportedException {
		return (Airport) super.clone();
	}

	private String airport;
	private String city;
	private String country;
	private String continent;
	private String airportID;
	
	public Airport(String airport, String city, String country, String continent, String airportID)
	{
		this.airport = airport;
		this.city = city;
		this.country = country;
		this.continent = continent;
		this.airportID = airportID;
	}

	@Override
	public String toString()
	{
		return airport + " (" + airportID + ")";
	}
	public String getAirport() {
		return airport;
	}

	public void setAirport(String airport) {
		this.airport = airport;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getAirportID() {
		return airportID;
	}

	public void setAirportID(String airportID) {
		this.airportID = airportID;
	}
}
