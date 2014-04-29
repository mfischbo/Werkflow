package de.artignition.werkflow.client.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import de.artignition.fxplumber.event.ConnectionEvent;
import de.artignition.fxplumber.model.Connector;
import de.artignition.fxplumber.model.Connector.ConnectorType;
import de.artignition.fxplumber.model.Graph;
import de.artignition.fxplumber.model.GraphNode;
import de.artignition.fxplumber.view.ViewFactory;
import de.artignition.werkflow.client.component.PluginEntry;
import de.artignition.werkflow.client.service.EngineService;
import de.artignition.werkflow.client.service.JobDescriptorService;
import de.artignition.werkflow.client.service.PluginDescriptorService;
import de.artignition.werkflow.client.view.WFGraphNodeFactory;
import de.artignition.werkflow.domain.Connection;
import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.domain.JobPlugin;
import de.artignition.werkflow.dto.ConnectionDescriptor;
import de.artignition.werkflow.dto.PluginDescriptor;

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

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class JobEditController extends StageController implements
		Initializable, EventHandler<MouseEvent> {

	@Autowired
	private PluginDescriptorService service;
	
	@Autowired
	private JobDescriptorService	jdService;
	
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

	private Graph	graph;
	
	private WFGraphNodeFactory gnFactory;
	
	private Map<GraphNode, JobPlugin> nodeMapping;
	private Map<Connector, ConnectionDescriptor> connectorMapping;
	
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

		// initialize fxplumber
		ViewFactory viewFactory = new ViewFactory();
		this.gnFactory = new WFGraphNodeFactory();
		viewFactory.setNodeFactory(this.gnFactory);
		graph = new Graph(canvas, viewFactory);
		
		this.nodeMapping = new HashMap<GraphNode, JobPlugin>();
		this.connectorMapping = new HashMap<Connector, ConnectionDescriptor>();

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

				PluginDescriptor pd = service.getPluginByClassName(clazz);
				gnFactory.setTitle(pd.getName());
				GraphNode gn = graph.createNode(event.getX(), event.getY());
				
				for (ConnectionDescriptor cd : pd.getInputs()) {
					Connector c = gn.addConnector(ConnectorType.INPUT);
					connectorMapping.put(c, cd);
				}
				for (ConnectionDescriptor cd : pd.getOutputs()) {
					Connector c = gn.addConnector(ConnectorType.OUTPUT);
					connectorMapping.put(c, cd);
				}
				
				JobPlugin jp = new JobPlugin();
				jp.setClassname(pd.getClassname());
				nodeMapping.put(gn, jp);
			}

		});
		
		
		canvas.addEventHandler(ConnectionEvent.CONNECTION_CREATED, new EventHandler<ConnectionEvent>() {
			@Override
			public void handle(ConnectionEvent e) {
				log.info("Adding new connection for " + e.getConnection());
				GraphNode source = e.getConnection().getSource().getGraphNode();
				GraphNode target = e.getConnection().getTarget().getGraphNode();
				
				JobPlugin sp = nodeMapping.get(source);
				JobPlugin tp = nodeMapping.get(target);
				
				ConnectionDescriptor sd = connectorMapping.get(e.getConnection().getSource());
				ConnectionDescriptor td = connectorMapping.get(e.getConnection().getTarget());
				
				Connection conn = new Connection();
				conn.setSource(sp);
				conn.setTarget(tp);
				conn.setSourcePort(sd.getNumber());
				conn.setTargetPort(td.getNumber());
				sp.getOutboundConnections().add(conn);
				tp.getInboundConnections().add(conn);
				
				
			}
		});
		
		// set available engines 
		ObservableList<String> list = FXCollections.observableArrayList(engineService.getAvailableEngines());
		this.engineChoice.setItems(list);
	}

	
	public void saveJob() {
		log.info("Sending job to " + this.engineChoice.getSelectionModel().getSelectedItem());
		
		JobDescriptor jd = new JobDescriptor();
		jd.setName("foo");
		jd.setDescription("foo description");
		jd.setDateCreated(LocalDateTime.now());
		jd.setDateModified(jd.getDateCreated());
		
		Set<JobPlugin> plugins = new HashSet<JobPlugin>();
		for (JobPlugin pd : nodeMapping.values()) {
			pd.setJobDescriptor(jd);
			jd.getPlugins().add(pd);
		}
		
		jdService.createJobDescription(jd, this.engineChoice.getSelectionModel().getSelectedItem());
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
