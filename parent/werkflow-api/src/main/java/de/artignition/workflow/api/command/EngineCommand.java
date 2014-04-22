package de.artignition.workflow.api.command;

import java.io.Serializable;

public class EngineCommand implements Serializable {

	private static final long serialVersionUID = 8072144501485846594L;

	public enum Action {
		START,
		HALT,
		PAUSE,
		RESUME
	}
	
	private Action action;

	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
