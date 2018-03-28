package factories;

import javafx.scene.control.ListCell;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import objects.Airport;

public class AirportCell extends ListCell<Airport>{
	
	private final int MAX_LENGTH = 15;
	@Override
	public void updateItem(Airport item, boolean empty)
	{

		super.updateItem(item, empty);
		
		if(item == null || empty)
		{
			setText("");
			setGraphic(null);
		}
		else
		{
			TextFlow flow = new TextFlow();
			Text airportName = new Text(item.getAirport() + "(" + item.getAirportID() + ")\n");
			Text cityName = new Text(item.getCity() + "\n");
			Text countryName = new Text(item.getCountry());
			//double calculation = (airportName.getText().length()*(airportName.getText().length()/3)) * Math.pow(0.9, airportName.getText().length());
			if(airportName.getText().length() > MAX_LENGTH)
				airportName.setText(airportName.getText().substring(0, MAX_LENGTH) + "...\n");
			airportName.setFont(Font.font("System", FontWeight.BOLD, 20));
			cityName.setFont(Font.font(14));
			countryName.setFont(Font.font("System", FontPosture.ITALIC, 10));
			flow.getChildren().addAll(airportName, cityName, countryName);
			setGraphic(flow);
			setText("");
		}

	}
}
