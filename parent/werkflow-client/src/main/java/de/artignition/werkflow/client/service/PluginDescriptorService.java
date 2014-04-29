package de.artignition.werkflow.client.service;

import de.artignition.werkflow.command.EntityCommand;
import de.artignition.werkflow.command.Verb;
import de.artignition.werkflow.dto.PluginDescriptor;
import de.artignition.werkflow.messaging.EntityCommandMessageCreator;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PluginDescriptorService {

	@Autowired
	private JmsTemplate jms;

	private PluginDescriptor[] 		pluginCache;
	
	private Logger log	= LoggerFactory.getLogger(getClass());
	
	public PluginDescriptor[] getAllPlugins() throws JMSException, Exception {

		EntityCommand ec = new EntityCommand();
		ec.setType(PluginDescriptor.class);
		ec.setVerb(Verb.GET);
		jms.send(new EntityCommandMessageCreator(ec));
		
		jms.setReceiveTimeout(5000L);
		Object m = jms.receiveAndConvert("AdminRequest");
		if (m != null && m instanceof Object[]) {
			Object[] q = (Object[]) m;
			PluginDescriptor[] retval = new PluginDescriptor[q.length];
			for (int i=0; i < q.length; i++) {
				retval[i] = (PluginDescriptor) q[i];
			}
			log.debug("Received plugins : " + retval.length);
			this.pluginCache = retval;
			return retval;
		} else {
			throw new Exception("Unable to receive data");
		}
	}
	
	
	
	public PluginDescriptor getPluginByClassName(String classname) {
		for (PluginDescriptor p : pluginCache) {
			if (p.getClassname().equals(classname))
				return p;
		}
		return null;
	}
}
