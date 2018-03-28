package factories;

import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import objects.Airport;

public class QuoteResultCell extends ListCell<VBox>{

	@Override
	public void updateItem(VBox item, boolean empty)
	{
		super.updateItem(item, empty);
		if(item == null || empty)
		{
			this.setText("");
			this.setGraphic(null);
		}
		else
		{
			
		}
	}
}
