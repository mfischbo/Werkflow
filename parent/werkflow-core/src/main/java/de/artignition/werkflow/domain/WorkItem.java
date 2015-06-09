package de.artignition.werkflow.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Werkflow_WorkItem")
public class WorkItem  {

	@Id
	private ObjectId			id;
	
	private Object				payloadObject;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Object getPayloadObject() {
		return payloadObject;
	}

	public void setPayloadObject(Object payloadObject) {
		this.payloadObject = payloadObject;
	}
}