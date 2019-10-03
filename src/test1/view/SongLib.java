// Yao Ting and Kshitij Bafna
package test1.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import test1.view.LibController;

public class SongLib extends Application
{
	@Override
	public void start(Stage stage) throws Exception{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/test1/view/list.fxml"));
			AnchorPane pane = (AnchorPane)loader.load();
			
			LibController lib = loader.getController();
			lib.start(stage);
			
			Scene scene = new Scene(pane);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
			
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}