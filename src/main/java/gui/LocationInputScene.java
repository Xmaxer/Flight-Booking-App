package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import connections.DBConnection;
import constants.Filter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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
			inboundField.setSearchable(true);
			inboundField.setPromptText("Inbound location");
			inboundField.setFilter(Filter.LETTERS_ONLY);
			inboundField.setMaxWidth(200);
			outboundField.setPromptText("Outbound location");
			outboundField.setFilter(Filter.LETTERS_ONLY);
			outboundField.setMaxWidth(200);
			inboundField.setFocusTraversable(false);
			outboundField.setFocusTraversable(false);

			fields.getChildren().addAll(outboundField, inboundField);
			start.getChildren().addAll(fields);
			
			inboundField.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {

					if(!newVal.isEmpty())
					{
						inboundField.getResultsBox().clear();
						ResultSet result = DBConnection.query("SELECT c.name AS city, ct.name AS country, a.name AS airport, a.airportID FROM country ct JOIN  city c ON c.countryID = ct.countryID " + 
								"JOIN airport a ON a.cityID = c.cityID " + 
								"WHERE c.name LIKE '%" + newVal + "%' " + 
								"OR ct.name LIKE '%" + newVal + "%' " + 
								"ORDER BY c.name ASC LIMIT 5");

						try {
							int results = 0;
							Collection<Node> resultBoxesCollection = null;
							List<Node> resultBoxes = new ArrayList<Node>();

							while(result.next())
							{
								String country = result.getString("country");
								String city = result.getString("city");
								String airport = result.getString("airport");
								String airportID = result.getString("airportID");

								VBox resultBox = new VBox();
								Text airportName = new Text(airport + "(" + airportID + ")");
								airportName.setFont(Font.font("System", FontWeight.BOLD, 20));
								Text cityName = new Text(city);
								cityName.setFont(new Font(14));
								Text countryName = new Text(country);
								countryName.setFont(Font.font("System", FontPosture.ITALIC, 10));
								resultBox.setPadding(new Insets(10));

								resultBox.setOpacity(0.8);

								resultBox.getChildren().addAll(airportName, cityName, countryName);
								

								resultBox.setBackground(new Background(new BackgroundFill(Paint.valueOf("#4280f4"), new CornerRadii(10), null)));
								resultBox.setAlignment(Pos.CENTER_LEFT);
								resultBox.setMinHeight(75);
								resultBox.setMinWidth(100);



								resultBoxes.add(resultBox);
								resultBoxesCollection = resultBoxes;
								results++;
							}
							if(results == 0)
							{
								inboundField.getResultsBox().addDeadResult();
							}
							else if(resultBoxesCollection != null)
							{
								inboundField.getResultsBox().addResults(resultBoxesCollection);
							}
							/*if(!inboundField.getResultsBox().isShowing())
								inboundField.getResultsBox().display();*/
							inboundField.requestFocus();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					/*else
						inboundField.getResultsBox().hide();*/
				}

			});
		}

	}
}
