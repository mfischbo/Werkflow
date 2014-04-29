package de.artignition.werkflow.io;

import java.io.Serializable;

import de.artignition.werkflow.command.CommandFailedResponse;
import de.artignition.werkflow.command.CommandSuccessResponse;
import de.artignition.werkflow.command.EngineCommand;
import de.artignition.werkflow.command.EngineCommand.Action;
import de.artignition.werkflow.command.EntityCommand;
import de.artignition.werkflow.command.Verb;
import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.dto.PluginDescriptor;
import de.artignition.werkflow.service.JobDescriptorService;
import de.artignition.werkflow.service.PluginDiscoveryService;

import org.springframework.beans.factory.annotation.Autowired;

public class MessageReceiver implements ResponsiveObjectMessageDelegate {

	@Autowired
	private PluginDiscoveryService		pluginService;

	@Autowired
	private JobDescriptorService		jdService;
	
	@Override
	public Serializable handleMessage(Serializable object) {
		if (object instanceof EngineCommand) {
			EngineCommand e = (EngineCommand) object;
			if (e.getAction() == Action.START)
				return "Engine started";
		}
	
		if (object instanceof EntityCommand) {
			EntityCommand ec = (EntityCommand) object;
			
			if (ec.getType() == PluginDescriptor.class)
				if (ec.getVerb() == Verb.GET) {
					if (ec.getId() == null)
						return pluginService.getAllPlugins().toArray();
				}
			
			
			if (ec.getType() == JobDescriptor.class) {
				if (ec.getVerb() == Verb.GET) {
					if (ec.getId() == null) {
						return jdService.getAllJobDescriptors().toArray();
					} else {
						return jdService.getJobDescriptorById(ec.getId());
					}
					
				}
				
				if (ec.getVerb() == Verb.POST) {
					try {
						JobDescriptor d = (JobDescriptor) ec.getPayload();
						d = jdService.createJobDescriptor(d);
						return new CommandSuccessResponse(d.getId());
					} catch (ClassCastException ex) {
						
					}
				}
			}
		}
		
		return new CommandFailedResponse("Unable to dispatch command of type : " + object.getClass() + ". Details : " + object.toString());
	}
}