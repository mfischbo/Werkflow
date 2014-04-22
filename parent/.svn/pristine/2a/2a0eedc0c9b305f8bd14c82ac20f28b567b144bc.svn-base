package de.artignition.werkflow.plugin;

/**
 * Interface for plugin's.
 * Note that this interface is not thought to be implemented by custom plugins. Please
 * extend either {@link ManagedPlugin} or {@link UnmanagedPlugin} for custom implementations.
 * 
 * @author M. Fischboeck
 *
 */
public interface Plugin {

	/**
	 * The main execution method.
	 * This method will be called upon each new incoming {@link de.werkflow.model.ObjectStore}.
	 */
	public PluginExitStatus execute();
	
	/**
	 * Callback method that is called once before the entire plugin is being executed.
	 */
	public void beforeExecution();
	
	/**
	 * Callback method that is called once after the entire Plugin finished execution
	 */
	public void afterExecution();
	
	/**
	 * Callback method that is called each time a new input object has arrived, but before being processed.
	 */
	public void beforeWorkItemProcessing();
	
	/**
	 * Callback method that is called each time after the {@link #execute()} method has finished execution.
	 */
	public void afterWorkItemProcessing();
}