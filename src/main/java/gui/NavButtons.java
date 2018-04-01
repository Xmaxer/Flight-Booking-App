package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wrappers.CustomerDetailsScene;
import wrappers.LocationInputScene;
import wrappers.PickFlightsScene;

public class NavButtons extends HBox{
	private final static int WIDTH = GUI.FIRST_PAGE_WIDTH;
	private final static int HEIGHT = 50;
	private static Button exit = new Button("Exit");
	private static Button next = new Button("Next");
	private static Button back = new Button("Back");
	private static Stage mainStage;
	private static List<Scene> scenes;
	public NavButtons(Stage mainStage, List<Scene> scenes)
	{
		NavButtons.scenes = scenes;
		NavButtons.mainStage = mainStage;
		List<Button> navButtons = new ArrayList<Button>();

		navButtons.addAll(Arrays.asList(exit, next, back));

		for(Button btn : navButtons)
		{
			btn.setPrefWidth(100);
			btn.setPrefHeight(40);
		}
		this.getChildren().addAll(back, next, exit);
		this.setSpacing(20);
		this.setMinSize(WIDTH, HEIGHT);
		this.setMaxSize(WIDTH, HEIGHT);
		this.setAlignment(Pos.CENTER);

		exit.setOnAction(e -> System.exit(0));
		
		mainStage.sceneProperty().addListener((obs, o ,n)->{
			if(n instanceof LocationInputScene)
			{
				((LocationInputContent) ((VBox) n.getRoot()).getChildren().get(0)).setOnAction();
			}
			if(n instanceof PickFlightsScene)
			{
				VBox r = (VBox) n.getRoot();
				PickFlightsContent c = (PickFlightsContent) r.getChildren().get(0);
				c.updateList();
				c.setOnAction();
			}
			if(n instanceof CustomerDetailsScene)
			{
				((CustomerDetailsContent) ((VBox) n.getRoot()).getChildren().get(0)).setOnAction();
				if(GUI.customer.getBooking().isReturning())
					CustomerDetailsContent.getSeatsReturn().setDisable(false);
				else
					CustomerDetailsContent.getSeatsReturn().setDisable(true);
			}
		});
		
		back.setOnAction(e -> {
			for(int i = 0, size = scenes.size(); i < size; i++)
				if(mainStage.getScene().equals(scenes.get(i)) && i > 0)
					moveScene(i, -1);
		});
	}

	public static Button getExit() {
		return exit;
	}

	public static Button getNext() {
		return next;
	}

	public static Button getBack() {
		return back;
	}

	public static void moveScenes()
	{
		for(int i = 0, size = scenes.size(); i < size; i++)
		{
			if(mainStage.getScene().equals(scenes.get(i)) && i < (size-1))
			{
				moveScene(i, 1);
				break;
			}
		}
	}
	private static void moveScene(int sceneNo, int i)
	{
		ObservableList<Node> obslist = scenes.get(sceneNo).getRoot().getChildrenUnmodifiable();
		List<Node> list = new ArrayList<Node>();

		NavButtons moveable = null;
		Iterator<Node> iterator = obslist.iterator();
		while(iterator.hasNext())
		{
			Node current = iterator.next();
			if(!(current instanceof NavButtons))
			{
				list.add(current);
			}
			else
			{
				moveable = (NavButtons) current;
			}
		}

		VBox root = new VBox();
		root.getChildren().setAll(list);

		scenes.get(sceneNo).setRoot(root);

		obslist = scenes.get(sceneNo + i).getRoot().getChildrenUnmodifiable();
		list = new ArrayList<Node>();

		iterator = obslist.iterator();
		while(iterator.hasNext())
		{
			Node current = iterator.next();
			if(!(current instanceof NavButtons))
			{
				list.add(current);
			}
			else
			{
				moveable = (NavButtons) current;
			}
		}

		VBox root2 = new VBox();
		root2.getChildren().addAll(list);
		root2.getChildren().add(moveable);
		scenes.get(sceneNo + i).setRoot(root2);

		mainStage.setScene(scenes.get(sceneNo + i));
	}
}
