package gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import connections.DBConnection;
import factories.QuoteResultCellFactory;
import interfaces.Actionable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import objects.Airport;
import objects.FlightBooking;
import skyscanner.SkyScannerAPI;
import wrappers.PickFlightsScene;

public class PickFlightsContent extends VBox implements Actionable{

	private final static double HEIGHT = GUI.FIRST_PAGE_HEIGHT - 80;
	private final static double WIDTH = GUI.FIRST_PAGE_WIDTH;
	private ListView<FlightBooking> results;
	private final Text noResultsText = new Text("No results found");
	private final Text text = new Text("Pick from results below.");
	public PickFlightsContent()
	{
		this.setMaxSize(WIDTH, HEIGHT);
		this.setMinSize(WIDTH, HEIGHT);

		results = new ListView<FlightBooking>();
		results.setCellFactory(new QuoteResultCellFactory());

		noResultsText.setFont(Font.font("System", FontWeight.BOLD, 20));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);


	}

	public void setOnAction()
	{
		NavButtons.getNext().setOnAction(new NextButtonAction());
	}
	private class NextButtonAction implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			if(GUI.getMainStage().getScene() instanceof PickFlightsScene)
			{
				FlightBooking fb = results.getSelectionModel().getSelectedItem();
				if(fb != null)
				{
/*					DBConnection.query("SELECT * FROM flight "
							+ "WHERE airportdeparture = '" + fb.getAirportOutbound().getAirportID() + "' "
							+ "AND airportarrival = '" + fb.getAirportOutbound().getAirportID() + "' "
							+ "AND departuredate = '" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss").format(fb.getReturnDate()) + "'");*/
					GUI.customer.setBooking(fb);
				}
				else
					return;
				NavButtons.moveScenes();
			}
		}

	}
	public void updateList()
	{
		//VBox dataPiece = new VBox();
		HashMap<String, String> carriers = new HashMap<String, String>();
		HashMap<String, String[]> locations = new HashMap<String, String[]>();
		String data = SkyScannerAPI.getflights(GUI.customer.getBooking());
		System.out.println(data);
		JSONParser parser = new JSONParser();
		try {
			List<FlightBooking> options = new ArrayList<FlightBooking>();
			Iterator j = ((JSONArray) ((JSONObject) parser.parse(data)).get("Carriers")).iterator();
			while(j.hasNext())
			{
				String s = String.valueOf(j.next());
				carriers.put(String.valueOf(((JSONObject)parser.parse(s)).get("CarrierId")), String.valueOf(((JSONObject)parser.parse(s)).get("Name")));
			}

			j = ((JSONArray) ((JSONObject) parser.parse(data)).get("Places")).iterator();

			while(j.hasNext())
			{
				String s = String.valueOf(j.next());
				locations.put(String.valueOf(((JSONObject)parser.parse(s)).get("PlaceId")), new String[] {String.valueOf(((JSONObject)parser.parse(s)).get("Name")), String.valueOf(((JSONObject)parser.parse(s)).get("SkyscannerCode"))});
			}

			j = ((JSONArray) ((JSONObject) parser.parse(data)).get("Quotes")).iterator();
			while(j.hasNext())
			{
				String s = String.valueOf(j.next());
				boolean direct = Boolean.valueOf(String.valueOf(((JSONObject) parser.parse(s)).get("Direct")));
				if(direct)
				{
					FlightBooking fb = new FlightBooking();
					double price = Double.valueOf(String.valueOf(((JSONObject) parser.parse(s)).get("MinPrice")));
					JSONObject obj = (JSONObject) ((JSONObject) parser.parse(s)).get("OutboundLeg");
					String originID = String.valueOf(obj.get("OriginId"));
					String destinationID = String.valueOf(obj.get("DestinationId"));
					String departureTime = String.valueOf(obj.get("DepartureDate"));
					JSONArray array = ((JSONArray) parser.parse(String.valueOf(obj.get("CarrierIds"))));
					fb.setAirlineDeparture(carriers.get(String.valueOf(array.get(0))));
					fb.setTotalCost(price);
					fb.setAirportOutbound(new Airport(locations.get(originID)[0], "", "", "", locations.get(originID)[1]));
					fb.setAirportInbound(new Airport(locations.get(destinationID)[0], "", "", "", locations.get(destinationID)[1]));
					fb.setOutboundDate(LocalDateTime.parse(departureTime));
					obj = (JSONObject) ((JSONObject) parser.parse(s)).get("InboundLeg");
					if(obj != null)
					{
						array = ((JSONArray) parser.parse(String.valueOf(obj.get("CarrierIds"))));
						fb.setAirlineReturn(carriers.get(String.valueOf(array.get(0))));
						fb.setReturnDate(LocalDateTime.parse(String.valueOf(obj.get("DepartureDate"))));
					}

					options.add(fb);
				}
			}

			results.setItems(FXCollections.observableList(options));
		} catch (ParseException f) {
			f.printStackTrace();
		}

		this.getChildren().removeAll(text, results, noResultsText);
		if(results.getItems().size() == 0)
		{
			this.getChildren().add(noResultsText);
		}
		else
		{
			this.getChildren().addAll(text, results);
		}
	}
}
