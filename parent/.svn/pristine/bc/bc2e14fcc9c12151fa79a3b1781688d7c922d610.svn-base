package de.artignition.werkflow.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.artignition.werkflow.client.service.EngineService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

@Controller
public class RootController extends StageController implements Initializable, EventHandler<MouseEvent> {

	@FXML
	private SplitPane		mwEnginePanel;
	
	@FXML
	private AnchorPane		mwJobEditPanel;
	
	@FXML
	private ListView<String> engineListView;
	
	@FXML
	private TabPane			engineTabPane;

	@Autowired
	private	EngineService			service; 

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.engineListView.getItems().addAll(service.getAvailableEngines());
		this.engineListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.engineListView.setOnMouseClicked(this);
	}
	
	
	public void startEngineCmd(ActionEvent e) {
		System.out.println("Starting engine");
		service.sendStartEngineRequest(null);
	}
	
	public void haltEngineCmd(ActionEvent e) {
		System.out.println("Halting engine");
	}
	
	public void pauseOrResumeEngineCmd(ActionEvent e) {
		System.out.println("Pausing / Resuming engine");
	}
	
	public void createJobDefinition(ActionEvent e) {
		log.debug("Opening Job Definition view");
		this.mwEnginePanel.visibleProperty().set(false);
		this.mwJobEditPanel.visibleProperty().set(true);
	}
	
	@Override
	public void handle(MouseEvent e) {
		if (e.getSource() == this.engineListView) {
			String item = this.engineListView.getSelectionModel().getSelectedItem();
			System.out.println("Selected : " + item);
			this.engineTabPane.visibleProperty().set(true);
		}
	}
}
