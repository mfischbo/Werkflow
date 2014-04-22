package de.artignition.werkflow.io;

import java.io.Serializable;
import java.util.UUID;

import org.joda.time.LocalDateTime;

public class AdministrationCommand implements Serializable {

	private static final long serialVersionUID = -5307404404319807527L;


	public enum Scope {
		JOB,				// the command regards a job description
		JOBINSTANCE,		// the command regards a single job instance
		ENGINE				// the command regards the werkflow engine
	}
	
	public enum Command {
		START,				// only available for scope == JOB. Launches the job
		RESUME,				// resumes the scoped object from where it where paused
		PAUSE,				// pauses the scoped object
		HALT				// halts (terminates) the execution of the scoped object
	}
	
	public Scope			scope;
	public Command			command;
	public LocalDateTime	dateCreated;
	public UUID				referenceId;
	public Scope getScope() {
		return scope;
	}
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}
	public UUID getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(UUID referenceId) {
		this.referenceId = referenceId;
	}
}
