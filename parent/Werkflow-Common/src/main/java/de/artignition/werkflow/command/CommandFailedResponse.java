package de.artignition.werkflow.command;

import java.io.Serializable;

public class CommandFailedResponse implements Serializable {

	private static final long serialVersionUID = -1492424142434478580L;
	
	private String cause;
	
	public CommandFailedResponse(String cause) {
		this.cause = cause;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
}