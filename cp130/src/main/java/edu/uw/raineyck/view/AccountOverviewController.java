package edu.uw.raineyck.view;

import edu.uw.raineyck.account.MainAccountApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccountOverviewController {

	@FXML
	private Label name;
	
	@FXML
	private Label accountName;
	
	@FXML
	private Label address;
	
	@FXML
	private Label phoneNumber;
	
	@FXML
	private Label balance;
	
	//Reference to the main account application
	private MainAccountApp mainApp;
	
	/**
	 * Constructor. This is called before the initialize() method.
	 */
	public AccountOverviewController() {
	}
	
	@FXML
	private void initialize() {
	}
	
	public void setMainAccountApp(MainAccountApp mainApp) {
		this.mainApp = mainApp;
	}
}
