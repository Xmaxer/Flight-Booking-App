package gui;

import constants.Filter;
import exceptions.InvalidShakeSpeedException;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class KTextField<T> extends TextField{

	private int maxLength;
	private Filter filter;
	public static final double DEFAULT_STRENGTH = 5;
	public static final double DEFAULT_SPEED = 0.5;
	public static final int DEFAULT_MAX_LENGTH = 25;
	private VBox container;
	private ListView<T> resultsBox;

	public KTextField(String text)
	{
		super(text);
		start();
	}
	public KTextField()
	{
		super();
		start();
	}

	public void shakeAnimation(double strength, double speed)
	{
		if(speed <= 0)
			try {
				throw new InvalidShakeSpeedException(speed + " is an invalid speed. Speed must be > 0.");
			} catch (InvalidShakeSpeedException e) {
				e.printStackTrace();
				return;
			}
		SequentialTransition st = new SequentialTransition();
		st.setAutoReverse(false);
		st.setCycleCount(1);
		for(double i = strength; i > 0; i-=speed)
		{
			TranslateTransition tt = new TranslateTransition();
			TranslateTransition tt2 = new TranslateTransition();
			tt2.setDuration(Duration.millis((i)*2));
			tt2.setCycleCount(1);
			tt2.setAutoReverse(false);
			tt2.setNode(this);
			tt2.setToX(-i);
			tt.setDuration(Duration.millis(i));
			tt.setCycleCount(1);
			tt.setAutoReverse(false);
			tt.setNode(this);
			tt.setToX(i);
			st.getChildren().addAll(tt, tt2);
		}
		st.play();
	}
	public void shakeAnimation()
	{
		shakeAnimation(DEFAULT_STRENGTH, DEFAULT_SPEED);
	}

	public void setMaxWidthCustom(double value)
	{
		this.setMaxWidth(value);
		this.setMinWidth(value);
		resultsBox.setMaxWidth(this.getMaxWidth());

	}
	private void start() {
		KTextField<T> itself = this;
		container = new VBox();
		maxLength = DEFAULT_MAX_LENGTH;
		filter = Filter.ALLOW_ALL;
		resultsBox = new ListView<T>();
		resultsBox.setMaxHeight(0);
		resultsBox.setOpacity(0);
		container.getChildren().addAll(this, resultsBox);

		resultsBox.getSelectionModel().selectedItemProperty().addListener((obs, old, n) -> {
			if(n != null)
			{
				itself.setText(n.toString());
				itself.resultsBox.setOpacity(0);
			}
		});
		this.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {

				String actual = "";

				char[] chars = newVal.toCharArray();

				switch(filter)
				{
				case LETTERS_ONLY:
					for(char c : chars)
						if(Character.isLetter(c) || Character.isSpaceChar(c))
							actual += String.valueOf(c);
					break;
				case NUMBERS_ONLY:
					for(char c : chars)
						if(Character.isDigit(c))
							actual += String.valueOf(c);
					break;
				case NUMBERS_AND_LETTERS_ONLY:
					for(char c : chars)
						if(Character.isAlphabetic(c) || Character.isSpaceChar(c))
							actual += String.valueOf(c);
					break;
				case ALLOW_ALL:
					actual = newVal;
					break;
				default:
					break;
				}

				if(actual.length() > maxLength)
					actual = actual.substring(0, maxLength + 1);

				itself.setText(actual);
			}

		});
	}
	public Filter getFilter() {
		return filter;
	}
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	public VBox getContainer() {
		return container;
	}
	public void setContainer(VBox container) {
		this.container = container;
	}
	public ListView<T> getResultsBox() {
		return resultsBox;
	}
	public void setResultsBox(ListView<T> resultsBox) {
		this.resultsBox = resultsBox;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
