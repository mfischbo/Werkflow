package de.artignition.werkflow.command;

import java.io.Serializable;
import java.util.UUID;

public class CommandSuccessResponse implements Serializable {

	private static final long serialVersionUID = -1150951208683349802L;
	
	private UUID id;
	
	public CommandSuccessResponse(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
