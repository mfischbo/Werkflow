package de.artignition.werkflow.io;

import de.artignition.werkflow.domain.Domain;
import de.artignition.workflow.api.command.PluginDescriptorCommand.Verb;

import java.util.UUID;

public class EntityCommand {

	private UUID					id;
	private Class<? extends Domain>	type;
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

	public void setType(Class<? extends Domain> type) {
		this.type = type;
	}

	public Verb getVerb() {
		return verb;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}
}
