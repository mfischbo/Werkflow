package de.artignition.werkflow.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bson.types.ObjectId;

public class PluginInstance {

	private ObjectId				id;
	private Integer 				stepCount;
	private PluginExecutionState	state;
	private Map<String, Object>		parameters;

	private Map<Integer, ConcurrentLinkedQueue<WorkItem>>		inQueue;
	private Map<Integer, ConcurrentLinkedQueue<WorkItem>> 		outQueue;

	public PluginInstance(JobPlugin plug) {
		this.id = new ObjectId();
		this.inQueue  = new HashMap<Integer, ConcurrentLinkedQueue<WorkItem>>();
		this.outQueue = new HashMap<Integer, ConcurrentLinkedQueue<WorkItem>>();
		for (Connection c : plug.getConnections()) {
			outQueue.put(c.getTargetPort(), new ConcurrentLinkedQueue<>());
		}
		this.stepCount = 0;
		this.state = PluginExecutionState.BOOTING;
		this.parameters = plug.getParameters();
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Integer getStepCount() {
		return stepCount;
	}

	public void setStepCount(Integer stepCount) {
		this.stepCount = stepCount;
	}

	public PluginExecutionState getState() {
		return state;
	}

	public void setState(PluginExecutionState state) {
		this.state = state;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	public ConcurrentLinkedQueue<WorkItem> getOutqueueByPort(Integer portNumber) {
		if (this.outQueue.containsKey(portNumber))
			return outQueue.get(portNumber);
		return null;
	}


	public WorkItem pollItem(Integer port) {
		return this.inQueue.get(port).poll();
	}
	
	public boolean hasItemsInInQueue() {
		boolean isEmpty = false;
		for (Integer x : inQueue.keySet()) {
			isEmpty = isEmpty || inQueue.get(x).isEmpty();
		}
		return !isEmpty;
	}
	
	public void setInQueue(Integer port, ConcurrentLinkedQueue<WorkItem> queue) {
		this.inQueue.put(port, queue);
	}
}