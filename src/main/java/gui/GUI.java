package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import objects.Customer;
import wrappers.CustomerDetailsScene;
import wrappers.LocationInputScene;
import wrappers.PickFlightsScene;

public class GUI extends Application{

	public static final int FIRST_PAGE_HEIGHT = 500;
	public static final int FIRST_PAGE_WIDTH = 500;
	public static Customer customer = new Customer();

	@Override
	public void start(Stage mainStage) throws Exception {


		VBox firstPageRoot = new VBox();
		VBox secondPageRoot = new VBox();
		VBox thirdPageRoot = new VBox();


		List<Scene> scenes = new ArrayList<Scene>();

		LocationInputScene firstPageScene = new LocationInputScene(firstPageRoot, FIRST_PAGE_WIDTH, FIRST_PAGE_HEIGHT);
		scenes.add(firstPageScene);

		PickFlightsScene secondPageScene = new PickFlightsScene(secondPageRoot, FIRST_PAGE_WIDTH, FIRST_PAGE_HEIGHT);
		scenes.add(secondPageScene);

		CustomerDetailsScene thirdPageScene = new CustomerDetailsScene(thirdPageRoot, FIRST_PAGE_WIDTH, FIRST_PAGE_HEIGHT);
		scenes.add(thirdPageScene);

		LocationInputContent searchInputs = new LocationInputContent();
		CustomerDetailsPage customerDetails = new CustomerDetailsPage();
		PickFlightsContent flightChoice = new PickFlightsContent();

		NavButtons buttonsContainer = new NavButtons(mainStage, scenes);
		//	NavButtons buttonsContainer2 = new NavButtons(mainStage, scenes);

		thirdPageRoot.getChildren().addAll(customerDetails, buttonsContainer);
		secondPageRoot.getChildren().addAll(flightChoice, buttonsContainer);
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
