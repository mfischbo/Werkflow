package de.artignition.werkflow.domain;

import org.bson.types.ObjectId;

public class Connection {

	private ObjectId	targetPluginId;
	private int			sourcePort;
	private int			targetPort;

	public ObjectId getTargetPluginId() {
		return targetPluginId;
	}

	public void setTargetPluginId(ObjectId targetPluginId) {
		this.targetPluginId = targetPluginId;
	}

	public int getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}
}