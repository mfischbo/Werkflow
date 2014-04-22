package de.artignition.werkflow.client.controller;

import de.artignition.werkflow.client.component.PluginEntry;
import de.artignition.werkflow.client.service.EngineService;
import de.artignition.werkflow.client.service.PluginDescriptorService;
import de.artignition.werkflow.dto.ConnectionDescriptor;
import de.artignition.werkflow.dto.PluginDescriptor;

import eu.mihosoft.vrl.workflow.FlowFactory;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class JobEditController extends StageController implements
		Initializable, EventHandler<MouseEvent> {

	@Autowired
	private PluginDescriptorService service;
	
	@Autowired
	private EngineService			engineService;

	@FXML
	private ChoiceBox<String>		engineChoice;
	
	@FXML
	private VBox pluginsContentBox;

	@FXML
	private Label lblSelPlgName;
	@FXML
	private Label lblSelPlgDesc;
	@FXML
	private Label lblSelPlgNumIns;
	@FXML
	private Label lblSelPlgTypesIn;
	@FXML
	private Label lblSelPlgNumOuts;
	@FXML
	private Label lblSelPlgTypesOut;

	@FXML
	private Pane canvas;

	private VFlow vFlow;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		log.info("Initializing job edit controller");

		try {
			PluginDescriptor[] plugins = service.getAllPlugins();
			for (PluginDescriptor p : plugins) {
				PluginEntry e = new PluginEntry();
				e.setLabel(p.getName());
				e.setIcon(new Image("/Plugin.png", true));
				e.setOnMouseClicked(this);
				e.setOnDragDetected(this);
				e.setClassname(p.getClassname());
				pluginsContentBox.getChildren().add(e);
			}

		} catch (Exception ex) {
			log.error("An error occured inserting the plugins. Cause: "
					+ ex.getMessage());
		}

		// initialize VFlow engine
		this.vFlow = FlowFactory.newFlow(new FXSkinFactory(this.canvas));
		this.vFlow.setVisible(true);

		// attach Drop Target handler to canvas
		this.canvas.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != canvas
						&& event.getDragboard().hasString()) {
					event.acceptTransferModes(TransferMode.COPY);
				}
				event.consume();
			}
		});

		// attach DragExit handler
		this.canvas.setOnDragExited(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				String clazz = event.getDragboard().getString();
				log.info("Creating new vworkflow node now with type: " + clazz);

				VNode node = vFlow.newNode();
				node.setX(event.getX());
				node.setY(event.getY());
				

				// get the plugin descriptor and set inputs / outputs
				PluginDescriptor pd = service.getPluginByClassName(clazz);
				for (ConnectionDescriptor cd : pd.getInputs()) {
					node.addInput(cd.getType());
				}

				for (ConnectionDescriptor cd : pd.getOutputs())
					node.addOutput(cd.getType());
				node.setTitle(pd.getName());
				event.consume();
			}

		});
		
		// set available engines 
		ObservableList<String> list = FXCollections.observableArrayList(engineService.getAvailableEngines());
		this.engineChoice.setItems(list);
	}

	
	public void saveJob() {
		log.info("Sending job to " + this.engineChoice.getSelectionModel().getSelectedItem());
	}
	
	public void cancelEdit() {
		log.info("Aborting editing");
	}
	
	@Override
	public void handle(MouseEvent e) {
		if (e.getSource() instanceof PluginEntry) {
			PluginEntry selected = (PluginEntry) e.getSource();

			if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
				handleSelection(selected);
			}

			if (e.getEventType() == MouseEvent.DRAG_DETECTED) {
				handleDragDetected(e, selected);
			}
		}
	}

	private void handleDragDetected(MouseEvent event, PluginEntry e) {
		log.info("Drag detected on entry : " + e.getClassname());
		Dragboard db = e.startDragAndDrop(TransferMode.COPY);
		ClipboardContent content = new ClipboardContent();
		content.putString(e.getClassname());
		db.setContent(content);
		event.consume();
	}


	private void handleSelection(PluginEntry selected) {
		selected.setSelected(true);

		PluginDescriptor pd = service.getPluginByClassName(selected
				.getClassname());
		this.lblSelPlgName.setText(selected.getLabel());
		this.lblSelPlgDesc.setText(pd.getDescription());
		this.lblSelPlgNumIns.setText(pd.getInputs().length + "");
		this.lblSelPlgNumOuts.setText(pd.getOutputs().length + "");

		StringBuffer b = new StringBuffer();
		for (ConnectionDescriptor d : pd.getInputs())
			b.append(d.getType()).append(" ");
		this.lblSelPlgTypesIn.setText(b.toString());

		b = new StringBuffer();
		for (ConnectionDescriptor d : pd.getOutputs())
			b.append(d.getType()).append(" ");
		this.lblSelPlgTypesOut.setText(b.toString());
	}
}