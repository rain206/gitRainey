package edu.uw.raineyck.account;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
	
	/**	BorderPane for the root layout */
	private BorderPane rootLayout;
	
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
		
		showAccountOverview();
	}
	
	/**
	 * Initializes the root layout
	 */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainAccountApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			//Show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the account overview inside the root layout
	 */
	public void showAccountOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainAccountApp.class.getResource("view/AccountOverview.fxml"));
			AnchorPane accountOverview = (AnchorPane) loader.load();
			
			//Set the account overview into the center of root layout
			rootLayout.setCenter(accountOverview);
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
