package de.artignition.werkflow.domain;

import java.util.Set;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

public class JobInstance {

	private ObjectId	  			id;
	private DateTime 				dateCreated;
	private DateTime 				dateStarted;
	private DateTime 				dateFinished;
	private Set<PluginInstance>		pluginInstances;
	private ObjectId	 			descriptorId;
	
	public JobInstance() {
		this.id = new ObjectId();
	}
	

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public DateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(DateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public DateTime getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(DateTime dateStarted) {
		this.dateStarted = dateStarted;
	}

	public DateTime getDateFinished() {
		return dateFinished;
	}

	public void setDateFinished(DateTime dateFinished) {
		this.dateFinished = dateFinished;
	}

	public Set<PluginInstance> getPluginInstances() {
		return pluginInstances;
	}

	public void setPluginInstances(Set<PluginInstance> pluginInstances) {
		this.pluginInstances = pluginInstances;
	}

	public ObjectId getDescriptorId() {
		return descriptorId;
	}

	public void setDescriptorId(ObjectId descriptorId) {
		this.descriptorId = descriptorId;
	}
}