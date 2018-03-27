package gui;

import java.util.Collection;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ResultsBox extends Stage{

	private VBox root;
	private Scene scene;
	private KTextField field;
	private boolean triggeredByShow;

	public ResultsBox(KTextField field)
	{
		super();
		triggeredByShow = false;
		this.field = field;
		setupStage();
	}


	private void setupStage() {

		root = new VBox();
		scene = new Scene(root);
		setScene(scene);
		root.setBackground(Background.EMPTY);
		scene.setFill(Color.TRANSPARENT);
		initStyle(StageStyle.TRANSPARENT);
		initModality(Modality.NONE);
		setAlwaysOnTop(true);
		field.focusedProperty().addListener((obs, o, n) ->{
			if(!triggeredByShow)
			{
				if(!n && isShowing())
				{
					hide();
				}
				else if(n && !isShowing())
				{
					addDeadResult();
					display();
				}
				triggeredByShow = false;
			}
		});
		field.textProperty().addListener((obs, o , n) -> {
			if(!n.isEmpty())
			{
				display();
				triggeredByShow = false;
			}
		});
	}

	public void addResults(Collection<Node> c)
	{
		root.getChildren().clear();
		root.getChildren().addAll(c);
		calculateParameters();
		//show();
	}
	public void addDeadResult()
	{
		VBox resultBox = new VBox();
		Text defaultText = new Text("No results");
		resultBox.getChildren().add(defaultText);
		root.getChildren().setAll(resultBox);
		calculateParameters();
		//hide();
	}
	public void calculateParameters()
	{
		Bounds bounds = field.localToScreen(field.getBoundsInLocal());
		int x = (int) bounds.getMinX();
		int y = (int) bounds.getMinY();
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();
		this.setX(x);
		this.setY(y + height);
		this.setWidth(width + 300);
		int childHeight = (int) ((VBox) root.getChildren().get(0)).getMinHeight();
		this.setHeight(root.getChildren().size()*((childHeight <= 0) ? 50 : childHeight));
	}

	public void clear()
	{
		root.getChildren().clear();
		addDeadResult();
	}
	public KTextField getField() {
		return field;
	}

	public void setField(KTextField field) {
		this.field = field;
	}

	public void display()
	{
		triggeredByShow = true;
		show();
		ObservableList<Window> windows = Window.getWindows();
		windows.get(0).requestFocus();
	}
}
