package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import constants.Filter;
import databases.DBTables;
import factories.AirportCellFactory;
import interfaces.Actionable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import objects.Airport;
import wrappers.LocationInputScene;

public class LocationInputContent extends VBox implements Actionable{

	private final static double HEIGHT = GUI.FIRST_PAGE_HEIGHT - 80;
	private final static double WIDTH = GUI.FIRST_PAGE_WIDTH;
	private KTextField<Airport> outboundField;
	private KTextField<Airport> inboundField;
	private DatePicker forwardDate;
	private DatePicker returnDate;
	private CheckBox returning;

	public LocationInputContent()
	{
		forwardDate = new DatePicker();
		returnDate = new DatePicker();
		returnDate.setDisable(true);
		forwardDate.setMaxWidth(200);
		returnDate.setMaxWidth(200);
		forwardDate.setTooltip(new Tooltip("Outbound date"));
		returnDate.setTooltip(new Tooltip("Return date"));
		HBox dateFields = new HBox();
		dateFields.getChildren().addAll(forwardDate, returnDate);
		dateFields.setSpacing(50);
		dateFields.setAlignment(Pos.CENTER);
		this.setMinSize(WIDTH, HEIGHT);
		this.setMaxSize(WIDTH, HEIGHT);
		HBox fields = new HBox();
		HBox checkboxContainer = new HBox();
		checkboxContainer.setAlignment(Pos.CENTER_RIGHT);
		checkboxContainer.setPadding(new Insets(0, 25, 0, 0));
		returning = new CheckBox();
		checkboxContainer.getChildren().add(returning);
		returning.setText("Return");
		Text top = new Text("Search flights using fields below.");
		top.setFont(Font.font(20));
		this.setSpacing(10);
		this.setAlignment(Pos.TOP_CENTER);
		this.getStyleClass().add("start-box");
		fields.setAlignment(Pos.CENTER);
		fields.setSpacing(50);
		outboundField = new KTextField<Airport>();
		inboundField = new KTextField<Airport>();
		inboundField.setSearchable(true);
		outboundField.setSearchable(true);
		inboundField.setMaxLength(30);
		outboundField.setMaxLength(30);
		inboundField.setPromptText("Inbound location");
		inboundField.setFilter(Filter.LETTERS_ONLY);
		inboundField.setMaxWidthCustom(200);
		outboundField.setPromptText("Outbound location");
		outboundField.setFilter(Filter.LETTERS_ONLY);
		outboundField.setMaxWidthCustom(200);

		inboundField.getResultsBox().setCellFactory(new AirportCellFactory());
		outboundField.getResultsBox().setCellFactory(new AirportCellFactory());
		fields.getChildren().addAll(outboundField.getContainer(), inboundField.getContainer());
		this.getChildren().addAll(top, dateFields, checkboxContainer, fields);

		inboundField.textProperty().addListener(new FlightSearchListener(inboundField));
		outboundField.textProperty().addListener(new FlightSearchListener(outboundField));
		inboundField.focusedProperty().addListener(new FlightSearchFocusedListener(inboundField));
		outboundField.focusedProperty().addListener(new FlightSearchFocusedListener(outboundField));
		returning.selectedProperty().addListener((obs, o, n) -> {
			if(n)
			{
				returnDate.setDisable(false);
				GUI.customer.getBooking().setReturning(true);
			}
			else
			{
				returnDate.setDisable(true);
				GUI.customer.getBooking().setReturning(false);
			}
		});
	}
	private class FlightSearchFocusedListener implements ChangeListener<Boolean>
	{
		private KTextField<Airport> field;
		public FlightSearchFocusedListener(KTextField<Airport> field)
		{
			this.field = field;
		}
		@Override
		public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean newVal) {
			if(!newVal)
			{
				field.getResultsBox().setOpacity(0);
			}
			else
				field.getResultsBox().setOpacity(1);
		}

	}
	private class FlightSearchListener implements ChangeListener<String>
	{
		private KTextField<Airport> field;
		public FlightSearchListener(KTextField<Airport> field)
		{
			this.field = field;
		}
		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String newVal) {
			ResultSet result = DBTables.getInputLocation(newVal);

			try {
				int results = 0;
				ObservableList<Airport> list = null;
				List<Airport> items = new ArrayList<Airport>();
				while(result.next())
				{
					String country = result.getString("country");
					String city = result.getString("city");
					String airport = result.getString("airport");
					String airportID = result.getString("airportID");
					String continent = result.getString("continent");

					items.add(new Airport(airport, city, country, continent, airportID));
					results++;
				}

				if((results == 0 || newVal.isEmpty()) && field.getResultsBox().getSelectionModel().getSelectedItem() == null)
				{
					field.getResultsBox().setItems(FXCollections.observableList(Arrays.asList(new Airport[] {new Airport("", "", "", "", "No result")})));
				}
				else if(results != 0)
				{
					list = FXCollections.observableList(items);
					field.getResultsBox().setItems(list);
				}
				field.getResultsBox().setMaxHeight(65*field.getResultsBox().getItems().size());
				field.getResultsBox().setMinHeight(65*field.getResultsBox().getItems().size());
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}	
	}
	public KTextField<Airport> getOutboundField() {
		return outboundField;
	}
	public KTextField<Airport> getInboundField() {
		return inboundField;
	}
	@Override
	public void setOnAction() {
		NavButtons.getNext().setOnAction(new NextButtonAction());
	}

	private class NextButtonAction implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			if(GUI.getMainStage().getScene() instanceof LocationInputScene)
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");

				if(forwardDate.getValue() != null && LocalDate.now().compareTo(forwardDate.getValue()) <= 1)
					GUI.customer.getBooking().setOutboundDate(forwardDate.getValue().atTime(12, 0));
				else
				{
					alert.setContentText("Invalid departure date.");
					alert.showAndWait();
					return;
				}
				if(returning.isSelected())
					if(returnDate.getValue() != null && LocalDate.now().compareTo(returnDate.getValue()) <= 1)
						GUI.customer.getBooking().setReturnDate(returnDate.getValue().atTime(12, 0));
					else
					{
						alert.setContentText("Invalid return date.");
						alert.showAndWait();
						return;
					}
				else
					GUI.customer.getBooking().setReturnDate(null);

				if(GUI.customer.getBooking().getReturnDate() != null)
				{
					if(forwardDate.getValue().compareTo(returnDate.getValue()) >= 0)
					{
						alert.setContentText("Departure date must occur BEFORE arrival.");
						alert.showAndWait();
						return;
					}
				}
				if(inboundField.getResultsBox().getSelectionModel().getSelectedItem() != null && !inboundField.getResultsBox().getSelectionModel().getSelectedItem().getCity().isEmpty())
					GUI.customer.getBooking().setAirportInbound((Airport) inboundField.getResultsBox().getSelectionModel().getSelectedItem());
				else
				{
					alert.setContentText("No valid inbound destination selected.");
					inboundField.shakeAnimation();
					alert.showAndWait();
					return;
				}
				if(outboundField.getResultsBox().getSelectionModel().getSelectedItem() != null && !outboundField.getResultsBox().getSelectionModel().getSelectedItem().getCity().isEmpty())
					GUI.customer.getBooking().setAirportOutbound((Airport) outboundField.getResultsBox().getSelectionModel().getSelectedItem());
				else
				{
					alert.setContentText("No valid outbound destination selected.");
					outboundField.shakeAnimation();
					alert.showAndWait();
					return;
				}
				if(outboundField.getResultsBox().getSelectionModel().getSelectedItem().getAirportID().equalsIgnoreCase(inboundField.getResultsBox().getSelectionModel().getSelectedItem().getAirportID()))
				{
					alert.setContentText("Departure and arrival airports cannot be the same.");
					alert.showAndWait();
					return;
				}
				NavButtons.moveScenes();
			}
		}

	}
}
