package edu.uw.raineyck.view;

import edu.uw.raineyck.account.AccountImplModel;
import edu.uw.raineyck.account.MainAccountApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {
	
	/**	Username entered for log in */
	@FXML
	private TextField username;
	
	/**	Password entered for log in */
	@FXML
	private TextField password;
	
	/**	Boolean if log in button is clicked */
	private boolean loginClicked = false;
	
	/**	Boolean if sign up button is clicked */
	private boolean signUpClicked = false;
	
	/**	MainAccountApp */
	@FXML
	private MainAccountApp mainApp;
	
	
	/**
	 * Default constructor
	 * This is called before the intitialize method
	 */
	public LogInController() {
	}
	
	
	
	/**
	 * Initializes the controller class. This method is automatically called after the fxml
	 * file has been loaded
	 */
	@FXML
	private void initialize() {
	}
	
	/**
	 * Sets the stage for log in
	 * 
	 * @param loginStage - stage to be set
	 */
	@FXML
	public void setMainAccountApp(MainAccountApp mainApp) {
		this.mainApp = mainApp;
	}

}
