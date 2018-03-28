package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import constants.Filter;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class CustomerDetailsPage extends VBox{

	private static final int WIDTH = GUI.FIRST_PAGE_WIDTH;
	private static final int HEIGHT = GUI.FIRST_PAGE_HEIGHT - 50;
	@SuppressWarnings("rawtypes")
	public CustomerDetailsPage()
	{
		this.setMaxSize(WIDTH, HEIGHT);
		this.setMinSize(WIDTH, HEIGHT);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(30);
		KTextField fnameField = new KTextField();
		KTextField lnameField = new KTextField();
		KTextField phoneField = new KTextField();
		KTextField passportField = new KTextField();
		
		List<KTextField> list = new ArrayList<KTextField>();
		list.addAll(Arrays.asList(fnameField, lnameField, phoneField, passportField));
		
		for(KTextField field : list)
		{
			field.setLabelled(true);
		}
		
		fnameField.getLabel().setText("First name: ");
		lnameField.getLabel().setText("Last name: ");
		phoneField.getLabel().setText("Phone number: ");
		passportField.getLabel().setText("Passport number:");
		
		fnameField.setFilter(Filter.LETTERS_ONLY);
		lnameField.setFilter(Filter.LETTERS_ONLY);
		phoneField.setFilter(Filter.NUMBERS_ONLY);
		passportField.setFilter(Filter.NUMBERS_ONLY);
		
		
		
		this.getChildren().addAll(fnameField.getContainer(), lnameField.getContainer(), phoneField.getContainer(), passportField.getContainer());
	}
}
