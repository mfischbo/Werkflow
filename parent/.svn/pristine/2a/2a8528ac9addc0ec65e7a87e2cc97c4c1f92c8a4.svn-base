package de.artignition.werkflow.plugin;

public class ExitFailed extends PluginExitStatus {

	private Throwable 		t;
	
	public ExitFailed() {
		this.exitStatus = Status.FAILED;
	}

	public ExitFailed(String message) {
		
	}
	
	public ExitFailed(Throwable t) {
		// this constructor provdes information why it failed
		// make it accessible through the api
		this.exitStatus = Status.FAILED;
	}
}
