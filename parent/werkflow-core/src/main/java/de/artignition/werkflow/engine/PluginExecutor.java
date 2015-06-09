package de.artignition.werkflow.engine;

import java.util.List;

import de.artignition.werkflow.domain.Connection;
import de.artignition.werkflow.domain.JobPlugin;
import de.artignition.werkflow.domain.PluginExecutionState;
import de.artignition.werkflow.domain.PluginInstance;
import de.artignition.werkflow.domain.WorkItem;
import de.artignition.werkflow.plugin.PluginExitStatus;
import de.artignition.werkflow.plugin.PluginExitStatus.Status;

public class PluginExecutor implements Runnable {

	private PluginInstance 			instance;
	private PluginExecutionProxy	proxy;
	private boolean					isExecuting;

	private JobPlugin				plugin;
	private List<PluginTuple>		predecessors;
	
	
	public PluginExecutor(PluginTuple tuple, List<PluginTuple> prev) {
		
		this.plugin 		= tuple.getPlugin();
		this.instance		= tuple.getInstance();
		this.predecessors	= prev;
		
		try {
			Class<?> x = Class.forName(plugin.getClassname());
			this.proxy    = new PluginExecutionProxy(x, instance.getParameters());
		} catch (Exception ex) {
			throw new RuntimeException("Unable to initialize plugin executor. Cause: " + ex.getMessage());
		}
	}
	
	@Override
	public void run() {
		this.isExecuting = true;
		
		instance.setState(PluginExecutionState.IDLE);
		this.proxy.beforeExecution();
	
		boolean isInputLoaded = false;
		
		while (isExecuting) {

			// check predecessor plugins
			isPredecessorWorking();
			
			// load inputs
			if (predecessors.size() != 0) 
				isInputLoaded = loadInputs();
		
			// do the work
			if (isInputLoaded || predecessors.size() == 0) {
				proxy.beforeWorkItemProcessing();
				PluginExitStatus status = proxy.execute();
				proxy.afterExecution();
				routeOutputs();
				setInstanceState(status);
				instance.setStepCount(instance.getStepCount() + 1);
			}
			
			// check if we're donw
			if (instance.getState() == PluginExecutionState.SUCCEEDED)
				isExecuting = false;
			if (instance.getState() == PluginExecutionState.FAILED)
				isExecuting = false;
		
			// route outputs
			Thread.yield();
			
			// check if predecessor has finished
			for (PluginTuple t : predecessors) {
				if (t.getInstance().getState() == PluginExecutionState.SUCCEEDED) {
					// check if there is more work in the queue
					if (!instance.hasItemsInInQueue()) {
						instance.setState(PluginExecutionState.SUCCEEDED);
						this.isExecuting = false;
					}
				}
			}
		}
		this.proxy.afterExecution();
	}

	private void setInstanceState(PluginExitStatus status) {
		if (status.getExitStatus() == Status.SUCCESS)
			instance.setState(PluginExecutionState.SUCCEEDED);
		if (status.getExitStatus() == Status.FAILED)
			instance.setState(PluginExecutionState.FAILED);
		if (status.getExitStatus() == Status.IDLE)
			instance.setState(PluginExecutionState.IDLE);
	}
	
	
	private boolean loadInputs() {
	
		boolean isFullyLoaded = true;
		for (int i : proxy.getInputPorts()) {
			WorkItem x = instance.pollItem(i);
			if (x != null) {
				try {
					proxy.setInput(x, i);
				} catch (Exception ex) {
					throw new RuntimeException("Unable to assign work item to port");
				}
			} else
				isFullyLoaded = false;
		}
		return isFullyLoaded;
	}
	
	
	private void routeOutputs() {
		
		for (Connection c : plugin.getConnections()) {
			try {
				WorkItem x = proxy.getOutput(c.getSourcePort());
				instance.getOutqueueByPort(c.getSourcePort()).add(x);
			} catch (Exception ex) {
				throw new RuntimeException("Unable to route Output");
			}
		}
	}
	
	
	private void isPredecessorWorking() throws RuntimeException {
		
		// check if each predecessor is either on SUCCESS or IDLE or FINISHED
		for (PluginTuple t : predecessors) {
			if (t.getInstance().getState() == PluginExecutionState.FAILED) {
				instance.setState(PluginExecutionState.FAILED);
				throw new RuntimeException("Failed Plugin due to predecessor failing");
			}
		}
	}
}