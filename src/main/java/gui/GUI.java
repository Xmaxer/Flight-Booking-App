package gui;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application{

	@Override
	public void start(Stage mainStage) throws Exception {
		
		VBox root = new VBox();
		LocationInputScene scene = new LocationInputScene(root);
		mainStage.setScene(scene);
		mainStage.setTitle("Flight booking");
		
		mainStage.show();
	}
	
	public static void launchGUI(String[] args)
	{
		launch(args);
	}
	
}