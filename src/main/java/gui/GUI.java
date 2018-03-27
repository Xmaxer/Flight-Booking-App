package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application{

	public static final int FIRST_PAGE_HEIGHT = 500;
	public static final int FIRST_PAGE_WIDTH = 500;
	@Override
	public void start(Stage mainStage) throws Exception {
		
		VBox firstPageRoot = new VBox();
		VBox secondPageRoot = new VBox();
		firstPageRoot.setAlignment(Pos.TOP_CENTER);
		
		List<Scene> scenes = new ArrayList<Scene>();
		
		Scene firstPageScene = new Scene(firstPageRoot, FIRST_PAGE_WIDTH, FIRST_PAGE_HEIGHT);
		scenes.add(firstPageScene);
		
		Scene secondPageScene = new Scene(secondPageRoot, FIRST_PAGE_WIDTH, FIRST_PAGE_HEIGHT);
		scenes.add(secondPageScene);
		
		LocationInputContent searchInputs = new LocationInputContent();
		
		
		NavButtons buttonsContainer = new NavButtons(mainStage, scenes);
		
		secondPageRoot.getChildren().addAll(buttonsContainer);
		
		firstPageRoot.getChildren().addAll(searchInputs, buttonsContainer);
		
		mainStage.setScene(firstPageScene);
		mainStage.setTitle("Flight booking");
		mainStage.show();
		mainStage.setResizable(false);
		firstPageScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		mainStage.setOnHiding(e -> System.exit(0));
		
	}
	
	public static void launchGUI(String[] args)
	{
		launch(args);
	}
	
}
