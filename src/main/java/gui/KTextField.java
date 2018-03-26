package gui;

import constants.Filter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class KTextField extends TextField{

	private int maxCharLength;
	private Filter filter;

	public KTextField(String text)
	{
		super(text);
		setDefault();
		maxCharLength = 30;

		start();
	}
	public KTextField()
	{
		super();
		setDefault();
		start();
	}
	private void setDefault() {
		maxCharLength = 30;
		filter = Filter.ALLOW_ALL;
	}
	private void start() {
		KTextField itself = this;

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
}
