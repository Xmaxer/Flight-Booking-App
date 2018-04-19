package factories;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import objects.FlightBooking;

public class QuoteResultCellFactory implements Callback<ListView<FlightBooking>, ListCell<FlightBooking>>{

	@Override
	public ListCell<FlightBooking> call(ListView<FlightBooking> arg0) {
		return new QuoteResultCell();
	}

}
