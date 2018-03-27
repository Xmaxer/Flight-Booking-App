package gui;

import constants.Filter;
import exceptions.InvalidShakeSpeedException;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class KTextField extends TextField{

	private int maxCharLength;
	private Filter filter;
	private final double DEFAULT_STRENGTH = 5;
	private final double DEFAULT_SPEED = 0.5;
	private ResultsBox resultsBox;
	private boolean searchable;

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
	
	private void start() {
		KTextField itself = this;
		maxCharLength = 30;
		filter = Filter.ALLOW_ALL;
		resultsBox = new ResultsBox(this);
		searchable = false;
		
		this.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {

				String actual = "";

				char[] chars = newVal.toCharArray();

				switch(filter)
				{
				case LETTERS_ONLY:
					for(char c : chars)
						if(Character.isLetter(c))
							actual += String.valueOf(c);
					break;
				case NUMBERS_ONLY:
					for(char c : chars)
						if(Character.isDigit(c))
							actual += String.valueOf(c);
					break;
				case NUMBERS_AND_LETTERS_ONLY:
					for(char c : chars)
						if(Character.isAlphabetic(c))
							actual += String.valueOf(c);
					break;
				case ALLOW_ALL:
					actual = newVal;
					break;
				default:
					break;
				}

				if(actual.length() > maxCharLength)
					actual = oldVal;

				itself.setText(actual);
			}

		});
	}
	public synchronized Filter getFilter() {
		return filter;
	}
	public synchronized void setFilter(Filter filter) {
		this.filter = filter;
	}
	public synchronized int getMaxCharLength() {
		return maxCharLength;
	}
	public synchronized void setMaxCharLength(int maxCharLength) {
		this.maxCharLength = maxCharLength;
	}
	public boolean isSearchable() {
		return searchable;
	}
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	public double getDEFAULT_STRENGTH() {
		return DEFAULT_STRENGTH;
	}
	public double getDEFAULT_SPEED() {
		return DEFAULT_SPEED;
	}
	public ResultsBox getResultsBox() {
		return resultsBox;
	}
	public void setResultsBox(ResultsBox resultsBox) {
		this.resultsBox = resultsBox;
	}
}
