package de.artignition.werkflow.client.view;

import java.util.ArrayList;
import java.util.List;

import de.artignition.fxplumber.model.Connector;
import de.artignition.fxplumber.model.Connector.ConnectorType;
import de.artignition.fxplumber.model.GraphNode;
import de.artignition.fxplumber.view.GraphNodeFactory;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class WFGraphNodeFactory implements GraphNodeFactory {

	String title;
	
	public WFGraphNodeFactory() {
		
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	@Override
	public Pane createGraphNode() {
		
		AnchorPane retval = new AnchorPane();
		retval.setPrefSize(80.d, 80.d);
		
		VBox box = new VBox();
		box.setPrefSize(80.d, 80.d);
		box.getStyleClass().add("plgBox");
		
		Label l = new Label();
		l.setPrefWidth(80.d);
		l.setMaxHeight(22.d);
		l.setTextAlignment(TextAlignment.CENTER);
		l.setText(this.title);
		l.getStyleClass().add("plgTitle");
		box.getChildren().add(l);
		
		ImageView iv = new ImageView();
		iv.setImage(new Image("/cogwheel.png", true));
		
		box.getChildren().add(iv);
	
		retval.getChildren().add(box);
		AnchorPane.setBottomAnchor(box, 0.0d);
		AnchorPane.setTopAnchor(box, 0.0d);
		AnchorPane.setLeftAnchor(box, 0.0d);
		AnchorPane.setRightAnchor(box, 0.0d);
		return retval;
	}

	
	
	@Override
	public Point2D adjustConnector(Pane nodePane, GraphNode g, Connector c) {
		// split by input / output connectors
		List<Connector> ins = new ArrayList<Connector>();
		List<Connector> outs = new ArrayList<Connector>();
		
		for (Connector con : g.getConnectors()) {
			if (con.getType() == ConnectorType.INPUT)
				ins.add(con);
			else
				outs.add(con);
		}
		
		if (c.getType() == ConnectorType.INPUT) {
			double x = -8.d;
			double y = (nodePane.getPrefHeight() / (ins.size() + 1)) * (ins.indexOf(c)+1) - 8.d;
			return new Point2D(x,y);
		} else {
			double x = nodePane.getPrefWidth();
			double f = (nodePane.getPrefHeight() / (outs.size() + 1));
			double y = f * (outs.indexOf(c) + 1);
			return new Point2D(x, y - 8.d);
		}
	}
}
