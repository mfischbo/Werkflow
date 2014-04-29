package de.artignition.werkflow.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.artignition.werkflow.client.util.JobDescriptorConverter;
import de.artignition.werkflow.domain.Connection;
import de.artignition.werkflow.domain.JobDescriptor;
import de.artignition.werkflow.domain.JobPlugin;

import eu.mihosoft.vrl.workflow.ConnectionResult;
import eu.mihosoft.vrl.workflow.FlowFactory;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;

import org.junit.Test;

public class ConverterTestCases {

	@Test
	public void reflectsVNodesCorrectly() {

		VFlow flow = FlowFactory.newFlow(); 
		VNode source = flow.newNode();
		VNode target = flow.newNode();

		source.addOutput("test");
		target.addInput("test");
		flow.connect(source, target, "data");
		
		
		JobDescriptor result = new JobDescriptor();
		JobDescriptorConverter.convert(result, flow);
	
		assertNotNull(result.getPlugins());
		assertTrue(result.getPlugins().size() == 2);
	}
	
	
	@Test
	public void isOneToOneConnectionCorrect() {
	
		VFlow flow = FlowFactory.newFlow();
		VNode source = flow.newNode();
		VNode target = flow.newNode();

		source.addOutput("test");
		target.addInput("test");
		
		ConnectionResult r = flow.connect(source, target, "test");
		if (r.getConnection() == null) {
			fail(r.getStatus().getMessage());
		}
			
		JobDescriptor result = new JobDescriptor();
		JobDescriptorConverter.convert(result, flow);
		
		JobPlugin psource = null;
		JobPlugin ptarget = null;
		for (JobPlugin p : result.getPlugins()) {
			if (p.getOutboundConnections().size() > 0)
				psource = p;
			if (p.getInboundConnections().size() > 0)
				ptarget = p;
		}
		assertNotNull(psource);
		assertNotNull(ptarget);
		assertFalse(ptarget.equals(psource));
		assertEquals(1, psource.getOutboundConnections().size() == 1);
		assertEquals(1, ptarget.getInboundConnections().size() == 1);
	
		Connection c = psource.getOutboundConnections().iterator().next();
		assertNotNull(c);
		assertEquals(target, c.getTarget());
		assertEquals(source, c.getSource());
	}
}
