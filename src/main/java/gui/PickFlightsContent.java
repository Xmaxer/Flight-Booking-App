package gui;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import skyscanner.SkyScannerAPI;

public class PickFlightsContent extends VBox{

	private final static double HEIGHT = GUI.FIRST_PAGE_HEIGHT - 50;
	private final static double WIDTH = GUI.FIRST_PAGE_WIDTH;
	public PickFlightsContent()
	{
		this.setMaxSize(WIDTH, HEIGHT);
		this.setMinSize(WIDTH, HEIGHT);
		
		ListView<VBox> results = new ListView<VBox>();
		Text text = new Text("Pick from results below.");
		
		this.getChildren().addAll(text, results);
		
		String data = SkyScannerAPI.getflights(GUI.customer.getBooking());
		JSONParser parser = new JSONParser();
		try {
			Iterator i = ((JSONArray) ((JSONObject) parser.parse(data)).get("Quotes")).iterator();
			while(i.hasNext())
			{
				String s = String.valueOf(i.next());
				boolean direct = Boolean.valueOf(String.valueOf(((JSONObject) parser.parse(s)).get("Direct")));
				if(direct)
				{
					double price = Double.valueOf(String.valueOf(((JSONObject) parser.parse(s)).get("MinPrice")));
					
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
}
