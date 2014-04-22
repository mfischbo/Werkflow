package de.artignition.werkflow.engine;

import de.artignition.werkflow.domain.Connection;
import de.artignition.werkflow.domain.JobPlugin;
import de.artignition.werkflow.domain.PluginInstance;
import de.artignition.werkflow.domain.PluginInstance_;
import de.artignition.werkflow.domain.WorkItem;
import de.artignition.werkflow.domain.WorkItem_;
import de.artignition.werkflow.repository.PluginInstanceRepository;
import de.artignition.werkflow.repository.WorkItemRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

class WorkItemRouter {

	private WorkItemRepository 			wiRepo;
	private PluginInstanceRepository 	instanceRepo;
	private PluginInstance				instance;
	private Map<Integer, PluginInstance>routingMap;

	
	public WorkItemRouter(PluginInstance instance, WorkItemRepository wiRepo, PluginInstanceRepository instanceRepo) {
		this.instance = instance;
		this.routingMap = new HashMap<Integer, PluginInstance>();
		this.wiRepo = wiRepo;
		this.instanceRepo = instanceRepo;
	}
	
	public void initialize() {

		Set<Connection> outbounds = this.instance.getJobPlugin().getOutboundConnections();
		for (Connection c : outbounds) {
			
			if (!routingMap.containsKey(c.getSourcePort())) {
				JobPlugin plug = c.getTarget();
				Specifications<PluginInstance> specs = Specifications.where(forJobPlugin(plug));
				PluginInstance i = this.instanceRepo.findOne(specs);
				this.routingMap.put(c.getSourcePort(), i);
			}
		}	
	}
	
	
	public WorkItem[] getItemOfWork() {

		int predecessors = instance.getJobPlugin().getInboundConnections().size();
		
		// if we do not have predecessors return null
		if (predecessors == 0)
			return new WorkItem[0];

		// get next matching item
		if (predecessors == 1) {
			Specifications<WorkItem> specs = Specifications.where(PluginInstanceIs(instance));
        
			WorkItem item = wiRepo.findOne(specs);
			if (item == null) {
				return new WorkItem[0];
			}
			WorkItem[] retval = new WorkItem[1];
			retval[0] = item;
			return retval;
		} 

		// take the join hint into consideration to find a matching tuple
		if (predecessors > 1) {
			// TODO: Implement this! Needs to find all workitems for the given instance with equal joinHints
		}
		return null;
	}
	
	
	public boolean hasItemOfWork() {
		return (wiRepo.count(Specifications.where(PluginInstanceIs(instance))) > 0); 
	}

	public void routeOutput(WorkItem[] output) {
		
		for (WorkItem i : output) {
			PluginInstance target = this.routingMap.get(i.getSourceOutput());
			i.setOwner(target);
			wiRepo.saveAndFlush(i);
		}
	}
	
	
	static Specification<WorkItem> PluginInstanceIs(final PluginInstance instance) {
		return new Specification<WorkItem>() {
			@Override
			public Predicate toPredicate(Root<WorkItem> root,
				CriteriaQuery<?> arg1, CriteriaBuilder cb) {
				return cb.equal(root.get(WorkItem_.owner), instance);
			}
		};
	}
	
	static Specification<PluginInstance> forJobPlugin(final JobPlugin plugin) {
		return new Specification<PluginInstance>() {
			@Override
			public Predicate toPredicate(Root<PluginInstance> root,
				CriteriaQuery<?> arg1, CriteriaBuilder cb) {
					return cb.equal(root.get(PluginInstance_.jobPlugin), plugin);
			}
		};
	}
}
