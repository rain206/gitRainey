package edu.uw.raineyck.account;

import java.io.File;
import java.io.IOException;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.raineyck.dao.AccountDaoImpl;
import edu.uw.raineyck.view.AccountOverviewController;
import edu.uw.raineyck.view.EditAccountNameController;
import edu.uw.raineyck.view.LogInController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Main application for the accounts and SimpleBroker application
 * 
 * @author craigrainey
 *
 */
public class MainAccountApp extends Application {
	
	/**	Primary Stage */
	private Stage primaryStage;
	
	/**	Account Overview Stage */
	private Stage acctOverViewStage;
	
	/**	BorderPane for the root layout */
	private BorderPane rootLayout;
	
	private ObservableList<Account> accountData = FXCollections.observableArrayList();
	
	/**	Default constructor */
	public MainAccountApp() {
	}

	/**
	 * Starts the user interface application
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AccountApp");
		
		initRootLayout();
		
		showLogin();
	}
	
	/**
	 * Initializes the root layout
	 */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainAccountApp.class.getResource("/view/AccountRootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			//Show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainAccountApp.class.getResource("/view/Login.fxml"));
			AnchorPane login = (AnchorPane) loader.load();
			
			rootLayout.setCenter(login);
			
			LogInController controller = loader.getController();
			controller.setMainAccountApp(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the account overview inside the root layout
	 */
	public void showAccountOverview(Account userAcct) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainAccountApp.class.getResource("/view/AccountOverview.fxml"));
			AnchorPane accountOverview = (AnchorPane) loader.load();
			
			//Create the overview stage.
			Stage acctStage = new Stage();
			acctStage.setTitle(userAcct.getName());
			acctStage.initModality(Modality.WINDOW_MODAL);
			acctStage.initOwner(primaryStage);
			Scene scene = new Scene(accountOverview);
			acctStage.setScene(scene);
			this.acctOverViewStage = acctStage;
			
			AccountOverviewController controller = loader.getController();
			controller.setMainAccountApp(this);
			controller.setAccount(userAcct);
			
			//Show the acctStage and wait until the user closes it
			acctStage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showEditAccountName(Account acctToEdit) {
		try {
			FXMLLoader editNameLoader = new FXMLLoader();
			editNameLoader.setLocation(MainAccountApp.class.getResource("/view/EditAccountName.fxml"));
			AnchorPane editAccountName = (AnchorPane) editNameLoader.load();
			
			//Create the edit account name stage.
			Stage editAcct = new Stage();
			editAcct.setTitle("Edit Account Name");
			editAcct.initModality(Modality.WINDOW_MODAL);
			editAcct.initOwner(acctOverViewStage);
			Scene scene = new Scene(editAccountName);
			editAcct.setScene(scene);
			
			EditAccountNameController controller = editNameLoader.getController();
			controller.setAcctToEdit(acctToEdit);
			
			editAcct.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the mainstage
	 * @return - primaryStage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
