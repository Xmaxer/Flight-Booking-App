package factories;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import objects.Customer;

public class BookingCellFactory implements Callback<ListView<Customer>, ListCell<Customer>>{

	@Override
	public ListCell<Customer> call(ListView<Customer> arg0) {
		return new BookingCell();
	}
}
