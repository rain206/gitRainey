package app;

import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.raineyck.account.AccountManagerImpl;
import edu.uw.raineyck.dao.AccountDaoImpl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
/**
 * This is the user interface for the broker application from Java 130
 * 
 * @author craigrainey
 *
 */
public class UserInterface extends Application {
    
    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("JavaFX Welcome");

    	GridPane grid = new GridPane(); //Enables you to create a flexible grid of rows and columns in which to lay out controls
    	grid.setAlignment(Pos.CENTER); //Changes default pos. of the grid from the top left of the scene to the center.
    	grid.setHgap(10); //Manages the spacing between the rows and columns
    	grid.setVgap(10);
    	grid.setPadding(new Insets(25, 25, 25, 25)); //Manages the space around the edges of the grid pane (Top, Right, Bottom, Left)
    	
    	Text sceneTitle = new Text("Welcome to the virtual broker"); //Creates Text object that cannot be edited.
    	sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    	grid.add(sceneTitle, 0, 0, 2, 1); //Adds the sceneTitle to the layout grid. add(Text, col, row, col span, row span)
    	
    	Label userName = new Label("User Name:");
    	grid.add(userName, 0, 1);
    	
    	TextField userTextField = new TextField();
    	userTextField.setPrefWidth(200.0);
    	grid.add(userTextField, 1, 1);
    	
    	Label password = new Label("password");
    	grid.add(password, 0, 2);
    	
    	PasswordField passwordBox = new PasswordField();
    	passwordBox.setPrefWidth(200.0);
    	grid.add(passwordBox, 1, 2);
    	
    	
    	Button btn = new Button("Sign in"); //Creates a button with label
    	HBox hbBtn = new HBox(10); //Creates an HBox layout pane with spacing of 10 pixels
    	hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	hbBtn.getChildren().add(btn); //Add button as a child of the HBox pane.
    	grid.add(hbBtn, 1, 4);
    	
    	final Text actionTarget = new Text();
    	grid.add(actionTarget, 1, 6);
    	
    	//Register an event handler that sets the actionTarget object to sign in button pressed
    	//when the user presses the button.
    	btn.setOnAction(new EventHandler<ActionEvent>() {
    		
    		@Override
    		public void handle(ActionEvent e) {
    			AccountDao dao = new AccountDaoImpl();
    			AccountManager acctManager = new AccountManagerImpl(dao);
    			try {
					if (acctManager.validateLogin(userTextField.getText(), passwordBox.getText())) {
						actionTarget.setText("SUCCESSFULLY LOGGED IN");
					} else {
						actionTarget.setFill(Color.FIREBRICK);
						actionTarget.setText("username of password is invalid.");
					}
				} catch (AccountException e1) {
					e1.printStackTrace();
				}
    		}
    	});
    	
    	
    	//grid.setGridLinesVisible(true);//Displays the grid lines
    	
    	Scene scene = new Scene(grid, 700, 600);
    	primaryStage.setScene(scene);
    	
    	primaryStage.show();
    }
    
	 public static void main(String[] args) {
	        launch(args);
     }
}
