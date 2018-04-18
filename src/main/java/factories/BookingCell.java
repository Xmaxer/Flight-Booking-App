package factories;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import objects.Customer;
import objects.FlightBooking;

public class BookingCell extends ListCell<Customer>{

	@Override
	public void updateItem(Customer item, boolean empty)
	{
		super.updateItem(item, empty);
		if(item != null && !empty)
		{
			VBox result = new VBox();
			result.setAlignment(Pos.CENTER);
			FlightBooking booking = item.getBooking();
			Text outboundLoc = new Text(booking.getAirportOutbound().getAirportID());
			Text inboundLoc = new Text(booking.getAirportInbound().getAirportID());
			Text outboundAirline = new Text(booking.getAirlineDeparture());
			
			HBox h = new HBox();
			
			VBox left = new VBox();
			VBox right = new VBox();
			
			left.getChildren().addAll(outboundLoc);
			right.getChildren().addAll(inboundLoc);
			left.setAlignment(Pos.CENTER);
			right.setAlignment(Pos.CENTER);
			h.setAlignment(Pos.CENTER);
			h.getChildren().addAll(left, right);
			result.getChildren().addAll(outboundAirline, h);
			h.setSpacing(100);
			this.setGraphic(result);
		}
		else
		{
			setText("");
			setGraphic(null);
		}
	}
}
