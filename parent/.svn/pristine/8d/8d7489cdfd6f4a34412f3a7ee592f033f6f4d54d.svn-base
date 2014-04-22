package de.artignition.werkflow.client;

import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import de.artignition.werkflow.client.config.ApplicationConfig;
import de.artignition.werkflow.client.controller.StageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application {

	private ApplicationContext ctx;
	private Logger _log = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
	
		ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		
		try {
			URL baseLocation = getClass().getResource("/Application.fxml");
			FXMLLoader loader = new FXMLLoader(baseLocation);
			loader.setLocation(baseLocation);
			loader.setControllerFactory(new Callback<Class<?>, Object>() {

				@Override
				public Object call(Class<?> arg0) {
					StageController sc = (StageController) ctx.getBean(arg0);
					sc.setStage(stage);
					return sc;
				}
			});
			
			InputStream fxmlStream = getClass().getResourceAsStream("/Application.fxml");
			Parent root = (Parent) loader.load(fxmlStream);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception ex) {
			_log.error("Failed to create scene. Cause: " + ex.getMessage());
		}
	}
}