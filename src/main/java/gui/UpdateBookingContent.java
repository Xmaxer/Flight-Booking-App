package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import connections.DBConnection;
import constants.Filter;
import databases.DBTables;
import factories.BookingCellFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import objects.Airport;
import objects.Customer;
import objects.FlightBooking;
import wrappers.CustomerDetailsScene;
@SuppressWarnings("rawtypes")
public class UpdateBookingContent extends VBox{

	private Button updateBtn;
	private KTextField fnameField;
	private KTextField lnameField;
	private KTextField phoneField;
	private KTextField passportField;
	private KTextField emailField;
	private KTextField countryField;
	private DatePicker dob;
	private ComboBox<String> seatsDeparture;
	private ListView<Customer> results;
	//private CustomerDetailsContent fields;
	public UpdateBookingContent()
	{
		this.setSpacing(20);
		this.setAlignment(Pos.CENTER);
		updateBtn = new Button("Update");
		Button selectBtn = new Button("Select");
		Button searchBtn = new Button("Search");
		fnameField = new KTextField();
		lnameField = new KTextField();
		phoneField = new KTextField();
		passportField = new KTextField();
		emailField = new KTextField();
		countryField = new KTextField();
		dob = new DatePicker();
		seatsDeparture = new ComboBox<String>();
		//fields = new CustomerDetailsContent();
		results = new ListView<Customer>();
		List<KTextField> list = new ArrayList<KTextField>();
		list.addAll(Arrays.asList(fnameField, lnameField, phoneField, passportField, emailField, countryField));

		for(KTextField field : list)
		{
			field.setLabelled(true);
		}
		passportField.getLabel().setText("Passport number:");
		passportField.setFilter(Filter.NUMBERS_ONLY);
		results.setCellFactory(new BookingCellFactory());

		this.getChildren().setAll(passportField.getContainer(), searchBtn);
		selectBtn.setOnAction(e -> {
			if(results.getSelectionModel().getSelectedItem() != null)
			{
				Customer s = results.getSelectionModel().getSelectedItem();
				fnameField.setText(s.getFname());
				lnameField.setText(s.getLname());
				dob.setValue(s.getDob());
				countryField.setText(s.getNationality());
				phoneField.setText(s.getPhoneNo());
				passportField.setText(s.getPassportNo());
				emailField.setText(s.getEmail());
				HBox seatChoice = new HBox();
				HBox departureSeats = new HBox();
				HBox names = new HBox();
				HBox contact = new HBox();
				HBox origin = new HBox();
				VBox dobBox = new VBox();
				VBox fields = new VBox();
				Label dobLabel = new Label("Date of birth: ");
				seatsDeparture.setVisibleRowCount(10);
				Label seatsLabelDeparture = new Label("Seat number: ");
				departureSeats.getChildren().addAll(seatsLabelDeparture, seatsDeparture);
				dobBox.getChildren().addAll(dobLabel, dob);
				seatChoice.getChildren().addAll(departureSeats);
				dobBox.setAlignment(Pos.CENTER_LEFT);
				names.getChildren().addAll(fnameField.getContainer(), lnameField.getContainer(), dobBox);
				contact.getChildren().addAll(phoneField.getContainer(), emailField.getContainer());
				origin.getChildren().addAll(passportField.getContainer(), countryField.getContainer());
				origin.setSpacing(20);
				origin.setAlignment(Pos.CENTER);
				seatChoice.setSpacing(30);
				seatChoice.setAlignment(Pos.CENTER);
				departureSeats.setSpacing(10);
				departureSeats.setAlignment(Pos.CENTER);
				names.setSpacing(20);
				contact.setSpacing(20);
				names.setAlignment(Pos.CENTER);
				contact.setAlignment(Pos.CENTER);
				fields.getChildren().addAll(names, contact, origin, seatChoice);

				countryField.getLabel().setText("Nationality: ");
				fnameField.getLabel().setText("First name: ");
				lnameField.getLabel().setText("Last name: ");
				phoneField.getLabel().setText("Phone number: ");
				passportField.getLabel().setText("Passport number:");
				emailField.getLabel().setText("Email: ");
				countryField.setFilter(Filter.LETTERS_ONLY);
				fnameField.setFilter(Filter.LETTERS_ONLY);
				lnameField.setFilter(Filter.LETTERS_ONLY);
				phoneField.setFilter(Filter.NUMBERS_ONLY);
				passportField.setFilter(Filter.NUMBERS_ONLY);
				passportField.setDisable(true);
				fields.setAlignment(Pos.TOP_CENTER);
				fields.setSpacing(10);
				setPossibleSeats(s);
				seatsDeparture.getSelectionModel().select(s.getBooking().getSeatNumberDeparture());
				this.getChildren().setAll(fields, updateBtn);
			}
		});
		searchBtn.setOnAction(e -> {
			if(!passportField.getText().isEmpty())
			{
				ResultSet rs = DBConnection.query("SELECT * FROM booking b JOIN flight f ON f.flightnumber = b.flightnumber JOIN customer c ON c.passportnumber = b.passportnumber "
						+ "WHERE c.passportnumber = '" + passportField.getText() + "'");

				try {
					List<Customer> resultsList = new ArrayList<Customer>();
					while(rs.next())
					{
						Customer c = new Customer(rs.getString("firstname"), rs.getString("lastname"), rs.getString("telnumber"), rs.getString("passportnumber"));
						c.setNationality(rs.getString("nationality"));
						c.setEmail(rs.getString("email"));
						c.setDob(LocalDate.parse(rs.getString("dob")));
						FlightBooking booking = new FlightBooking();
						booking.setSeatNumberDeparture(rs.getString("seatnumber"));
						booking.setAirportOutbound(new Airport("", "", "", "", rs.getString("airportdeparture")));
						booking.setAirportInbound(new Airport("", "", "", "", rs.getString("airportarrival")));
						booking.setOutboundDate(LocalDateTime.parse(rs.getString("departuredate").replaceFirst(" ", "T")));
						booking.setAirlineDeparture(rs.getString("airline"));
						c.setBooking(booking);
						resultsList.add(c);
					}

					if(!resultsList.isEmpty())
					{
						results.getItems().setAll(resultsList);
						this.getChildren().setAll(new Text("Results"), results, selectBtn);
					}
					else
					{
						Alert noResults = new Alert(AlertType.ERROR);
						noResults.setContentText("Passport number not found. Try again.");
						noResults.setHeaderText("Error");
						noResults.showAndWait();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});

		updateBtn.setOnAction(new UpdateButtonAction());

	}
	private class UpdateButtonAction implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			if(!fnameField.getText().isEmpty() && !lnameField.getText().isEmpty())
			{
				GUI.customer.setFname(fnameField.getText());
				GUI.customer.setLname(lnameField.getText());
			}
			else
			{
				fnameField.shakeAnimation();
				lnameField.shakeAnimation();
				return;
			}
			if(!phoneField.getText().isEmpty())
				GUI.customer.setPhoneNo(phoneField.getText());
			else
			{
				phoneField.shakeAnimation();
				return;
			}
			if(!emailField.getText().isEmpty())
				GUI.customer.setEmail(emailField.getText());
			else
			{
				emailField.shakeAnimation();
				return;
			}
			if(!passportField.getText().isEmpty())
				GUI.customer.setPassportNo(passportField.getText());
			else
			{
				passportField.shakeAnimation();
				return;
			}
			if(!countryField.getText().isEmpty())
				GUI.customer.setNationality(countryField.getText());
			else
			{
				countryField.shakeAnimation();
				return;
			}
			if(dob.getValue() != null)
				GUI.customer.setDob(dob.getValue());
			else
				return;
			if(seatsDeparture.getSelectionModel().getSelectedItem() != null)
				results.getSelectionModel().getSelectedItem().getBooking().setSeatNumberDeparture(seatsDeparture.getSelectionModel().getSelectedItem());
			else
				return;

			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.setContentText("Are all the details correct?");
			confirmation.setHeaderText("Confirmation");
			Optional<ButtonType> result = confirmation.showAndWait();
			if(result.get() == ButtonType.OK)
			{
				GUI.customer.setBooking(results.getSelectionModel().getSelectedItem().getBooking());
				DBTables.updateBooking(GUI.customer);

				confirmation.close();
				confirmation.setContentText("Changes successful! Close all dialogues to finish");
				confirmation.setHeaderText("Success!");
				confirmation.showAndWait();
				updateBtn.setDisable(true);
				GUI.customer = new Customer();
				GUI.getMainStage().close();
				Platform.runLater(() -> {
					try {
						new GUI().start(new Stage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
			else
			{
				return;
			}		
		}
	}

	private void setPossibleSeats(Customer c)
	{
		ResultSet rs = DBConnection.query("SELECT * FROM flight "
				+ "WHERE airportdeparture = '" + c.getBooking().getAirportOutbound().getAirportID() + "' "
				+ "AND airportarrival = '" + c.getBooking().getAirportInbound().getAirportID() + "' "
				+ "AND departuredate = '" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(c.getBooking().getOutboundDate()) + "'");

		List<String> seats = new ArrayList<String>();
		try {
			if(rs.next())
			{
				ResultSet rs2 = DBConnection.query("SELECT * FROM booking "
						+ "WHERE flightnumber = '" + rs.getString("flightnumber") +"'");

				while(rs2.next())
					seats.add(rs2.getString("seatnumber"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		for(int i = 'A'; i < 'F'; i++)
		{
			for(int j = 1; j <= 30; j++)
			{
				String seatNo = String.valueOf(((char) i)) + String.valueOf(j);
				if(!seats.contains(seatNo) || c.getBooking().getSeatNumberDeparture().equals(seatNo))
				{
					seatsDeparture.getItems().add(seatNo);
				}
			}
		}
	}
}
