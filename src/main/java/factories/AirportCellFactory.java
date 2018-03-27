package factories;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import objects.Airport;

public class AirportCellFactory implements Callback<ListView<Airport>, ListCell<Airport>>{

	@Override
	public ListCell<Airport> call(ListView<Airport> arg0) {
		return new AirportCell();
	}

}
