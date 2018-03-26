package gui;

import constants.Filter;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LocationInputScene extends Scene{

	private final static double HEIGHT = 500;
	private final static double WIDTH = 500;
	
	public LocationInputScene(Parent root)
	{
		super(root, HEIGHT, WIDTH);
		start(root);
		
		
	}
	public LocationInputScene(Parent root, double width, double height) {
		super(root, width, height);
		
		start(root);
		
	}
	private void start(Parent root) {
		
		if(root.getClass().isInstance(new VBox()))
		{
			VBox start = (VBox) root;
			HBox fields = new HBox();
			
			
			start.setAlignment(Pos.CENTER);
			fields.setAlignment(Pos.CENTER);
			fields.setSpacing(50);
			
			KTextField outboundField = new KTextField();
			KTextField inboundField = new KTextField();
			
			inboundField.setPromptText("Inbound location");
			inboundField.setFilter(Filter.LETTERS_ONLY);
			inboundField.setMaxWidth(200);
			outboundField.setPromptText("Outbound location");
			outboundField.setFilter(Filter.LETTERS_ONLY);
			outboundField.setMaxWidth(200);
			
			fields.getChildren().addAll(outboundField, inboundField);
			start.getChildren().add(fields);
		}
		
	}
}
