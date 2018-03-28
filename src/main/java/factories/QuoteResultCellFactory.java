package factories;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class QuoteResultCellFactory implements Callback<ListView<VBox>, ListCell<VBox>>{

	@Override
	public ListCell<VBox> call(ListView<VBox> arg0) {
		return new QuoteResultCell();
	}

}
