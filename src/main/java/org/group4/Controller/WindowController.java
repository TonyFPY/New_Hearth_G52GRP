package org.group4.Controller;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.group4.Main;

/**
 * This class is used to switch the game interfaces.
 *
 * @author Team 4
 */
public class WindowController {
	
	@FXML
	public void startPressed() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/org/group4/FXML/MainGame.fxml"));
		Scene scene = new Scene(root, 600, 400);
		scene.setFill(Color.TRANSPARENT);
		scene.getRoot().requestFocus();
		Main.getStage().setScene(scene);
	}

	@FXML
	public void exitPressed() throws IOException {
		System.exit(0);
	}
}