package edu.uw.raineyck.view;

import edu.uw.ext.framework.account.Account;
import edu.uw.raineyck.account.MainAccountApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AccountOverviewController {
	
	/**	Account for this user */
	private Account account;

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
	
	@FXML
	private Label emailAddress;
	
	/**	SignOut Button */
	@FXML
	private Button closeButton;
	
	@FXML
	private Label passwordHashed;
	
	//Reference to the main account application
	private MainAccountApp mainApp;
	
	/**
	 * Constructor. This is called before the initialize() method.
	 */
	public AccountOverviewController() {
	}
	
	/**
	 * Initialize the controller
	 */
	@FXML
	private void initialize() {
	}
	
	
	/**
	 * Set the MainAccountApp
	 * @param mainApp - MainAccountApp for this AccountOverviewController.
	 */
	public void setMainAccountApp(MainAccountApp mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * Sets the account
	 * @param setAcct - account to set this.account as.
	 */
	public void setAccount(Account setAcct) {
		this.account = setAcct;
		accountName.setText(account.getName());
		name.setText(setAcct.getName());
		
		if (!setAcct.getPhone().equals("<null>")) {	
			phoneNumber.setText(setAcct.getPhone());
		} else {
			phoneNumber.setText("(###)###-####");
		}
		
		if (!setAcct.getEmail().equals("<null>")) {
			emailAddress.setText(setAcct.getEmail());
		} else {
			emailAddress.setText("");
		}
		passwordHashed.setText("******");
		
		
		balance.setText(Integer.toString(setAcct.getBalance()));
	}
	
	@FXML
	private void handleEditAccountName() {
		mainApp.showEditAccountName(account);
	}
	
	/**
	 * Closes the window and logs out of the account when the user clicks "sign out"
	 * @param event - event where user clicks "sign out".
	 */
	@FXML
	private void handleSignOut() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
}
