package de.artignition.werkflow.command;

import java.util.UUID;

public class CommandSuccessResponse {

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
