package gui;

import java.sql.ResultSet;
import java.sql.SQLException;

import databases.DBTables;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AllFlightsContent extends Stage{
	
	@SuppressWarnings("unchecked")
	public AllFlightsContent()
	{
		super();
		VBox root = new VBox();
		Scene scene = new Scene(root, 600, 400);
		this.setScene(scene);
		
		TableView<Flight> table = new TableView<Flight>();
		
		TableColumn<Flight, String> airline = new TableColumn<Flight, String>();
		TableColumn<Flight, String> flightNumber = new TableColumn<Flight, String>();
		TableColumn<Flight, String> airportDeparture = new TableColumn<Flight, String>();
		TableColumn<Flight, String> airportArrival = new TableColumn<Flight, String>();
		TableColumn<Flight, String> departureDate = new TableColumn<Flight, String>();
		TableColumn<Flight, String> numberOfBookings = new TableColumn<Flight, String>();
		
		airline.setText("airline");
		flightNumber.setText("Flight Number");
		airportDeparture.setText("Departure Airport");
		airportArrival.setText("Airport Arrival");
		departureDate.setText("Date");
		numberOfBookings.setText("Number of Bookings");
		airline.setCellValueFactory(new PropertyValueFactory<Flight, String>("airline"));
		flightNumber.setCellValueFactory(new PropertyValueFactory<Flight, String>("flightNumber"));
		airportDeparture.setCellValueFactory(new PropertyValueFactory<Flight, String>("airportDeparture"));
		airportArrival.setCellValueFactory(new PropertyValueFactory<Flight, String>("airportArrival"));
		departureDate.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureDate"));
		numberOfBookings.setCellValueFactory(new PropertyValueFactory<Flight, String>("numberOfBookings"));
		
		table.getColumns().setAll(airline, flightNumber, airportDeparture, airportArrival, departureDate, numberOfBookings);
		
		root.getChildren().addAll(new Text("Results"), table);
		root.setAlignment(Pos.CENTER);
		root.setSpacing(20);
		
		ResultSet rs = DBTables.getAllFlights();
		
		try {
			while(rs.next())
			{
				table.getItems().add(new Flight(rs.getString("airline"), rs.getString("flightnumber"), rs.getString("airportdeparture"), rs.getString("airportarrival"), rs.getString("departuredate"), rs.getString("counter")));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		this.show();
	}
	
	public class Flight {
		SimpleStringProperty airline;
		SimpleStringProperty flightNumber;
		SimpleStringProperty airportDeparture;
		SimpleStringProperty airportArrival;
		SimpleStringProperty departureDate;
		SimpleStringProperty numberOfBookings;
		
		public Flight(String a, String f, String ad, String aa, String dd, String nob)
		{
			this.airline = new SimpleStringProperty(a);
			this.flightNumber = new SimpleStringProperty(f);
			this.airportDeparture = new SimpleStringProperty(ad);
			this.airportArrival = new SimpleStringProperty(aa);
			this.departureDate = new SimpleStringProperty(dd);
			this.numberOfBookings = new SimpleStringProperty(nob);
		}

		public String getAirline() {
			return airline.get();
		}

		public void setAirline(SimpleStringProperty airline) {
			this.airline = airline;
		}

		public String getFlightNumber() {
			return flightNumber.get();
		}

		public void setFlightNumber(SimpleStringProperty flightNumber) {
			this.flightNumber = flightNumber;
		}

		public String getAirportDeparture() {
			return airportDeparture.get();
		}

		public void setAirportDeparture(SimpleStringProperty airportDeparture) {
			this.airportDeparture = airportDeparture;
		}

		public String getAirportArrival() {
			return airportArrival.get();
		}

		public void setAirportArrival(SimpleStringProperty airportArrival) {
			this.airportArrival = airportArrival;
		}

		public String getDepartureDate() {
			return departureDate.get();
		}

		public void setDepartureDate(SimpleStringProperty departureDate) {
			this.departureDate = departureDate;
		}

		public String getNumberOfBookings() {
			return numberOfBookings.get();
		}

		public void setNumberOfBookings(SimpleStringProperty numberOfBookings) {
			this.numberOfBookings = numberOfBookings;
		}
	}
}
