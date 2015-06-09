package de.artignition.werkflow.domain;

import java.util.Set;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Werkflow_JobDescriptor")
public class JobDescriptor {

	@Id
	private ObjectId		id;

	@Indexed
	private String 			name;
	private String 			description;
	private DateTime 		dateCreated;
	private DateTime 		dateModified;
	
	private Set<JobPlugin>	plugins;
	
	public JobDescriptor() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(DateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public DateTime getDateModified() {
		return dateModified;
	}

	public void setDateModified(DateTime dateModified) {
		this.dateModified = dateModified;
	}

	public Set<JobPlugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(Set<JobPlugin> plugins) {
		this.plugins = plugins;
	}
}
