package com.stationservice;
import java.io.IOException;

//import com.stationservice.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.RuntimeException;
import java.net.URL;

public class App extends Application{
	
	private final String WINDOW_TITLE = "SiteWeb";
	private final int WINDOW_HEIGHT = 642;
	private final int WINDOW_WIDTH = 480;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		URL fxmlLocation = getClass().getResource("/fxml/main.fxml");
		if (fxmlLocation == null) {
			throw new RuntimeException("Impossible de trouver FXML");
		}
		//FXMLLoader loader = new FXMLLoader();
		Parent root = FXMLLoader.load(fxmlLocation);
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Station Essence - Gestion");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

/*scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) System.out.println("on veux quitter");
			}
		});
		
		//Label someLabel = new Label();
		//Button someButton = new Button("Test");
		
		
		//someButton.setOnMouseClicked(event -> {
			//someLabel.setText("Clicqueeeeee");
			//System.out.println("Click");
		//});
		
		//someButton.removeEventHandler(ActionEvent.ACTION, new ButtonHandler());  */
