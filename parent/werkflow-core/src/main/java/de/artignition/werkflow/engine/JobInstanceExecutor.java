package de.artignition.werkflow.engine;

import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.domain.JobInstance;
import de.artignition.werkflow.domain.JobPlugin;
import de.artignition.werkflow.domain.PluginExecutionState;
import de.artignition.werkflow.domain.PluginInstance;
import de.artignition.werkflow.repository.JobInstanceRepository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;


/**
 * This class holds and controls the execution of the actual {@link de.artignition.werkflow.domain.JobInstance}.
 * This implementation delegates all plugin specific calls to underlying {@link PluginExecutor} instances.
 * 
 * @author M. Fischboeck 
 *
 */
public class JobInstanceExecutor {
	
	private ApplicationContext					ctx;
	private JobInstance							instance;
	private ThreadGroup							threadGroup;
	private Thread[]							threads;
	private boolean								initialized;
	private ConcurrentMap<UUID, PluginExecutionState>		stateMap;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private	JobInstanceRepository	jobInstanceRepo;
	
	JobInstanceExecutor(ApplicationContext ctx, JobInstanceRepository repo) {
		this.ctx = ctx;
		this.jobInstanceRepo = repo;
		this.stateMap = new ConcurrentHashMap<UUID, PluginExecutionState>();
	}
	
	void initialize(JobDescriptor descriptor) {

		// create the instance for persistence
		instance = new JobInstance();
		instance.setDateCreated(LocalDateTime.now());
		instance.setDescriptor(descriptor);
		instance.setPluginInstances(new ArrayList<PluginInstance>(descriptor.getPlugins().size()));
		
		for (JobPlugin jp : descriptor.getPlugins()) {
			PluginInstance pi = new PluginInstance();
			pi.setJobInstance(instance);
			pi.setJobPlugin(jp);
			pi.setStepCount(0);
			pi.setState(PluginExecutionState.IDLE);
			pi.setParameters(jp.getParameters());
			instance.getPluginInstances().add(pi);
			this.stateMap.put(jp.getId(), PluginExecutionState.IDLE);
		}

		// persist the jobInstance
		this.instance = jobInstanceRepo.saveAndFlush(this.instance);
		
		// create a thread group and threads for each plugin
		threadGroup = new ThreadGroup("TG_" + instance.getId());
		this.threads = new Thread[instance.getPluginInstances().size()];
		int k=0;
		for (PluginInstance i : instance.getPluginInstances()) {
			try {
				PluginExecutor executor = PluginInstanceExecutorFactory.newInstance(ctx, i);
				executor.setPluginStateMap(this.stateMap);
				Thread t = new Thread(threadGroup, executor, "PI-" + i.getId());
				this.threads[k] = t;
				k++;
			} catch (Exception ex) {
				log.error("Unable to create PluginInstance " + i.getJobPlugin().getClassname() + ". Cause: " + ex.getMessage());
			}
		}
		this.initialized = true;
	}
	
	
	public void startJob() {
		if (!initialized) 
			throw new IllegalStateException("Can not start Job. Executor is not initialized!");
		
		for (Thread t : threads) {
			t.run();
		}
		
		instance.setDateStarted(LocalDateTime.now());
		this.instance = jobInstanceRepo.saveAndFlush(instance);
	}
}
