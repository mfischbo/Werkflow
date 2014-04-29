package de.artignition.werkflow.command;

import java.io.Serializable;
import java.util.UUID;

public class EntityCommand implements Serializable {

	private static final long serialVersionUID = 701106111266532679L;
	private UUID					id;
	private Class<?>				type;
	private Verb					verb;
	
	public EntityCommand() {
		
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Verb getVerb() {
		return verb;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}
}
