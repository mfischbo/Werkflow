package de.artignition.werkflow.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.domain.JobInstance;
import de.artignition.werkflow.domain.PluginInstance;
import de.artignition.werkflow.domain.WorkItem;


/**
 * This class holds and controls the execution of the actual {@link de.artignition.werkflow.domain.JobInstance}.
 * This implementation delegates all plugin specific calls to underlying {@link PluginExecutor} instances.
 * 
 * @author M. Fischboeck 
 *
 */
public class JobInstanceExecutor {

	private JobInstance							instance;
	private ThreadGroup							threadGroup;
	private Thread[]							threads;
	private boolean								initialized;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public JobInstanceExecutor(JobDescriptor descriptor) {
		initialize(descriptor);
	}
	
	private void initialize(JobDescriptor descriptor) {

		// create the instance for persistence
		instance = new JobInstance();
		instance.setDateCreated(DateTime.now());
		instance.setDescriptorId(descriptor.getId());
		instance.setPluginInstances(new HashSet<PluginInstance>(descriptor.getPlugins().size()));

		List<PluginTuple> tuples = new ArrayList<>(descriptor.getPlugins().size());
		descriptor.getPlugins().forEach(p -> {
			PluginInstance pi = new PluginInstance(p);
			instance.getPluginInstances().add(pi);
			PluginTuple pt = new PluginTuple(p, pi);
			tuples.add(pt);
		});
	
		// create a thread group and threads for each plugin
		threadGroup = new ThreadGroup("TG_" + instance.getId());
		threads = new Thread[instance.getPluginInstances().size()];

		int k = 0;
		for (PluginInstance i : instance.getPluginInstances()) {
		
			// find the plugin tuple for the executors instance
			PluginTuple xi = tuples.stream().filter(t -> t.getInstance().equals(i)).findFirst().orElse(null);
			
			// find all preceeding tuples
			List<PluginTuple> predecessors = new LinkedList<>();
			tuples.forEach(t -> {
				t.getPlugin().getConnections().forEach(c -> {
					if (c.getTargetPluginId().equals(xi.getPlugin().getId())) {
						ConcurrentLinkedQueue<WorkItem> outQueue = t.getInstance().getOutqueueByPort(c.getSourcePort());
						if (outQueue != null) 
							xi.getInstance().setInQueue(c.getTargetPort(), outQueue);
						predecessors.add(t);
					}
				});
			});
			
			try {
				PluginExecutor exec = new PluginExecutor(xi, predecessors);
				Thread t = new Thread(threadGroup, exec, "PI-" + i.getId());
				threads[k] = t;
				k++;
			} catch (Exception ex) {
				log.error("Unable to create PluginInstance " + xi.getPlugin().getClassname() + ". Cause: " + ex.getMessage());
			}
		}
		this.initialized = true;
	}


	public void startJob() {
		if (!initialized) 
			throw new IllegalStateException("Can not start Job. Executor is not initialized!");
		
		for (Thread t : threads) {
			t.start();
		}
		
		instance.setDateStarted(DateTime.now());
	}
	
	public JobInstance getWrappedInstance() {
		return this.instance;
	}
}