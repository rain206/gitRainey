package edu.uw.raineyck.view;


import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.raineyck.account.AccountManagerImpl;
import edu.uw.raineyck.account.MainAccountApp;
import edu.uw.raineyck.dao.AccountDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {
	
	/**	Username entered for log in */
	@FXML
	private TextField usernameField;
	
	/**	Password entered for log in */
	@FXML
	private PasswordField passwordField;
	
	/**	Boolean if log in button is clicked */
	private boolean loginClicked = false;
	
	/**	Boolean if sign up button is clicked */
	private boolean signUpClicked = false;
	
	/**	CLose Button */
	@FXML
	private Button closeButton;
	
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
	
	@FXML
	private void handleLogin() {
		AccountDao dao = new AccountDaoImpl();
		AccountManager manager = new AccountManagerImpl(dao);
		
		try {
			if (manager.validateLogin(usernameField.getText(), passwordField.getText())) {
				Account userAcct = manager.getAccount(usernameField.getText());
				mainApp.showAccountOverview(userAcct);
			} else {
				System.out.println("Login FAILED");
			}
		} catch (AccountException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the stage for log in
	 * 
	 * @param loginStage - stage to be set
	 */
	public void setMainAccountApp(MainAccountApp mainApp) {
		this.mainApp = mainApp;
	}

}
