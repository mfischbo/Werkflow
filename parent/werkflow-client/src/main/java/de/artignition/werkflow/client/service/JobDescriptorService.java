package de.artignition.werkflow.client.service;

import de.artignition.werkflow.command.CommandSuccessResponse;
import de.artignition.werkflow.command.EntityCommand;
import de.artignition.werkflow.command.Verb;
import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.messaging.EntityCommandMessageCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobDescriptorService {

	@Autowired
	private JmsTemplate		jms;
	
	public void createJobDescription(JobDescriptor d, String server) {
	
		EntityCommand c = new EntityCommand();
		c.setType(JobDescriptor.class);
		c.setVerb(Verb.POST);
		c.setPayload(d);
		
		jms.send(new EntityCommandMessageCreator(c));
		//jms.setReceiveTimeout(5000L);
		Object o = jms.receiveAndConvert();
		if (o instanceof CommandSuccessResponse) {
			CommandSuccessResponse csr = (CommandSuccessResponse) o;
			System.out.println("Created JobDescription with id : " + csr.getId());
		}
	}
}
