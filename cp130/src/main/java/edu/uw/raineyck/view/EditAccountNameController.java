package edu.uw.raineyck.view;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.raineyck.account.AccountManagerImpl;
import edu.uw.raineyck.dao.AccountDaoImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditAccountNameController {
	
	@FXML
	private Label currentName;
	
	@FXML
	private TextField newNameField;
	
	@FXML
	private TextField verifyNewNameField;
	
	@FXML
	private Button cancelButton;
	
	private Account acctToEdit;
	
	/**
	 * Default constructor
	 */
	public EditAccountNameController() {
	}
	
	/**
	 * Initializes this controller class. This method is automatically called after the fxml
	 * file has been loaded.
	 */
	@FXML
	private void initialize() {
	}
	
	/**
	 * Sets the currentName label and acctToEdit.
	 * @param acctToEdit - target account to edit.
	 */
	public void setAcctToEdit(Account acctToEdit) {
		this.acctToEdit = acctToEdit;
		this.currentName.setText(acctToEdit.getName());
	}
	
	@FXML
	private void changeAcctName() {
		if (newNameField.getText().equals(verifyNewNameField.getText())) {
			AccountDao dao = new AccountDaoImpl();
			
			//Deletes old account and creates new one with updated information
			try {
				acctToEdit.setName(newNameField.getText());
				dao.deleteAccount(currentName.getText());
				dao.setAccount(acctToEdit);
				
				handleCancel(); //Closes window after edit
				
			} catch (AccountException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Closes the window and cancels any further operations
	 */
	@FXML
	private void handleCancel() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
}
