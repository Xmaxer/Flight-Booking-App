package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import connections.DBConnection;
import constants.Filter;
import factories.AirportCellFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import objects.Airport;

public class LocationInputContent extends VBox{

	private final static double HEIGHT = GUI.FIRST_PAGE_HEIGHT - 50;
	private final static double WIDTH = GUI.FIRST_PAGE_WIDTH;

	public LocationInputContent()
	{
		this.setMinSize(WIDTH, HEIGHT);
		this.setMaxSize(WIDTH, HEIGHT);
		HBox fields = new HBox();

		Text top = new Text("Search flights using fields below.");
		top.setFont(Font.font(20));
		this.setSpacing(10);
		this.setAlignment(Pos.TOP_CENTER);
		this.getStyleClass().add("start-box");
		fields.setAlignment(Pos.CENTER);
		fields.setSpacing(50);
		
		KTextField<Airport> outboundField = new KTextField<Airport>();
		KTextField<Airport> inboundField = new KTextField<Airport>();
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
		//	inboundField.setFocusTraversable(false);
		//	outboundField.setFocusTraversable(false);
		//inboundField.getResultsBox().prefHeightProperty().bind(Bindings.size(itemListProperty).multiply(LIST_CELL_HEIGHT));

		inboundField.getResultsBox().setCellFactory(new AirportCellFactory());
		outboundField.getResultsBox().setCellFactory(new AirportCellFactory());
		fields.getChildren().addAll(outboundField.getContainer(), inboundField.getContainer());
		this.getChildren().addAll(top, fields);

		inboundField.textProperty().addListener(new FlightSearchListener(inboundField));
		outboundField.textProperty().addListener(new FlightSearchListener(outboundField));
		inboundField.focusedProperty().addListener(new FlightSearchFocusedListener(inboundField));
		outboundField.focusedProperty().addListener(new FlightSearchFocusedListener(outboundField));
	}
	private void start(Parent root) {


		if(root.getClass().isInstance(new VBox()))
		{
			VBox start = (VBox) root;
			HBox fields = new HBox();

			Text top = new Text("Search flights using fields below.");
			top.setFont(Font.font(20));
			start.setSpacing(10);
			start.setAlignment(Pos.TOP_CENTER);
			start.getStyleClass().add("start-box");
			fields.setAlignment(Pos.CENTER);
			fields.setSpacing(50);
			
			KTextField<Airport> outboundField = new KTextField<Airport>();
			KTextField<Airport> inboundField = new KTextField<Airport>();
			inboundField.setMaxLength(30);
			outboundField.setMaxLength(30);
			inboundField.setPromptText("Inbound location");
			inboundField.setFilter(Filter.LETTERS_ONLY);
			inboundField.setMaxWidthCustom(200);
			outboundField.setPromptText("Outbound location");
			outboundField.setFilter(Filter.LETTERS_ONLY);
			outboundField.setMaxWidthCustom(200);
			//	inboundField.setFocusTraversable(false);
			//	outboundField.setFocusTraversable(false);
			//inboundField.getResultsBox().prefHeightProperty().bind(Bindings.size(itemListProperty).multiply(LIST_CELL_HEIGHT));

			inboundField.getResultsBox().setCellFactory(new AirportCellFactory());
			outboundField.getResultsBox().setCellFactory(new AirportCellFactory());
			fields.getChildren().addAll(outboundField.getContainer(), inboundField.getContainer());
			start.getChildren().addAll(top, fields);

			inboundField.textProperty().addListener(new FlightSearchListener(inboundField));
			outboundField.textProperty().addListener(new FlightSearchListener(outboundField));
			inboundField.focusedProperty().addListener(new FlightSearchFocusedListener(inboundField));
			outboundField.focusedProperty().addListener(new FlightSearchFocusedListener(outboundField));
		}
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
			//System.out.println(newVal);
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
			ResultSet result = DBConnection.query("SELECT c.name AS city, ct.name AS country, a.name AS airport, a.airportID, con.name AS continent FROM country ct JOIN  city c ON c.countryID = ct.countryID " + 
					"JOIN airport a ON a.cityID = c.cityID "
					+ "JOIN continent con ON con.continentID = ct.continentID " + 
					"WHERE c.name LIKE '%" + newVal + "%' " + 
					"OR ct.name LIKE '%" + newVal + "%' "
					+ "OR a.name LIKE '%" + newVal + "%' " + 
					"ORDER BY c.name ASC LIMIT 5");

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

				if(results == 0 || newVal.isEmpty())
				{
					field.getResultsBox().setItems(FXCollections.observableList(Arrays.asList(new Airport[] {new Airport("", "",  "", "", "No result")})));

				}
				else
				{
					list = FXCollections.observableList(items);
					field.getResultsBox().setItems(list);
				}
				field.getResultsBox().refresh();
				field.getResultsBox().setMaxHeight(65*field.getResultsBox().getItems().size());
				field.getResultsBox().setMinHeight(65*field.getResultsBox().getItems().size());
			} catch (SQLException e) {
				e.printStackTrace();
			}		}	
	}
}
