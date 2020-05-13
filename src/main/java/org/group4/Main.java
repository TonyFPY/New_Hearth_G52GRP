package org.group4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This is the Main class to launch the game and initialise the game interface
 * @author Team 4
 */
public class Main extends Application {
	/**
	 * The main stage of the game
	 */
	private static Stage stage;

	/**
	 * Loads the current the scene and initialise the css style
	 * @param primaryStage the primary stage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			primaryStage.setTitle("Game");

			//load the starting page
			Parent root = FXMLLoader.load(getClass().getResource("/org/group4/FXML/MainGame.fxml"));
			Scene scene = new Scene(root, 600, 400);

			//initialise the property of the scene and stage
			stage.initStyle(StageStyle.TRANSPARENT);
			scene.setFill(Color.TRANSPARENT);
			scene.getRoot().requestFocus();

			//show the current scene
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getStage() {
		return stage;
	}
}
