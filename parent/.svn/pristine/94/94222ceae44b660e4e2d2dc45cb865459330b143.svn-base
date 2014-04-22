package de.artignition.werkflow.client.component;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class PluginEntry extends AnchorPane {

	@FXML private ImageView		plgIcon;
	@FXML private Label			plgLabel;
	private String				classname;		// classname of the plugin. Serves as id
	
	
	public PluginEntry() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PluginEntry.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		try {
			fxmlLoader.load();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public String getLabel() {
		return this.plgLabel.getText();
	}
	
	public void setLabel(String text) {
		this.plgLabel.setText(text);
	}
	
	public void setIcon(Image img) {
		plgIcon.setImage(img);
	}
	
	public void setSelected(boolean selected) {
		if (selected) {
			this.plgLabel.setTextFill(Color.rgb(0, 0, 255));
		} else {
			this.plgLabel.setTextFill(Color.rgb(0, 0, 0));
		}
	}
	
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	public String getClassname() {
		return this.classname;
	}
}
