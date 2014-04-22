package de.artignition.werkflow.engine;

import de.artignition.werkflow.domain.PluginInstance;
import de.artignition.werkflow.repository.PluginInstanceRepository;
import de.artignition.werkflow.repository.WorkItemRepository;

import org.springframework.context.ApplicationContext;

class PluginInstanceExecutorFactory {

	static PluginExecutor newInstance(ApplicationContext ctx, PluginInstance instance) {
	
		PluginInstanceRepository piRepo = ctx.getBean(PluginInstanceRepository.class);
		WorkItemRepository wiRepo = ctx.getBean(WorkItemRepository.class);
		WorkItemRouter router = new WorkItemRouter(instance, wiRepo, piRepo);
		
		try {
			PluginExecutionProxy proxy = new PluginExecutionProxy(instance);
			PluginExecutor retval = new PluginExecutor(instance, router, proxy, piRepo);
			return retval;
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to create PluginExecutor. Cause: " + ex.getMessage());
		}
	}
}
