package de.artignition.werkflow.engine;

import de.artignition.werkflow.domain.Connection;
import de.artignition.werkflow.domain.PluginExecutionState;
import de.artignition.werkflow.domain.PluginInstance;
import de.artignition.werkflow.domain.WorkItem;
import de.artignition.werkflow.plugin.PluginExitStatus;
import de.artignition.werkflow.plugin.PluginExitStatus.Status;
import de.artignition.werkflow.repository.PluginInstanceRepository;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

class PluginExecutor implements Runnable {

	private PluginInstanceRepository piRepo;
	
	private PluginInstance 			instance;

	private WorkItemRouter 			router;
	
	private PluginExecutionProxy	proxy;
	
	private ConcurrentMap<UUID, PluginExecutionState>	stateMap;

	PluginExecutor(PluginInstance instance, WorkItemRouter router, PluginExecutionProxy proxy, PluginInstanceRepository piRepo) throws Exception {
		this.piRepo = piRepo;
		this.instance = instance;
		this.router = router;
		this.proxy = proxy;
		this.proxy.initializePlugin();
	}
	
	void setPluginStateMap(ConcurrentMap<UUID, PluginExecutionState> stateMap) {
		this.stateMap = stateMap;
	}

	@Override
	public void run() {
		
		// initialize the router
		// NOTE: It's important that it's called that late, since the whole JobInstance must be available
		// in the database when initializing the router
		router.initialize();

		// execute the beforeExecution call.
		this.proxy.beforeExecution();

		while (true) {
			int result = getStateResult();
			if (result == -1) {
				// one of the predecessors failed. Fail too!
				instance.setState(PluginExecutionState.FAILED);
				createSavePoint();
				return;
			}
			
			if (result == 1) {
				// plugin execution is succceeded
				instance.setState(PluginExecutionState.SUCCEEDED);
				createSavePoint();
				return;
			}
		
			if (result == 4) {
				// plugins without inbound connections execute without WorkItems 
				executeStep(new WorkItem[0]);
			}
			
			if (result == 0 && !router.hasItemOfWork()) {
				// last item of work done. State has been saved by executeStep
				return;
			}
		
			if (result == 0 && router.hasItemOfWork()) {
				executeStep(router.getItemOfWork());
			}
			
			try {
				Thread.sleep(500L);
				Thread.yield();
			} catch (Exception ex) {
				
			}
		}
	}

	private void executeStep(WorkItem[] items) {
		
		try {
			this.proxy.setInputs(items);
		} catch (Exception ex) {
			throw new RuntimeException("Failed to set the plugins inputs. Cause: " + ex.getMessage());
		}

		proxy.beforeWorkItemProcessing();

		instance.setState(PluginExecutionState.PROCESSING);
		createSavePoint();

		PluginExitStatus status = proxy.execute();
	
		proxy.afterWorkItemProcessing();
		if (status.getExitStatus() == Status.SUCCESS || status.getExitStatus() == Status.IDLE) 
			router.routeOutput(proxy.getOutput());
		
		instance.setStepCount(instance.getStepCount() + 1);
			
		if (status.getExitStatus() == Status.SUCCESS)
			instance.setState(PluginExecutionState.SUCCEEDED);
		if (status.getExitStatus() == Status.IDLE)
			instance.setState(PluginExecutionState.IDLE);
		if (status.getExitStatus() == Status.FAILED)
			instance.setState(PluginExecutionState.FAILED);
		createSavePoint();
	}
	
	
	private int getStateResult() {
	
		// check the plugins own state
		if (instance.getState() == PluginExecutionState.FAILED)
			return -1;
		if (instance.getState() == PluginExecutionState.SUCCEEDED)
			return 1;
		
		// input plugins return special number 4 when not failed or succeeded
		if (instance.getJobPlugin().getInboundConnections().size() == 0)
			return 4;
			
		// check predecessor states
		Set<Connection> inbounds = instance.getJobPlugin().getInboundConnections();
		ArrayList<PluginExecutionState> current = new ArrayList<PluginExecutionState>(inbounds.size());
		for (Connection c : inbounds) {
			UUID sourcePlugin = c.getSource().getId();
			current.add(this.stateMap.get(sourcePlugin));
		}
		
		if (current.contains(PluginExecutionState.FAILED))
			return -1;
		return 0;
	}

	
	private void createSavePoint() {
		this.stateMap.put(this.instance.getJobPlugin().getId(), this.instance.getState());
		piRepo.saveAndFlush(instance);
	}
}