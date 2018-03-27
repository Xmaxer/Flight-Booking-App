package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class NavButtons extends HBox{
	private final static int WIDTH = GUI.FIRST_PAGE_WIDTH;
	private final static int HEIGHT = 50;
	public NavButtons(Stage mainStage, List<Scene> scenes)
	{
		List<Button> navButtons = new ArrayList<Button>();
		Button exit = new Button("Exit");
		Button next = new Button("Next");
		Button back = new Button("Back");
		
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
		next.setOnAction(e -> {
			for(int i = 0, size = scenes.size(); i < size; i++)
			{
				if(mainStage.getScene().equals(scenes.get(i)) && i < (size-1))
				{
					mainStage.setScene(scenes.get(i + 1));
				}
			}
		});
	}
}
