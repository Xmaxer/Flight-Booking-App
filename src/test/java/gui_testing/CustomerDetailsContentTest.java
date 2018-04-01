package gui_testing;

import org.junit.Test;

import gui.CustomerDetailsContent;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerDetailsContentTest extends Application{

	@Test
	public void testConstructor() {
		launch();
	}

	@Override
	public void start(Stage s) throws Exception {
		VBox root = new VBox();
		s.setScene(new Scene(root, 500, 500));
		root.getChildren().add(new CustomerDetailsContent());
		//root.setAlignment(Pos.CENTER);
		s.show();
	}

}
