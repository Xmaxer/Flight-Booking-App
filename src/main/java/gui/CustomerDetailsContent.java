package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import constants.Filter;
import databases.DBTables;
import interfaces.Actionable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import objects.Customer;
import wrappers.CustomerDetailsScene;
@SuppressWarnings("rawtypes")
public class CustomerDetailsContent extends VBox implements Actionable{

	private static final int WIDTH = GUI.FIRST_PAGE_WIDTH;
	private static final int HEIGHT = GUI.FIRST_PAGE_HEIGHT - 50;
	private KTextField fnameField;
	private KTextField lnameField;
	private KTextField phoneField;
	private KTextField passportField;
	private KTextField emailField;
	private KTextField countryField;
	private DatePicker dob;
	private static ComboBox<String> seatsDeparture;
	private static ComboBox<String> seatsReturn;

	public CustomerDetailsContent()
	{
		Text startup = new Text("Fill out all the fields below.");
		this.setMaxSize(WIDTH, HEIGHT);
		this.setMinSize(WIDTH, HEIGHT);
		this.setPrefSize(WIDTH, HEIGHT);
		this.setAlignment(Pos.CENTER);
		//this.setSpacing(30);
		fnameField = new KTextField();
		lnameField = new KTextField();
		phoneField = new KTextField();
		passportField = new KTextField();
		emailField = new KTextField();
		countryField = new KTextField();
		dob = new DatePicker();
		seatsDeparture = new ComboBox<String>();
		seatsReturn = new ComboBox<String>();
		Label dobLabel = new Label("Date of birth: ");
		seatsDeparture.setVisibleRowCount(10);
		Label seatsLabelDeparture = new Label("Seat number (outbound): ");
		Label seatsLabelReturn = new Label("Seat number (return): ");

/*		for(int i = 'A'; i < 'F'; i++)
		{
			for(int j = 1; j <= 30; j++)
				seatsDeparture.getItems().add(String.valueOf(((char) i)) + String.valueOf(j));
		}
		for(int i = 'A'; i < 'F'; i++)
		{
			for(int j = 1; j <= 30; j++)
				seatsReturn.getItems().add(String.valueOf(((char) i)) + String.valueOf(j));
		}*/
		VBox fields = new VBox();

		List<KTextField> list = new ArrayList<KTextField>();
		list.addAll(Arrays.asList(fnameField, lnameField, phoneField, passportField, emailField, countryField));

		for(KTextField field : list)
		{
			field.setLabelled(true);
		}
		HBox seatChoice = new HBox();
		HBox departureSeats = new HBox();
		HBox returnSeats = new HBox();
		HBox names = new HBox();
		HBox contact = new HBox();
		HBox origin = new HBox();
		VBox dobBox = new VBox();
		departureSeats.getChildren().addAll(seatsLabelDeparture, seatsDeparture);
		returnSeats.getChildren().addAll(seatsLabelReturn, seatsReturn);
		dobBox.getChildren().addAll(dobLabel, dob);
		seatChoice.getChildren().addAll(departureSeats, returnSeats);
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
		returnSeats.setSpacing(10);
		returnSeats.setAlignment(Pos.CENTER);
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

		fields.setAlignment(Pos.TOP_CENTER);
		fields.setSpacing(10);
		this.getChildren().addAll(startup, fields);

	}
	@Override
	public void setOnAction() {
		NavButtons.getNext().setOnAction(new NextButtonAction());
	}

	private class NextButtonAction implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			if(GUI.getMainStage().getScene() instanceof CustomerDetailsScene)
			{
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
					GUI.customer.getBooking().setSeatNumberDeparture(seatsDeparture.getSelectionModel().getSelectedItem());
				else
					return;
				if(GUI.customer.getBooking().isReturning())
					if(seatsReturn.getSelectionModel().getSelectedItem() != null)
						GUI.customer.getBooking().setSeatNumberReturn(seatsReturn.getSelectionModel().getSelectedItem());
					else
						return;
				
				Alert confirmation = new Alert(AlertType.CONFIRMATION);
				confirmation.setContentText("Are all the details correct?");
				confirmation.setHeaderText("Confirmation");
				Optional<ButtonType> result = confirmation.showAndWait();
				if(result.get() == ButtonType.OK)
				{
					DBTables.insertBooking(GUI.customer);
					GUI.customer = new Customer();
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

	}

	public static ComboBox<String> getSeatsReturn() {
		return seatsReturn;
	}
	public static ComboBox<String> getSeatsDeparture() {
		return seatsDeparture;
	}
	public static void setSeatsReturn(ComboBox<String> seatsReturn) {
		CustomerDetailsContent.seatsReturn = seatsReturn;
	}
}
