package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.layout.VBox;

public class CustomerDetailsPage extends VBox{

	private static final int WIDTH = GUI.FIRST_PAGE_WIDTH;
	private static final int HEIGHT = GUI.FIRST_PAGE_HEIGHT - 50;
	@SuppressWarnings("rawtypes")
	public CustomerDetailsPage()
	{
		this.setMaxSize(WIDTH, HEIGHT);
		this.setMinSize(WIDTH, HEIGHT);
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
		
		this.getChildren().addAll(fnameField.getContainer(), lnameField.getContainer(), phoneField.getContainer(), passportField.getContainer());
	}
}
