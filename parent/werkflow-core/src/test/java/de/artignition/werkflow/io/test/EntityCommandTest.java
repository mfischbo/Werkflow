package de.artignition.werkflow.io.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import de.artignition.werkflow.command.CommandFailedResponse;
import de.artignition.werkflow.command.EntityCommand;
import de.artignition.werkflow.command.Verb;
import de.artignition.werkflow.config.CoreConfig;
import de.artignition.werkflow.dto.PluginDescriptor;
import de.artignition.werkflow.messaging.EntityCommandMessageCreator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class} )
public class EntityCommandTest {

	
	@Autowired
	private JmsTemplate		jms;
	
	
	@Test
	public void canExecuteGetAllPluginDescriptorsCommand() {
		
		EntityCommand ec = new EntityCommand();
		ec.setType(PluginDescriptor.class); 
		ec.setVerb(Verb.GET);
	
		jms.send(new EntityCommandMessageCreator(ec));
		jms.setReceiveTimeout(5000L);
		Object m = jms.receiveAndConvert("AdminRequest");
		assertNotNull(m);
		assertFalse(CommandFailedResponse.class == m.getClass());

		try {
			Object[] q = (Object[]) m;
			PluginDescriptor[] retval = Arrays.copyOf(q, q.length, PluginDescriptor[].class); 
			assertTrue(retval.length > 0);
		} catch (ClassCastException ex) {
			fail("Unable to cast from " + m.getClass().getCanonicalName() + " to List of " + PluginDescriptor.class.getCanonicalName());
		}
	}
}
