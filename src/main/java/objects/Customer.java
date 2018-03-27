package objects;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private String fname;
	private String lname;
	private String phoneNo;
	private String passportNo;
	private List<FlightBooking> bookings = new ArrayList<FlightBooking>();
	
	public Customer(String fname, String lname, String phoneNo, String passportNo)
	{
		this.fname = fname;
		this.lname = lname;
		this.phoneNo = phoneNo;
		this.passportNo = passportNo;
	}
	
	public void makeBooking(FlightBooking booking)
	{
		try {
			bookings.add((FlightBooking) booking.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
}
