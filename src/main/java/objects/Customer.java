package objects;

import java.time.LocalDate;

public class Customer {
	private String fname;
	private String lname;
	private String phoneNo;
	private String passportNo;
	private FlightBooking booking;
	private LocalDate dob;
	private String email;
	private String nationality;
	
	public Customer(String fname, String lname, String phoneNo, String passportNo)
	{
		this.fname = fname;
		this.lname = lname;
		this.phoneNo = phoneNo;
		this.passportNo = passportNo;
		this.booking = new FlightBooking();
	}
	
	public Customer()
	{
		this.fname = "";
		this.lname = "";
		this.phoneNo = "";
		this.passportNo = "";
		this.booking = new FlightBooking();
	}
	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public FlightBooking getBooking() {
		return booking;
	}

	public void setBooking(FlightBooking booking) {
		try {
			this.booking = (FlightBooking) booking.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}



}
