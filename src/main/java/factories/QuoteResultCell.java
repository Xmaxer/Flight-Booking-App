package factories;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import objects.FlightBooking;

public class QuoteResultCell extends ListCell<FlightBooking>{

	@Override
	public void updateItem(FlightBooking item, boolean empty)
	{
		super.updateItem(item, empty);
		if(item == null || empty)
		{
			this.setText("");
			this.setGraphic(null);
		}
		else
		{
			VBox result = new VBox();
			result.setAlignment(Pos.CENTER);
			
			Text price = new Text("€" + String.valueOf(item.getTotalCost()));
			Text outboundLoc = new Text("From: " + item.getAirportOutbound().getAirport());
			Text inboundLoc = new Text("To: " + item.getAirportInbound().getAirport());
			Text outboundAirline = new Text("Airline: " + item.getAirlineDeparture());
			Text inboundAirline = new Text("Airline: " + item.getAirlineReturn());
			if(inboundAirline.getText().substring(9, 13).equals("null"))
				inboundAirline.setText("");
			
			HBox h = new HBox();
			
			VBox left = new VBox();
			VBox right = new VBox();
			
			left.getChildren().addAll(outboundAirline, outboundLoc);
			right.getChildren().addAll(inboundAirline, inboundLoc);
			left.setAlignment(Pos.CENTER);
			right.setAlignment(Pos.CENTER);
			h.setAlignment(Pos.CENTER);
			h.getChildren().addAll(left, right);
			result.getChildren().addAll(price, h);
			h.setSpacing(100);
			this.setGraphic(result);
		}
	}
}
