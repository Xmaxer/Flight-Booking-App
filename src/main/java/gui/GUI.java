package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databases.DBTables;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objects.Customer;
import wrappers.CustomerDetailsScene;
import wrappers.LocationInputScene;
import wrappers.PickFlightsScene;

public class GUI extends Application{

	public static final int FIRST_PAGE_HEIGHT = 600;
	public static final int FIRST_PAGE_WIDTH = 500;
	public static Customer customer = new Customer();
	private static Stage mainStage;

	@Override
	public void start(Stage mainStage) throws Exception {

		GUI.mainStage = mainStage;
		VBox firstPageRoot = new VBox();
		VBox secondPageRoot = new VBox();
		VBox thirdPageRoot = new VBox();

		List<Scene> scenes = new ArrayList<Scene>();

		LocationInputScene firstPageScene = new LocationInputScene(firstPageRoot, FIRST_PAGE_WIDTH, FIRST_PAGE_HEIGHT);
		scenes.add(firstPageScene);

		PickFlightsScene secondPageScene = new PickFlightsScene(secondPageRoot, FIRST_PAGE_WIDTH, FIRST_PAGE_HEIGHT);
		scenes.add(secondPageScene);

		CustomerDetailsScene thirdPageScene = new CustomerDetailsScene(thirdPageRoot, FIRST_PAGE_WIDTH, FIRST_PAGE_HEIGHT);
		scenes.add(thirdPageScene);

		LocationInputContent searchInputs = new LocationInputContent();
		searchInputs.setOnAction();

		CustomerDetailsContent customerDetails = new CustomerDetailsContent();
		PickFlightsContent flightChoice = new PickFlightsContent();


		NavButtons buttonsContainer = new NavButtons(mainStage, scenes);

		thirdPageRoot.getChildren().addAll(customerDetails, buttonsContainer);
		secondPageRoot.getChildren().addAll(flightChoice, buttonsContainer);
		firstPageRoot.getChildren().addAll(searchInputs, buttonsContainer);

		MenuBar mb = new MenuBar();
		Menu options = new Menu("Options");
		Menu view = new Menu("View");
		mb.getMenus().addAll(options, view);
		MenuItem updateBooking = new MenuItem("Update booking...");
		MenuItem viewFlights = new MenuItem("Flights");
		//MenuItem viewBookings = new MenuItem("Bookings");
		options.getItems().add(updateBooking);
		view.getItems().addAll(viewFlights/*, viewBookings*/);
		firstPageRoot.getChildren().add(0, mb);
		
		mainStage.sceneProperty().addListener((obs, o ,n)->{
			try {
			((VBox) n.getRoot()).getChildren().add(0, mb);
			} catch (IllegalArgumentException e) {};
			
			options.setDisable(false);
			if(n instanceof LocationInputScene)
			{
				((LocationInputContent) ((VBox) n.getRoot()).getChildren().get(1)).setOnAction();
			}
			if(n instanceof PickFlightsScene)
			{
				VBox r = (VBox) n.getRoot();
				PickFlightsContent c = (PickFlightsContent) r.getChildren().get(1);
				c.updateList();
				c.setOnAction();
			}
			if(n instanceof CustomerDetailsScene)
			{
				options.setDisable(true);
				((CustomerDetailsContent) ((VBox) n.getRoot()).getChildren().get(1)).setOnAction();
				CustomerDetailsContent.getSeatsReturn().setDisable(!GUI.customer.getBooking().isReturning());
				ResultSet rs = DBTables.getFlightForCustomer();

				List<String> seats = new ArrayList<String>();
				try {
					if(rs.next())
					{
						ResultSet rs2 = DBTables.getAllFlightsByFlightNumber(rs);

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
						if(!seats.contains(seatNo))
						{
							CustomerDetailsContent.getSeatsDeparture().getItems().add(seatNo);
						}
					}
				}
				if(GUI.customer.getBooking().isReturning())
				{
					rs = DBTables.getAllReturningFlightsForCustomer();
					seats = new ArrayList<String>();
					try {
						if(rs.next())
						{
							ResultSet rs2 = DBTables.getAllFlightsByFlightNumber(rs);

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
							if(!seats.contains(seatNo))
							{
								CustomerDetailsContent.getSeatsReturn().getItems().add(seatNo);
							}
						}
					}
				}
			}
		});
		mainStage.setScene(firstPageScene);
		mainStage.setTitle("Flight booking");
		mainStage.show();
		mainStage.setResizable(false);
		firstPageScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
		updateBooking.setOnAction(e -> {
			UpdateBookingContent ubc = new UpdateBookingContent();
			Scene updateFlightsScene = new Scene(ubc, 400, 400);
			Stage updateFlightsStage = new Stage();
			updateFlightsStage.setTitle("Update Booking");
			updateFlightsStage.setScene(updateFlightsScene);
			updateFlightsStage.initModality(Modality.APPLICATION_MODAL);
			updateFlightsStage.show();
		});
		
		viewFlights.setOnAction(e -> {
			new AllFlightsContent();
		});
		
	}

	public static void launchGUI(String[] args)
	{
		launch(args);
	}

	public static Stage getMainStage() {
		return mainStage;
	}
	public static void closeGUI()
	{
		mainStage.close();
	}

}
